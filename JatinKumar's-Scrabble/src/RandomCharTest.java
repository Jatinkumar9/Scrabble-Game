import Developer.Utils;
import org.junit.Test;

public class RandomCharTest {
    private final Utils _u = new Utils(true);
    private final BoardManager  bm = new BoardManager(_u);
    @Test
    public void test() {
        System.out.println(bm.getRandomAlphabets());
    }
}




