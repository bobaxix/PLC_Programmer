package Tests;

import com.PI.load.Compilator;
import com.PI.load.Order;
import com.PI.load.OrdersLoader;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by bobaxix on 17.09.17.
 */
public class CompilatorTest {

    @Test
    public void testLabels() throws IOException{

        String code = "AND M0.0 \n" +
                "JMP skok \n" +
                "AND M1.0 \n" +
                "skok: \n" +
                "AND M2.0";
        ArrayList<Order> orderList = new OrdersLoader().loadOrdersFromTxtFile();
        Compilator compilator = new Compilator(orderList);

        ArrayList<Integer> codeList = compilator.compile(code);
        assertEquals(0x0C000003 ,(int) codeList.get(1));
    }

    @Test
    public void anotherTestLabels() throws IOException{

        String code = "JMP skok1 \n" +
                "JMP skok2 \n" +
                "AND M1.0 \n" +
                "skok2: \n" +
                "NOT \n" +
                "AND M2.0 \n" +
                "skok1: \n" +
                "OR MB2 \n";

        ArrayList<Order> orderList = new OrdersLoader().loadOrdersFromTxtFile();
        Compilator compilator = new Compilator(orderList);

        ArrayList<Integer> codeList = compilator.compile(code);
        assertEquals(0x0C000003 ,(int) codeList.get(1));
    }
}
