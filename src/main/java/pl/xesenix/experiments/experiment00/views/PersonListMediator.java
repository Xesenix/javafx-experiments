package pl.xesenix.experiments.experiment00.views;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment00.commands.ICommandProvider;
import pl.xesenix.experiments.experiment00.commands.LoadPersonsListPropertyCommand;
import pl.xesenix.experiments.experiment00.model.IPersonSelectionModel;
import pl.xesenix.experiments.experiment00.model.Person;

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

		public void handle(WorkerStateEvent workerStateEvent)
		{
			view.bindPersonList((ListProperty<Person>) workerStateEvent.getSource().getValue());
		}
		
	}

}
