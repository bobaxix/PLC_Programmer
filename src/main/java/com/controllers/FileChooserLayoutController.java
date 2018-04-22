package com.controllers;

import com.common.FileManager;
import com.programmer.connect.LoadSaveData;
import com.programmer.path.ProjectPath;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileChooserLayoutController {

    @FXML
    private ListView<String> fileListView;

    @FXML
    private TextField filename;

    @FXML
    private Label localization;

    @FXML
    private Label warning;

    private FileManager fileManager;
    private Operation operationToDo;
    private TextArea programTextField;
    private Runnable backToPrevious;
    private boolean saveFlag = false;


    private SimpleStringProperty localizationProperty;

    @FXML
    public void initialize(){

        fileManager = new FileManager();
        fileListView.setCellFactory(param -> new ListCell<String>(){
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String name, boolean empty){
                super.updateItem(name, empty);
                Path actualPath = fileManager.getActualPath();
                fileListView.getSelectionModel().selectFirst();
                Image i;
                if(empty){
                    setText(null);
                    setGraphic(null);
                }
                else{
                    File f = Paths.get(actualPath.toAbsolutePath().toString() ,name).toFile();
                    if(f.isDirectory())
                        i = new Image(getClass().getResource("/icons/folder.png").toString());
                    else
                        i = new Image(getClass().getResource("/icons/file.png").toString());

                    imageView.setImage(i);

                    setText(name);
                    setGraphic(imageView);
                }
            }
        });

        ObservableList<String> pathList = fileManager.getPathList();
        fileListView.setItems(pathList);

        fileListView.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue) ->{
            filename.setText(newValue);
        } );

        Path actualPath = fileManager.getActualPath();
        localizationProperty = new SimpleStringProperty(actualPath.toAbsolutePath().toString());

        localization.textProperty().bind(localizationProperty);

    }

    @FXML
    private void goUpWithDirectory(){
        try {
            Path actualPath = fileManager.getActualPath().toAbsolutePath().getParent();
            fileManager.setActualPath(actualPath);
            localizationProperty.set(actualPath.toAbsolutePath().toString());
            saveFlag = false;
            warning.setVisible(false);
        }
        catch(NullPointerException n){}
    }

    @FXML void addDirectory(){
        String actualPath = fileManager.getActualPath().toAbsolutePath().toString();
        String dirName = filename.getText();
        File f = new File(actualPath);
        boolean isRepeated = false;
        if(dirName != null){
            for(File ff : f.listFiles()){
                if(ff.getName().equals(dirName)){
                    isRepeated = true;
                    warning.setText("Directory exists!");
                    warning.setVisible(true);
                    break;
                }
            }
            if(!isRepeated) {
                warning.setVisible(false);
                f = new File(actualPath+File.separator+dirName);
                f.mkdir();
                fileManager.refreshFileList();
            }
        }
    }

    @FXML
    private void delete(){
        String name = fileListView.getSelectionModel().getSelectedItem();
        fileManager.delete(name);
        warning.setVisible(false);
    }
    @FXML
    private void openButtonHandle(){
        File f = getSelectedFile();

        if(f.isDirectory()){
            Path actualPath = f.toPath();
            fileManager.setActualPath(actualPath);
            localizationProperty.set(actualPath.toAbsolutePath().toString());
        }
        else{
            if(operationToDo == Operation.SAVE){
                if(f.exists()){
                    if(saveFlag == false){
                        warning.setText("File exists. Overwrite?");
                        warning.setVisible(true);
                        saveFlag = true;
                    }
                    else{
                        warning.setVisible(false);
                        saveFlag = false;
                        ProjectPath projectPath = ProjectPath.getProjectPath();
                        projectPath.setProjectPath(f.getParent(), f.getName());
                        okButtonHandle();
                    }
                }
                else {
                    okButtonHandle();
                    ProjectPath projectPath = ProjectPath.getProjectPath();
                    projectPath.setProjectPath(f.getParent(), f.getName());
                    warning.setVisible(false);
                }
            }
            else{
                try {
                    String programText = LoadSaveData.loadProject(f).toString();
                    programTextField.setText(programText);
                    ProjectPath projectPath = ProjectPath.getProjectPath();
                    projectPath.setProjectPath(f.getParent(), f.getName());
                    closeChoosePane();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void okButtonHandle(){
        File f = getSelectedFile();
        try {
            LoadSaveData.saveProject(f, programTextField.getText());
            closeChoosePane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getSelectedFile(){
        String name = filename.getText();
        Path actualPath = fileManager.getActualPath();
        return Paths.get(actualPath.toAbsolutePath().toString(), name).toFile();
    }

    public void setActualOperation(Operation o){
        operationToDo = o;
        if(operationToDo == Operation.LOAD)
            filename.setEditable(false);
        else
            filename.setEditable(true);
    }

    public void setTextField(TextArea textArea){
        programTextField = textArea;
    }

    @FXML
    private void closeChoosePane(){
        backToPrevious.run();
    }

    public void setBackToPrevious(Runnable r){
        backToPrevious = r;
    }

}
