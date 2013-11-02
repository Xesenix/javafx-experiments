package pl.xesenix.experiments.experiment00;

import javafx.concurrent.Service;

public interface ICommandProvider
{
	<T extends Service> T get(Class<T> type);
}
