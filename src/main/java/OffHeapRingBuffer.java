import java.nio.ByteBuffer;

public class OffHeapRingBuffer {
    public static int cursor = 0;
    public static boolean finished = false;
    public final int entrySize;
    public final int bufferSize;
    private final ByteBuffer buffer;

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
    public void put(byte[] data) {
        int startPosition = perThreadBuffer.get().position();
        int newPosition = ( startPosition + entrySize ) & ( (bufferSize * entrySize) - 1 );
        try {
            perThreadBuffer.get().put(data);
        } finally {
            perThreadBuffer.get().position(newPosition);
            cursor++;
        }
    }
}