public interface DataProvider<ByteBuffer> {

    ByteBuffer get(long sequence);
}
