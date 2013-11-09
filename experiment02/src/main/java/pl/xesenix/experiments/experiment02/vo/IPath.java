
package pl.xesenix.experiments.experiment02.vo;

import java.util.Collection;

import javafx.collections.ObservableList;


public interface IPath
{

	void addPathPoint(IPathPoint point);


	void addPathPoints(IPathPoint... points);


	void addPathPoints(Collection<? extends IPathPoint> points);


	ObservableList<IPathPoint> getPathPoints();


	void removePoint(IPathPoint point);

}