package org.example;

public class PriorityQueue {
        private HuffmanNode[] heap;
        private int size;

        public PriorityQueue(int capacity) {
            heap = new HuffmanNode[capacity];
            size = 0;
        }

        public void offer(HuffmanNode node) {
            if (size == heap.length) {
                resize();
            }
            heap[size++] = node;
            heapifyUp(size - 1);
        }

        public HuffmanNode poll() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            HuffmanNode min = heap[0];
            heap[0] = heap[--size];
            heapifyDown(0);
            return min;
        }

        private void heapifyUp(int index) {
            while (index > 0 && heap[parent(index)].compareTo(heap[index]) > 0) {
                swap(index, parent(index));
                index = parent(index);
            }
        }

        private void heapifyDown(int index) {
            int smallest = index;
            int left = leftChild(index);
            int right = rightChild(index);

            if (left < size && heap[left].compareTo(heap[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && heap[right].compareTo(heap[smallest]) < 0) {
                smallest = right;
            }
            if (smallest != index) {
                swap(index, smallest);
                heapifyDown(smallest);
            }
        }

        private void swap(int i, int j) {
            HuffmanNode temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }

        private void resize() {
            HuffmanNode[] newHeap = new HuffmanNode[2 * heap.length];
            System.arraycopy(heap, 0, newHeap, 0, heap.length);
            heap = newHeap;
        }

        private int parent(int i) {
            return (i - 1) / 2;
        }

        private int leftChild(int i) {
            return 2 * i + 1;
        }

        private int rightChild(int i) {
            return 2 * i + 2;
        }

        public boolean isEmpty() {
            return size == 0;
        }
    }