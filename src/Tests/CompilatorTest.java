package Tests;

import com.PI.load.CodeList;
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

        CodeList codeList = compilator.compile(code);
        assertEquals(0x0C000003 ,(int) codeList.getCodeListIndexOf(2));
    }

    @Test
    public void anotherTestLabels() throws IOException{

        String code = "JMP skok1 \n" +
                "JMP skok2 \n" +
                "AND MD2 \n" +
                "skok2: \n" +
                "NOT \n" +
                "AND M2.0 \n" +
                "skok1: \n" +
                "OR MB2 \n" +
                "APB_WR C0.CU";

        ArrayList<Order> orderList = new OrdersLoader().loadOrdersFromTxtFile();
        Compilator compilator = new Compilator(orderList);

        CodeList codeList = compilator.compile(code);

        assertEquals(0x0C000005, (int) codeList.getCodeListIndexOf(1));
        assertEquals(0x0C000003 ,(int) codeList.getCodeListIndexOf(2));
        assertEquals(0x01000380, (int) codeList.getCodeListIndexOf(3));
        assertEquals(0x0B000000, (int) codeList.getCodeListIndexOf(4));
        assertEquals(0x1E000000, (int) codeList.getCodeListIndexOf(7));
    }
}
