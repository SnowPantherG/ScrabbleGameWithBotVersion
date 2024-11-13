import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * The WordDictionary class represents a dictionary of English words that can be used
 * to validate words and provide random English words. It loads the words from a file containing
 * a list of common English words.
 *
 * @version v1, 19th October, 2024
 * @author Shenhao Gong
 *
 * @version v2, 21st October, 2024
 * @author Shenhao Gong
 * change 5000_common_word to 10000_common_words
 *
 */
public class WordDictionary {
    private Set<String> words;

    public WordDictionary() {
        words = new HashSet<>();
        loadWords();
    }


    private void loadWords() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("10000_common_words.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
            System.out.println("Load English Word successful, in total " + words.size() + " words。");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * check if the word is English word
     * @param word going to compare
     * @return if vaild True, if not False
     */
    public boolean isEnglishWord(String word) {
        if (word == null) {
            return false;
        }
        return words.contains(word.toLowerCase());
    }

    // possible api
    /**
     * get a random English word
     * @return random word
     */
    public String getRandomWord() {
        int size = words.size();
        int item = new java.util.Random().nextInt(size);
        int i = 0;
        for(String word : words) {
            if (i == item)
                return word;
            i++;
        }
        return null;
    }
}