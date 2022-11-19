public class Main {
    public static void main(String[] args) {
        int entrySize = 4;
        int bufferSize = 8;
        OffHeapRingBuffer offHeapRingBuffer =
                new OffHeapRingBuffer(entrySize, bufferSize);

        Producer producer = new Producer(offHeapRingBuffer);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        Consumer consumer = new Consumer(offHeapRingBuffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        Consumer consumer2 = new Consumer(offHeapRingBuffer);
        Thread consumer2Thread = new Thread(consumer2);
        consumer2Thread.start();
    }
}
