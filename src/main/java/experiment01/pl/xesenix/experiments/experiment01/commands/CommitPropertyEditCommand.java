
package pl.xesenix.experiments.experiment01.commands;

import javafx.beans.property.Property;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class CommitPropertyEditCommand<T> extends Service<Void>
{
	public Property<T> property;
	
	
	public T value;


	@Override
	protected Task<Void> createTask()
	{
		return new Task<Void>() {

			protected Void call() throws Exception
			{
				property.setValue(value);

				return null;
			}
		};
	}

}
