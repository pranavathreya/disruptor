import java.nio.ByteBuffer;

public class RingBufferViewer implements Runnable {
    private OffHeapRingBuffer offHeapRingBuffer;

    public RingBufferViewer (OffHeapRingBuffer offHeapRingBuffer) {
        this.offHeapRingBuffer = offHeapRingBuffer;
    }
    public void run () {
        offHeapRingBuffer.perThreadBuffer.get().mark();
        while (true) {
            //System.out.println("\n" + perThreadBuffer.get());
            for (int i = 0; i < offHeapRingBuffer.perThreadBuffer.get().limit() - 1; i++) {
                System.out.print(offHeapRingBuffer.perThreadBuffer.get().get() + ", ");
            }
            System.out.print("\nCursor: "+offHeapRingBuffer.cursor);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            offHeapRingBuffer.perThreadBuffer.get().reset();
        }
    }
}
