package pl.xesenix.experiments.experiment00;

import com.google.inject.Inject;

import pl.xesenix.experiments.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PersonOverviewMediator implements IPersonOverviewMediator
{
	@Inject
	public IPersonSelectionModel personSelectionModel;
	
	
	@Inject
	public IPersonDetailView view;
	
	
	public void init()
	{
		personSelectionModel.getSelectedPersonProperty().addListener(new SelectedPersonChangeHandler());
	}

	private class SelectedPersonChangeHandler implements ChangeListener<Person> {

		public void changed(ObservableValue<? extends Person> arg0, Person oldPerson, Person newPerson)
		{
			if (newPerson != null)
			{
				view.updatePersonDetailsView(newPerson);
			}
		}
		
	}
}
