import java.nio.ByteBuffer;

public class OffHeapRingBuffer {
    public int cursor = 0;
    private final int entrySize;
    private final int bufferSize;
    private final ByteBuffer buffer;
    private boolean readNow;

    ThreadLocal<ByteBuffer> perThreadBuffer =
            new ThreadLocal <ByteBuffer>() {
                protected ByteBuffer initialValue() {
                    return buffer.duplicate();
                }
            };
    public OffHeapRingBuffer(int entrySize, int bufferSize) {
        this.entrySize = entrySize;
        this.bufferSize = bufferSize;
        buffer = ByteBuffer.allocateDirect(
                bufferSize * entrySize
        );
   }
   public void put(byte[] data) {
        System.out.println(perThreadBuffer.get());
        int startPosition = perThreadBuffer.get().position();
        int newPosition = ( startPosition + entrySize ) % ( bufferSize * entrySize );
       // TODO: Enforce 2^n buffer size; bit masking
       try {
            perThreadBuffer.get().put(data);
            System.out.println(perThreadBuffer.get());
        } finally {
            perThreadBuffer.get().position(newPosition);
            cursor++;
            notify();
        }
   }
   public byte[] get() throws InterruptedException {
        while (!readNow) {
            wait();
        }
       int startPosition = perThreadBuffer.get().position();
       int newPosition = ( startPosition + entrySize ) % ( bufferSize * entrySize );
       // TODO: Enforce 2^n buffer size; bit masking
       byte[] data = new byte[] {};
       for (int i=0; i<entrySize; i++) {
           data[i] = perThreadBuffer.get().get();
       }
       perThreadBuffer.get().position(newPosition);
       readNow = false;
       return data;
   }
}