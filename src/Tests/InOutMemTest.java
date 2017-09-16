package Tests;

import com.PI.load.Instructions.InOutMem;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 16.09.17.
 */
public class InOutMemTest {

    @Test
    public void generateCodeIsEqualCodeForMemoryBitAccess(){
        InOutMem iom = new InOutMem();
        iom.setParameters("M0.3",0);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x0043, (int) code.get(0));
    }

    @Test
    public void generateCodeIsEqualCodeForInputByteAccess(){
        InOutMem iom = new InOutMem();
        iom.setParameters("IB3",0);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x0118, (int) code.get(0));
    }

    @Test
    public void generateCodeIsEqualCodeForOutputWordAccess(){
        InOutMem iom = new InOutMem();
        iom.setParameters("OW1",0);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x0230, (int) code.get(0));
    }

    @Test
    public void generateCodeIsEqualCodeForMemoryDoubleWordAccess(){
        InOutMem iom = new InOutMem();
        iom.setParameters("MD2",0);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(0x0380, (int) code.get(0));
    }

}
