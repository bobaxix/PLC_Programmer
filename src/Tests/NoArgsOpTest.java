package Tests;

import com.PI.load.Instructions.InOutMem;
import com.PI.load.Instructions.Instruction;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 17.09.17.
 */
public class NoArgsOpTest {

    @Test
    public void generatedCodeIsEqualCodeForMemoryBitAccess(){
        Instruction iom = new InOutMem();
        iom.setParameters("NOT",11, "", 10);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x0B000000, (int) code.get(0));
    }
}
