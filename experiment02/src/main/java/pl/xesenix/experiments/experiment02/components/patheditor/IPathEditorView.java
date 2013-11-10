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


public interface IPathEditorView
{
	void updatePath(IPath path);


	void createPathView(IPath path);


	void focusPath(IPath path);


	void focusPoint(IPathPoint point);


	void update();
}
