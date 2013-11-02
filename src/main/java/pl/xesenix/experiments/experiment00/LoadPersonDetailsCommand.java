
package pl.xesenix.experiments.experiment00;

import pl.xesenix.experiments.Person;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class LoadPersonDetailsCommand extends Service<Person>
{
	public Person person;


	@Override
	protected Task<Person> createTask()
	{
		return new Task<Person>() {

			@Override
			protected Person call() throws Exception
			{
				return person;
			}
		};
	}

}
