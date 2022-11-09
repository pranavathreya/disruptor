import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        SingleProducerSequencer sequencer = new SingleProducerSequencer();
        int entrySize = 4;
        OffHeapRingBuffer offHeapRingBuffer =
                new OffHeapRingBuffer(sequencer,
                        entrySize);

        // Each processor runs on a separate thread
        //EXECUTOR.submit(batchProcessor);

        // Publishers claim events in sequence
        long cursor = offHeapRingBuffer.next();


        // publish the event so it is available to EventProcessors
        offHeapRingBuffer.publish();

        for(int i=0; i<byteBuffer.limit()-1; ++i){
            System.out.println(byteBuffer.get());
        }
    }
}