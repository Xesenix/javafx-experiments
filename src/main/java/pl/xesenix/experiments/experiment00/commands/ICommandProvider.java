package pl.xesenix.experiments.experiment00.commands;

import javafx.concurrent.Service;

public interface ICommandProvider
{
	<T extends Service> T get(Class<T> type);
}
