import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Consumer implements Runnable{
    int consumerCursor = 0;
    private OffHeapRingBuffer offHeapRingBuffer;
    private boolean hasRead;
    private String messageFileName;
    private long mappedBufferPosition = 0;
    private MappedByteBuffer data;

    public Consumer (OffHeapRingBuffer offHeapRingBuffer) {
        this.offHeapRingBuffer = offHeapRingBuffer;
    }
    public void run () {
        messageFileName = Thread.currentThread().getName()+".txt";
        try (FileChannel fc = FileChannel.open(Path.of(messageFileName), StandardOpenOption.WRITE,
                StandardOpenOption.READ)) {
            while (!offHeapRingBuffer.finished) {
                while ((consumerCursor == offHeapRingBuffer.cursor)) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                long mappedBufferSize = (offHeapRingBuffer.cursor - consumerCursor) * offHeapRingBuffer.entrySize;
                MappedByteBuffer data = fc.map(FileChannel.MapMode.READ_WRITE,
                        mappedBufferPosition, mappedBufferSize
                        );
                while (consumerCursor < offHeapRingBuffer.cursor) {
                    int startPosition = (consumerCursor & (offHeapRingBuffer.bufferSize-1))
                            * offHeapRingBuffer.entrySize;
                    offHeapRingBuffer.perThreadBuffer.get().position(startPosition);
                    for (int i = 0; i < offHeapRingBuffer.entrySize; i++) {
                        data.put(offHeapRingBuffer.perThreadBuffer.get().get());
                    }
                    if (offHeapRingBuffer.perThreadBuffer.get().position() ==
                            offHeapRingBuffer.perThreadBuffer.get().limit()) {
                        offHeapRingBuffer.perThreadBuffer.get().position(0);
                    }
                    consumerCursor++;
                }
                mappedBufferPosition = mappedBufferPosition + mappedBufferSize;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
