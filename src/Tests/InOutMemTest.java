package Tests;

import com.PI.load.Instructions.InOutMem;
import com.PI.load.Instructions.Instruction;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 16.09.17.
 */
public class InOutMemTest {

    @Test
    public void generatedCodeIsEqualCodeForMemoryBitAccess(){
        Instruction iom = new InOutMem();
        iom.setParameters("AND",16, "M0.3", 10);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x10000043, (int) code.get(0));
    }

    @Test
    public void generatedCodeIsEqualCodeForInputByteAccess(){
        Instruction iom = new InOutMem();
        iom.setParameters("AND",2, "IB3",5);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x02000118, (int) code.get(0));
    }

    @Test
    public void generatedCodeIsEqualCodeForOutputWordAccess(){
        Instruction iom = new InOutMem();
        iom.setParameters("OR",20, "OW1", 6);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x14000230, (int) code.get(0));
    }

    @Test
    public void generatedCodeIsEqualCodeForMemoryDoubleWordAccess(){
        Instruction iom = new InOutMem();
        iom.setParameters("AND",5, "MD2", 1);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x05000380, (int) code.get(0));
    }

    @Test
    public void cannotGeneratedCodeForInvaildArg(){

        Instruction iom = new InOutMem();
        iom.setParameters("AND", 5, "MZ.2", 10);
        ArrayList<Integer> codeLine = iom.generateCodeForInstruction();
        assertEquals(true,codeLine.isEmpty());
    }

}
