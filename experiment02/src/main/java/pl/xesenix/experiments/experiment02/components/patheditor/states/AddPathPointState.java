package pl.xesenix.experiments.experiment02.components.patheditor.states;

import com.google.inject.Inject;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import pl.xesenix.experiments.experiment02.components.patheditor.PathsEditor;

public class AddPathPointState extends BaseState
{
	@Override
	public void manageCanvasMouseEvent(MouseEvent event)
	{
		if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED) && event.getButton().equals(MouseButton.PRIMARY))
		{
			Point2D point = pathsEditor.getCanvas().sceneToLocal(event.getSceneX(), event.getSceneY());
			log.debug("=== REQUESTING CREATE POINT AT {} ===", point);
			pathsEditor.getMediator().createPoint(point.getX(), point.getY());
			event.consume();
		}
		else
		{
			super.manageCanvasMouseEvent(event);
		}
	}
}
