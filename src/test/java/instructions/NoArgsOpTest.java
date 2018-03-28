package instructions;

import com.instructions.InOutMem;
import com.instructions.Instruction;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 17.09.17.
 */
public class NoArgsOpTest {

    @Test
    public void generatedCodeIsEqualCode(){
        Instruction iom = new InOutMem();
        iom.set("NOT",11, "", 10);
        ArrayList<Integer> code = iom.generateCodeForInstruction();

        assertEquals(null, code);
    }
}
