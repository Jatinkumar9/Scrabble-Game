/**
 * 
 */
package tests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;

import game.scrabble.model.CellType;
import game.scrabble.utils.BoardGenerator;

/**
 * @author Jatin Kumar
 *
 */
class BoardGeneratorTest {

    @Test
    void test() {
        BoardGenerator bg = new BoardGenerator();
        CellType [][] types =  new CellType[BoardGenerator.SIZE][BoardGenerator.SIZE];
        bg.init(types);
        assertEquals(types[0][0], CellType.TRIPLE_WORD);
        assertEquals(types[7][7], CellType.STAR);
    }

}
