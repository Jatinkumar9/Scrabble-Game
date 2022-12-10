package tests.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import game.scrabble.model.Tile;
import game.scrabble.model.TileBag;

public class TileBagTest {
    @Test
    void testTileBagInit() {
        TileBag tb = new TileBag();
        tb.initialize();

        assertNotNull(tb.tiles);
        assertEquals(98, tb.tiles.size());
    }
    
    @Test
    void testTileBagRemove() {
        TileBag tb = new TileBag();
        tb.initialize();

        assertEquals(98, tb.tiles.size());
        
        Tile t = tb.removeAndGiveARandomTile();
        boolean valid = t.character>='A'  && t.character<='Z';
        assertTrue(valid);
        
        assertEquals(97, tb.tiles.size());
           
    }
}
