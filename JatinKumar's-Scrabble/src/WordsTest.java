import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WordsTest {
    @Test
    public void test() {
    DictionaryManager dm = new DictionaryManager();
    Assert.assertFalse(dm.validWordCheck("abcdef"));
    }
    @Test
    public void testEmptyString(){
        DictionaryManager dm = new DictionaryManager();
        Assert.assertFalse(dm.validWordCheck(""));
    }

}

