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
package pl.xesenix.experiments.experiment01.views.persons;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.xesenix.experiments.experiment01.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment01.model.console.ConsoleMessage;
import pl.xesenix.experiments.experiment01.model.persons.IPersonSelectionModel;
import pl.xesenix.experiments.experiment01.requests.CommitPropertyEditCommand;
import pl.xesenix.experiments.experiment01.requests.IRequestProvider;
import pl.xesenix.experiments.experiment01.requests.UpdatePersonsListRequest;
import pl.xesenix.experiments.experiment01.views.console.IConsoleView;
import pl.xesenix.experiments.experiment01.vo.Person;

import com.google.inject.Inject;


public class PersonDetailsMediator implements IPersonDetailsMediator
{
	@Inject
	private IRequestProvider requestProvider;


	@Inject
	private IPersonSelectionModel selectionModel;


	@Inject
	private ITranslationProvider translationProvider;


	@Inject
	private IPersonDetailView view;


	@Inject
	private IConsoleView consoleView;


	@Inject
	private IPersonListView listView;


	/**
	 * @interface IPersonDetailsMediator
	 */
	public void setName(String name)
	{
		if (selectionModel.getSelectedPerson() == null)
		{
			errorMessage("error.no_selection");
		}
		else
		{
			Person editedPerson = selectionModel.getSelectedPerson();

			if (!editedPerson.getName().equals(name))
			{
				@SuppressWarnings("unchecked")
				CommitPropertyEditCommand<String> request = requestProvider.get(CommitPropertyEditCommand.class);
				request.setOnSucceeded(new PropertyCommitEventHandler<String>(
					String.format(
						translationProvider.getString("person.name_changed"),
						editedPerson.getName(),
						name
					),
					editedPerson.getNameProperty(),
					name
				));
				request.start();
			}
		}
	}


	/**
	 * @interface IPersonDetailsMediator
	 */
	public void setAge(Integer age)
	{
		if (selectionModel.getSelectedPerson() == null)
		{
			errorMessage("error.no_selection");
		}
		else
		{
			Person editedPerson = selectionModel.getSelectedPerson();

			if (!editedPerson.getAge().equals(age))
			{
				@SuppressWarnings("unchecked")
				CommitPropertyEditCommand<Number> request = requestProvider.get(CommitPropertyEditCommand.class);
				request.setOnSucceeded(new PropertyCommitEventHandler<Number>(
					String.format(
						translationProvider.getString("person.age_changed"),
						editedPerson.getName(),
						age
					),
					editedPerson.getAgeProperty(),
					age
				));
				request.start();
			}
		}
	}


	/**
	 * @interface IPersonDetailsMediator
	 */
	public void infoMessage(String message)
	{
		consoleView.showMessage(ConsoleMessage.info(translationProvider.getString(message)));
	}


	/**
	 * @interface IPersonDetailsMediator
	 */
	public void errorMessage(String message)
	{
		consoleView.showMessage(ConsoleMessage.error(translationProvider.getString(message)));
	}


	/**
	 * @interface IPersonDetailsMediator
	 */
	public void invalidAge()
	{
		errorMessage("error.person.invalid.age");
		
		Person editedPerson = selectionModel.getSelectedPerson();
		
		view.updatePersonDetailsView(editedPerson);
	}


	/**
	 * @interface IPersonDetailsMediator
	 */
	public void updatePersons()
	{
		UpdatePersonsListRequest request = requestProvider.get(UpdatePersonsListRequest.class);
		request.setOnSucceeded(new LoadPersonsSucceedHandler());
		request.start();
	}


	private class PropertyCommitEventHandler<T> implements EventHandler<WorkerStateEvent>
	{
		private String successMessage;
		private Property<T> property;
		private T value;


		public PropertyCommitEventHandler(String successMessage, Property<T> property, T value)
		{
			this.successMessage = successMessage;
			this.property = property;
			this.value = value;
		}


		public void handle(WorkerStateEvent workerStateEvent)
		{
			// cannot do this outside of FX thread 
			property.setValue(value);
			
			Person editedPerson = selectionModel.getSelectedPerson();
			
			view.updatePersonDetailsView(editedPerson);
			
			infoMessage(successMessage);
		}
	}


	private class LoadPersonsSucceedHandler implements EventHandler<WorkerStateEvent>
	{
		@SuppressWarnings("unchecked")
		public void handle(WorkerStateEvent workerStateEvent)
		{
			listView.updatePersonList((ObservableList<Person>) workerStateEvent.getSource().getValue());
		}
	}

}
