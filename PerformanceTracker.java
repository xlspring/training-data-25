import java.util.Arrays;
import java.lang.Runnable;

/**
 * Клас PerformanceTracker відстежує продуктивність операцій з даними.
 * Підтримує бенчмаркінг з 10,000 ітерацій та обчислення статистики.
 */
public class PerformanceTracker {
    private static final int BENCHMARK_ITERATIONS = 10_000;
    private static final int WARMUP_ITERATIONS = 1_000;

    /**
     * Результати бенчмарку з статистикою.
     */
    public record BenchmarkResult(
        String operationName,
        long[] timings,
        double averageNs,
        double medianNs,
        double percentile95Ns,
        double percentile99Ns,
        long minNs,
        long maxNs,
        double stdDevNs
    ) {
        public void display() {
            System.out.println("\n========= Бенчмарк операції '" + operationName + "' =========");
            System.out.printf("  Ітерацій: %,d%n", timings.length);
            System.out.printf("  Середнє: %.2f нс (%.4f мс)%n", averageNs, averageNs / 1_000_000);
            System.out.printf("  Медіана: %.2f нс%n", medianNs);
            System.out.printf("  95-й перцентиль: %.2f нс%n", percentile95Ns);
            System.out.printf("  99-й перцентиль: %.2f нс%n", percentile99Ns);
            System.out.printf("  Мінімум: %,d нс%n", minNs);
            System.out.printf("  Максимум: %,d нс%n", maxNs);
            System.out.printf("  Стандартне відхилення: %.2f нс%n", stdDevNs);
            System.out.println("=".repeat(50));
        }
    }

    /**
     * Відображає тривалість виконання операції в наносекундах (legacy метод).
     * 
     * @param startTime Початковий час операції в наносекундах.
     * @param operationName Назва операції.
     */
    public static void displayOperationTime(long startTime, String operationName) {
        long finishTime = System.nanoTime();
        long executionTime = (finishTime - startTime);
        System.out.println("\n========= Тривалість операції '" + operationName + "': " + executionTime + " нс =========");
    }

    /**
     * Виконує бенчмарк операції з 10,000 ітераціями та обчислює статистику.
     * 
     * @param operation Операція для бенчмарку.
     * @param operationName Назва операції.
     * @return Результат бенчмарку зі статистикою.
     */
    public static BenchmarkResult benchmark(Runnable operation, String operationName) {
        // Прогрів JVM
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            operation.run();
        }

        // Збір вимірів
        long[] timings = new long[BENCHMARK_ITERATIONS];
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            long start = System.nanoTime();
            operation.run();
            timings[i] = System.nanoTime() - start;
        }

        return computeStatistics(operationName, timings);
    }

    /**
     * Виконує бенчмарк операції з заданою кількістю ітерацій.
     * 
     * @param operation Операція для бенчмарку.
     * @param operationName Назва операції.
     * @param iterations Кількість ітерацій.
     * @return Результат бенчмарку зі статистикою.
     */
    public static BenchmarkResult benchmark(Runnable operation, String operationName, int iterations) {
        // Прогрів JVM
        int warmup = Math.min(iterations / 10, WARMUP_ITERATIONS);
        for (int i = 0; i < warmup; i++) {
            operation.run();
        }

        // Збір вимірів
        long[] timings = new long[iterations];
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            operation.run();
            timings[i] = System.nanoTime() - start;
        }

        return computeStatistics(operationName, timings);
    }

    /**
     * Обчислює статистику з масиву вимірів.
     */
    private static BenchmarkResult computeStatistics(String operationName, long[] timings) {
        // Сортуємо для обчислення перцентилів
        long[] sorted = timings.clone();
        Arrays.sort(sorted);

        // Середнє
        double sum = 0;
        for (long t : timings) {
            sum += t;
        }
        double average = sum / timings.length;

        // Медіана
        double median;
        int mid = sorted.length / 2;
        if (sorted.length % 2 == 0) {
            median = (sorted[mid - 1] + sorted[mid]) / 2.0;
        } else {
            median = sorted[mid];
        }

        // Перцентилі
        double p95 = percentile(sorted, 95);
        double p99 = percentile(sorted, 99);

        // Мін/Макс
        long min = sorted[0];
        long max = sorted[sorted.length - 1];

        // Стандартне відхилення
        double variance = 0;
        for (long t : timings) {
            variance += Math.pow(t - average, 2);
        }
        variance /= timings.length;
        double stdDev = Math.sqrt(variance);

        return new BenchmarkResult(operationName, timings, average, median, p95, p99, min, max, stdDev);
    }

    /**
     * Обчислює перцентиль відсортованого масиву.
     */
    private static double percentile(long[] sorted, double percentile) {
        double index = (percentile / 100.0) * (sorted.length - 1);
        int lower = (int) Math.floor(index);
        int upper = (int) Math.ceil(index);
        
        if (lower == upper) {
            return sorted[lower];
        }
        
        double fraction = index - lower;
        return sorted[lower] * (1 - fraction) + sorted[upper] * fraction;
    }
}
