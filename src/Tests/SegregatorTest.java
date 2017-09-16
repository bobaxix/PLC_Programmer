package Tests;

import com.PI.load.Instructions.InOutMem;
import com.PI.load.Instructions.Instruction;
import com.PI.load.Order;
import com.PI.load.OrdersLoader;
import com.PI.load.Segregator;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by bobaxix on 16.09.17.
 */
public class SegregatorTest {

    @Test
    public void forIomSegregatorShouldReturnIomObject() throws IOException{

        OrdersLoader ordersLoader = new OrdersLoader();
        ArrayList<Order> orderList = ordersLoader.loadOrdersFromTxtFile();
        Segregator segregator = new Segregator(orderList);
        Instruction instruction = segregator.getInstructionObject("AND M0.0");

        assertEquals(instruction.getOrderCode(), (byte) 1);
    }
}
