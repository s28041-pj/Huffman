package org.example;

public class HuffmanNode implements Comparable<HuffmanNode> {
        String label;
        int frequency;
        HuffmanNode left, right;

        public HuffmanNode(String label, int frequency) {
            this.label = label;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }