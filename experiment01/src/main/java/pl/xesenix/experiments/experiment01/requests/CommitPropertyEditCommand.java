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
package pl.xesenix.experiments.experiment01.requests;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class CommitPropertyEditCommand<T> extends Service<Void>
{
	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>() {

			protected Void call() throws Exception
			{
				return null;
			}
		};
	}

}
