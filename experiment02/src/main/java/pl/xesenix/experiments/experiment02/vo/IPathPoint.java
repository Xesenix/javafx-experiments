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

	double getX();


	void setX(double value);


	double getY();


	void setY(double value);


	double getInX();


	void setInX(double value);


	double getInY();


	void setInY(double value);


	public double getOutX();


	void setOutX(double value);


	double getOutY();


	void setOutY(double value);


	IPath getPath();


	void setPath(IPath path);


	void removeFromPath(IPath path);
}
