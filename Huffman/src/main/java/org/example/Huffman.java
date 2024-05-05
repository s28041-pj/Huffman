package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.stream.IntStream;

public class Huffman {

    public static HuffmanNode buildHuffmanTree(char[] input) {
        HashMap<String, Integer> frequencyMap = getFrequencyMap(input);

        HuffmanNode root = buildTreeFromFrequencyMap(frequencyMap);

        return root;
    }

    private static HuffmanNode buildTreeFromFrequencyMap(HashMap<String, Integer> frequencyMap) {
        HuffmanNode[] nodes = new HuffmanNode[frequencyMap.size()];
        int index = 0;
        for (String label : frequencyMap.keySet()) {
            nodes[index] = new HuffmanNode(label, frequencyMap.get(label));
            index++;
        }

        while (nodes.length > 1) {
            int minIndex1 = 0, minIndex2 = 1;
            if (nodes[minIndex1].compareTo(nodes[minIndex2]) > 0) {
                int temp = minIndex1;
                minIndex1 = minIndex2;
                minIndex2 = temp;
            }

            for (int i = 2; i < nodes.length; i++) {
                if (nodes[i].compareTo(nodes[minIndex1]) < 0) {
                    minIndex2 = minIndex1;
                    minIndex1 = i;
                } else if (nodes[i].compareTo(nodes[minIndex2]) < 0) {
                    minIndex2 = i;
                }
            }

            HuffmanNode z = new HuffmanNode(nodes[minIndex1].label + nodes[minIndex2].label,
                    nodes[minIndex1].frequency + nodes[minIndex2].frequency);
            z.left = nodes[minIndex1];
            z.right = nodes[minIndex2];

            HuffmanNode[] newNodes = new HuffmanNode[nodes.length - 1];
            int newIndex = 0;
            for (int i = 0; i < nodes.length; i++) {
                if (i != minIndex1 && i != minIndex2) {
                    newNodes[newIndex++] = nodes[i];
                }
            }
            newNodes[newIndex] = z;
            nodes = newNodes;
        }

        return nodes[0];
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