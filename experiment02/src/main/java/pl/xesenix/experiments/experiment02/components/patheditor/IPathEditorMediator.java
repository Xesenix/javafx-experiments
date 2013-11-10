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
package pl.xesenix.experiments.experiment02.components.patheditor;

import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

public interface IPathEditorMediator
{
	void createPath();
	
	
	void setCurrentPath(IPath path);


	void createPoint(double x, double y);
	
	
	void addPointToPath(IPathPoint point);
	
	
	void removePoint(IPathPoint point);


	void registerView(IPathEditorView pathView);


	void smoothPath();


	void selectPoint(IPathPoint point);


	void updatePoint(double x, double y, double inX, double inY, double outX, double outY);
}
