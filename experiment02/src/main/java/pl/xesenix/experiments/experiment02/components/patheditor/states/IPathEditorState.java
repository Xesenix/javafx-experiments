
package pl.xesenix.experiments.experiment02.components.patheditor.states;

import pl.xesenix.experiments.experiment02.components.patheditor.PathsEditor;
import javafx.scene.input.MouseEvent;


public interface IPathEditorState
{
	void manageCanvasMouseEvent(MouseEvent event);


	void initialize(PathsEditor pathsEditor);
}
