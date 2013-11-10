
package pl.xesenix.experiments.experiment02.components.patheditor.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;
import pl.xesenix.experiments.experiment02.vo.PathPointVO.PathPointBuilder;
import pl.xesenix.experiments.experiment02.vo.PathVO.PathBuilder;
import pl.xesenix.experiments.experiment02.vo.PathVO.SmoothPathBuilder;

import com.google.inject.Singleton;


@Singleton
public class PathDrawingContext
{
	private static final Logger log = LoggerFactory.getLogger(PathDrawingContext.class);
	
	
	private ObservableList<IPathPoint> points = FXCollections.observableArrayList();


	private ObservableList<IPath> paths = FXCollections.observableArrayList();


	private IPath currentEditedPath;


	public IPath createPath()
	{
		log.debug("creating path");
		
		IPath path = PathBuilder.create().build();
		
		paths.add(path);
		
		return path;
	}


	public IPath getEditedPath()
	{
		return currentEditedPath;
	}


	public void setEditedPath(IPath path)
	{
		log.debug("selecting for edit path [{}]", path);
		
		currentEditedPath = path;
	}


	public IPathPoint createPoint(double x, double y)
	{
		log.debug("creating point at ({}, {})", x, y);
		
		// TODO Check for current drawing state
		// return currentDrawingState.createPoint(x, y); - state pattern
		
		IPathPoint point = PathPointBuilder.create().setX(x).setY(y).build();
		
		points.add(point);
		
		currentEditedPath.addPathPoint(point);

		return point;
	}


	public void removePoint(IPathPoint point)
	{
		log.debug("removing point at [{}]", point);
		
		IPath path = point.getPath();
		
		path.removePoint(point);
		
		points.remove(point);
	}


	public void smoothPath()
	{
		log.debug("smoothing path [{}]", currentEditedPath);
		
		SmoothPathBuilder.create().applyTo(currentEditedPath);
	}
}
