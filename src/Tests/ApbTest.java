package Tests;

import com.PI.load.Instructions.Apb;
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
        apb.set("APB_WR", 0x32, "C1.CV", 10);
        apb.generateCodeForInstruction();
        assertEquals(0x3200000D, (int) apb.getCodeLine().get(0));
    }

    @Test
    public void generatedCodeIsEqualForTimerOrder(){

        Apb apb = new Apb();
        apb.set("APB_WR", 0x56, "T2.ET", 10);
        apb.generateCodeForInstruction();
        assertEquals(0x56000814, (int) apb.getCodeLine().get(0));
    }

    @Test
    public void generatedCodeIsEqualForInputOrder(){

        Apb apb = new Apb();
        apb.set("APB_WR", 0x56, "IN", 10);
        apb.generateCodeForInstruction();
        assertEquals(0x56001000, (int) apb.getCodeLine().get(0));
    }

    @Test
    public void cannotGeneratedCodeForInvaildArg(){

        Apb apb = new Apb();
        apb.set("APB_WR", 0x56, "X2.CC", 10);
        ArrayList<Integer> codeLine = apb.generateCodeForInstruction();
        assertEquals(null, codeLine);
    }
}
