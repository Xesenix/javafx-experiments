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
package pl.xesenix.experiments.experiment00.views.persons;

import javafx.beans.property.ListProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.xesenix.experiments.experiment00.commands.ICommandProvider;
import pl.xesenix.experiments.experiment00.commands.LoadPersonsListPropertyCommand;
import pl.xesenix.experiments.experiment00.model.IPersonSelectionModel;
import pl.xesenix.experiments.experiment00.model.Person;

import com.google.inject.Inject;

public class PersonListMediator implements IPersonListMediator
{
	@Inject
	private ICommandProvider commandProvider;
	
	
	@Inject
	private IPersonSelectionModel personSelectionModel;
	
	
	@Inject
	private IPersonListView view;
	
	
	public void loadPersons()
	{
		LoadPersonsListPropertyCommand command = commandProvider.get(LoadPersonsListPropertyCommand.class);
		command.setOnSucceeded(new LoadPersonsSucceedHandler());
		command.start();
	}

	public void updateSelectedPerson(Person person)
	{
		personSelectionModel.setSelectedPerson(person);
	}
	
	
	private class LoadPersonsSucceedHandler implements EventHandler<WorkerStateEvent> {

		@SuppressWarnings("unchecked")
		public void handle(WorkerStateEvent workerStateEvent)
		{
			view.bindPersonList((ListProperty<Person>) workerStateEvent.getSource().getValue());
		}
		
	}

}
