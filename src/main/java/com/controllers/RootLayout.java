package com.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import com.panel.connect.MyThread;
import com.panel.view.ViewManager;
import com.programmer.orders.Order;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Window;


public class RootLayout extends Controller{

	private BorderPane textLayout;
	private BorderPane editorLayout;
	private textLayoutController textLayoutController;
	private EditorLayoutController editorLayoutController;
	private ArrayList<Order> ordersList;
	private Runnable backToStart;

	private MyThread myThread;

	@FXML
	ListView<Path> pathList;

	@FXML
	BorderPane rootLayout;

	@FXML
	HBox startView;

	@FXML
	private BorderPane testLayout;

	@FXML
	private ChoiceBox<String> visualizationChooser;


	ObservableList<Path> fileList = FXCollections.observableArrayList();

	private PopupWindow keyboard;

	private final Rectangle2D bounds = Screen.getPrimary().getBounds();


	@FXML
	private void initialize(){

		backToStart = () -> backToStartScreen();

		try {
			openTextLayout();
			openEditorLayout();
		} catch (IOException e) {
			e.printStackTrace();
		}

		refreshFileList();
		pathList.setItems(fileList);


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
			rootLayout.setCenter(startView);
			refreshFileList();
	}

	@FXML
	private void loadExistingProject(){
		try {
			StringBuilder sb = getProjectFromSelectedFile();
			textLayoutController.setProject(sb);
			setTextLayout();

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

		Path path = pathList.getSelectionModel().getSelectedItem();
		String filePath = path.getPath();
		String userDirectory = System.getProperty("user.dir");
		File files = new File(userDirectory);
		for(File f : files.listFiles()){
			if(f.getPath().equals(filePath)) {
				f.delete();
				fileList.remove(path);
			}
		}
	}

	@FXML
	private void closeApp(){
		Platform.exit();
	}

	private void refreshFileList(){

		fileList.clear();

		String userDirectory = System.getProperty("user.dir");
		File files = new File(userDirectory);
		for(File f : files.listFiles()){
			if(f.getName().endsWith(".txt"))
				fileList.add(new Path(f.getPath(),f.getName()));
		}

	}

	private StringBuilder getProjectFromSelectedFile() throws IOException {

		Path filePath = pathList.getSelectionModel().getSelectedItem();
		BufferedReader br = new BufferedReader(new FileReader(filePath.getPath()));
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

	public class Path{

		String fullPath;
		String fileName;

		public Path(String path, String name){
			this.fullPath = path;
			this.fileName = name;
		}

		public String getPath(){
			return fullPath;
		}

		@Override
		public String toString(){
			return fileName;
		}

	}

	private void loadElevator() throws IOException{
		FXMLLoader loader = getFXMLLoader("panel/elevator_layout.fxml");
		testLayout = getPane(loader);
		ElevatorController controller = getController((loader));
		rootLayout.setCenter(testLayout);
		controller.setBackToStart(() -> {
			myThread.cancel();
			myThread.reset();
			backToStartScreen();
		});
		ViewManager vw = new ViewManager("elevator.config", myThread.getControl());
		myThread.setViewManager(vw);
		myThread.start();
		controller.setViewManager(vw);
		controller.setActionsForFloorIndicator();
		controller.setActionsCabinRequests();
		controller.setActionsControls();
	}

	public void setSpiService(MyThread thread){
		this.myThread = thread;
	}

	@FXML
	private void loadVisualization(){

		try {
			switch (visualizationChooser.getValue()) {
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
		}
		catch(IOException e){}
	}

}
