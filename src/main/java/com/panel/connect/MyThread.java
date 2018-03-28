package com.panel.connect;

import com.panel.transaction.BufferManager;
import com.panel.view.ViewManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class MyThread extends Service<Void>{
    /* TO DO:
        make safety valve: send random data when
        user needs less than control size
     */
    private SpiManager spiManager;
    private int[] buffer;
    private ViewManager vm;
    private int control;

    public MyThread(SpiManager spiManager){
//        spiManager = new SpiManager();
//        spiManager.initializeSpi();
//        control = readControl();
        control = 4;
        buffer = new int[control];
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {

                    BufferManager nbm = vm.getBufferManager();
                    int[] buffers = nbm.getBuffers();
                    System.out.println(buffers);
//                    synchronized (buffers) {
//                        for (int i = 0; i < buffers.length; i++)
//                            spiManager.sendDoubleWord(buffers[i])
//                    }
//                nbm.clean();
//
//
//                    for(int i =0; i < buffer.length; i++) {
//                        buffer[i] = spiManager.sendDoubleWord(0);
//                        System.out.println("Received data "+i+": "+buffer[i]);
//                    }
                buffer[0] = 0xFF;
                vm.getPropertyManager().setProperties(buffer);
                rest(1000);

                System.out.println("TRANSMISSION");
                return null;

//                int status = readStatus();
//                System.out.println("Status: " + Integer.toBinaryString(status));
//
//                // Send data to bridge
//                if((status & Status.SPI_EMPTY) != 0 ) {
//                    BufferManager nbm = vm.getBufferManager();
//                    int[] buffers = nbm.getBuffers();
//                    System.out.println(buffers);
//                    synchronized (buffers) {
//                        for (int i = 0; i < buffers.length; i++)
//                            sendData(buffers[i]);
//                            nbm.clean();
//                    }
//                }
//
//                // Receive data from bridge
//                if((status & (int)Status.APB_READY) != 0)
//                    for(int i =0; i < buffer.length; i++) {
//                        buffer[i] = receiveData();
//                        System.out.println("Received data "+i+": "+buffer[i]);
//                    }
//
//                vm.getPropertyManager().setProperties(buffer);
//                rest(1000);
//
//                System.out.println("TRANSMISSION");
//                return null;
            }
        };
    }


    private int readStatus(){
        spiManager.sendAddress(Operation.READ, Register.STATUS);
        return spiManager.readRegister();
    }

    private int readControl(){
        spiManager.sendAddress(Operation.READ, Register.CTRL);
        return spiManager.readRegister();
    }

    private void sendData(int data){
        spiManager.sendAddress(Operation.WRITE, Register.RX_FIFO);
        spiManager.sendDoubleWord(data);
    }

    private int receiveData(){
        spiManager.sendAddress(Operation.READ, Register.TX_FIFO);
        return spiManager.readRegister();
    }


    public void setViewManager(ViewManager vm){this.vm = vm;}

    private void rest(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
