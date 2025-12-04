import java.util.Arrays;
import java.util.Collections;
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
    private Short shortValueToSearch;
    private Short[] shortArray;
    private PriorityQueue<Short> shortPriorityQueue;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param shortValueToSearch Значення для пошуку
     * @param shortArray Масив short
     */
    BasicDataOperationUsingQueue(Short shortValueToSearch, Short[] shortArray) {
        this.shortValueToSearch = shortValueToSearch;
        this.shortArray = shortArray;
        this.shortPriorityQueue = new PriorityQueue<>(Arrays.asList(shortArray));
    }
    
    /**
     * Запускає комплексну обробку даних з використанням черги.
     * 
     * Метод завантажує дані, виконує операції з чергою та масивом short.
     */
    public void runDataProcessing() {
        // спочатку обробляємо чергу short
        findInQueue();
        locateMinMaxInQueue();
        performQueueOperations();

        // потім працюємо з масивом
        findInArray();
        locateMinMaxInArray();

        performArraySorting();

        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до файлу
        DataFileHandler.writeArrayToFile(shortArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Сортує масив об'єктiв short та виводить початковий i вiдсортований масиви.
     * Вимiрює та виводить час, витрачений на сортування масиву в наносекундах.
     */
    private void performArraySorting() {
        // вимірюємо тривалість упорядкування масиву short
        long timeStart = System.nanoTime();

        shortArray = Arrays.stream(shortArray)
            .sorted()
            .toArray(Short[]::new);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву значень");
    }

    /**
     * Здійснює пошук конкретного значення в масиві short.
     */
    private void findInArray() {
        // відстежуємо час виконання пошуку в масиві
        long timeStart = System.nanoTime();

				int position = IntStream.range(0, shortArray.length)
					.filter(i -> shortValueToSearch.equals(shortArray[i]))
					.findFirst()
					.orElse(-1);
        
        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в масивi short");

        if (position >= 0) {
            System.out.println("Елемент '" + shortValueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + shortValueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві short.
     */
    private void locateMinMaxInArray() {
        if (shortArray == null || shortArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        // відстежуємо час на визначення граничних значень
        long timeStart = System.nanoTime();

				Short minValue = Arrays.stream(shortArray)
						.min(Short::compareTo)
						.orElse(null);

				Short maxValue = Arrays.stream(shortArray)
						.max(Short::compareTo)
						.orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмального i максимального значення в масивi");

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Здійснює пошук конкретного значення в черзі short.
     */
    private void findInQueue() {
        // вимірюємо час пошуку в черзі
        long timeStart = System.nanoTime();

        boolean elementExists = this.shortPriorityQueue.contains(shortValueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в Queue short");

        if (elementExists) {
            System.out.println("Елемент '" + shortValueToSearch + "' знайдено в Queue");
        } else {
            System.out.println("Елемент '" + shortValueToSearch + "' відсутній в Queue.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в черзі short.
     */
    private void locateMinMaxInQueue() {
        if (shortPriorityQueue == null || shortPriorityQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // відстежуємо час пошуку граничних значень
        long timeStart = System.nanoTime();

			Short minValue = shortPriorityQueue.stream()
					.min(Short::compareTo)
					.orElse(null);

			Short maxValue = shortPriorityQueue.stream()
					.max(Short::compareTo)
					.orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмального i максимального значення в Queue");

        System.out.println("Найменше значення в Queue: " + minValue);
        System.out.println("Найбільше значення в Queue: " + maxValue);
    }

    /**
     * Виконує операції peek і poll з чергою short.
     */
    private void performQueueOperations() {
        if (shortPriorityQueue == null || shortPriorityQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        Short headElement = shortPriorityQueue.peek();
        System.out.println("Головний елемент черги (peek): " + headElement);

        headElement = shortPriorityQueue.poll();
        System.out.println("Видалений елемент черги (poll): " + headElement);

        headElement = shortPriorityQueue.peek();
        System.out.println("Новий головний елемент черги: " + headElement);
    }
}