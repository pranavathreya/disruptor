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
            try {
                while (!offHeapRingBuffer.readNow) {
                    wait();
                }
                ByteBuffer data = ByteBuffer.allocateDirect(offHeapRingBuffer.entrySize
                        * (offHeapRingBuffer.cursor - consumerCursor));
                data.mark();
                while (consumerCursor < offHeapRingBuffer.cursor) {
                    int startPosition = (offHeapRingBuffer.cursor % offHeapRingBuffer.bufferSize)
                            * offHeapRingBuffer.entrySize;
                    int nextStartPosition = ( startPosition + offHeapRingBuffer.entrySize )
                            % ( offHeapRingBuffer.bufferSize * offHeapRingBuffer.entrySize );
                    // TODO: Enforce 2^n buffer size; bit masking
                    for (int i=0; i<offHeapRingBuffer.entrySize; i++) {
                        data.put(offHeapRingBuffer.perThreadBuffer.get().get());
                    }
                    // offHeapRingBuffer.perThreadBuffer.get().position(nextStartPosition);
                    consumerCursor++;
                }
                offHeapRingBuffer.readNow = false;
                data.reset();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.print(Thread.currentThread().getName()+" received: ");
            while (data.position()<data.limit()) {
                System.out.print(data.get()+", ");
            }
            System.out.println();
        }
    }
}
