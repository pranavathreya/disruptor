import java.nio.ByteBuffer;

public class BufferEventHandler
        implements EventHandler<ByteBuffer> {

    public void onEvent(ByteBuffer buffer,
                        long sequence,
                        boolean endOfBatch) {
        // Do Stuff
    }
}