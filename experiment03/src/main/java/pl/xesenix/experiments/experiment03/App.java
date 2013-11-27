
package pl.xesenix.experiments.experiment03;

import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.SceneBuilder;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
import com.cathive.fx.guice.GuiceFXMLLoader.Result;
import com.google.inject.Inject;
import com.google.inject.Module;


public class App extends GuiceApplication
{
	private static final Logger log = LoggerFactory.getLogger(App.class);


	@Inject
	private GuiceFXMLLoader fxmlLoader;


	@Inject
	ResourceBundle resource;


	public static void main(String[] args) throws Exception
	{
		launch(args);
	}


	public void start(Stage stage) throws Exception
	{
		log.debug("application start");

		final Result loaderResult = fxmlLoader.load(getClass().getResource("/fxml/app.fxml"), resource);

		StageBuilder.create().title(resource.getString("app.name")).resizable(false)
			.scene(SceneBuilder.create().root((Parent) loaderResult.getRoot()).stylesheets("/styles/app.css").build())
			.resizable(true).applyTo(stage);

		// stage.initStyle(StageStyle.DECORATED);
		stage.show();
	}


	public void init(List<Module> modules) throws Exception
	{
		modules.add(new AppModule());
	}

}
