
import java.lang.reflect.Array;
import java.util.*;

public class MallocSystem {

    private int size = 100;

    private LinkedList<MemorySegment> memorySegments;

    private Stack<MemorySegment> freeSegments;

    private ArrayList<MemorySegment> linksToSegments = new ArrayList();

    public MallocSystem(int size) {
        this.size = size;
        memorySegments = new LinkedList<>();
        freeSegments = new Stack<>();
        MemorySegment initMemorySegment = new MemorySegment(SegmentStatus.IS_FREE, size);
        memorySegments.add(initMemorySegment);
        freeSegments.push(initMemorySegment);
        linksToSegments.add(0, initMemorySegment);

        for (int i = 0; i < size - 1; i++) {
            linksToSegments.add(i, null);
        }
    }

    public int malloc(int n) {

        System.out.println("Выделяем " + n + " байт.\n");

        if (n <= 0) {
            System.out.println("n должно быть больше 0");
            return -1;
        }

        while (freeSegments.peek().getStatus().equals(SegmentStatus.IS_DELETED))
            freeSegments.pop();
        if (freeSegments.peek().getLength() < n) {
            System.out.println("Недостаточно памяти для выделения " + n + " байт\n");
            return -1;
        } else {
            MemorySegment currentFreeSegment = freeSegments.pop();

            int index = memorySegments.indexOf(currentFreeSegment);

            memorySegments.remove(index);

            MemorySegment leftAllocatedSegment = new MemorySegment(SegmentStatus.IS_ALLOCATED, n);

            memorySegments.add(index, leftAllocatedSegment);

            int cellIndex = 0;

            for (MemorySegment memorySegment :
                    memorySegments) {
                if (memorySegment.equals(leftAllocatedSegment)) break;
                cellIndex = cellIndex + memorySegment.getLength();
            }

            linksToSegments.set(cellIndex, leftAllocatedSegment);

            if (currentFreeSegment.getLength() > n) {
                MemorySegment rightFreeSegment = new MemorySegment(SegmentStatus.IS_FREE, currentFreeSegment.getLength() - n);
                memorySegments.add(index + 1, rightFreeSegment);
                freeSegments.push(rightFreeSegment);
            }

            return cellIndex;
        }
    }

    public void printMemorySegments() {
        System.out.println("-------------------------------------------------");
        System.out.println("Состояние списка отрезков памяти: ");
        for (MemorySegment ms : memorySegments
        ) {
            System.out.println("{length = " + ms.getLength() + ", status = " + ms.getStatus() + "}");
        }
        System.out.println();
    }

    public void printLinksToSegments() {
        System.out.println("Состояние массива ссылок на выделенные отрезки памяти: ");
        if (linksToSegments.size() == 0) System.out.println("Нет выделенных отрезков памяти");
        for (int i = 0; i < linksToSegments.size() - 1; i++) {
            if (linksToSegments.get(i) != null) {
                System.out.println("{" + "address = " + i + ", length = " + linksToSegments.get(i).getLength() + ", status = " + linksToSegments.get(i).getStatus() + "}");
            }
        }
        System.out.println("-------------------------------------------------");
        System.out.println();
    }

    public void printFreeSegments() {
        System.out.println("Состояние стека свободных отрезков памяти: ");

        Iterator<MemorySegment> iterator = freeSegments.iterator();
        ArrayList<String> stackElements = new ArrayList<>();

        //Queue<String> stackElements = new LinkedList<>();


        while (iterator.hasNext()) {
            MemorySegment current = iterator.next();
            //System.out.println("{length = " + current.getLength() + ", status = " + current.getStatus() + "}");
            stackElements.add("{length = " + current.getLength() + ", status = " + current.getStatus() + "}");

        }

        Collections.reverse(stackElements);

        for (String stackElement : stackElements
        ) {
            System.out.println(stackElement);
        }

        System.out.println();
    }

    public int free(int i) {
        System.out.println("Освобождаем память, address = " + i + "\n");

        if (i < 0) return -1;
        if (linksToSegments.get(i) == null) return -1;

        MemorySegment currentSegment = linksToSegments.get(i);
        linksToSegments.set(i, null);

        int index = memorySegments.indexOf(currentSegment);

        MemorySegment leftSegment;
        int leftSegmentLength = 0;
        int leftSegmentWasDelete = 0;
        MemorySegment rightSegment;
        int rightSegmentLength = 0;
        int size = memorySegments.size();


        if (index - 1 >= 0) {
            leftSegment = memorySegments.get(index - 1);
            if (leftSegment.getStatus() == SegmentStatus.IS_FREE) {
                leftSegment.setStatus(SegmentStatus.IS_DELETED);
                leftSegmentLength = leftSegment.getLength();
                memorySegments.remove(leftSegment);
                leftSegmentWasDelete = 1;
            }
        }

        if (index + 1 - leftSegmentWasDelete < size - leftSegmentWasDelete) {
            rightSegment = memorySegments.get(index + 1 - leftSegmentWasDelete);
            if (rightSegment.getStatus() == SegmentStatus.IS_FREE) {
                rightSegment.setStatus(SegmentStatus.IS_DELETED);
                rightSegmentLength = rightSegment.getLength();
                memorySegments.remove(rightSegment);
            }
        }

        int length = currentSegment.getLength() + leftSegmentLength + rightSegmentLength;

        MemorySegment freeSegment = new MemorySegment(SegmentStatus.IS_FREE, length);
        memorySegments.set(memorySegments.indexOf(currentSegment), freeSegment);

        freeSegments.push(freeSegment);

        return 0;
    }

}
