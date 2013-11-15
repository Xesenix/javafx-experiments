/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/
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


	private IPathPoint currentSelectedPoint;


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
		log.debug("selecting for edit path: [{}]", path);
		
		currentEditedPath = path;
	}


	public IPathPoint createPoint(double x, double y, double inX, double inY, double outX, double outY)
	{
		log.debug("creating point at: ({}, {})", x, y);
		
		// TODO Check for current drawing state
		// return currentDrawingState.createPoint(x, y); - state pattern
		
		IPathPoint point = PathPointBuilder.create()
			.setX(x)
			.setY(y)
			.setInX(inX)
			.setInY(inY)
			.setOutX(outX)
			.setOutY(outY)
			.build();
		
		points.add(point);

		return point;
	}


	public void removePoint(IPathPoint point)
	{
		log.debug("removing point at: [{}]", point);
		
		IPath path = point.getPath();
		
		path.removePoint(point);
		
		points.remove(point);
	}


	public void smoothPath()
	{
		log.debug("smoothing path: [{}]", currentEditedPath);
		
		SmoothPathBuilder.create().applyTo(currentEditedPath);
	}


	public void selectPoint(IPathPoint point)
	{
		log.debug("selecting point: [{}]", point);
		
		currentSelectedPoint = point;
	}


	public void updateSelectedPoint(double x, double y, double inX, double inY, double outX, double outY)
	{
		if (currentSelectedPoint != null)
		{
			currentSelectedPoint.setX(x);
			currentSelectedPoint.setY(y);
			currentSelectedPoint.setInX(inX);
			currentSelectedPoint.setInY(inY);
			currentSelectedPoint.setOutX(outX);
			currentSelectedPoint.setOutY(outY);
		}
	}


	public IPathPoint getEditedPoint()
	{
		return currentSelectedPoint;
	}


	public void addPointToCurrentPath(IPathPoint point)
	{
		if (currentEditedPath != null)
		{
			currentEditedPath.addPathPoint(point);
		}
	}
}
