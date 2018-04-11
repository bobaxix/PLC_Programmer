package programmer.instructions;

import com.programmer.instructions.Instruction;
import com.programmer.instructions.WithConst;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 16.09.17.
 */
public class WithConstTest {

    @Test
    public void generatedCodeIsEqualCodeForConst353(){
        WithConst wc = new WithConst();
        ArrayList<Integer> code = wc.generateCode("#355", 10, 10 );
        assertEquals(0x0A000000, (int) code.get(0));
        assertEquals(0x00000163, (int) code.get(1));
    }

    @Test
    public void generateCodeForHex(){
        WithConst wc = new WithConst();
        ArrayList<Integer> code = wc.generateCode("0x10", 10, 10 );
        assertEquals(0x0A000000, (int) code.get(0));
        assertEquals(0x00000010, (int) code.get(1));

    }

    @Test
    public void generateCodeForHexGreaterThanMax(){
        WithConst wc = new WithConst();
        ArrayList<Integer> code = wc.generateCode("0x1FAAAAFFF", 10, 10 );
        assertEquals(null, code);
    }

    @Test
    public void generateCodeForBadHex(){
        WithConst wc = new WithConst();
        ArrayList<Integer> code = wc.generateCode("0x1FAAAAFG", 10, 10 );
        assertEquals(null, code);
    }
}
