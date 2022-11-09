public class BatchEventProcessor implements EventProcessor {
    private final Sequencer sequencer;
    private final DataProvider dataProvider;
    private final BufferEventHandler bufferEventHandler;

    public BatchEventProcessor(Sequencer sequencer,
                               DataProvider dataProvider,
                               BufferEventHandler bufferEventHandler) {
        this.sequencer = sequencer;
        this.dataProvider = dataProvider;
        this.bufferEventHandler = bufferEventHandler;
    }

    @Override
    public void run() {
        // do something
    }
}
