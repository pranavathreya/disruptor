public interface Sequencer {
    long next();
    int getBufferSize();
    void publish();
}
