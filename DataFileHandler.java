import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Клас DataFileHandler управляє роботою з файлами даних short.
 */
public class DataFileHandler {
    /**
     * Завантажує масив об'єктів short з файлу.
     * 
     * @param filePath Шлях до файлу з даними.
     * @return Масив об'єктів short.
     */
    public static Short[] loadArrayFromFile(String filePath) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
					return fileReader.lines()
							.map(current -> current.trim().replaceAll("^\\uFEFF", ""))
							.filter(current -> !current.isEmpty())
							.map(Short::parseShort)
							.toArray(Short[]::new);
				} catch (IOException exception) {
					throw new RuntimeException("Error reading data from the file: " + filePath, exception);
				}
    }

    /**
     * Зберігає масив об'єктів short у файл.
     * 
     * @param shortArray Масив об'єктів short.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(Short[] shortArray, String filePath) {
			try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
				String content = Arrays.stream(shortArray)
						.map(String::valueOf)
						.collect(Collectors.joining(System.lineSeparator()));

				fileWriter.write(content);
			} catch (IOException exception) {
				throw new RuntimeException("Error writing data to the file: " + filePath, exception);
			}
    }
}
