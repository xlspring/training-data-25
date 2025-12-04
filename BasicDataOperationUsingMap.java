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
	private final Cow KEY_TO_SEARCH_AND_DELETE = new Cow("Зіронька", 15.2);
	private final Cow KEY_TO_ADD = new Cow("Малинка", 17.5);

	private final String VALUE_TO_SEARCH_AND_DELETE = "Василина";
	private final String VALUE_TO_ADD = "Софія";

	private HashMap<Cow, String> hashmap;
	private LinkedHashMap<Cow, String> linkedHashmap;

	/**
	 * Внутрішній клас Cow для зберігання інформації про домашню тварину.
	 *
	 * Реалізує Comparable<Cow> для визначення природного порядку сортування.
	 * Природний порядок: спочатку за кличкою (nickname) за зростанням, потім за видом (species) за спаданням.
	 */
	public static class Cow implements Comparable<Cow> {
		private final String nickname;
		private final Double milking;

		public Cow(String nickname) {
			this.nickname = nickname;
			this.milking = null;
		}

		public Cow(String nickname, Double milking) {
			this.nickname = nickname;
			this.milking = milking;
		}

		public String getNickname() {
			return nickname;
		}

		public Double getMilking() {
			return milking;
		}

		/**
		 * Порівнює цей об'єкт Cow з іншим для визначення порядку сортування.
		 * Природний порядок: спочатку за кличкою (nickname) за зростанням, потім за надоями (milking) за зростанням.
		 *
		 * @param other Cow об'єкт для порівняння
		 * @return негативне число, якщо цей Cow < other;
		 *         0, якщо цей Cow == other;
		 *         позитивне число, якщо цей Cow > other
		 *
		 * Критерій порівняння: поля nickname (кличка) за зростанням та milking за зростанням.
		 *
		 * Цей метод використовується:
		 * - LinkedHashMap для автоматичного сортування ключів Cow за nickname (зростання), потім за milking (зростання)
		 * - Collections.sort() для сортування Map.Entry за ключами Cow
		 * - Collections.binarySearch() для пошуку в відсортованих колекціях
		 */
		@Override
		public int compareTo(Cow other) {
			if (other == null) return 1;

			// Спочатку порівнюємо за кличкою (за зростанням)
			int nicknameComparison = 0;
			if (this.nickname == null && other.nickname == null) {
				nicknameComparison = 0;
			} else if (this.nickname == null) {
				nicknameComparison = -1;
			} else if (other.nickname == null) {
				nicknameComparison = 1;
			} else {
				nicknameComparison = this.nickname.compareTo(other.nickname);
			}

			// Якщо клички різні, повертаємо результат
			if (nicknameComparison != 0) {
				return nicknameComparison;
			}

			// Якщо клички однакові, порівнюємо за видом (за спаданням - інвертуємо результат)
			if (this.milking == null && other.milking == null) return 0;
			if (this.milking == null) return 1;  // null йде в кінець при спаданні
			if (other.milking == null) return -1;
			return this.milking.compareTo(other.milking);  // Не інвертоване - зростання
		}

		/**
		 * Перевіряє рівність цього Cow з іншим об'єктом.
		 * Два Cow вважаються рівними, якщо їх клички (nickname) та види (species) однакові.
		 *
		 * @param obj об'єкт для порівняння
		 * @return true, якщо об'єкти рівні; false в іншому випадку
		 *
		 * Критерій рівності: поля nickname (кличка) та species (вид).
		 *
		 * Важливо: метод узгоджений з compareTo() - якщо equals() повертає true,
		 * то compareTo() повертає 0, оскільки обидва методи порівнюють за nickname та species.
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			Cow Cow = (Cow) obj;

			boolean nicknameEquals = Objects.equals(nickname, Cow.nickname);
			boolean milkingEquals = Objects.equals(milking, Cow.milking);

			return nicknameEquals && milkingEquals;
		}

		/**
		 * Повертає хеш-код для цього Cow.
		 *
		 * @return хеш-код, обчислений на основі nickname та species
		 *
		 * Базується на полях nickname та species для узгодженості з equals().
		 *
		 * Важливо: узгоджений з equals() - якщо два Cow рівні за equals()
		 * (мають однакові nickname та species), вони матимуть однаковий hashCode().
		 */
		@Override
		public int hashCode() {
			// Початкове значення: хеш-код поля nickname (або 0, якщо nickname == null)
			int result = nickname != null ? nickname.hashCode() : 0;

			// Комбінуємо хеш-коди полів за формулою: result = 31 * result + hashCode(поле)
			// Множник 31 - просте число, яке дає хороше розподілення хеш-кодів
			// і оптимізується JVM як (result << 5) - result
			// Додаємо хеш-код виду (або 0, якщо species == null) до загального результату
			result = 31 * result + (milking != null ? milking.hashCode() : 0);

			return result;
		}

		/**
		 * Повертає строкове представлення Cow.
		 *
		 * @return кличка тварини (nickname), вид (species) та hashCode
		 */
		@Override
		public String toString() {
			if (milking != null) {
				return "Cow{nickname='" + nickname + "', milking='" + milking + "', hashCode=" + hashCode() + "}";
			}
			return "Cow{nickname='" + nickname + "', hashCode=" + hashCode() + "}";
		}
	}

	/**
	 * Конструктор, який ініціалізує об'єкт з готовими даними.
	 *
	 * @param hashmap HashMap з початковими даними (ключ: Cow, значення: ім'я власника)
	 * @param linkedHashmap LinkedHashMap з початковими даними (ключ: Cow, значення: ім'я власника)
	 */
	BasicDataOperationUsingMap(HashMap<Cow, String> hashmap, LinkedHashMap<Cow, String> linkedHashmap) {
		this.hashmap = hashmap;
		this.linkedHashmap = linkedHashmap;
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
		findByKeyInHashMap();
		findByValueInHashMap();

		printHashMap();
		sortHashMap();
		printHashMap();

		// Пошук після сортування
		findByKeyInHashMap();
		findByValueInHashMap();

		addEntryToHashMap();

		removeByKeyFromHashMap();
		removeByValueFromHashMap();

		System.out.println("Кінцевий розмір HashMap: " + hashmap.size());

		// Потім обробляємо LinkedHashMap
		System.out.println("\n\n========= Операції з LinkedHashMap =========");
		System.out.println("Початковий розмір LinkedHashMap: " + linkedHashmap.size());


		// Пошук до сортування
		findByKeyInLinkedHashMap();
		findByValueInLinkedHashMap();

		printLinkedHashMap();
		sortLinkedHashMap();
		printLinkedHashMap();

		// Пошук після сортування
		findByKeyInLinkedHashMap();
		findByValueInLinkedHashMap();
		addEntryToLinkedHashMap();

		removeByKeyFromLinkedHashMap();
		removeByValueFromLinkedHashMap();

		System.out.println("Кінцевий розмір LinkedHashMap: " + linkedHashmap.size());
	}


	// ===== Методи для HashMap =====

	/**
	 * Виводить вміст HashMap без сортування.
	 * HashMap не гарантує жодного порядку елементів.
	 */
	private void printHashMap() {
		System.out.println("\n=== Пари ключ-значення в HashMap ===");
		long timeStart = System.nanoTime();

		hashmap.entrySet().forEach(entry -> {
			System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
		});

		PerformanceTracker.displayOperationTime(timeStart, "виведення пари ключ-значення в HashMap");
	}

	/**
	 * Сортує HashMap за ключами.
	 * Використовує Collections.sort() з природним порядком Cow (Cow.compareTo()).
	 * Перезаписує hashtable відсортованими даними.
	 */
	private void sortHashMap() {
		long timeStart = System.nanoTime();

		hashmap = hashmap.entrySet().stream()
						.sorted(Map.Entry.comparingByKey())
						.collect(Collectors.toMap(
								Map.Entry::getKey,
								Map.Entry::getValue,
								(e1, e2) -> e1,
								HashMap::new
						));

		PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap за ключами");
	}

	/**
	 * Здійснює пошук елемента за ключем в HashMap.
	 * Використовує Cow.hashCode() та Cow.equals() для пошуку.
	 */
	void findByKeyInHashMap() {
		long timeStart = System.nanoTime();

		Map.Entry<Cow, String> result = hashmap.entrySet().stream()
				.filter(entry -> entry.getKey().equals(KEY_TO_SEARCH_AND_DELETE))
				.findFirst()
				.orElse(null);

		PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в HashMap");

		if (result != null) {
			String value = hashmap.get(KEY_TO_SEARCH_AND_DELETE);
			System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
		} else {
			System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
		}
	}

	/**
	 * Здійснює пошук елемента за значенням в HashMap.
	 * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
	 */
	void findByValueInHashMap() {
		long timeStart = System.nanoTime();

		Map.Entry<Cow, String> result = hashmap.entrySet().stream()
						.filter(entry -> entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
						.findFirst()
						.orElse(null);

		PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в HashMap");

		if (result != null) {
			System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Cow: " + result.getKey());
		} else {
			System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
		}
	}

	/**
	 * Додає новий запис до HashMap.
	 */
	void addEntryToHashMap() {
		long timeStart = System.nanoTime();

		hashmap.put(KEY_TO_ADD, VALUE_TO_ADD);

		PerformanceTracker.displayOperationTime(timeStart, "додавання запису до HashMap");

		System.out.println("Додано новий запис: Cow='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
	}

	/**
	 * Видаляє запис з HashMap за ключем.
	 */
	void removeByKeyFromHashMap() {
		long timeStart = System.nanoTime();

		Map.Entry<Cow, String> removedValue = hashmap.entrySet().stream()
				.filter(entry -> entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
				.findFirst()
				.orElse(null);

		hashmap = hashmap.entrySet().stream()
				.filter(entry -> !entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						HashMap::new
				));

		PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з HashMap");

		if (removedValue != null) {
			System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue.getValue());
		} else {
			System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
		}
	}

	/**
	 * Видаляє записи з HashMap за значенням.
	 */
	void removeByValueFromHashMap() {
		long timeStart = System.nanoTime();

		List<Cow> keysToRemove = hashmap.entrySet().stream()
				.filter(entry -> entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
				.map(Map.Entry::getKey)
				.toList();

		keysToRemove.forEach(hashmap::remove);

		PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з HashMap");

		System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
	}

	// ===== Методи для HashMap =====

	/**
	 * Виводить вміст LinkedHashMap.
	 * LinkedHashMap автоматично відсортована за ключами (Cow nickname за зростанням, species за спаданням).
	 */
	private void printLinkedHashMap() {
		System.out.println("\n=== Пари ключ-значення в HashMap ===");

		long timeStart = System.nanoTime();


		PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в HashMap");
	}

	/**
	* Сортує LinkedHashMap за ключами
	*	Використовує Collections.sort() з природнім порядком Cow (Cow.compareTo()).
	* Перезаписує LinkedHashMap відсортованими даними
	*/
	private void sortLinkedHashMap() {
		long timeStart = System.nanoTime();

		linkedHashmap = linkedHashmap.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));

		PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap за ключами");
	}

	/**
	 * Здійснює пошук елемента за ключем в HashMap.
	 * Використовує Cow.compareTo() для навігації по дереву.
	 */
	void findByKeyInLinkedHashMap() {
		long timeStart = System.nanoTime();

		Map.Entry<Cow, String> result = linkedHashmap.entrySet().stream()
				.filter(entry -> entry.getKey().equals(KEY_TO_SEARCH_AND_DELETE))
				.findFirst()
				.orElse(null);

		PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в LinkedHashMap");

		if (result != null) {
			String value = linkedHashmap.get(KEY_TO_SEARCH_AND_DELETE);
			System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
		} else {
			System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
		}
	}

	/**
	 * Здійснює пошук елемента за значенням в LinkedHashMap.
	 * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
	 */
	void findByValueInLinkedHashMap() {
		long timeStart = System.nanoTime();

		Map.Entry<Cow, String> result = linkedHashmap.entrySet().stream()
				.filter(entry -> entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
				.findFirst()
				.orElse(null);

		PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в LinkedHashMap");

		if (result != null) {
			System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Cow: " + result.getKey());
		} else {
			System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
		}
	}

	/**
	 * Додає новий запис до LinkedHashMap.
	 */
	void addEntryToLinkedHashMap() {
		long timeStart = System.nanoTime();

		linkedHashmap.put(KEY_TO_ADD, VALUE_TO_ADD);

		PerformanceTracker.displayOperationTime(timeStart, "додавання запису до LinkedHashMap");

		System.out.println("Додано новий запис: Cow='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
	}

	/**
	 * Видаляє запис з LinkedHashMap за ключем.
	 */
	void removeByKeyFromLinkedHashMap() {
		long timeStart = System.nanoTime();

		Map.Entry<Cow, String> removedValue = linkedHashmap.entrySet().stream()
				.filter(entry -> entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
				.findFirst()
				.orElse(null);

		linkedHashmap = linkedHashmap.entrySet().stream()
				.filter(entry -> !entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
		PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з LinkedHashMap");

		if (removedValue != null) {
			System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
		} else {
			System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
		}
	}

	/**
	 * Видаляє записи з LinkedHashMap за значенням.
	 */
	void removeByValueFromLinkedHashMap() {
		long timeStart = System.nanoTime();

		List<Cow> keysToRemove = linkedHashmap.entrySet().stream()
				.filter(entry -> entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE))
				.map(Map.Entry::getKey)
				.toList();

		keysToRemove.forEach(linkedHashmap::remove);

		PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з LinkedHashMap");

		System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
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