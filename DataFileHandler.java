import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
        Short[] temporaryArray = new Short[1000];
        int currentIndex = 0;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                // Видаляємо можливі невидимі символи та BOM
                currentLine = currentLine.trim().replaceAll("^\\uFEFF", "");
                if (!currentLine.isEmpty()) {
                    short parsedValue = Short.parseShort(currentLine);
                    temporaryArray[currentIndex++] = parsedValue;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Short[] resultArray = new Short[currentIndex];
        System.arraycopy(temporaryArray, 0, resultArray, 0, currentIndex);

        return resultArray;
    }

    /**
     * Зберігає масив об'єктів short у файл.
     * 
     * @param shortArray Масив об'єктів short.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(Short[] shortArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            for (Short shortElement : shortArray) {
                fileWriter.write(Short.toString(shortElement));
                fileWriter.newLine();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
