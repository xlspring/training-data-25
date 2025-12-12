import java.util.*;
import java.util.stream.Collectors;

/**
 * Клас BasicDataOperationUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення.
 *
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними Map.</li>
 *   <li>{@link #findByKey()} - Здійснює пошук елемента за ключем в Map.</li>
 *   <li>{@link #findByValue()} - Здійснює пошук елемента за значенням в Map.</li>
 *   <li>{@link #addEntry()} - Додає новий запис до Map.</li>
 *   <li>{@link #removeByKey()} - Видаляє запис з Map за ключем.</li>
 *   <li>{@link #removeByValue()} - Видаляє записи з Map за значенням.</li>
 *   <li>{@link #sortByKey()} - Сортує Map за ключами.</li>
 *   <li>{@link #sortByValue()} - Сортує Map за значеннями.</li>
 * </ul>
 */
public class BasicDataOperationUsingMap {
	/**
	 * Внутрішній запис Cow для зберігання інформації про домашню тварину.
	 */
	public record Cow(String nickname, Double milking) { }

	/**
	 * Конфігурація для операцій пошуку та видалення.
	 */
	public record MapConfig(Cow keyToSearchAndDelete, Cow keyToAdd, String valueToSearchAndDelete, String valueToAdd) { }

	private final MapConfig config;
	private HashMap<Cow, String> hashmap;
	private LinkedHashMap<Cow, String> linkedHashmap;

	private static final Comparator<Cow> COW_COMPARATOR = Comparator.comparing(Cow::nickname).thenComparing(Cow::milking, Collections.reverseOrder());

	/**
	 * Конструктор, який ініціалізує об'єкт з готовими даними.
	 *
	 * @param hashmap HashMap з початковими даними (ключ: Cow, значення: ім'я власника)
	 * @param linkedHashmap LinkedHashMap з початковими даними (ключ: Cow, значення: ім'я власника)
	 */
	BasicDataOperationUsingMap(HashMap<Cow, String> hashmap, LinkedHashMap<Cow, String> linkedHashmap) {
		this.config = new MapConfig(
			new Cow("Зіронька", 15.2),
			new Cow("Малинка", 17.5),
			"Василина",
			"Софія"
		);
		this.hashmap = new HashMap<>(hashmap);
		this.linkedHashmap = new LinkedHashMap<>(linkedHashmap);
	}

	/**
	 * Виконує комплексні операції з Map.
	 *
	 * Метод виконує різноманітні операції з Map: пошук, додавання, видалення та сортування.
	 */
	public void executeDataOperations() {
		// Спочатку працюємо з HashMap
		System.out.println("========= Операції з HashMap =========");
		System.out.println("Початковий розмір HashMap: " + hashmap.size());

		// Пошук до сортування
		PerformanceTracker.benchmark(this::findByKeyInHashMap, "пошук за ключем в HashMap").display();
		PerformanceTracker.benchmark(this::findByValueInHashMap, "пошук за значенням в HashMap").display();

		PerformanceTracker.benchmark(this::sortHashMap, "сортування HashMap за ключами").display();

		// Пошук після сортування
		PerformanceTracker.benchmark(this::findByKeyInHashMap, "пошук за ключем в HashMap (після сортування)").display();
		PerformanceTracker.benchmark(this::findByValueInHashMap, "пошук за значенням в HashMap (після сортування)").display();

		PerformanceTracker.benchmark(this::addEntryToHashMap, "додавання запису до HashMap").display();
		PerformanceTracker.benchmark(this::removeByKeyFromHashMap, "видалення за ключем з HashMap").display();
		PerformanceTracker.benchmark(this::removeByValueFromHashMap, "видалення за значенням з HashMap").display();

		System.out.println("Кінцевий розмір HashMap: " + hashmap.size());

		// Потім обробляємо LinkedHashMap
		System.out.println("\n\n========= Операції з LinkedHashMap =========");
		System.out.println("Початковий розмір LinkedHashMap: " + linkedHashmap.size());

		// Пошук до сортування
		PerformanceTracker.benchmark(this::findByKeyInLinkedHashMap, "пошук за ключем в LinkedHashMap").display();
		PerformanceTracker.benchmark(this::findByValueInLinkedHashMap, "пошук за значенням в LinkedHashMap").display();

		PerformanceTracker.benchmark(this::sortLinkedHashMap, "сортування LinkedHashMap за ключами").display();

		// Пошук після сортування
		PerformanceTracker.benchmark(this::findByKeyInLinkedHashMap, "пошук за ключем в LinkedHashMap (після сортування)").display();
		PerformanceTracker.benchmark(this::findByValueInLinkedHashMap, "пошук за значенням в LinkedHashMap (після сортування)").display();

		PerformanceTracker.benchmark(this::addEntryToLinkedHashMap, "додавання запису до LinkedHashMap").display();
		PerformanceTracker.benchmark(this::removeByKeyFromLinkedHashMap, "видалення за ключем з LinkedHashMap").display();
		PerformanceTracker.benchmark(this::removeByValueFromLinkedHashMap, "видалення за значенням з LinkedHashMap").display();

		System.out.println("Кінцевий розмір LinkedHashMap: " + linkedHashmap.size());
	}


