import Developer.Utils;
import jdk.jshell.execution.Util;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.Utilities;

public class UtilsTest {
    @Test
    public void test(){
        Utils u = new Utils(true);
        Assert.assertTrue(u.isDev());
    }
    @Test
    public void checkPrintDev(){
        Utils u = new Utils(true);
        u.printDev("Linux");
    }
}
