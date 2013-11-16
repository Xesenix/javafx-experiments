
package pl.xesenix.experiments.experiment03.components.viewport;

import pl.xesenix.experiments.experiment03.components.viewport.states.SimpleViewportStateManager;
import pl.xesenix.experiments.experiment03.components.viewport.states.IViewportState;
import pl.xesenix.experiments.experiment03.components.viewport.states.IViewportStateManager;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class Viewport extends Pane
{

	private Group canvas = new Group();


	protected IViewportState state;


	protected IViewportStateManager stateManager;


	private CanvasEventHandler canvasEventHandler = new CanvasEventHandler();


	private Rectangle clipingRect = new Rectangle();


	public Viewport()
	{
		this(new SimpleViewportStateManager());
	}
	
	
	public Viewport(IViewportStateManager stateManager)
	{
		this.stateManager = stateManager;
		
		getChildren().add(canvas);
		
		clipingRect.widthProperty().bind(this.widthProperty());
		clipingRect.heightProperty().bind(this.heightProperty());
		
		this.setClip(clipingRect);
		
		this.addEventHandler(MouseEvent.ANY, canvasEventHandler);
		
		setState(stateManager.getDefaultState());
	}


	private void setState(IViewportState state)
	{
		this.state = state;
		
		if (this.state != null)
		{
			this.state.initialize(this);
		}
	}


	public Group getCanvas()
	{
		return canvas;
	}
	
	
	public void addNodeToCanvas(Node node)
	{
		state.addNodeToCanvas(node);
	}


	private class CanvasEventHandler implements EventHandler<MouseEvent>
	{
		public void handle(MouseEvent event)
		{
			state.manageCanvasMouseEvents(event);
		}
	}
}
