import java.nio.ByteBuffer;

public class Producer implements Runnable {
    private OffHeapRingBuffer offHeapRingBuffer;

    public Producer (OffHeapRingBuffer offHeapRingBuffer) {
        this.offHeapRingBuffer = offHeapRingBuffer;
    }
    public void run () {
        byte[] data = new byte[] { (byte)120, (byte)120, (byte)120 };
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println( "putting in:"+data );
            offHeapRingBuffer.put(data);
        }
    }
}
