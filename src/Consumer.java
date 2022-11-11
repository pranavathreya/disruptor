public class Consumer {
    private OffHeapRingBuffer offHeapRingBuffer;
    private boolean hasRead;

    public Consumer (OffHeapRingBuffer offHeapRingBuffer) {
        this.offHeapRingBuffer = offHeapRingBuffer;
    }
    public void run () throws InterruptedException {
        while (! hasRead) {
            byte[] data = offHeapRingBuffer.get();
        }
    }
}
