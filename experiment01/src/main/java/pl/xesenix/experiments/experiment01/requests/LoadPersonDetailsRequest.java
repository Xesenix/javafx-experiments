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
import pl.xesenix.experiments.experiment01.vo.PersonVO;


public class LoadPersonDetailsRequest extends Service<PersonVO>
{
	public PersonVO person;


	@Override
	protected Task<PersonVO> createTask()
	{
		return new Task<PersonVO>() {

			@Override
			protected PersonVO call() throws Exception
			{
				return person;
			}
		};
	}

}