	// ===== Методи для HashMap =====

	/**
	 * Сортує HashMap за ключами.
	 */
	private void sortHashMap() {
		hashmap = hashmap.entrySet().stream()
						.sorted(Map.Entry.comparingByKey(COW_COMPARATOR))
						.collect(Collectors.toMap(
								Map.Entry::getKey,
								Map.Entry::getValue,
								(e1, e2) -> e1,
								HashMap::new
						));
	}

	/**
	 * Здійснює пошук елемента за ключем в HashMap.
	 */
	void findByKeyInHashMap() {
		Map.Entry<Cow, String> result = hashmap.entrySet().stream()
				.filter(entry -> entry.getKey().equals(config.keyToSearchAndDelete()))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Здійснює пошук елемента за значенням в HashMap.
	 */
	void findByValueInHashMap() {
		Map.Entry<Cow, String> result = hashmap.entrySet().stream()
						.filter(entry -> entry.getValue().equals(config.valueToSearchAndDelete()))
						.findFirst()
						.orElse(null);
	}

	/**
	 * Додає новий запис до HashMap.
	 */
	void addEntryToHashMap() {
		hashmap.put(config.keyToAdd(), config.valueToAdd());
	}

	/**
	 * Видаляє запис з HashMap за ключем.
	 */
	void removeByKeyFromHashMap() {
		hashmap = hashmap.entrySet().stream()
				.filter(entry -> !entry.getKey().equals(config.keyToSearchAndDelete()))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						HashMap::new
				));
	}

	/**
	 * Видаляє записи з HashMap за значенням.
	 */
	void removeByValueFromHashMap() {
		List<Cow> keysToRemove = hashmap.entrySet().stream()
				.filter(entry -> entry.getValue() != null && entry.getValue().equals(config.valueToSearchAndDelete()))
				.map(Map.Entry::getKey)
				.toList();

		keysToRemove.forEach(hashmap::remove);
	}

	// ===== Методи для LinkedHashMap =====

	/**
	* Сортує LinkedHashMap за ключами
	*/
	private void sortLinkedHashMap() {
		linkedHashmap = linkedHashmap.entrySet().stream()
				.sorted(Map.Entry.comparingByKey(COW_COMPARATOR))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
	}

	/**
	 * Здійснює пошук елемента за ключем в LinkedHashMap.
	 */
	void findByKeyInLinkedHashMap() {
		Map.Entry<Cow, String> result = linkedHashmap.entrySet().stream()
				.filter(entry -> entry.getKey().equals(config.keyToSearchAndDelete()))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Здійснює пошук елемента за значенням в LinkedHashMap.
	 */
	void findByValueInLinkedHashMap() {
		Map.Entry<Cow, String> result = linkedHashmap.entrySet().stream()
				.filter(entry -> entry.getValue().equals(config.valueToSearchAndDelete()))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Додає новий запис до LinkedHashMap.
	 */
	void addEntryToLinkedHashMap() {
		linkedHashmap.put(config.keyToAdd(), config.valueToAdd());
	}

	/**
	 * Видаляє запис з LinkedHashMap за ключем.
	 */
	void removeByKeyFromLinkedHashMap() {
		linkedHashmap = linkedHashmap.entrySet().stream()
				.filter(entry -> !entry.getKey().equals(config.keyToSearchAndDelete()))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
	}

	/**
	 * Видаляє записи з LinkedHashMap за значенням.
	 */
	void removeByValueFromLinkedHashMap() {
		List<Cow> keysToRemove = linkedHashmap.entrySet().stream()
				.filter(entry -> entry.getValue() != null && entry.getValue().equals(config.valueToSearchAndDelete()))
				.map(Map.Entry::getKey)
				.toList();

		keysToRemove.forEach(linkedHashmap::remove);
	}

	/**
	 * Головний метод для запуску програми.
	 */
	public static void main(String[] args) {
		HashMap<Cow, String> hashMap = new HashMap<>();

		hashMap.put(new Cow("Буря", 12.5), "Степан");
		hashMap.put(new Cow("Зіронька", 15.2), "Оксана");
		hashMap.put(new Cow("Маруся", 18.7), "Василина");
		hashMap.put(new Cow("Ряба", 10.8), "Тарас");
		hashMap.put(new Cow("Зіронька", 20.3), "Мирослав");
		hashMap.put(new Cow("Цвітка", 14.6), "Василина");
		hashMap.put(new Cow("Білянка", 16.9), "Наталія");
		hashMap.put(new Cow("Ластівка", 13.1), "Орест");
		hashMap.put(new Cow("Маруся", 19.4), "Оксана");
		hashMap.put(new Cow("Калинка", 11.7), "Ярослав");

		LinkedHashMap<Cow, String> linkedHashmap = new LinkedHashMap<>(hashMap);

		// Створюємо об'єкт і виконуємо операції
		BasicDataOperationUsingMap operations = new BasicDataOperationUsingMap(hashMap, linkedHashmap);
		operations.executeDataOperations();
	}
}
