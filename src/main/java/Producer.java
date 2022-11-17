public class Producer implements Runnable {
    private OffHeapRingBuffer offHeapRingBuffer;

    public Producer (OffHeapRingBuffer offHeapRingBuffer) {
        this.offHeapRingBuffer = offHeapRingBuffer;
    }
    public void run () {
        for (int i=0; i<21; i++) {
            byte[] data = new byte[] { (byte)i, (byte)i, (byte)i };
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+" putting in: "+
                    i+", "+i+", "+i+"." );
            offHeapRingBuffer.put(data);
            System.out.println(Thread.currentThread().getName()+": main cursor: "+offHeapRingBuffer.cursor);
        }
        offHeapRingBuffer.finished = true;
    }
}
