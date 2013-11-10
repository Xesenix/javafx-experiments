
package pl.xesenix.experiments.experiment02.components.patheditor.states;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment02.components.patheditor.PathsEditor;
import pl.xesenix.experiments.experiment02.components.patheditor.views.PathPointView;


public class EditPathState extends BaseState
{
	@Override
	public void manageCanvasMouseEvent(MouseEvent event)
	{
		if (event.getTarget() instanceof PathPointView)
		{
			log.debug("mouse event over point {}", event.getTarget());
		}
		
		super.manageCanvasMouseEvent(event);
	}
}
