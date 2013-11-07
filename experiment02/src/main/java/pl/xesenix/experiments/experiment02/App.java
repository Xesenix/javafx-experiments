package pl.xesenix.experiments.experiment02;

import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import javafx.scene.SceneBuilder;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;
import javafx.stage.StageStyle;

import com.cathive.fx.guice.GuiceApplication;
import com.cathive.fx.guice.GuiceFXMLLoader;
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


	public void start(final Stage stage) throws Exception
	{
		log.debug("application start");
		
		final Controller controller = (Controller) fxmlLoader.load(getClass().getResource("/fxml/app.fxml"), resource).getController();
		
		StageBuilder
			.create()
			.title(resource.getString("app.name")).resizable(false)
			.scene(SceneBuilder
				.create()
				.root(controller.getView())
				.stylesheets("/styles/app.css")
				.build()
			)
			.resizable(true)
			.applyTo(stage);
		
		//stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}


	@Override
	public void init(List<Module> modules) throws Exception
	{
		modules.add(new AppModule());
	}

}
