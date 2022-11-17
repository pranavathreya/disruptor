import java.lang.invoke.MethodHandle;
import java.nio.ByteBuffer;

public class Main {
    public static void main (String[] args) {
        int entrySize = 4;
        int bufferSize = 10;
        OffHeapRingBuffer offHeapRingBuffer =
                new OffHeapRingBuffer(entrySize, bufferSize);

        // Thread: Ring Buffer viewer
//        RingBufferViewer ringBufferViewer = new RingBufferViewer(offHeapRingBuffer);
//        Thread ringBufferViewerThread = new Thread(ringBufferViewer);
//        ringBufferViewerThread.start();

        // Thread: Producer
        Producer producer = new Producer(offHeapRingBuffer);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        // Thread: Consumer
        Consumer consumer = new Consumer(offHeapRingBuffer);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        // Thread: Consumer
        Consumer consumer2 = new Consumer(offHeapRingBuffer);
        Thread consumer2Thread = new Thread(consumer2);
        consumer2Thread.start();
    }
}

