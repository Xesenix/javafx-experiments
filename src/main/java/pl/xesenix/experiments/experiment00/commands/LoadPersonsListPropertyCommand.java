
package pl.xesenix.experiments.experiment00.commands;

import java.util.List;

import com.google.inject.Inject;

import javafx.beans.property.ListProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment00.model.Person;
import pl.xesenix.experiments.experiment00.service.IPersonService;


public class LoadPersonsListPropertyCommand extends Service<ListProperty<Person>>
{
	@Inject
	public IPersonService personService;


	@Override
	protected Task<ListProperty<Person>> createTask()
	{
		return new Task<ListProperty<Person>>() {

			@Override
			protected ListProperty<Person> call() throws Exception
			{
				return personService.getPersonsListProperty();
			}
		};
	}

}
