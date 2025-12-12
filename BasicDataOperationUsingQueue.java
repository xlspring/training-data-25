import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

/**
 * Клас BasicDataOperationUsingQueue реалізує роботу з колекціями типу Queue для short.
 * 
 * <p>Основні функції класу:</p>
 * <ul>
 *   <li>{@link #runDataProcessing()} - Запускає комплекс операцій з даними.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив short.</li>
 *   <li>{@link #findInArray()} - Пошук значення в масиві short.</li>
 *   <li>{@link #locateMinMaxInArray()} - Знаходить мінімальне і максимальне значення в масиві.</li>
 *   <li>{@link #findInQueue()} - Пошук значення в черзі short.</li>
 *   <li>{@link #locateMinMaxInQueue()} - Знаходить граничні значення в черзі.</li>
 *   <li>{@link #performQueueOperations()} - Виконує операції peek і poll з чергою.</li>
 * </ul>
 * 
 */
public class BasicDataOperationUsingQueue {
    /**
     * Конфігурація для операцій з чергою.
     */
    public record QueueConfig(Short valueToSearch, Short[] array) { }

    private final QueueConfig config;
    private Short[] shortArray;
    private PriorityQueue<Short> shortPriorityQueue;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param shortValueToSearch Значення для пошуку
     * @param shortArray Масив short
     */
    BasicDataOperationUsingQueue(Short shortValueToSearch, Short[] shortArray) {
        this.config = new QueueConfig(shortValueToSearch, shortArray.clone());
        this.shortArray = shortArray.clone();
        this.shortPriorityQueue = new PriorityQueue<>(Arrays.asList(shortArray));
    }
    
    /**
     * Запускає комплексну обробку даних з використанням черги.
     * 
     * Метод завантажує дані, виконує операції з чергою та масивом short.
     */
    public void runDataProcessing() {
        // спочатку обробляємо чергу short
        PerformanceTracker.benchmark(this::findInQueue, "пошук елемента в Queue short").display();
        PerformanceTracker.benchmark(this::locateMinMaxInQueue, "визначення мін/макс в Queue").display();
        performQueueOperationsBenchmark();

        // потім працюємо з масивом
        PerformanceTracker.benchmark(this::findInArray, "пошук елемента в масивi short").display();
        PerformanceTracker.benchmark(this::locateMinMaxInArray, "визначення мін/макс в масиві").display();

        PerformanceTracker.benchmark(this::performArraySorting, "упорядкування масиву значень").display();

        PerformanceTracker.benchmark(this::findInArray, "пошук елемента в масивi short (після сортування)").display();
        PerformanceTracker.benchmark(this::locateMinMaxInArray, "визначення мін/макс в масиві (після сортування)").display();

        // зберігаємо відсортований масив до файлу
        DataFileHandler.writeArrayToFile(shortArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Сортує масив об'єктiв short та виводить початковий i вiдсортований масиви.
     */
    private void performArraySorting() {
        shortArray = Arrays.stream(shortArray)
            .sorted()
            .toArray(Short[]::new);
    }

    /**
     * Здійснює пошук конкретного значення в масиві short.
     */
    private void findInArray() {
        int position = IntStream.range(0, shortArray.length)
            .filter(i -> config.valueToSearch().equals(shortArray[i]))
            .findFirst()
            .orElse(-1);
    }

    /**
     * Визначає найменше та найбільше значення в масиві short.
     */
    private void locateMinMaxInArray() {
        if (shortArray == null || shortArray.length == 0) {
            return;
        }

        Short minValue = Arrays.stream(shortArray)
            .min(Short::compareTo)
            .orElse(null);

        Short maxValue = Arrays.stream(shortArray)
            .max(Short::compareTo)
            .orElse(null);
    }

    /**
     * Здійснює пошук конкретного значення в черзі short.
     */
    private void findInQueue() {
        boolean elementExists = this.shortPriorityQueue.contains(config.valueToSearch());
    }

    /**
     * Визначає найменше та найбільше значення в черзі short.
     */
    private void locateMinMaxInQueue() {
        if (shortPriorityQueue == null || shortPriorityQueue.isEmpty()) {
            return;
        }

        Short minValue = shortPriorityQueue.stream()
            .min(Short::compareTo)
            .orElse(null);

        Short maxValue = shortPriorityQueue.stream()
            .max(Short::compareTo)
            .orElse(null);
    }

    /**
     * Виконує бенчмарк операцій peek і poll з чергою short.
     */
    private void performQueueOperationsBenchmark() {
        if (shortPriorityQueue == null || shortPriorityQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // Бенчмарк peek
        PerformanceTracker.benchmark(() -> {
            Short headElement = shortPriorityQueue.peek();
        }, "операція peek в Queue").display();

        // Для poll потрібно відновлювати чергу кожну ітерацію
        System.out.println("\n--- Одноразові операції poll ---");
        Short headElement = shortPriorityQueue.peek();
        System.out.println("Головний елемент черги (peek): " + headElement);

        headElement = shortPriorityQueue.poll();
        System.out.println("Видалений елемент черги (poll): " + headElement);

        headElement = shortPriorityQueue.peek();
        System.out.println("Новий головний елемент черги: " + headElement);
    }
}
