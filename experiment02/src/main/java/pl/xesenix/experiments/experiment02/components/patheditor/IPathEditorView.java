
package pl.xesenix.experiments.experiment02.components.patheditor;

import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;


public interface IPathEditorView
{
	void updatePath(IPath path);


	void createPathView(IPath path);


	void focusPath(IPath path);


	void focusPoint(IPathPoint point);


	void update();
}
