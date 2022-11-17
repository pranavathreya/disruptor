import java.nio.ByteBuffer;

public class RingBufferViewer implements Runnable {
    private OffHeapRingBuffer offHeapRingBuffer;

    public RingBufferViewer (OffHeapRingBuffer offHeapRingBuffer) {
        this.offHeapRingBuffer = offHeapRingBuffer;
    }
    public void run () {
        offHeapRingBuffer.perThreadBuffer.get().mark();
        System.out.println(Thread.currentThread().getName()+": ");
        while (true) {
            for (int i = 0; i < offHeapRingBuffer.perThreadBuffer.get().limit(); i++) {
                System.out.print(offHeapRingBuffer.perThreadBuffer.get().get() + ", ");
            }
            System.out.println("\n"+Thread.currentThread().getName()+
                    " Cursor: "+offHeapRingBuffer.cursor);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            offHeapRingBuffer.perThreadBuffer.get().reset();
        }
    }
}
