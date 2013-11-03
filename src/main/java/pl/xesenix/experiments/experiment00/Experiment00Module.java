
package pl.xesenix.experiments.experiment00;

import pl.xesenix.experiments.experiment00.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment00.application.translations.ResourceBundleTranslationProvider;
import pl.xesenix.experiments.experiment00.commands.CommandProvider;
import pl.xesenix.experiments.experiment00.commands.ICommandProvider;
import pl.xesenix.experiments.experiment00.model.IPersonSelectionModel;
import pl.xesenix.experiments.experiment00.model.PersonSelectionModel;
import pl.xesenix.experiments.experiment00.service.IPersonService;
import pl.xesenix.experiments.experiment00.service.StubPersonService;
import pl.xesenix.experiments.experiment00.views.IPersonDetailView;
import pl.xesenix.experiments.experiment00.views.IPersonListMediator;
import pl.xesenix.experiments.experiment00.views.IPersonListView;
import pl.xesenix.experiments.experiment00.views.IPersonOverviewMediator;
import pl.xesenix.experiments.experiment00.views.PersonListMediator;
import pl.xesenix.experiments.experiment00.views.PersonOverviewMediator;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;


public class Experiment00Module extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(Experiment00Module.class).toInstance(this);

		mapViews();
		mapMediators();
		mapCommands();
		mapServices();
		mapModels();
		mapInfrastructure();
	}


	private void mapViews()
	{
		bind(IPersonListView.class).to(Controller.class);
		bind(IPersonDetailView.class).to(Controller.class);
	}


	private void mapMediators()
	{
		bind(IPersonOverviewMediator.class).to(PersonOverviewMediator.class);
		bind(IPersonListMediator.class).to(PersonListMediator.class);
	}


	private void mapCommands()
	{
		bind(ICommandProvider.class).to(CommandProvider.class);
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
