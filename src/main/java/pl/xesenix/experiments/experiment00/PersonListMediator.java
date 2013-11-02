package pl.xesenix.experiments.experiment00;

import java.util.List;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import com.google.inject.Inject;

import pl.xesenix.experiments.Person;

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
		LoadPersonsCommand command = commandProvider.get(LoadPersonsCommand.class);
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
			view.updatePersonList((List<Person>) workerStateEvent.getSource().getValue());
		}
		
	}

}
