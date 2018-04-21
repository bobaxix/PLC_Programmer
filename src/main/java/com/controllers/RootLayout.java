package com.controllers;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.panel.connect.MyThread;
import com.panel.view.ViewManager;
import com.programmer.orders.Order;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private Path actualPath;

	@FXML
	ListView<String> pathListView;

	@FXML
	BorderPane rootLayout;

	@FXML
	HBox startView;

	@FXML
	private BorderPane testLayout;

	@FXML
	private ChoiceBox<String> visualizationChooser;


	ObservableList<String> pathList;

	private PopupWindow keyboard;

	private final Rectangle2D bounds = Screen.getPrimary().getBounds();


	@FXML
	private void initialize(){

	    actualPath = new File(System.getProperty("user.dir")).toPath();

	    pathList = FXCollections.observableArrayList();

		backToStart = () -> backToStartScreen();

		try {
			openTextLayout();
			openEditorLayout();
		} catch (IOException e) {
			e.printStackTrace();
		}

		refreshFileList();

        pathListView.setCellFactory(param -> new ListCell<String>(){
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String name, boolean empty){
                super.updateItem(name, empty);
                pathListView.getSelectionModel().selectFirst();
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

		pathListView.setItems(pathList);


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
            actualPath = new File(System.getProperty("user.dir")).toPath();
			rootLayout.setCenter(startView);
			refreshFileList();
	}

	@FXML
	private void loadExistingProject(){
		try {
            String filePath = actualPath.toAbsolutePath().toString() +
                    File.separator +
                    pathListView.getSelectionModel().getSelectedItem();
            File f = new File(filePath);
            if(filePath != null){
                if(f.isDirectory()) {
                    actualPath = f.toPath();
                    refreshFileList();
                }
                else {
                    StringBuilder sb = getProjectFromSelectedFile(
                            new File(filePath).getAbsolutePath());
                    textLayoutController.setProject(sb);
                    setTextLayout();
                }
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
		if(path != null) {
            File files = actualPath.toFile();
            for (File f : files.listFiles()) {
                if (f.getPath().equals(path)) {
                    f.delete();
                    pathList.remove(path);
                }
            }
        }
	}

	@FXML
	private void closeApp(){
		Platform.exit();
	}

    private void refreshFileList(){
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

	private StringBuilder getProjectFromSelectedFile(String filePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while((line = br.readLine()) != null){
			sb.append(line+System.lineSeparator());
		}

		br.close();
		return sb;
	}

	public void setCommandsList(ArrayList<Order> ordersList){
	    this.ordersList =  ordersList;
	    editorLayoutController.setOrderList(ordersList);
	    textLayoutController.setOrderList(ordersList);
	}

	private void loadElevator() throws IOException{
		FXMLLoader loader = getFXMLLoader("panel/elevator_layout.fxml");
		testLayout = getPane(loader);
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
            actualPath = actualPath.toAbsolutePath().getParent();
            refreshFileList();
        }
        catch(NullPointerException n){}
    }
}
