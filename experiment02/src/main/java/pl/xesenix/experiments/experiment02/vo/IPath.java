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

import java.util.Collection;

import javafx.collections.ObservableList;


public interface IPath
{

	void addPathPoint(IPathPoint point);


	void addPathPoints(IPathPoint... points);


	void addPathPoints(Collection<? extends IPathPoint> points);


	ObservableList<IPathPoint> getPathPoints();


	void removePoint(IPathPoint point);
	
	
	int getSize();
}
