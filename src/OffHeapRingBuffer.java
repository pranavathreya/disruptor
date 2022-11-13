import java.nio.ByteBuffer;

public class OffHeapRingBuffer {
    public int cursor = 0;
    public final int entrySize;
    public final int bufferSize;
    private final ByteBuffer buffer;
    public static boolean readNow;

    ThreadLocal<ByteBuffer> perThreadBuffer =
            new ThreadLocal <ByteBuffer>() {
                protected ByteBuffer initialValue() {
                    return buffer.duplicate();
                }
            };
    public OffHeapRingBuffer(int entrySize, int bufferSize) {
        this.entrySize = entrySize;
        this.bufferSize = bufferSize;
        this.buffer = ByteBuffer.allocateDirect(
                bufferSize * entrySize
        );
   }
   public synchronized void put(byte[] data) {
//        System.out.println(perThreadBuffer.get());
        int startPosition = perThreadBuffer.get().position();
        int newPosition = ( startPosition + entrySize ) % ( bufferSize * entrySize );
       // TODO: Enforce 2^n buffer size; bit masking
       try {
            perThreadBuffer.get().put(data);
//            System.out.println(perThreadBuffer.get());
        } finally {
            perThreadBuffer.get().position(newPosition);
            cursor++;
            readNow = true;
            notifyAll();
        }
   }
   public synchronized ByteBuffer get(int consumerCursor) throws InterruptedException {
        while (!readNow) {
            wait();
        }
       ByteBuffer data = ByteBuffer.allocateDirect(entrySize * (cursor - consumerCursor));
       data.mark();
       while (consumerCursor < cursor) {
           int startPosition = (cursor % bufferSize) * entrySize;
           int nextStartPosition = ( startPosition + entrySize ) % ( bufferSize * entrySize );
           // TODO: Enforce 2^n buffer size; bit masking
           for (int i=0; i<entrySize; i++) {
               data.put(perThreadBuffer.get().get());
           }
           perThreadBuffer.get().position(nextStartPosition);
           consumerCursor++;
        }
       readNow = false;
       data.reset();
       return data;
   }
}