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
package pl.xesenix.experiments.experiment02.components.patheditor.requests;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment02.components.patheditor.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

import com.google.inject.Inject;


public class CreatePointRequest extends Service<IPath>
{
	@Inject
	public PathDrawingContext context;
	
	
	public double x;
	
	
	public double y;


	@Override
	protected Task<IPath> createTask()
	{
		// TODO Auto-generated method stub
		return new Task<IPath>() {

			@Override
			protected IPath call() throws Exception
			{
				IPathPoint point = context.createPoint(x, y, 0, 0, 0, 0);
				context.addPointToCurrentPath(point);
				
				return context.getEditedPath();
			}
		};
	}

}
