import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Клас BasicDataOperationUsingList реалізує операції з колекціями типу ArrayList для даних short.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив елементів short.</li>
 *   <li>{@link #findInArray()} - Здійснює пошук елемента в масиві short.</li>
 *   <li>{@link #locateMinMaxInArray()} - Визначає найменше і найбільше значення в масиві.</li>
 *   <li>{@link #sortList()} - Сортує колекцію List з short.</li>
 *   <li>{@link #findInList()} - Пошук конкретного значення в списку.</li>
 *   <li>{@link #locateMinMaxInList()} - Пошук мінімального і максимального значення в списку.</li>
 * </ul>
 */
public class BasicDataOperationUsingList {
    /**
     * Конфігурація для операцій з даними.
     */
    public record ListConfig(Short valueToSearch, Short[] array) { }

    private final ListConfig config;
    private Short[] shortArray;
    private Vector<Short> shortList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param shortValueToSearch Значення для пошуку
     * @param shortArray Масив short
     */
    BasicDataOperationUsingList(Short shortValueToSearch, Short[] shortArray) {
        this.config = new ListConfig(shortValueToSearch, shortArray.clone());
        this.shortArray = shortArray.clone();
        this.shortList = new Vector<>(Arrays.asList(shortArray));
    }
    
    /**
     * Виконує комплексні операції з структурами даних.
     * 
     * Метод завантажує масив і список об'єктів short, 
     * здійснює сортування та пошукові операції.
     */
    public void executeDataOperations() {
        // спочатку працюємо з колекцією List
        PerformanceTracker.benchmark(this::findInList, "пошук елемента в List short").display();
        PerformanceTracker.benchmark(this::locateMinMaxInList, "визначення мін/макс в List").display();
        
        PerformanceTracker.benchmark(this::sortList, "упорядкування ArrayList short").display();
        
        PerformanceTracker.benchmark(this::findInList, "пошук елемента в List short (після сортування)").display();
        PerformanceTracker.benchmark(this::locateMinMaxInList, "визначення мін/макс в List (після сортування)").display();

        // потім обробляємо масив дати та часу
        PerformanceTracker.benchmark(this::findInArray, "пошук елемента в масивi short").display();
        PerformanceTracker.benchmark(this::locateMinMaxInArray, "визначення мін/макс в масиві").display();

        PerformanceTracker.benchmark(this::performArraySorting, "упорядкування масиву short").display();
        
        PerformanceTracker.benchmark(this::findInArray, "пошук елемента в масивi short (після сортування)").display();
        PerformanceTracker.benchmark(this::locateMinMaxInArray, "визначення мін/макс в масиві (після сортування)").display();

        // зберігаємо відсортований масив до окремого файлу
        DataFileHandler.writeArrayToFile(shortArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Упорядковує масив об'єктів short за зростанням.
     */
    void performArraySorting() {
        shortArray = Arrays.stream(shortArray)
            .sorted()
            .toArray(Short[]::new);
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    void findInArray() {
        int position = IntStream.range(0, shortArray.length)
            .filter(i -> config.valueToSearch().equals(shortArray[i]))
            .findFirst()
            .orElse(-1);
    }

    /**
     * Визначає найменше та найбільше значення в масиві дати та часу.
     */
    void locateMinMaxInArray() {
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
     * Шукає конкретне значення дати та часу в колекції ArrayList.
     */
    void findInList() {
        int position = IntStream.range(0, shortList.size())
            .filter(i -> config.valueToSearch().equals(shortList.get(i)))
            .findFirst()
            .orElse(-1);
    }

    /**
     * Визначає найменше і найбільше значення в колекції ArrayList з датами.
     */
    void locateMinMaxInList() {
        if (shortList == null || shortList.isEmpty()) {
            return;
        }

        Short minValue = shortList.stream()
            .min(Short::compareTo)
            .orElse(null);

        Short maxValue = shortList.stream()
            .max(Short::compareTo)
            .orElse(null);
    }

    /**
     * Упорядковує колекцію List з об'єктами short за зростанням.
     */
    void sortList() {
        shortList = shortList.stream()
            .sorted()
            .collect(Collectors.toCollection(Vector::new));
    }
}
