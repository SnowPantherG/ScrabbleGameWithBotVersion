import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordDictionaryTest {

    private WordDictionary dictionary;


    @Before
    public void setUp() {
        dictionary = new WordDictionary();
    }


    @Test
    public void testValidWord() {
        assertTrue(dictionary.isEnglishWord("apple"));
        assertTrue(dictionary.isEnglishWord("banana"));
        assertTrue(dictionary.isEnglishWord("engineer"));
        assertTrue(dictionary.isEnglishWord("math"));
    }


    @Test
    public void testInvalidWord() {
        assertFalse(dictionary.isEnglishWord("poepqwe"));
        assertFalse(dictionary.isEnglishWord("qwertyuiop"));
        assertFalse(dictionary.isEnglishWord("12332654"));
    }


    @Test
    public void testCaseInsensitive() {
        assertTrue(dictionary.isEnglishWord("Apple"));
        assertTrue(dictionary.isEnglishWord("BANANA"));
    }


    @Test
    public void testEmptyWord() {
        assertFalse(dictionary.isEnglishWord(""));
        assertFalse(dictionary.isEnglishWord(null));
    }
}
