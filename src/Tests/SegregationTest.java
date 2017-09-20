package Tests;

import com.PI.load.Instructions.Instruction;
import com.PI.load.Order;
import com.PI.load.OrdersLoader;
import com.PI.load.Segregation;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by bobaxix on 16.09.17.
 */
public class SegregationTest {

    @Test
    public void forIomSegregatorShouldReturnIomObject() throws IOException{

        OrdersLoader ordersLoader = new OrdersLoader();
        ArrayList<Order> orderList = ordersLoader.loadOrdersFromTxtFile();
        Segregation segregation = new Segregation(orderList);
        Instruction instruction = segregation.getInstructionObject("AND M0.0", 10);

        assertEquals(instruction.getOrderCode(), (byte) 1);
    }
}