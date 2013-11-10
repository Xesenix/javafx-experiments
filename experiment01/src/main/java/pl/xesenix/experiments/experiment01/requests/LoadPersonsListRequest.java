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

import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment01.service.IPersonService;
import pl.xesenix.experiments.experiment01.vo.Person;

import com.google.inject.Inject;


public class LoadPersonsListRequest extends Service<ObservableList<Person>>
{
	@Inject
	public IPersonService personService;


	@Override
	protected Task<ObservableList<Person>> createTask()
	{
		return new Task<ObservableList<Person>>() {

			@Override
			protected ObservableList<Person> call() throws Exception
			{
				personService.loadPersons();
				
				return personService.getPersons();
			}
		};
	}
}
