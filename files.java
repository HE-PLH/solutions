import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FileWordCounterTest {

    private static final String TEST_RESOURCES_PATH = "src/";

    public static int countWords(String filePath, String separator) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int wordCount = 0;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split(separator);
                wordCount += words.length;
            }
            return wordCount;
        }
    }

    @Test
    public void csv_toReturnCorrectCount() throws IOException {
        String filePath = TEST_RESOURCES_PATH + "my_csv.csv";
        String separator = ",";
        int wordCount = countWords(filePath, separator);
        assertEquals(6, wordCount);
    }

    @Test
    public void json_toReturnsCorrectCount() throws IOException {
        String filePath = TEST_RESOURCES_PATH + "my_json.json";
        String separator = " ";
        int wordCount = countWords(filePath, separator);
        assertEquals(11, wordCount);
    }

    @Test
    public void sc_toReturnsCorrectCount() throws IOException {
        String filePath = TEST_RESOURCES_PATH + "sc.txt";
        String separator = ";";
        int wordCount = countWords(filePath, separator);
        assertEquals(7, wordCount);
    }

    @Test(expected = IOException.class)
    public void nofile_ThrowsIOException() throws IOException {
        String filePath = TEST_RESOURCES_PATH + "nf.txt";
        String separator = ",";
        countWords(filePath, separator);
    }
}
