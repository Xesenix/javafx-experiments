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

import javafx.beans.property.DoubleProperty;





public interface IPathPoint
{

	public double getX();


	public void setX(double value);


	public double getY();


	public void setY(double value);


	public double getInX();


	public void setInX(double value);


	public double getInY();


	public void setInY(double value);


	public double getOutX();


	public void setOutX(double value);


	public double getOutY();


	public void setOutY(double value);


	public void setInDirection(double direction);


	public void setOutDirection(double direction);


	public void setPath(IPath path);


	public IPath getPath();


	public void removeFromPath(IPath path);

}
