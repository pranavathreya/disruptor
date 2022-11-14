import java.nio.ByteBuffer;

public class Consumer implements Runnable{
    int consumerCursor = 0;
    private OffHeapRingBuffer offHeapRingBuffer;
    private boolean hasRead;
    ByteBuffer data;

    public Consumer (OffHeapRingBuffer offHeapRingBuffer) {
        this.offHeapRingBuffer = offHeapRingBuffer;
    }
    public void run () {
        while (true) {
            while (consumerCursor==offHeapRingBuffer.cursor) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            ByteBuffer data = ByteBuffer.allocateDirect(offHeapRingBuffer.entrySize
                    * (offHeapRingBuffer.cursor - consumerCursor));
            data.mark();
            while (consumerCursor < offHeapRingBuffer.cursor) {
                int startPosition = (consumerCursor % offHeapRingBuffer.bufferSize)
                        * offHeapRingBuffer.entrySize;
                offHeapRingBuffer.perThreadBuffer.get().position(startPosition);
                // TODO: Enforce 2^n buffer size; bit masking
                for (int i=0; i<offHeapRingBuffer.entrySize; i++) {
                    data.put(offHeapRingBuffer.perThreadBuffer.get().get());
                }
                if (offHeapRingBuffer.perThreadBuffer.get().position() ==
                        offHeapRingBuffer.perThreadBuffer.get().limit()) {
                    offHeapRingBuffer.perThreadBuffer.get().position(0);
                }
                consumerCursor++;
            }
            data.reset();
            System.out.print(Thread.currentThread().getName()+" received: ");
            while (data.position()<data.limit()) {
                System.out.print(data.get()+", ");
            }
            System.out.println();
        }
    }
}
