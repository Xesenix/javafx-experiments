
package pl.xesenix.experiments;

import java.util.Locale;

import pl.xesenix.experiments.experiment00.CommandProvider;
import pl.xesenix.experiments.experiment00.ICommandProvider;
import pl.xesenix.experiments.experiment00.IPersonDetailView;
import pl.xesenix.experiments.experiment00.IPersonListMediator;
import pl.xesenix.experiments.experiment00.IPersonListView;
import pl.xesenix.experiments.experiment00.IPersonOverviewMediator;
import pl.xesenix.experiments.experiment00.IPersonSelectionModel;
import pl.xesenix.experiments.experiment00.IPersonService;
import pl.xesenix.experiments.experiment00.PersonListMediator;
import pl.xesenix.experiments.experiment00.PersonOverviewMediator;
import pl.xesenix.experiments.experiment00.PersonSelectionModel;
import pl.xesenix.experiments.experiment00.StubPersonService;
import pl.xesenix.experiments.experiment00.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment00.application.translations.ResourceBundleTranslationProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;


public class PersonManagerModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(PersonManagerModule.class).toInstance(this);

		mapViews();
		mapMediators();
		mapCommands();
		mapServices();
		mapModels();
		mapInfrastructure();
	}


	private void mapViews()
	{
		bind(IPersonListView.class).to(Experiment00Controller.class);
		bind(IPersonDetailView.class).to(Experiment00Controller.class);
	}


	private void mapMediators()
	{
		bind(IPersonOverviewMediator.class).to(PersonOverviewMediator.class);
		bind(IPersonListMediator.class).to(PersonListMediator.class);
	}


	private void mapCommands()
	{
		bind(ICommandProvider.class).to(CommandProvider.class);

		/*bind(LoadEventsCommand.class);
		bind(AcceptEventCommand.class);
		bind(DeclineEventCommand.class);*/
	}


	private void mapServices()
	{
		bind(IPersonService.class).to(StubPersonService.class);
	}


	private void mapModels()
	{
		bind(IPersonSelectionModel.class).to(PersonSelectionModel.class).in(Singleton.class);
	}


	private void mapInfrastructure()
	{
		bind(ITranslationProvider.class).to(ResourceBundleTranslationProvider.class).in(Singleton.class);
	}
}
