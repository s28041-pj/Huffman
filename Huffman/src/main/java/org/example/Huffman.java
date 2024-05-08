package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.stream.IntStream;

public class Huffman {

    public static HuffmanNode buildHuffmanTree(char[] input) {
        HashMap<String, Integer> frequencyMap = getFrequencyMap(input);

        PriorityQueue priorityQueue = new PriorityQueue(frequencyMap.size());
        for (String label : frequencyMap.keySet()) {
            priorityQueue.offer(new HuffmanNode(label, frequencyMap.get(label)));
        }

        while (!priorityQueue.isEmpty()) {
            HuffmanNode left = priorityQueue.poll();
            if (priorityQueue.isEmpty()) {
                return left;
            }
            HuffmanNode right = priorityQueue.poll();

            HuffmanNode z = new HuffmanNode(left.label + right.label,
                    left.frequency + right.frequency);
            z.left = left;
            z.right = right;

            priorityQueue.offer(z);
        }

        return null;
    }

    private static HashMap<String, Integer> getFrequencyMap(char[] input) {
        HashMap<String, Integer> frequencyMap = new HashMap<>();

        for (char c : input) {
            String label = String.valueOf(c);
            frequencyMap.put(label, frequencyMap.getOrDefault(label, 0) + 1);
        }
        return frequencyMap;
    }

    public static HashMap<String, String> generateHuffmanCodes(HuffmanNode root) {
        HashMap<String, String> codes = new HashMap<>();
        generateCodesHelper(root, "", codes);
        return codes;
    }

    private static void generateCodesHelper(HuffmanNode huffmanNode, String code, HashMap<String, String> codes) {
        if (huffmanNode != null) {
            if (huffmanNode.left == null && huffmanNode.right == null) {
                codes.put(huffmanNode.label, code);
            } else {
                generateCodesHelper(huffmanNode.left, code + "0", codes);
                generateCodesHelper(huffmanNode.right, code + "1", codes);
            }
        }
    }

    public static String encode(String input, HuffmanNode root) {
        HashMap<String, String> codes = generateHuffmanCodes(root);
        StringBuilder encodedString = new StringBuilder();
        for (char c : input.toCharArray()) {
            encodedString.append(codes.get(String.valueOf(c)));
        }
        return encodedString.toString();
    }

    public static String readStringToEncodeFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/input.txt"));
        StringBuilder inputBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            inputBuilder.append(line);
        }
        reader.close();
        return inputBuilder.toString();
    }

    public static void saveHuffmanCodesToFile(HuffmanNode root) throws IOException {
        HashMap<String, String> huffmanCodes = generateHuffmanCodes(root);

        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/encode.txt"));
        System.out.println("Huffman codes:");
        for (String label : huffmanCodes.keySet()) {
            System.out.println(label + " code: " + huffmanCodes.get(label));
            writer.write(label + " code: " + huffmanCodes.get(label));
            writer.newLine();
        }

        writer.newLine();
        writer.close();
    }

    public static void saveBinaryEncodedStringToFile(String encodedString, String filePath) {
        int paddingSize = (8 - encodedString.length() % 8) % 8;
        String paddedEncodedText = encodedString + "0".repeat(paddingSize);
        byte[] byteArr = new byte[paddedEncodedText.length() / 8];

        IntStream.range(0, byteArr.length)
                .forEachOrdered(i -> {
                    byteArr[i] = (byte) Integer.parseInt(paddedEncodedText.substring(i * 8, (i + 1) * 8), 2);
                });

        try (FileOutputStream fos = new FileOutputStream(filePath, true)) {
            fos.write(byteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String inputText = readStringToEncodeFromFile();
            HuffmanNode root = buildHuffmanTree(inputText.toCharArray());
            String encodedString = encode(inputText, root);

            saveHuffmanCodesToFile(root);
            saveBinaryEncodedStringToFile(encodedString, "src/main/resources/encode.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}