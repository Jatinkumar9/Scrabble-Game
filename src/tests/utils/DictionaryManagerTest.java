package tests.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import game.scrabble.utils.DictionaryManager;
import game.scrabble.utils.DictionaryUtil;

public class DictionaryManagerTest {

    @Test
    void testDictionaryManagerInit() {
        DictionaryManager dm = new DictionaryManager(null);
        
        
        assertNotNull(dm.getWords());
        assertEquals(10000, dm.getWords().size());
    }
    
    @Test
    void testDictionaryManagerExistingWords() {
        DictionaryManager dm = new DictionaryManager(new DictionaryUtil(true));

        
        assertTrue( dm.isWord("no"));
        assertTrue( dm.isWord("man"));
        assertTrue( dm.isWord("earth"));
        
        assertFalse( dm.isWord("abrakaerewwssfsd"));
        
    }
    

    @Test
    void testDictionaryManagerNonExistingWords() {
        DictionaryManager dm = new DictionaryManager(new DictionaryUtil(true));
        
        assertFalse( dm.isWord("abrakaerewwssfsd"));
        
    }

}
