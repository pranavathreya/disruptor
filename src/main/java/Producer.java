public class Producer implements Runnable {
    private OffHeapRingBuffer offHeapRingBuffer;

    public Producer (OffHeapRingBuffer offHeapRingBuffer) {
        this.offHeapRingBuffer = offHeapRingBuffer;
    }
    public void run () {
        for (int i=0; i<21; i++) {
            byte[] inputBytes = new byte[] {
                    (byte) 0x53, (byte) 0x41, (byte) 0x53, (byte) 0x0A,
            };
            System.out.println(Thread.currentThread().getName()+" putting in: "+
                    i+", "+i+", "+i+"." );
            offHeapRingBuffer.put(inputBytes);
            System.out.println(Thread.currentThread().getName()+": main cursor: "+offHeapRingBuffer.cursor);
        }
        offHeapRingBuffer.finished = true;
    }
}
