import java.util.*;
import java.util.stream.IntStream;

/**
 * Клас BasicDataOperationUsingSet реалізує операції з множиною HashSet для short.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataAnalysis()} - Запускає аналіз даних.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив short.</li>
 *   <li>{@link #findInArray()} - Пошук значення в масиві short.</li>
 *   <li>{@link #locateMinMaxInArray()} - Знаходить граничні значення в масиві.</li>
 *   <li>{@link #findInSet()} - Пошук значення в множині short.</li>
 *   <li>{@link #locateMinMaxInSet()} - Знаходить мінімальне і максимальне значення в множині.</li>
 *   <li>{@link #analyzeArrayAndSet()} - Аналізує елементи масиву та множини.</li>
 * </ul>
 */
public class BasicDataOperationUsingSet {
    /**
     * Конфігурація для операцій з множиною.
     */
    public record SetConfig(Short valueToSearch, Short[] array) { }

    private final SetConfig config;
    private Short[] shortArray;
    private TreeSet<Short> shortSet;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param shortValueToSearch Значення для пошуку
     * @param shortArray Масив short
     */
    BasicDataOperationUsingSet(Short shortValueToSearch, Short[] shortArray) {
        this.config = new SetConfig(shortValueToSearch, shortArray.clone());
        this.shortArray = shortArray.clone();
        this.shortSet = new TreeSet<>(Arrays.asList(shortArray));
    }
    
    /**
     * Запускає комплексний аналіз даних з використанням множини HashSet.
     * 
     * Метод завантажує дані, виконує операції з множиною та масивом short.
     */
    public void executeDataAnalysis() {
        // спочатку аналізуємо множину short
        PerformanceTracker.benchmark(this::findInSet, "пошук елемента в HashSet short").display();
        PerformanceTracker.benchmark(this::locateMinMaxInSet, "визначення мін/макс в HashSet").display();
        PerformanceTracker.benchmark(this::analyzeArrayAndSet, "аналіз масиву та множини").display();

        // потім обробляємо масив
        PerformanceTracker.benchmark(this::findInArray, "пошук елемента в масивi short").display();
        PerformanceTracker.benchmark(this::locateMinMaxInArray, "визначення мін/макс в масиві").display();

        PerformanceTracker.benchmark(this::performArraySorting, "упорядкування масиву short").display();

        PerformanceTracker.benchmark(this::findInArray, "пошук елемента в масивi short (після сортування)").display();
        PerformanceTracker.benchmark(this::locateMinMaxInArray, "визначення мін/макс в масиві (після сортування)").display();

        // зберігаємо відсортований масив до файлу
        DataFileHandler.writeArrayToFile(shortArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Упорядковує масив об'єктів short за зростанням.
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
     * Здійснює пошук конкретного значення в множині short.
     */
    private void findInSet() {
        boolean elementExists = shortSet.stream()
            .anyMatch(shortVal -> shortVal.equals(config.valueToSearch()));
    }

    /**
     * Визначає найменше та найбільше значення в множині short.
     */
    private void locateMinMaxInSet() {
        if (shortSet == null || shortSet.isEmpty()) {
            return;
        }

        Short minValue = shortSet.stream()
            .min(Short::compareTo)
            .orElse(null);

        Short maxValue = shortSet.stream()
            .max(Short::compareTo)
            .orElse(null);
    }

    /**
     * Аналізує та порівнює елементи масиву та множини.
     */
    private void analyzeArrayAndSet() {
        boolean allElementsPresent = Arrays.stream(shortArray)
            .allMatch(shortSet::contains);
    }
}
