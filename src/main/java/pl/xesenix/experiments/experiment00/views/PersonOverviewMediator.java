
package pl.xesenix.experiments.experiment00.views;

import java.util.List;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment00.commands.ICommandProvider;
import pl.xesenix.experiments.experiment00.commands.LoadPersonDetailsCommand;
import pl.xesenix.experiments.experiment00.commands.LoadPersonsListPropertyCommand;
import pl.xesenix.experiments.experiment00.model.IPersonSelectionModel;
import pl.xesenix.experiments.experiment00.model.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


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
