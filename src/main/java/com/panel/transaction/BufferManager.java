package com.panel.transaction;

import java.util.ArrayList;

public class BufferManager {

    private ArrayList<PanelField> buffersList;
    private int[] buffers;
    private int[] toggleBuffer;

    public BufferManager(ArrayList<PanelField> buffersList, int bufferSize){
        this.buffersList = buffersList;
        buffers = new int[bufferSize];
        toggleBuffer = new int[bufferSize];
    }

    public void setParameter (String id, int value) throws NullPointerException{
        PanelField buffer = findBuffer(id);
        BufferReaderWriter.set(buffer.getAddress(), value,
                buffers, buffer.getAccessType());
    }

    public void setToggleBuffer (String id, int value) throws NullPointerException{
        PanelField buffer = findBuffer(id);
        if(buffer != null)
            BufferReaderWriter.set(buffer.getAddress(), value,
                   toggleBuffer, buffer.getAccessType());
    }


    public int[] getBuffers(){
        return buffers;
    }

    public int[] getToggleBuffer() {return toggleBuffer;}

    public int[] getSumarizeBuffer(){
        int[] data = new int[buffers.length];
        for(int x = 0; x < data.length; x++){
            data[x] = buffers[x] | toggleBuffer[x];
        }
        return data;
    }

    private PanelField findBuffer(String id){
        if(id == null)
            return null;

        for(PanelField buffer : buffersList){
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
