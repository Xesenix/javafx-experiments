package pl.xesenix.experiments.experiment02.views.path;

import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;
import pl.xesenix.experiments.experiment02.vo.PathPointVO;

public interface IPathMediator
{
	void createPath();
	
	
	void setCurrentPath(IPath path);


	void createPoint(double x, double y);
	
	
	void addPointToPath(IPathPoint point);
	
	
	void removePoint(IPathPoint point);


	void registerView(IPathView pathView);
}
