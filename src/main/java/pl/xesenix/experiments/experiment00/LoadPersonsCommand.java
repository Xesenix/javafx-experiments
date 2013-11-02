
package pl.xesenix.experiments.experiment00;

import java.util.List;

import com.google.inject.Inject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.Person;


public class LoadPersonsCommand extends Service<List<Person>>
{
	@Inject
	public IPersonService personService;


	@Override
	protected Task<List<Person>> createTask()
	{
		return new Task<List<Person>>() {

			@Override
			protected List<Person> call() throws Exception
			{
				return personService.getPersons();
			}
		};
	}

}
