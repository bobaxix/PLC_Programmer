package com.programmer.tags;

import java.util.ArrayList;

public class TagList {

    static public TagList tagsList = new TagList();

    private ArrayList<Tag> tagList = new ArrayList<>();

    private TagList(){}

    public void add(Tag tag){
        tagList.add(tag);
    }

    public void clear(){
        tagList.clear();
    }

    public void setTagList(ArrayList<Tag> tagList){
        this.tagList = tagList;
    }

    public boolean isEmpty(){
        return tagList.isEmpty();
    }

    public String findTag(String tag) {
        if(tagList != null && !tagList.isEmpty()){
            for(Tag t_tag : tagList){
                if(tag.equals(t_tag.getTag())){
                    return t_tag.getAddress();
                }
            }
        }
        return tag;
    }

    public static TagList getTagList(){
        return tagsList;
    }
}
