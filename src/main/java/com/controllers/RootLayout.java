package com.controllers;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.common.FileManager;
import com.panel.connect.MyThread;
import com.panel.view.ViewManager;
import com.programmer.orders.Order;
import com.programmer.path.ProjectPath;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;


public class RootLayout extends Controller{

	private BorderPane textLayout;
	private BorderPane editorLayout;
	private TextLayoutController textLayoutController;
	private EditorLayoutController editorLayoutController;
	private ArrayList<Order> ordersList;
	private Runnable backToStart;
	private MyThread myThread;
    private FileManager fileManager;

	@FXML
	ListView<String> pathListView;

	@FXML
	BorderPane rootLayout;

	@FXML
	HBox startView;

	@FXML
	ChoiceBox<String> visualizationChooser;

	private PopupWindow keyboard;
	private final Rectangle2D bounds = Screen.getPrimary().getBounds();


	@FXML
	private void initialize(){

	    fileManager = new FileManager();
		backToStart = () -> backToStartScreen();

		try {
			openTextLayout();
			openEditorLayout();
		} catch (IOException e) {
			e.printStackTrace();
		}

        pathListView.setCellFactory(param -> new ListCell<String>(){
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String name, boolean empty){
                super.updateItem(name, empty);
                pathListView.getSelectionModel().selectFirst();
                Path actualPath = fileManager.getActualPath();
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

		pathListView.setItems(fileManager.getPathList());


	}

	private void openTextLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/programmer/programming_layout.fxml"));
		textLayout = loader.load();
		textLayoutController = loader.getController();
		textLayoutController.setBackButtonHandle(backToStart);
	}

	@FXML
	private void setTextLayout(){
		rootLayout.setCenter(textLayout);
	}

	@FXML
	private void setEditorLayout(){
		rootLayout.setCenter(editorLayout);
		editorLayoutController.setOrderList(ordersList);
	}


	private void backToStartScreen(){
            fileManager.setActualPath(
                    new File(System.getProperty("user.dir")).toPath());
			rootLayout.setCenter(startView);
	}

	@FXML
	private void loadExistingProject(){
		try {
		    String filename = pathListView.getSelectionModel().getSelectedItem();
		    String project = fileManager.load(filename);
		    if(project != null) {
                textLayoutController.setProject(project);
                setTextLayout();
                Path actualPath = fileManager.getActualPath();
                ProjectPath.getProjectPath().setPath(actualPath.toAbsolutePath().toString());
                ProjectPath.getProjectPath().setLastFilename(filename);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openEditorLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/programmer/editor_Layout.fxml"));
		editorLayout = loader.load();
		editorLayoutController = loader.getController();
		editorLayoutController.setBackToStart(backToStart);
	}

	@FXML
	private void deleteProject(){
		String path = pathListView.getSelectionModel().getSelectedItem();
        fileManager.delete(path);
	}

	@FXML
	private void closeApp(){
		Platform.exit();
	}

	public void setCommandsList(ArrayList<Order> ordersList){
	    this.ordersList =  ordersList;
	    editorLayoutController.setOrderList(ordersList);
	    textLayoutController.setOrderList(ordersList);
	}

	private void loadElevator() throws IOException{
		FXMLLoader loader = getFXMLLoader("panel/elevator_layout.fxml");
		BorderPane testLayout = getPane(loader);
		ElevatorController controller = getController((loader));
		rootLayout.setCenter(testLayout);

		myThread = new MyThread();
		myThread.setOnSucceeded( value -> myThread.restart());

		controller.setBackToStart(() -> {
			myThread.cancel();
			myThread.reset();
			backToStartScreen();
		});
		ViewManager vw = new ViewManager("elevator.config");
		myThread.setViewManager(vw);
		myThread.start();
		controller.setViewManager(vw);
		controller.setActionsForFloorIndicator();
		controller.setActionsCabinRequests();
		controller.setActionsControls();
	}

	@FXML
	private void loadVisualization(){
        String chosenValue = visualizationChooser.getValue();
        if(chosenValue != null) {
            try {
                switch (chosenValue) {
                    case "Gantry": {
                        break;
                    }
                    case "Elevator": {
                        loadElevator();
                        break;
                    }
                    default:
                        break;
                }
            } catch (IOException e) {
            }
        }
	}

    @FXML
    private void goUpWithDirectory(){
        try {
            Path actualPath = fileManager.getActualPath();
            Path newActualPath = actualPath.toAbsolutePath().getParent();
            fileManager.setActualPath(newActualPath);
        }
        catch(NullPointerException n){}
    }
}
