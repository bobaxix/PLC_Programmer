package programmer.instructions;

import com.programmer.instructions.InOutMem;
import com.programmer.instructions.Instruction;
import com.programmer.instructions.NoArgsOp;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 17.09.17.
 */
public class NoArgsOpTest {

    @Test
    public void generatedCodeIsEqualCode(){
        NoArgsOp nap = new NoArgsOp();
        ArrayList<Integer> code = nap.generateCode(null, 11, 10);

        assertEquals(0x0B000000, (int) code.get(0));
    }
}
