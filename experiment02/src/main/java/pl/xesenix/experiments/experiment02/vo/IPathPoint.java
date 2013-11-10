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

public interface IPathPoint
{
	DoubleProperty xProperty();


	double getX();


	void setX(double value);


	DoubleProperty yProperty();


	double getY();


	void setY(double value);


	DoubleProperty inXProperty();


	double getInX();


	void setInX(double value);


	DoubleProperty inYProperty();


	double getInY();


	void setInY(double value);


	DoubleProperty outXProperty();


	public double getOutX();


	void setOutX(double value);


	DoubleProperty outYProperty();


	double getOutY();


	void setOutY(double value);
	
	
	IPath getPath();
	
	
	void addToPath(IPath path);


	void removeFromPath(IPath path);
}
