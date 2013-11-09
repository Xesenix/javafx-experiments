
package pl.xesenix.experiments.experiment02.vo;

import java.util.ArrayList;
import java.util.Collection;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class PathVO implements IPath
{
	ListProperty<IPathPoint> pathPoints = new SimpleListProperty<IPathPoint>(this, "pathPoints", FXCollections.<IPathPoint>observableArrayList());


	/*
	 * (non-Javadoc)
	 * @see
	 * pl.xesenix.experiments.experiment02.vo.IPath#addPathPoint(pl.xesenix.experiments.experiment02.vo.IPathPoint)
	 */
	@Override
	public void addPathPoint(IPathPoint point)
	{
		point.addToPath(this);
		pathPoints.add(point);
	}


	/*
	 * (non-Javadoc)
	 * @see
	 * pl.xesenix.experiments.experiment02.vo.IPath#addPathPoints(pl.xesenix.experiments.experiment02.vo.IPathPoint)
	 */
	@Override
	public void addPathPoints(IPathPoint... points)
	{
		for (IPathPoint point : points)
		{
			addPathPoint(point);
		}
	}


	/*
	 * (non-Javadoc)
	 * @see
	 * pl.xesenix.experiments.experiment02.vo.IPath#addPathPoints(java.util.Collection)
	 */
	@Override
	public void addPathPoints(Collection<? extends IPathPoint> points)
	{
		for (IPathPoint point : points)
		{
			addPathPoint(point);
		}
	}


	/*
	 * (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPath#removePoint(pl.xesenix.experiments.experiment02.vo.IPathPoint)
	 */
	public void removePoint(IPathPoint point)
	{
		point.removeFromPath(this);
		pathPoints.remove(point);
	}


	/*
	 * (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPath#getPathPoints()
	 */
	@Override
	public ObservableList<IPathPoint> getPathPoints()
	{
		return pathPoints.get();
	}
	
	
	@Override
	public String toString()
	{
		return "PathVO {" + getPathPoints().toString() + "}";
	}


	public static class PathBuilder
	{
		ArrayList<IPathPoint> points = new ArrayList<IPathPoint>();


		public static PathBuilder create()
		{
			return new PathBuilder();
		}


		public PathBuilder addPathPoint(IPathPoint point)
		{
			points.add(point);

			return this;
		}


		public PathBuilder addPathPoints(IPathPoint... points)
		{
			for (IPathPoint point : points)
			{
				addPathPoint(point);
			}

			return this;
		}


		public IPath build()
		{
			IPath path = new PathVO();

			path.addPathPoints(points);

			return path;
		}
	}
}
