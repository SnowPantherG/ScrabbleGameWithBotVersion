import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordDictionaryTest {

    private WordDictionary dictionary;

    // 在测试之前加载单词库
    @Before
    public void setUp() {
        dictionary = new WordDictionary();
    }

    // 测试有效的英文单词
    @Test
    public void testValidWord() {
        assertTrue(dictionary.isEnglishWord("apple"));   // apple 是有效的单词
        assertTrue(dictionary.isEnglishWord("banana"));  // banana 是有效的单词
    }

    // 测试无效的英文单词
    @Test
    public void testInvalidWord() {
        assertFalse(dictionary.isEnglishWord("poepqwe"));  // xyzabc 不是有效的单词
        assertFalse(dictionary.isEnglishWord("qwertyuiop"));  // qwertyuiop 不是有效的单词
    }

    // 测试大小写敏感
    @Test
    public void testCaseInsensitive() {
        assertTrue(dictionary.isEnglishWord("Apple"));  // 即使大小写不同，也应该是有效的单词
        assertTrue(dictionary.isEnglishWord("BANANA")); // 大写的 BANANA 也应该通过
    }

    // 测试空单词
    @Test
    public void testEmptyWord() {
        assertFalse(dictionary.isEnglishWord(""));  // 空字符串不是有效单词
        assertFalse(dictionary.isEnglishWord(null));  // null 不是有效单词
    }
}
