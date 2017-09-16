package com.PI.load;
import com.PI.load.Order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by bobaxix on 16.09.17.
 */
public class OrdersLoader {

    ArrayList<Order> ordersList = new ArrayList<Order>();

    public ArrayList<Order> loadOrdersFromTxtFile() throws IOException{

        BufferedReader br = new BufferedReader(new FileReader("orders.txt"));
        String line;
        br.readLine();

        while((line = br.readLine()) != null){

            String[] splittedLine = line.split(Pattern.quote("|"));
            String name = splittedLine[0].trim();
            Byte code = Byte.parseByte(splittedLine[1].trim());
            String type = splittedLine[2].trim();

            Order order = new Order(name, code, type);

            ordersList.add(order);
        }
    return ordersList;
    }
}
