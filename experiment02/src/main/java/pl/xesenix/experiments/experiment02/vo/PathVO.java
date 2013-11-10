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
	
	
	public static class SmoothPathBuilder extends PathBuilder
	{
		ArrayList<IPathPoint> points = new ArrayList<IPathPoint>();


		public static SmoothPathBuilder create()
		{
			return new SmoothPathBuilder();
		}
		
		
		public void applyTo(IPath path)
		{
			ObservableList<IPathPoint> list = path.getPathPoints();
			
			IPathPoint first = list.get(0);
			IPathPoint second = null;
			
			if (list.size() > 1)
			{
				second = list.get(1);
				
				IPathPoint previous = first;
				IPathPoint point = second;
				IPathPoint next = null;
				
				for (int i = 2; i < list.size(); i++)
				{
					next = list.get(i);
					
					smooth(point, previous, next);
					
					previous = point;
					point = next;
				}
				
				smooth(point, previous, null);
			}
			
			smooth(first, null, second);
		}


		public void smooth(IPathPoint point, IPathPoint previousPoint, IPathPoint nextPoint)
		{
			double dx, dy, direction, weightIn, weightOut;
			
			if (previousPoint == null)
			{
				if (nextPoint == null)
				{
					return;
				}
				else
				{
					dx = nextPoint.getInX() + nextPoint.getX() - point.getX();
					dy = nextPoint.getInY() + nextPoint.getY() - point.getY();
					
					direction = Math.atan2(dy, dx);
					weightIn = weightOut = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) / 2f;
				}
			}
			else
			{
				if (nextPoint == null)
				{
					dx = point.getX() - (previousPoint.getOutX() + previousPoint.getX());
					dy = point.getY() - (previousPoint.getOutY() + previousPoint.getY());
					
					direction = Math.atan2(dy, dx);
					
					weightIn = weightOut = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) / 2f;
				}
				else
				{
					dx = nextPoint.getX() - previousPoint.getX();
					dy = nextPoint.getY() - previousPoint.getY();
					
					direction = Math.atan2(dy, dx);
					
					dx = point.getX() - previousPoint.getX();
					dy = point.getY() - previousPoint.getY();
					
					weightIn = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) / 2f;
					
					dx = nextPoint.getX() - point.getX();
					dy = nextPoint.getY() - point.getY();
					
					weightOut = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) / 2f;
				}
			}
			
			point.setInX(weightIn * Math.cos(direction + Math.PI));
			point.setInY(weightIn * Math.sin(direction + Math.PI));
			point.setOutX(weightOut * Math.cos(direction));
			point.setOutY(weightOut * Math.sin(direction));
		}


		public IPath build()
		{
			IPath path = new PathVO();

			path.addPathPoints(points);
			
			applyTo(path);

			return path;
		}
	}
}
