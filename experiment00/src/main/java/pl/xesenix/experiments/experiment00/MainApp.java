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

import java.util.List;

import javafx.scene.SceneBuilder;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment00.application.translations.ITranslationProvider;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;


public class MainApp extends GuiceApplication
{

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);


	@Inject
	private GuiceFXMLLoader fxmlLoader;
	
	
	@Inject
	ITranslationProvider translation;


	public static void main(String[] args) throws Exception
	{
		launch(args);
	}


	public void start(final Stage stage) throws Exception
	{
		log.debug("application start");
		
		final Controller controller = (Controller) fxmlLoader.load(getClass().getResource("/fxml/experiment00.fxml"), translation.getResourceBundle()).getController();
		
		StageBuilder
			.create()
			.title(translation.getString("app.name")).resizable(false)
			.scene(SceneBuilder
				.create()
				.root(controller.getView())
				.stylesheets("/styles/experiment00.css")
				.build()
			)
			.resizable(true)
			.applyTo(stage);
		
		stage.show();
	}


	@Override
	public void init(List<Module> modules) throws Exception
	{
		modules.add(new Experiment00Module());
	}
}
