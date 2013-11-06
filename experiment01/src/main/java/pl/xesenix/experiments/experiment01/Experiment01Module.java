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
package pl.xesenix.experiments.experiment01;

import javafx.scene.text.Font;
import pl.xesenix.experiments.experiment01.application.translations.ITranslationProvider;
import pl.xesenix.experiments.experiment01.application.translations.ResourceBundleTranslationProvider;
import pl.xesenix.experiments.experiment01.commands.CommandProvider;
import pl.xesenix.experiments.experiment01.commands.ICommandProvider;
import pl.xesenix.experiments.experiment01.model.persons.IPersonSelectionModel;
import pl.xesenix.experiments.experiment01.model.persons.PersonSelectionModel;
import pl.xesenix.experiments.experiment01.service.IPersonService;
import pl.xesenix.experiments.experiment01.service.StubPersonService;
import pl.xesenix.experiments.experiment01.views.console.ConsoleMediator;
import pl.xesenix.experiments.experiment01.views.console.IConsoleMediator;
import pl.xesenix.experiments.experiment01.views.console.IConsoleView;
import pl.xesenix.experiments.experiment01.views.persons.IPersonDetailView;
import pl.xesenix.experiments.experiment01.views.persons.IPersonDetailsMediator;
import pl.xesenix.experiments.experiment01.views.persons.IPersonListMediator;
import pl.xesenix.experiments.experiment01.views.persons.IPersonListView;
import pl.xesenix.experiments.experiment01.views.persons.IPersonOverviewMediator;
import pl.xesenix.experiments.experiment01.views.persons.PersonDetailsMediator;
import pl.xesenix.experiments.experiment01.views.persons.PersonListMediator;
import pl.xesenix.experiments.experiment01.views.persons.PersonOverviewMediator;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;


public class Experiment01Module extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(Experiment01Module.class).toInstance(this);

		mapViews();
		mapMediators();
		mapCommands();
		mapServices();
		mapModels();
		mapInfrastructure();
		
		loadFonts();
	}


	private void loadFonts()
	{
		Font.loadFont(getClass().getResource("/fonts/Michroma.ttf").toExternalForm(), 12);
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
		bind(IPersonDetailsMediator.class).to(PersonDetailsMediator.class);
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
