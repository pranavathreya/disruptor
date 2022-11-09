public class SingleProducerSequencer
implements Sequencer {
    WaitStrategy waitStrategy = null;
    long cursor = 0;

    @Override
    public long next() {
        return cursor;
    }

    @Override
    public int getBufferSize() {
        return 10; //be relatively large
    }

    @Override
    public void publish() {
        cursor++;
    }
}
