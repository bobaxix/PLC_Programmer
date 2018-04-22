package com.common;

import com.programmer.connect.LoadSaveData;
import com.programmer.path.ProjectPath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private Path actualPath = new File(System.getProperty("user.dir")).toPath();
    private ObservableList<String> pathList = FXCollections.observableArrayList();

    public FileManager(){
        refreshFileList();
    }

    public Path getActualPath() {
        return actualPath;
    }

    public void setActualPath(Path actualPath) {
        this.actualPath = actualPath;
        refreshFileList();
    }

    public ObservableList<String> getPathList() {
        return pathList;
    }

    public void delete(String path) {
        if (path != null) {
            File files = actualPath.toFile();
            for (File f : files.listFiles()) {
                if (f.getName().equals(path)) {
                    f.delete();
                    pathList.remove(path);
                }
            }
        }
    }

    public String load(String filename) throws IOException {
        String filePath = actualPath.toAbsolutePath().toString() +
                File.separator + filename;
        File f = new File(filePath);
        if (filePath != null) {
            if (f.isDirectory()) {
                actualPath = f.toPath();
                refreshFileList();
                return null;
            } else {
                ProjectPath.getProjectPath().setPath(f.getParent());
                StringBuilder sb = LoadSaveData.loadProject(new File(filePath));
                return sb.toString();
            }
        }
        return null;
    }

    public void refreshFileList(){
        pathList.clear();
        FilenameFilter filter = (File dir, String name) -> {
            Path p = Paths.get(dir.getAbsolutePath(), name);
            if(name.endsWith(".txt") ||
                    (p.toFile().isDirectory() && !name.startsWith("."))
                    || name.endsWith(".tag"))
                return true;
            return false;
        };

        File ff = new File(actualPath.toAbsolutePath().toString());
        for(File f : ff.listFiles(filter))
            pathList.add(f.getName());
    }
}