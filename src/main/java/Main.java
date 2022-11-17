import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("info log {}", Main.class);
        logger.debug("Hello world.");

        // print internal state
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
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
