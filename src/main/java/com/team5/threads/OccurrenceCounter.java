package com.team5.threads;

import com.team5.collection.MyList;

/**
 * Многопоточный подсчёт вхождений элемента в коллекцию.
 */
public class OccurrenceCounter {

    private final int threadCount;

    public OccurrenceCounter(int threadCount) {
        if (threadCount < 1) {
            throw new IllegalArgumentException("Число потоков должно быть >= 1");
        }
        this.threadCount = threadCount;
    }

    public <T> int count(MyList<T> list, T target) {
        int size = list.size();
        int threads = Math.min(threadCount, Math.max(size, 1));
        int[] partial = new int[threads];
        Thread[] workers = new Thread[threads];
        int chunk = (size + threads - 1) / threads;

        for (int t = 0; t < threads; t++) {
            final int id = t;
            final int from = t * chunk;
            final int to = Math.min(from + chunk, size);
            workers[t] = new Thread(() -> {
                int local = 0;
                for (int i = from; i < to; i++) {
                    if (list.get(i).equals(target)) {
                        local++;
                    }
                }
                partial[id] = local;
            }, "counter-" + t);
            workers[t].start();
        }

        int total = 0;
        for (int t = 0; t < threads; t++) {
            try {
                workers[t].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Подсчёт прерван", e);
            }
            total += partial[t];
        }
        return total;
    }

    public <T> int countAndPrint(MyList<T> list, T target) {
        int result = count(list, target);
        System.out.println("Количество вхождений " + target + ": " + result);
        return result;
    }
}
