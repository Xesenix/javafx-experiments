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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class PathPointVO implements IPathPoint
{
	private DoubleProperty x = new SimpleDoubleProperty(this, "x");


	private DoubleProperty y = new SimpleDoubleProperty(this, "y");


	private DoubleProperty inX = new SimpleDoubleProperty(this, "inX");


	private DoubleProperty inY = new SimpleDoubleProperty(this, "inY");


	private DoubleProperty outX = new SimpleDoubleProperty(this, "outX");


	private DoubleProperty outY = new SimpleDoubleProperty(this, "outY");


	private IPath path;


	public DoubleProperty xProperty()
	{
		return x;
	}


	public double getX()
	{
		return x.get();
	}


	public void setX(double value)
	{
		this.x.setValue(value);
	}


	public DoubleProperty yProperty()
	{
		return y;
	}


	public double getY()
	{
		return y.get();
	}


	public void setY(double value)
	{
		this.y.setValue(value);
	}


	public DoubleProperty inXProperty()
	{
		return inX;
	}


	public double getInX()
	{
		return inX.get();
	}


	public void setInX(double value)
	{
		this.inX.setValue(value);
	}


	public DoubleProperty inYProperty()
	{
		return inY;
	}


	public double getInY()
	{
		return inY.get();
	}


	public void setInY(double value)
	{
		this.inY.setValue(value);
	}


	public DoubleProperty outXProperty()
	{
		return outX;
	}


	public double getOutX()
	{
		return outX.get();
	}


	public void setOutX(double value)
	{
		this.outX.setValue(value);
	}


	public DoubleProperty outYProperty()
	{
		return outY;
	}


	public double getOutY()
	{
		return outY.get();
	}


	public void setOutY(double value)
	{
		this.outY.setValue(value);
	}


	public void setInDirection(double direction)
	{
		setDirection(this.inX, this.inY, direction);
	}


	public void setOutDirection(double direction)
	{
		setDirection(this.outX, this.outY, direction);
	}


	protected void setDirection(DoubleProperty xProperty, DoubleProperty yProperty, double direction)
	{
		double weight = Math.sqrt(Math.pow(this.x.get(), 2) + Math.pow(this.y.get(), 2));
		this.x.set(weight * Math.cos(direction));
		this.y.set(weight * Math.sin(direction));
	}


	@Override
	public IPath getPath()
	{
		return path;
	}


	@Override
	public void addToPath(IPath path)
	{
		this.path = path;
	}


	@Override
	public void removeFromPath(IPath path)
	{
		this.path = null;
	}
	
	
	@Override
	public String toString()
	{
		return "PathPointVO (" + getX() + ", " + getY() + ")";
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


		public PathPointVO build()
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


		public PathPointVO build()
		{
			PathPointVO point = new PathPointVO();

			apply(point);

			return point;
		}
	}

}
