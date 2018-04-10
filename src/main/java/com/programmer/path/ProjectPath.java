package com.programmer.path;

import java.io.File;

public class ProjectPath {

    private String path;
    private String lastFilename;

    public static ProjectPath projectPath = new ProjectPath();

    private ProjectPath(){
        this.path = System.getProperty("user.dir");
        this.lastFilename = null;
    };

    public String getPath() {
        return path;
    }

    public void setProjectPath(String path, String lastFilename) {
        this.path = path;
        this.lastFilename = lastFilename;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLastFilename() {
        return lastFilename;
    }

    public void setLastFilename(String lastFilename) {
        this.lastFilename = lastFilename;
    }

    public String getFullPath(){
        return path + File.separator + lastFilename;
    }

    public static ProjectPath getProjectPath() {
        return projectPath;
    }

    public void cleanPath(){
        this.lastFilename = "";
        this.path = "";
    }


}
