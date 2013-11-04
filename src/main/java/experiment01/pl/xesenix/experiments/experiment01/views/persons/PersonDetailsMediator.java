
package pl.xesenix.experiments.experiment01.views.persons;

import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.xesenix.experiments.experiment01.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment01.commands.CommitPropertyEditCommand;
import pl.xesenix.experiments.experiment01.commands.ICommandProvider;
import pl.xesenix.experiments.experiment01.commands.UpdatePersonsListCommand;
import pl.xesenix.experiments.experiment01.model.Person;
import pl.xesenix.experiments.experiment01.model.console.ConsoleMessage;
import pl.xesenix.experiments.experiment01.model.persons.IPersonSelectionModel;
import pl.xesenix.experiments.experiment01.views.console.IConsoleView;


import com.google.inject.Inject;


public class PersonDetailsMediator implements IPersonDetailsMediator
{
	@Inject
	private ICommandProvider commandProvider;


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
				CommitPropertyEditCommand<String> command = commandProvider.get(CommitPropertyEditCommand.class);
				command.setOnSucceeded(new PropertyCommitEventHandler(
					String.format(
						translationProvider.getString("person.name_changed"),
						editedPerson.getName(),
						name
					)
				));
				command.property = editedPerson.getNameProperty();
				command.value = name;
				command.start();
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
				CommitPropertyEditCommand<Number> command = commandProvider.get(CommitPropertyEditCommand.class);
				command.setOnSucceeded(new PropertyCommitEventHandler(
					String.format(
						translationProvider.getString("person.age_changed"),
						editedPerson.getName(),
						age
					)
				));
				command.property = editedPerson.getAgeProperty();
				command.value = age;
				command.start();
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
		UpdatePersonsListCommand command = commandProvider.get(UpdatePersonsListCommand.class);
		command.setOnSucceeded(new LoadPersonsSucceedHandler());
		command.start();
	}


	private class PropertyCommitEventHandler implements EventHandler<WorkerStateEvent>
	{
		private String successMessage;


		public PropertyCommitEventHandler(String successMessage)
		{
			this.successMessage = successMessage;
		}


		@Override
		public void handle(WorkerStateEvent event)
		{
			infoMessage(successMessage);
			updatePersons();
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
