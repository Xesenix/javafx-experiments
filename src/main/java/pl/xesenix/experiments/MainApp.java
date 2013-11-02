
package pl.xesenix.experiments;

import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Named;


public class MainApp extends GuiceApplication
{

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);


	@Inject
	private GuiceFXMLLoader fxmlLoader;


	public static void main(String[] args) throws Exception
	{
		launch(args);
	}


	public void start(final Stage stage) throws Exception
	{
		log.debug("application start");
		
		final Experiment00Controller controller = (Experiment00Controller) fxmlLoader.load(getClass().getResource("/fxml/experiment00.fxml"), null).getController();
		
		StageBuilder
			.create()
			.title("Binding tests").resizable(false)
			.scene(SceneBuilder
				.create()
				.root(controller.getView())
				.stylesheets("/styles/styles.css")
				.build()
			)
			.resizable(true)
			.applyTo(stage);
		
		stage.show();
	}


	@Override
	public void init(List<Module> modules) throws Exception
	{
		modules.add(new PersonManagerModule());
	}
}
