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
    Short shortValueToSearch;
		Short[] shortArray;
    TreeSet<Short> shortSet = new TreeSet<>();

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param shortValueToSearch Значення для пошуку
     * @param shortArray Масив short
     */
    BasicDataOperationUsingSet(Short shortValueToSearch, Short[] shortArray) {
        this.shortValueToSearch = shortValueToSearch;
        this.shortArray = shortArray;
        this.shortSet = new TreeSet<>(Arrays.asList(shortArray));
    }
    
    /**
     * Запускає комплексний аналіз даних з використанням множини HashSet.
     * 
     * Метод завантажує дані, виконує операції з множиною та масивом short.
     */
    public void executeDataAnalysis() {
        // спочатку аналізуємо множину short
        findInSet();
        locateMinMaxInSet();
        analyzeArrayAndSet();

        // потім обробляємо масив
        findInArray();
        locateMinMaxInArray();

        performArraySorting();

        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до файлу
        DataFileHandler.writeArrayToFile(shortArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Упорядковує масив об'єктів short за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    private void performArraySorting() {
        long timeStart = System.nanoTime();

				shortArray = Arrays.stream(shortArray)
						.sorted()
						.toArray(Short[]::new);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву short");
    }

    /**
     * Здійснює пошук конкретного значення в масиві short.
     */
    private void findInArray() {
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
     * Здійснює пошук конкретного значення в множині short.
     */
    private void findInSet() {
        long timeStart = System.nanoTime();

        boolean elementExists = shortSet.stream()
            .anyMatch(shortVal -> shortVal.equals(shortValueToSearch));

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в HashSet short");

        if (elementExists) {
            System.out.println("Елемент '" + shortValueToSearch + "' знайдено в HashSet");
        } else {
            System.out.println("Елемент '" + shortValueToSearch + "' відсутній в HashSet.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в множині short.
     */
    private void locateMinMaxInSet() {
        if (shortSet == null || shortSet.isEmpty()) {
            System.out.println("HashSet є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        Short minValue = shortSet.stream()
            .min(Short::compareTo)
            .orElse(null);

				Short maxValue = shortSet.stream()
						.max(Short::compareTo)
						.orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмального i максимального значення в HashSet");

        System.out.println("Найменше значення в HashSet: " + minValue);
        System.out.println("Найбільше значення в HashSet: " + maxValue);
    }

    /**
     * Аналізує та порівнює елементи масиву та множини.
     */
    private void analyzeArrayAndSet() {
        System.out.println("Кiлькiсть елементiв в масивi: " + shortArray.length);
        System.out.println("Кiлькiсть елементiв в HashSet: " + shortSet.size());

        boolean allElementsPresent = Arrays.stream(shortArray)
						.allMatch(shortSet::contains);

        if (allElementsPresent) {
            System.out.println("Всi елементи масиву наявні в HashSet.");
        } else {
            System.out.println("Не всi елементи масиву наявні в HashSet.");
        }
    }
}
