package pl.xesenix.experiments.experiment02.components.patheditor;

import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

public interface IPathEditorMediator
{
	void createPath();
	
	
	void setCurrentPath(IPath path);


	void createPoint(double x, double y);
	
	
	void addPointToPath(IPathPoint point);
	
	
	void removePoint(IPathPoint point);


	void registerView(IPathEditorView pathView);


	void smoothPath();
}
