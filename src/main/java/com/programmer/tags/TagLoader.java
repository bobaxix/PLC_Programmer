package com.programmer.tags;

import java.io.*;
import java.util.ArrayList;

public class TagLoader {

    static public ArrayList<Tag> loadTags(String path){
        if(pathValidator(path)) {
            ArrayList<Tag> tagList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(path +
                    File.separator + "tags" + File.separator + "data.tag"))) {
                String line = br.readLine();
                while (line != null) {
                    String[] splittedLine = line.split(" +");
                    String tag = splittedLine[0].trim();
                    String address = splittedLine[1].trim();
                    tagList.add(new Tag(tag, address));
                    line = br.readLine();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return tagList;
        }
        return null;
    }

    static private boolean pathValidator(String path){
       return new File(path).exists();
    }
}
