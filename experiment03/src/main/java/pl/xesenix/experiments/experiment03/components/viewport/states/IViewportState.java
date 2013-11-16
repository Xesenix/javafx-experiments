package pl.xesenix.experiments.experiment03.components.viewport.states;

import pl.xesenix.experiments.experiment03.components.viewport.Viewport;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public interface IViewportState
{
	void initialize(Viewport viewport);


	void manageCanvasMouseEvents(MouseEvent event);


	void addNodeToCanvas(Node node);
}