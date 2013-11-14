/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/

package pl.xesenix.experiments.experiment02.vo;

import org.apache.commons.lang3.mutable.MutableDouble;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;


public class PathPointVO implements IPathPoint
{
	private MutableDouble x = new MutableDouble();


	private MutableDouble y = new MutableDouble();


	private MutableDouble inX = new MutableDouble();


	private MutableDouble inY = new MutableDouble();


	private MutableDouble outX = new MutableDouble();


	private MutableDouble outY = new MutableDouble();


	private IPath path = null;


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#getX()
	 */
	public double getX()
	{
		return x.getValue();
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setX(double)
	 */
	public void setX(double value)
	{
		this.x.setValue(value);
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#getY()
	 */
	public double getY()
	{
		return y.getValue();
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setY(double)
	 */
	public void setY(double value)
	{
		this.y.setValue(value);
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#getInX()
	 */
	public double getInX()
	{
		return inX.getValue();
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setInX(double)
	 */
	public void setInX(double value)
	{
		this.inX.setValue(value);
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#getInY()
	 */
	public double getInY()
	{
		return inY.getValue();
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setInY(double)
	 */
	public void setInY(double value)
	{
		this.inY.setValue(value);
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#getOutX()
	 */
	public double getOutX()
	{
		return outX.getValue();
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setOutX(double)
	 */
	public void setOutX(double value)
	{
		this.outX.setValue(value);
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#getOutY()
	 */
	public double getOutY()
	{
		return outY.getValue();
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setPath(pl.xesenix.experiments.experiment02.vo.IPath)
	 */
	public void setPath(IPath path)
	{
		IPath oldPath = this.getPath();

		this.path = path;

		if (path != oldPath && oldPath != null)
		{
			oldPath.removePoint(this);
		}
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#getPath()
	 */
	public IPath getPath()
	{
		return path;
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setOutY(double)
	 */
	public void setOutY(double value)
	{
		this.outY.setValue(value);
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setInDirection(double)
	 */
	public void setInDirection(double direction)
	{
		setDirection(this.inX, this.inY, direction);
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#setOutDirection(double)
	 */
	public void setOutDirection(double direction)
	{
		setDirection(this.outX, this.outY, direction);
	}


	/* (non-Javadoc)
	 * @see pl.xesenix.experiments.experiment02.vo.IPathPoint#removeFromPath(pl.xesenix.experiments.experiment02.vo.IPath)
	 */
	public void removeFromPath(IPath path)
	{
		if (path != null && path == this.getPath())
		{
			this.path = null;
		}
	}


	protected void setDirection(MutableDouble x, MutableDouble y, double direction)
	{
		double weight = Math.sqrt(Math.pow(x.getValue(), 2) + Math.pow(y.getValue(), 2));
		x.setValue(weight * Math.cos(direction));
		y.setValue(weight * Math.sin(direction));
	}


	@Override
	public String toString()
	{
		return String.format("PathPointVO {point: (%.2f, %.2f); in: (%.2f, %.2f); out: (%.2f, %.2f)}",
			getX(),
			getY(),
			getInX(),
			getInY(),
			getOutX(),
			getOutY());
	}


	public static class PathPointBuilder
	{
		private double x;


		private double y;


		public static PathPointBuilder create()
		{
			return new PathPointBuilder();
		}


		public PathPointBuilder setX(double x)
		{
			this.x = x;

			return this;
		}


		public PathPointBuilder setY(double y)
		{
			this.y = y;

			return this;
		}


		public PathPointBuilder apply(PathPointVO point)
		{
			point.setX(x);
			point.setY(y);

			return this;
		}


		public IPathPoint build()
		{
			PathPointVO point = new PathPointVO();

			apply(point);

			return point;
		}
	}

	public static class PathBazierCornerPointBuilder
	{
		private double x;


		private double y;


		private double inDirection;


		private double outDirection;


		private double inWeight;


		private double outWeight;


		public static PathBazierCornerPointBuilder create()
		{
			return new PathBazierCornerPointBuilder();
		}


		public PathBazierCornerPointBuilder setX(double x)
		{
			this.x = x;

			return this;
		}


		public PathBazierCornerPointBuilder setY(double y)
		{
			this.y = y;

			return this;
		}


		public PathBazierCornerPointBuilder setWeight(double weight)
		{
			this.inWeight = weight;
			this.outWeight = weight;

			return this;
		}


		public PathBazierCornerPointBuilder setDirection(double radian)
		{
			this.inDirection = radian;
			this.outDirection = (double) (radian + Math.PI / 2f);

			return this;
		}


		public PathBazierCornerPointBuilder setInDirection(double radian)
		{
			this.inDirection = radian;

			return this;
		}


		public PathBazierCornerPointBuilder setInWeight(double weight)
		{
			this.inWeight = weight;

			return this;
		}


		public PathBazierCornerPointBuilder setOuDirection(double radian)
		{
			this.outDirection = radian;

			return this;
		}


		public PathBazierCornerPointBuilder setOutWeight(double weight)
		{
			this.outWeight = weight;

			return this;
		}


		public PathBazierCornerPointBuilder apply(PathPointVO point)
		{
			point.setX(x);
			point.setY(y);

			point.setInX(inWeight);
			point.setInDirection(inDirection);

			point.setOutX(outWeight);
			point.setOutDirection(outDirection);

			return this;
		}


		public IPathPoint build()
		{
			PathPointVO point = new PathPointVO();

			apply(point);

			return point;
		}
	}

}
