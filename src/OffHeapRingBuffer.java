import java.nio.ByteBuffer;

public class OffHeapRingBuffer implements DataProvider<ByteBuffer> {
    public int cursor;
    private final int entrySize;
    private final ByteBuffer buffer;

    ThreadLocal<ByteBuffer> perThreadBuffer =
            new ThreadLocal <ByteBuffer>() {
                protected ByteBuffer initialValue() {
                    return buffer.duplicate();
                }
            };

    public OffHeapRingBuffer(Sequencer sequencer,
                             int entrySize) {
        this.sequencer = sequencer;
        this.entrySize = entrySize;
        buffer = ByteBuffer.allocateDirect(
                sequencer.getBufferSize() * entrySize
        );
   }

   private int index(long cursor) {
       // TODO: Enforce 2^n buffer size; bit masking
        long idx = cursor % sequencer.getBufferSize();
        return (int) idx;
   }

   public ByteBuffer get(long cursor) {
        int index = index(cursor);
        int position = index * entrySize;
        int limit = position + entrySize;

        ByteBuffer byteBuffer = perThreadBuffer.get();
        byteBuffer.position(position).limit(limit);

        return byteBuffer;
   }

   public void put(byte[] data) {
        long next = sequencer.next();
        try {
            get(next).put(data);
        } finally {
            sequencer.publish();
        }
   }

   public long next() {
        return sequencer.next();
   }

    public void publish() {
        sequencer.publish();
    }

}