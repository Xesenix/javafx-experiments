
package pl.xesenix.experiments.experiment02.components.patheditor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleButtonBuilder;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment02.components.patheditor.states.AddPathPointState;
import pl.xesenix.experiments.experiment02.components.patheditor.states.EditPathState;
import pl.xesenix.experiments.experiment02.components.patheditor.states.IPathEditorState;
import pl.xesenix.experiments.experiment02.components.patheditor.views.PathPointView;
import pl.xesenix.experiments.experiment02.components.patheditor.views.PathView;
import pl.xesenix.experiments.experiment02.cursors.CursorProvider;
import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;


@Singleton
public class PathsEditor extends StackPane implements IPathEditorView
{
	protected static final Logger log = LoggerFactory.getLogger(PathsEditor.class);


	@Inject
	protected EditPathState editPathState;


	@Inject
	protected AddPathPointState addPathPointState;


	@Inject
	protected IPathEditorMediator mediator;
	
	
	@Inject
	protected Injector injector;
	
	
	protected ListProperty<PathView> paths = new SimpleListProperty(this, "paths");
	
	
	protected ListProperty<PathPointView> points = new SimpleListProperty(this, "points");
	
	
	@Inject
	protected ResourceBundle resources;


	@FXML
	protected URL location;


	@FXML
	protected Pane canvas;


	@FXML
	protected Pane canvasContainer;


	@FXML
	protected ToolBar toolbar;


	@FXML
	protected StackPane view;


	protected ObservableMap<IPath, PathView> pathToViewMap;


	private IPathEditorState state;


	private Button newPathButton;


	private ToggleButton addPathPointsButton;


	private Button smoothPathButton;


	@FXML
	void initialize()
	{
		log.debug("initializing");
		
		assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'pathEditor.fxml'.";
		assert canvasContainer != null : "fx:id=\"canvasContainer\" was not injected: check your FXML file 'pathEditor.fxml'.";
		assert toolbar != null : "fx:id=\"toolbar\" was not injected: check your FXML file 'pathEditor.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'pathEditor.fxml'.";
		
		CanvasEventHandler canvasEventHandler = new CanvasEventHandler();
		canvasContainer.addEventHandler(MouseEvent.ANY, canvasEventHandler);
		
		newPathButton = ButtonBuilder.create().text(resources.getString("editor.add_new_path")).build();
		newPathButton.setOnMouseClicked(new AddPathEventHandler());
		
		toolbar.getItems().add(newPathButton);
		
		addPathPointsButton = ToggleButtonBuilder.create().text(resources.getString("editor.add_path_points")).build();
		addPathPointsButton.selectedProperty().addListener(new TogglePathPointsAddModeListener());
		
		toolbar.getItems().add(addPathPointsButton);
		
		smoothPathButton = ButtonBuilder.create().text(resources.getString("editor.smoot_path")).build();
		smoothPathButton.setOnMousePressed(new SmoothPathEventHandler());
		
		toolbar.getItems().add(smoothPathButton);
		
		setState(editPathState);
	}
	
	
	@Inject
	public PathsEditor(Injector injector)
	{
		log.debug("constructing");
		
		injector.injectMembers(this);
		
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


	public void setState(IPathEditorState state)
	{
		log.debug("setting editor state to: [{}]", state);
		
		state.initialize(this);
		
		this.state = state;
	}


	@Inject
	public void setMediator(IPathEditorMediator mediator)
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
		
		PathView pathView = injector.getInstance(PathView.class);
		pathView.update(path);
		
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
	public void focusPoint(IPathPoint point)
	{
		log.debug("focusing pathPointView for point: [{}]", point);
	}


	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}
	
	
	public Pane getCanvas()
	{
		return canvas;
	}


	public Pane getCanvasContainer()
	{
		return canvasContainer;
	}


	public IPathEditorMediator getMediator()
	{
		return mediator;
	}


	private class CanvasEventHandler implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent event)
		{
			state.manageCanvasMouseEvent(event);
		}
	}


	private class AddPathEventHandler implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent event)
		{
			log.debug("=== CREATE NEW PATH CLICKED ===");
			
			getMediator().createPath();
		}
	}
	
	
	public class SmoothPathEventHandler implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent event)
		{
			log.debug("=== SMOOTH PATH CLICKED ===");
			
			getMediator().smoothPath();
		}
	}
	
	
	public class TogglePathPointsAddModeListener implements ChangeListener<Boolean>
	{

		@Override
		public void changed(ObservableValue<? extends Boolean> observed, Boolean oldValue, Boolean newValue)
		{
			if (newValue)
			{
				setState(addPathPointState);
			}
			else
			{
				setState(editPathState);
			}
		}

	}
}
