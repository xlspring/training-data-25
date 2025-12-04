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
    private Short shortValueToSearch;
    private Short[] shortArray;
    private Vector<Short> shortList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param shortValueToSearch Значення для пошуку
     * @param shortArray Масив short
     */
    BasicDataOperationUsingList(Short shortValueToSearch, Short[] shortArray) {
        this.shortValueToSearch = shortValueToSearch;
        this.shortArray = shortArray;
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
        findInList();
        locateMinMaxInList();
        
        sortList();
        
        findInList();
        locateMinMaxInList();

        // потім обробляємо масив дати та часу
        findInArray();
        locateMinMaxInArray();

        performArraySorting();
        
        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до окремого файлу
        DataFileHandler.writeArrayToFile(shortArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Упорядковує масив об'єктів short за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    void performArraySorting() {
        long timeStart = System.nanoTime();

        shortArray = Arrays.stream(shortArray)
            .sorted()
            .toArray(Short[]::new);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву short");
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    void findInArray() {
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
     * Визначає найменше та найбільше значення в масиві дати та часу.
     */
    void locateMinMaxInArray() {
        if (shortArray == null || shortArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        short minValue = Arrays.stream(shortArray)
            .min(Short::compareTo)
            .orElse(null);

        short maxValue = Arrays.stream(shortArray)
            .max(Short::compareTo)
            .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмального i максимального значення в масивi");

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Шукає конкретне значення дати та часу в колекції ArrayList.
     */
    void findInList() {
        long timeStart = System.nanoTime();

        int position = IntStream.range(0, shortList.size())
            .filter(i -> shortValueToSearch.equals(shortList.get(i)))
            .findFirst()
            .orElse(-1);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в List short");

        if (position >= 0) {
            System.out.println("Елемент '" + shortValueToSearch + "' знайдено в ArrayList за позицією: " + position);
        } else {
            System.out.println("Елемент '" + shortValueToSearch + "' відсутній в ArrayList.");
        }
    }

    /**
     * Визначає найменше і найбільше значення в колекції ArrayList з датами.
     */
    void locateMinMaxInList() {
        if (shortList == null || shortList.isEmpty()) {
            System.out.println("Колекція ArrayList є пустою або не ініціалізованою.");
            return;
        }

        long timeStart = System.nanoTime();

        short minValue = shortList.stream()
            .min(Short::compareTo)
            .orElse(null);

        short maxValue = shortList.stream()
            .max(Short::compareTo)
            .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмального i максимального значення в List");

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    /**
     * Упорядковує колекцію List з об'єктами short за зростанням.
     * Відстежує та виводить час виконання операції сортування.
     */
    void sortList() {
        long timeStart = System.nanoTime();

        shortList = shortList.stream()
								.sorted()
								.collect(Collectors.toCollection(Vector::new));

			PerformanceTracker.displayOperationTime(timeStart, "упорядкування ArrayList short");
    }
}
