
package pl.xesenix.experiments.experiment02.views.path;

import pl.xesenix.experiments.experiment02.vo.IPath;


public interface IPathView
{
	void updatePath(IPath path);


	void createPathView(IPath path);


	void focusPath(IPath path);


	void update();
}
