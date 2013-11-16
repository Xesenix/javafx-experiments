package pl.xesenix.experiments.experiment03.components.viewport.states;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import pl.xesenix.experiments.experiment03.components.viewport.Viewport;

public class SimpleViewportState implements IViewportState
{
	private Viewport viewport;


	private double startDragX;


	private double startDragY;


	private Cursor mouseCursor;


	@Override
	public void initialize(Viewport viewport)
	{
		this.viewport = viewport;
	}


	@Override
	public void addNodeToCanvas(Node node)
	{
		this.viewport.getCanvas().getChildren().add(node);
	}


	@Override
	public void manageCanvasMouseEvents(MouseEvent event)
	{
		if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED))
		{
			Group canvas = viewport.getCanvas();
			
			startDragX = event.getSceneX() - canvas.getTranslateX();
			startDragY = event.getSceneY() - canvas.getTranslateY();
			mouseCursor = ((Node) event.getSource()).getCursor();
			
			event.consume();
		}
		else
		{
			if (event.getButton().equals(MouseButton.SECONDARY))
			{
				if (event.getEventType().equals(MouseEvent.DRAG_DETECTED))
				{
					((Node) event.getSource()).setCursor(Cursor.OPEN_HAND);
				}
				else
				{
					if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
					{
						((Node) event.getSource()).setCursor(mouseCursor);
					}
					else
					{
						if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED))
						{
							Group canvas = viewport.getCanvas();
							double x = event.getSceneX() - startDragX;
							double y = event.getSceneY() - startDragY;
							
							canvas.setTranslateX(x);
							canvas.setTranslateY(y);
							
							viewport.setStyle("-fx-background-position:" + x + " " + y);
							
							event.consume();
						}
					}
				}
			}
		}
	}

}