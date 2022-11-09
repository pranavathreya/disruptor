public interface EventProcessor<T> {

    Sequencer sequencer = null;

    DataProvider dataProvider = null;

    void run();

}
