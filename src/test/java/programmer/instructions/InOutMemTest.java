package programmer.instructions;

import com.programmer.instructions.InOutMem;
import com.programmer.instructions.Instruction;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 16.09.17.
 */
public class InOutMemTest {

    @Test
    public void generatedCodeIsEqualCodeForMemoryBitAccess(){
        InOutMem iom = new InOutMem();
        ArrayList<Integer> code =
                iom.generateCode("M0.3", 16, 10);

        assertEquals(0x10000083, (int) code.get(0));
    }

    @Test
    public void generatedCodeIsEqualCodeForInputByteAccess(){
        InOutMem iom = new InOutMem();
        ArrayList<Integer> code =
                iom.generateCode("IB3", 2, 5);
        assertEquals(0x02000118, (int) code.get(0));
    }

    @Test
    public void generatedCodeIsEqualCodeForOutputWordAccess(){
        InOutMem iom = new InOutMem();
        ArrayList<Integer> code =
                iom.generateCode("OW1", 20, 10);

        assertEquals(0x14000250, (int) code.get(0));
    }

    @Test
    public void generatedCodeIsEqualCodeForMemoryDoubleWordAccess(){
        InOutMem iom = new InOutMem();
        ArrayList<Integer> code =
                iom.generateCode("MD2", 1, 11);

        assertEquals(0x010003C0, (int) code.get(0));
    }

    @Test
    public void cannotGeneratedCodeForInvaildArg(){
        InOutMem iom = new InOutMem();
        ArrayList<Integer> code =
                iom.generateCode("MZ.3", 5, 10);
        assertEquals(null ,code);
    }

}
