/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/
package pl.xesenix.experiments.experiment00;

import pl.xesenix.experiments.experiment00.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment00.application.translations.ResourceBundleTranslationProvider;
import pl.xesenix.experiments.experiment00.model.persons.IPersonSelectionModel;
import pl.xesenix.experiments.experiment00.model.persons.PersonSelectionModel;
import pl.xesenix.experiments.experiment00.requests.IRequestProvider;
import pl.xesenix.experiments.experiment00.requests.RequestProvider;
import pl.xesenix.experiments.experiment00.service.IPersonService;
import pl.xesenix.experiments.experiment00.service.StubPersonService;
import pl.xesenix.experiments.experiment00.views.console.ConsoleMediator;
import pl.xesenix.experiments.experiment00.views.console.IConsoleMediator;
import pl.xesenix.experiments.experiment00.views.console.IConsoleView;
import pl.xesenix.experiments.experiment00.views.persons.IPersonDetailView;
import pl.xesenix.experiments.experiment00.views.persons.IPersonListMediator;
import pl.xesenix.experiments.experiment00.views.persons.IPersonListView;
import pl.xesenix.experiments.experiment00.views.persons.IPersonOverviewMediator;
import pl.xesenix.experiments.experiment00.views.persons.PersonListMediator;
import pl.xesenix.experiments.experiment00.views.persons.PersonOverviewMediator;

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
		bind(IConsoleView.class).to(Controller.class);
		bind(IPersonDetailView.class).to(Controller.class);
		bind(IPersonListView.class).to(Controller.class);
	}


	private void mapMediators()
	{
		bind(IConsoleMediator.class).to(ConsoleMediator.class);
		bind(IPersonOverviewMediator.class).to(PersonOverviewMediator.class);
		bind(IPersonListMediator.class).to(PersonListMediator.class);
	}


	private void mapCommands()
	{
		bind(IRequestProvider.class).to(RequestProvider.class);
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
