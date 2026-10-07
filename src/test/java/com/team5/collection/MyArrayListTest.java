package com.team5.collection;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MyArrayListTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Тесты MyArrayList ===\n");

        testAddAndGet();
        testAddByIndex();
        testSet();
        testRemoveByIndex();
        testRemoveByValue();
        testSizeAndIsEmpty();
        testClear();
        testContainsAndIndexOf();
        testAutoResize();
        testForEachLoop();
        testIteratorRemove();
        testIteratorNoSuchElement();
        testStreamCount();
        testStreamFilter();
        testStreamCollect();
        testToString();
        testIllegalArguments();

        System.out.println("\n=============================");
        System.out.println("Пройдено: " + passed);
        System.out.println("Провалено: " + failed);
        System.out.println("=============================");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[OK]   " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    private static void checkThrows(String name,
                                    Class<? extends Throwable> expected,
                                    Runnable action) {
        try {
            action.run();
            failed++;
            System.out.println("[FAIL] " + name + " — исключение не выброшено");
        } catch (Throwable t) {
            if (expected.isInstance(t)) {
                passed++;
                System.out.println("[OK]   " + name
                        + " (" + t.getClass().getSimpleName() + ")");
            } else {
                failed++;
                System.out.println("[FAIL] " + name + " — ожидалось "
                        + expected.getSimpleName() + ", получено "
                        + t.getClass().getSimpleName());
            }
        }
    }

    private static void testAddAndGet() {
        MyList<String> list = new MyArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        check("add/get: размер == 3", list.size() == 3);
        check("add/get: [0] == A", "A".equals(list.get(0)));
        check("add/get: [2] == C", "C".equals(list.get(2)));
    }

    private static void testAddByIndex() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(10);
        list.add(30);
        list.add(1, 20); // [10, 20, 30]
        check("add(index): размер == 3", list.size() == 3);
        check("add(index): [1] == 20", list.get(1) == 20);
        check("add(index): [2] == 30", list.get(2) == 30);

        list.add(0, 5);   // [5, 10, 20, 30]
        check("add(index=0): [0] == 5", list.get(0) == 5);

        list.add(list.size(), 40); // в конец
        check("add(size): [4] == 40", list.get(4) == 40);
    }

    private static void testSet() {
        MyList<String> list = new MyArrayList<>();
        list.add("old");
        String prev = list.set(0, "new");
        check("set: возвращает старое", "old".equals(prev));
        check("set: новое значение", "new".equals(list.get(0)));
    }

    private static void testRemoveByIndex() {
        MyList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        Integer removed = list.remove(2);
        check("remove(index): вернул 2", removed == 2);
        check("remove(index): размер == 4", list.size() == 4);
        check("remove(index): [2] == 3", list.get(2) == 3);
        check("remove(index): [3] == 4", list.get(3) == 4);
    }

    private static void testRemoveByValue() {
        MyList<String> list = new MyArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        check("remove(value): true", list.remove("b"));
        check("remove(value): размер == 2", list.size() == 2);
        check("remove(value): [1] == c", "c".equals(list.get(1)));
        check("remove(value) отсутствующего: false", !list.remove("zzz"));
    }

    private static void testSizeAndIsEmpty() {
        MyList<String> list = new MyArrayList<>();
        check("isEmpty: пустой == true", list.isEmpty());
        list.add("x");
        check("isEmpty: непустой == false", !list.isEmpty());
        check("size: == 1", list.size() == 1);
    }

    private static void testClear() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.clear();
        check("clear: size == 0", list.size() == 0);
        check("clear: isEmpty == true", list.isEmpty());
    }

    private static void testContainsAndIndexOf() {
        MyList<String> list = new MyArrayList<>();
        list.add("a");
        list.add("b");
        list.add("a");
        check("contains(a) == true", list.contains("a"));
        check("contains(z) == false", !list.contains("z"));
        check("indexOf(a) == 0", list.indexOf("a") == 0);
        check("indexOf(b) == 1", list.indexOf("b") == 1);
        check("indexOf(z) == -1", list.indexOf("z") == -1);
    }

    private static void testAutoResize() {
        MyList<Integer> list = new MyArrayList<>(2);
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        check("auto-resize: size == 1000", list.size() == 1000);
        boolean allOk = true;
        for (int i = 0; i < 1000; i++) {
            if (list.get(i) != i) {
                allOk = false;
                break;
            }
        }
        check("auto-resize: все элементы на месте", allOk);
    }

    private static void testForEachLoop() {
        MyList<Integer> list = new MyArrayList<>();
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }
        int sum = 0;
        for (Integer v : list) {
            sum += v;
        }
        check("for-each: сумма == 15", sum == 15);
    }

    private static void testIteratorRemove() {
        MyList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(i);
        }
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            Integer v = it.next();
            if (v % 2 == 0) {
                it.remove();
            }
        }
        check("iterator.remove: size == 3", list.size() == 3);
        check("iterator.remove: [0] == 1", list.get(0) == 1);
        check("iterator.remove: [1] == 3", list.get(1) == 3);
        check("iterator.remove: [2] == 5", list.get(2) == 5);
    }

    private static void testIteratorNoSuchElement() {
        MyList<String> list = new MyArrayList<>();
        list.add("a");
        Iterator<String> it = list.iterator();
        it.next();
        checkThrows("iterator: NoSuchElementException",
                NoSuchElementException.class, it::next);
    }

    private static void testStreamCount() {
        MyList<Integer> list = new MyArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        check("stream: count == 10", list.stream().count() == 10);
        check("stream: sum == 55",
                list.stream().mapToInt(Integer::intValue).sum() == 55);
    }

    private static void testStreamFilter() {
        MyList<Integer> list = new MyArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        long evens = list.stream().filter(x -> x % 2 == 0).count();
        check("stream.filter: evens == 5", evens == 5);
    }

    private static void testStreamCollect() {
        MyList<String> list = new MyArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("cherry");

        List<String> upper = list.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        check("stream.collect: размер == 3", upper.size() == 3);
        check("stream.collect: [0] == APPLE", "APPLE".equals(upper.get(0)));
        check("stream.collect: [2] == CHERRY", "CHERRY".equals(upper.get(2)));
    }

    private static void testToString() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        check("toString: [1, 2, 3]", "[1, 2, 3]".equals(list.toString()));
    }

    private static void testIllegalArguments() {
        MyList<String> list = new MyArrayList<>();
        list.add("a");

        checkThrows("get(-1) → IndexOutOfBoundsException",
                IndexOutOfBoundsException.class, () -> list.get(-1));
        checkThrows("get(size) → IndexOutOfBoundsException",
                IndexOutOfBoundsException.class, () -> list.get(list.size()));
        checkThrows("remove(5) → IndexOutOfBoundsException",
                IndexOutOfBoundsException.class, () -> list.remove(5));
        checkThrows("add(-1, x) → IndexOutOfBoundsException",
                IndexOutOfBoundsException.class, () -> list.add(-1, "x"));
        checkThrows("add(size+1, x) → IndexOutOfBoundsException",
                IndexOutOfBoundsException.class, () -> list.add(list.size() + 1, "x"));
        checkThrows("new MyArrayList(-1) → IllegalArgumentException",
                IllegalArgumentException.class, () -> new MyArrayList<>(-1));
    }
}