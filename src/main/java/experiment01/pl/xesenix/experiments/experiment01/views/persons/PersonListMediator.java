/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors: Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/

package pl.xesenix.experiments.experiment01.views.persons;

import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.xesenix.experiments.experiment01.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment01.commands.ICommandProvider;
import pl.xesenix.experiments.experiment01.commands.LoadPersonsListCommand;
import pl.xesenix.experiments.experiment01.model.console.ConsoleMessage;
import pl.xesenix.experiments.experiment01.model.persons.IPersonSelectionModel;
import pl.xesenix.experiments.experiment01.views.console.IConsoleView;
import pl.xesenix.experiments.experiment01.vo.Person;

import com.google.inject.Inject;


public class PersonListMediator implements IPersonListMediator
{
	@Inject
	private ICommandProvider commandProvider;


	@Inject
	private IPersonSelectionModel personSelectionModel;


	@Inject
	private ITranslationProvider translationProvider;


	@Inject
	private IPersonListView view;


	@Inject
	private IConsoleView consoleView;


	/**
	 * @interface IPersonListMediator
	 */
	public void loadPersons()
	{
		LoadPersonsListCommand command = commandProvider.get(LoadPersonsListCommand.class);
		command.setOnSucceeded(new LoadPersonsSucceedHandler());
		command.start();
	}


	/**
	 * @interface IPersonListMediator
	 */
	public void updateSelectedPerson(Person person)
	{
		Person editedPerson = personSelectionModel.getSelectedPerson();

		if (editedPerson == null || !editedPerson.equals(person))
		{
			consoleView.showMessage(ConsoleMessage.info(String.format(translationProvider.getString("person.editing"), person.getName())));
			personSelectionModel.setSelectedPerson(person);
		}
	}


	private class LoadPersonsSucceedHandler implements EventHandler<WorkerStateEvent>
	{
		@SuppressWarnings("unchecked")
		public void handle(WorkerStateEvent workerStateEvent)
		{
			view.updatePersonList((ObservableList<Person>) workerStateEvent.getSource().getValue());
		}
	}

}
