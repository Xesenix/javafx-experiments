
package pl.xesenix.experiments.experiment02.views.path;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment02.vo.IPath;

import com.google.inject.Inject;


public class PathsEditor extends StackPane implements IPathView
{
	private static final Logger log = LoggerFactory.getLogger(PathsEditor.class);


	private IPathMediator mediator;
	
	
	private ListProperty<PathView> paths = new SimpleListProperty(this, "paths");
	
	
	private ListProperty<PathPointView> points = new SimpleListProperty(this, "points");
	
	
	@FXML
	private ResourceBundle resources;


	@FXML
	private URL location;


	@FXML
	private Pane canvas;


	@FXML
	private Pane canvasContainer;


	@FXML
	private ToolBar toolbar;


	@FXML
	private StackPane view;


	private ObservableMap<IPath, PathView> pathToViewMap;


	@FXML
	void initialize()
	{
		log.debug("initialize");
		
		assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'pathEditor.fxml'.";
		assert toolbar != null : "fx:id=\"toolbar\" was not injected: check your FXML file 'pathEditor.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'pathEditor.fxml'.";
		
		CanvasEventHandler canvasEventHandler = new CanvasEventHandler();
		canvasContainer.setOnMousePressed(canvasEventHandler);
		canvasContainer.setOnMouseDragged(canvasEventHandler);
		canvasContainer.setOnMouseClicked(canvasEventHandler);
		
		Button newPathButton = ButtonBuilder.create().text("new path").build();
		newPathButton.setOnMouseClicked(new AddPathEventHandler());
		
		toolbar.getItems().add(newPathButton);
	}
	
	
	public PathsEditor()
	{
		pathToViewMap = FXCollections.<IPath, PathView>observableHashMap();
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/pathsEditor.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try
		{
			fxmlLoader.load();
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
	}


	@Inject
	public void setMediator(IPathMediator mediator)
	{
		log.debug("registring mediator: [{}] for view: [{}]", mediator, this);
		
		this.mediator = mediator;
		this.mediator.registerView(this);
	}


	@Override
	public void updatePath(IPath path)
	{
		log.debug("update pathView for path: [{}]", path);
		
		PathView pathView = pathToViewMap.get(path);
		
		pathView.update(path);
	}


	@Override
	public void createPathView(IPath path)
	{
		log.debug("creating pathView for path: [{}]", path);
		
		PathView pathView = new PathView(path);
		
		pathToViewMap.put(path, pathView);
		
		canvas.getChildren().add(pathView);
		
		mediator.setCurrentPath(path);
	}


	@Override
	public void focusPath(IPath path)
	{
		log.debug("focusing pathView for path: [{}]", path);
	}


	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}
	
	
	private class CanvasEventHandler implements EventHandler<MouseEvent>
	{
		private double startDragX;
		
		
		private double startDragY;
		
		
		@Override
		public void handle(MouseEvent event)
		{
			if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED) && event.getButton().equals(MouseButton.PRIMARY))
			{
				Point2D point = canvas.sceneToLocal(event.getSceneX(), event.getSceneY());
				log.debug("=== REQUESTING CREATE POINT AT {} ===", point);
				mediator.createPoint(point.getX(), point.getY());
			}
			else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED) && event.getButton().equals(MouseButton.SECONDARY))
			{
				startDragX = event.getSceneX() - canvas.getTranslateX();
				startDragY = event.getSceneY() - canvas.getTranslateY();
			}
			else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED) && event.getButton().equals(MouseButton.SECONDARY))
			{
				canvas.setTranslateX(event.getSceneX() - startDragX);
				canvas.setTranslateY(event.getSceneY() - startDragY);
			}
		}
	}


	private class AddPathEventHandler implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent event)
		{
			log.debug("=== CREATE NEW PATH CLICKED ===");
			
			mediator.createPath();
		}
	}
}
