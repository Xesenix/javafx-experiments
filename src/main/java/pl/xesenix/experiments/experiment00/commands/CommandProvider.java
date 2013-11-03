
package pl.xesenix.experiments.experiment00.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import javafx.concurrent.Service;

@Singleton
public class CommandProvider implements ICommandProvider
{
	@Inject
	public Injector injector;


	public <T extends Service> T get(Class<T> type)
	{
		 return injector.getInstance(type);
	}

}
