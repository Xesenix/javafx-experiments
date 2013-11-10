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
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

import com.google.inject.Inject;


public class RemovePointRequest extends Service<Void>
{
	@Inject
	public PathDrawingContext context;


	public IPathPoint point;


	@Override
	protected Task<Void> createTask()
	{
		// TODO Auto-generated method stub
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception
			{
				context.removePoint(point);

				return null;
			}
		};
	}

}
