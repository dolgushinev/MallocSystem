public class MemorySegment {

    private SegmentStatus status;
    private final int length;

    public MemorySegment(SegmentStatus status, int length) {
        this.status = status;
        this.length = length;
    }

    public SegmentStatus getStatus() {
        return status;
    }

    public int getLength() {
        return length;
    }

    public void setStatus(SegmentStatus status) {
        this.status = status;
    }
}
