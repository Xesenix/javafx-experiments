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
import pl.xesenix.experiments.experiment01.model.console.ConsoleMessage;
import pl.xesenix.experiments.experiment01.model.persons.IPersonSelectionModel;
import pl.xesenix.experiments.experiment01.requests.IRequestProvider;
import pl.xesenix.experiments.experiment01.requests.LoadPersonsListRequest;
import pl.xesenix.experiments.experiment01.views.console.IConsoleView;
import pl.xesenix.experiments.experiment01.vo.PersonVO;

import com.google.inject.Inject;


public class PersonListMediator implements IPersonListMediator
{
	@Inject
	private IRequestProvider requestProvider;


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
		LoadPersonsListRequest request = requestProvider.get(LoadPersonsListRequest.class);
		request.setOnSucceeded(new LoadPersonsSucceedHandler());
		request.start();
	}


	/**
	 * @interface IPersonListMediator
	 */
	public void updateSelectedPerson(PersonVO person)
	{
		PersonVO editedPerson = personSelectionModel.getSelectedPerson();

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
			view.updatePersonList((ObservableList<PersonVO>) workerStateEvent.getSource().getValue());
		}
	}

}
