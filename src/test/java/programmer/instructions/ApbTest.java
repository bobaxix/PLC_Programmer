package programmer.instructions;

import com.programmer.instructions.Apb;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 20.09.17.
 */
public class ApbTest {


    @Test
    public void generatedCodeIsEqualForCounterOrder(){

        Apb apb = new Apb();
        ArrayList<Integer> code = apb.generateCode("C1.CV", 0x32, 10);
        assertEquals(0x3200000D, (int) code.get(0));
    }

    @Test
    public void generatedCodeIsEqualForTimerOrder(){

        Apb apb = new Apb();
        ArrayList<Integer> code = apb.generateCode("T2.ET", 0x56, 10);
        assertEquals(0x56000814, (int) code.get(0));
    }

    @Test
    public void generatedCodeIsEqualForInputOrder(){

        Apb apb = new Apb();
        ArrayList<Integer> code = apb.generateCode("IN", 0x56, 10);
        assertEquals(0x56001800, (int) code.get(0));
    }

    @Test
    public void cannotGeneratedCodeForInvaildArg(){

        Apb apb = new Apb();
        ArrayList<Integer> code = apb.generateCode("X2.CC", 0x56, 10);
        assertEquals(null, code);
    }

    @Test
    public void generateCodeIsEqualForBridge(){
        Apb apb = new Apb();
        ArrayList<Integer> code = apb.generateCode("BR.STATUS",
                0x11,
                10);
        assertEquals(0x1100100C, (int) code.get(0));

        apb = new Apb();
        code = apb.generateCode("BR.RESET", 0x10, 10);
        assertEquals(0x10001028, (int) code.get(0));
    }
}
