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
package pl.xesenix.experiments.experiment00.commands;

import javafx.beans.property.ListProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment00.service.IPersonService;
import pl.xesenix.experiments.experiment00.vo.Person;

import com.google.inject.Inject;


public class LoadPersonsListPropertyCommand extends Service<ListProperty<Person>>
{
	@Inject
	public IPersonService personService;


	@Override
	protected Task<ListProperty<Person>> createTask()
	{
		return new Task<ListProperty<Person>>() {

			@Override
			protected ListProperty<Person> call() throws Exception
			{
				personService.loadPersons();
				
				return personService.getPersonsListProperty();
			}
		};
	}

}
