
package pl.xesenix.experiments.experiment02.components.patheditor.states;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment02.components.patheditor.PathsEditor;
import pl.xesenix.experiments.experiment02.components.patheditor.views.PathPointView;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;


public class EditPathState extends BaseState
{
	@Override
	public void manageCanvasMouseEvent(MouseEvent event)
	{
		log.debug("event: [{}]", event);
		
		Node target = (Node) event.getTarget();
		Node parent = target.getParent();
		
		if (parent instanceof PathPointView)
		{
			PathPointView pathPointView = (PathPointView) parent;
			IPathPoint point = (IPathPoint) pathPointView.getUserData();
			
			if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED))
			{
				startDragX = event.getSceneX() - pathPointView.getTranslateX();
				startDragY = event.getSceneY() - pathPointView.getTranslateY();
				mouseCursor = ((Node) event.getSource()).getCursor();
				
				pathsEditor.getMediator().selectPoint(point);
				
				event.consume();
			}
			else
			{
				if (event.getEventType().equals(MouseEvent.DRAG_DETECTED))
				{
					((Node) event.getSource()).setCursor(cursorProvider.get(Cursor.CLOSED_HAND));
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
							double x = event.getSceneX() - startDragX;
							double y = event.getSceneY() - startDragY;
							//pathPointView.setTranslateX(x);
							//pathPointView.setTranslateY(y);
							
							pathsEditor.getMediator().updatePointPosition(x, y);
							
							event.consume();
						}
					}
				}
			}
		}
		else
		{
			super.manageCanvasMouseEvent(event);
		}
	}
}
