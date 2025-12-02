import java.util.concurrent.*;

public class ParallelCPU {

    public static int countWordParallel(String text, String target, int threads) {
        if (threads < 1) threads = 1;

        String[] words = text.split("\\W+");
        int totalWords = words.length;

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Future<Integer>[] results = new Future[threads];

        int chunkSize = (int) Math.ceil(totalWords / (double) threads);

        for (int i = 0; i < threads; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, totalWords);

            results[i] = executor.submit(() -> {
                int localCount = 0;
                for (int k = start; k < end; k++) {
                    if (k >= totalWords) break;
                    if (words[k].equalsIgnoreCase(target)) localCount++;
                }
                return localCount;
            });
        }

        int total = 0;
        try {
            for (Future<Integer> f : results) total += f.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.shutdown();
        return total;
    }
}
