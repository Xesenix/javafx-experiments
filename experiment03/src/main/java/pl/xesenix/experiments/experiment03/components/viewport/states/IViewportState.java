package pl.xesenix.experiments.experiment03.components.viewport.states;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import pl.xesenix.experiments.experiment03.components.viewport.Viewport;

public interface IViewportState
{
	void initialize(Viewport viewport);


	void manageCanvasMouseEvents(MouseEvent event);


	void addNodeToCanvas(Node node);
}