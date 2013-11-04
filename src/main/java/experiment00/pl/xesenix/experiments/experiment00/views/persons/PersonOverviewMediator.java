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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.xesenix.experiments.experiment00.commands.ICommandProvider;
import pl.xesenix.experiments.experiment00.commands.LoadPersonDetailsCommand;
import pl.xesenix.experiments.experiment00.model.IPersonSelectionModel;
import pl.xesenix.experiments.experiment00.model.Person;

import com.google.inject.Inject;


public class PersonOverviewMediator implements IPersonOverviewMediator
{
	@Inject
	private ICommandProvider commandProvider;


	@Inject
	public IPersonSelectionModel personSelectionModel;


	@Inject
	public IPersonDetailView view;


	public void init()
	{
		personSelectionModel.getSelectedPersonProperty().addListener(new SelectedPersonChangeHandler());
	}


	private class SelectedPersonChangeHandler implements ChangeListener<Person>
	{

		public void changed(ObservableValue<? extends Person> arg0, Person oldPerson, Person newPerson)
		{
			LoadPersonDetailsCommand command = commandProvider.get(LoadPersonDetailsCommand.class);
			command.person = newPerson;
			command.setOnSucceeded(new LoadPersonDetailsSucceedHandler());
			command.start();
		}

	}
	
	
	private class LoadPersonDetailsSucceedHandler implements EventHandler<WorkerStateEvent> {

		public void handle(WorkerStateEvent workerStateEvent)
		{
			view.updatePersonDetailsView((Person) workerStateEvent.getSource().getValue());
		}
		
	}
}
