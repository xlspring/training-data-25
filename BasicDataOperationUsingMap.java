import java.util.*;

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
	 * Компаратор для сортування Map.Entry за значеннями String.
	 * Використовує метод String.compareTo() для порівняння імен власників.
	 */
	static class OwnerValueComparator implements Comparator<Map.Entry<Cow, String>> {
		@Override
		public int compare(Map.Entry<Cow, String> e1, Map.Entry<Cow, String> e2) {
			String v1 = e1.getValue();
			String v2 = e2.getValue();
			if (v1 == null && v2 == null) return 0;
			if (v1 == null) return -1;
			if (v2 == null) return 1;
			return v1.compareTo(v2);
		}
	}

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

		for (Map.Entry<Cow, String> entry : hashmap.entrySet()) {
			System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
		}

		PerformanceTracker.displayOperationTime(timeStart, "виведення пари ключ-значення в HashMap");
	}

	/**
	 * Сортує HashMap за ключами.
	 * Використовує Collections.sort() з природним порядком Cow (Cow.compareTo()).
	 * Перезаписує hashtable відсортованими даними.
	 */
	private void sortHashMap() {
		long timeStart = System.nanoTime();

		// Створюємо список ключів і сортуємо за природним порядком Cow
		List<Cow> sortedKeys = new ArrayList<>(hashmap.keySet());
		Collections.sort(sortedKeys);

		// Створюємо нову HashMap з відсортованими ключами
		HashMap<Cow, String> sortedHashMap = new HashMap<>();
		for (Cow key : sortedKeys) {
			sortedHashMap.put(key, hashmap.get(key));
		}

		// Перезаписуємо оригінальну HashMap
		hashmap = sortedHashMap;

		PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap за ключами");
	}

	/**
	 * Здійснює пошук елемента за ключем в HashMap.
	 * Використовує Cow.hashCode() та Cow.equals() для пошуку.
	 */
	void findByKeyInHashMap() {
		long timeStart = System.nanoTime();

		boolean found = hashmap.containsKey(KEY_TO_SEARCH_AND_DELETE);

		PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в HashMap");

		if (found) {
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

		// Створюємо список Entry та сортуємо за значеннями
		List<Map.Entry<Cow, String>> entries = new ArrayList<>(hashmap.entrySet());
		OwnerValueComparator comparator = new OwnerValueComparator();
		Collections.sort(entries, comparator);

		// Створюємо тимчасовий Entry для пошуку
		Map.Entry<Cow, String> searchEntry = new Map.Entry<Cow, String>() {
			public Cow getKey() { return null; }
			public String getValue() { return VALUE_TO_SEARCH_AND_DELETE; }
			public String setValue(String value) { return null; }
		};

		int position = Collections.binarySearch(entries, searchEntry, comparator);

		PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в HashMap");

		if (position >= 0) {
			Map.Entry<Cow, String> foundEntry = entries.get(position);
			System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Cow: " + foundEntry.getKey());
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

		String removedValue = hashmap.remove(KEY_TO_SEARCH_AND_DELETE);

		PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з HashMap");

		if (removedValue != null) {
			System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
		} else {
			System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
		}
	}

	/**
	 * Видаляє записи з HashMap за значенням.
	 */
	void removeByValueFromHashMap() {
		long timeStart = System.nanoTime();

		List<Cow> keysToRemove = new ArrayList<>();
		for (Map.Entry<Cow, String> entry : hashmap.entrySet()) {
			if (entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE)) {
				keysToRemove.add(entry.getKey());
			}
		}

		for (Cow key : keysToRemove) {
			hashmap.remove(key);
		}

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
		for (Map.Entry<Cow, String> entry : hashmap.entrySet()) {
			System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
		}

		PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в HashMap");
	}

	/**
	* Сортує LinkedHashMap за ключами
	*	Використовує Collections.sort() з природнім порядком Cow (Cow.compareTo()).
	* Перезаписує LinkedHashMap відсортованими даними
	*/
	private void sortLinkedHashMap() {
		long timeStart = System.nanoTime();

		List<Cow> sortedKeys = new ArrayList<>(linkedHashmap.keySet());
		Collections.sort(sortedKeys);

		LinkedHashMap<Cow, String> sortedCows = new LinkedHashMap<>();
		for (Cow cow : sortedKeys) {
			sortedCows.put(cow, linkedHashmap.get(cow));
		}

		linkedHashmap = sortedCows;

		PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap за ключами");
	}

	/**
	 * Здійснює пошук елемента за ключем в HashMap.
	 * Використовує Cow.compareTo() для навігації по дереву.
	 */
	void findByKeyInLinkedHashMap() {
		long timeStart = System.nanoTime();

		boolean found = linkedHashmap.containsKey(KEY_TO_SEARCH_AND_DELETE);

		PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в LinkedHashMap");

		if (found) {
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

		// Створюємо список Entry та сортуємо за значеннями
		List<Map.Entry<Cow, String>> entries = new ArrayList<>(linkedHashmap.entrySet());
		OwnerValueComparator comparator = new OwnerValueComparator();
		Collections.sort(entries, comparator);

		// Створюємо тимчасовий Entry для пошуку
		Map.Entry<Cow, String> searchEntry = new Map.Entry<Cow, String>() {
			public Cow getKey() { return null; }
			public String getValue() { return VALUE_TO_SEARCH_AND_DELETE; }
			public String setValue(String value) { return null; }
		};

		int position = Collections.binarySearch(entries, searchEntry, comparator);

		PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в LinkedHashMap");

		if (position >= 0) {
			Map.Entry<Cow, String> foundEntry = entries.get(position);
			System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Cow: " + foundEntry.getKey());
		} else {
			System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
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

		String removedValue = linkedHashmap.remove(KEY_TO_SEARCH_AND_DELETE);

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

		List<Cow> keysToRemove = new ArrayList<>();
		for (Map.Entry<Cow, String> entry : linkedHashmap.entrySet()) {
			if (entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE)) {
				keysToRemove.add(entry.getKey());
			}
		}

		for (Cow key : keysToRemove) {
			linkedHashmap.remove(key);
		}

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