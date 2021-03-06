package application;
	
import java.io.File;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	FileChooser mediaChooser;
	Player mediaPlayer;
	
	MenuBar menu;
	Menu filemenu;
	MenuItem openItem;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			
			mediaChooser = new FileChooser();
			
			menu = new MenuBar();
			filemenu = new Menu("File");
			openItem = new MenuItem("Open");
			
			filemenu.getItems().add(openItem);
			menu.getMenus().add(filemenu);
			
			openItem.setOnAction((e) -> {
				
				try {
					File mediaFile = mediaChooser.showOpenDialog(primaryStage);
					System.out.println(mediaFile.getAbsolutePath() + ": " + mediaFile.toURI().toURL().toExternalForm());
					mediaPlayer = new Player(mediaFile.toURI().toURL().toExternalForm());
					
					root.setCenter(mediaPlayer);
					
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			root.setTop(menu);
					
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
