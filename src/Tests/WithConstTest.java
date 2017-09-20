package Tests;

import com.PI.load.Instructions.Instruction;
import com.PI.load.Instructions.WithConst;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 16.09.17.
 */
public class WithConstTest {

    @Test
    public void generatedCodeIsEqualCodeForConst353(){
        Instruction wc = new WithConst();
        wc.setParameters("ANDI", 10, "#353", 10);
        ArrayList<Integer> code = wc.generateCodeForInstruction();
        assertEquals(0x0A000000, (int) code.get(0));
        assertEquals(0x00000161, (int) code.get(1));
    }
}
