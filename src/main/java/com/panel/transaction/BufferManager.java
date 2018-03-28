package com.panel.transaction;

import java.util.ArrayList;

public class BufferManager {

    private ArrayList<PanelField> buffersList;
    private int[] buffers;

    public BufferManager(ArrayList<PanelField> buffersList){
        this.buffersList = buffersList;
        buffers = new int[4];
    }

    public void setParameter (String id, int value) throws NullPointerException{
        PanelField buffer = findBuffer(id);
        BufferReaderWriter.set(buffer.getAddress(), value,
                buffers, buffer.getAccessType());
    }

    public int[] getBuffers(){
        return buffers;
    }

    private PanelField findBuffer(String id){
        for(PanelField buffer : buffersList){
            System.out.println("Id: "+id+" getId: "+buffer.getId());
            if(buffer.getId().equals(id))
                return buffer;
        }
        return null;
    }

    public void clean(){
        for(int x = 0; x < buffers.length; x++)
            buffers[x] = 0;
    }

}
