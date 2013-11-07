
package pl.xesenix.experiments.experiment02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

@Singleton
public class Controller
{
	private static final Logger log = LoggerFactory.getLogger(Controller.class);


	@FXML
	private AnchorPane view;
	
	
	public Controller()
	{
		log.debug("construct");
	}


	public AnchorPane getView()
	{
		return view;
	}


	@FXML
	public void initialize()
	{
		log.debug("initializing");

		Canvas canvas = new Canvas(800, 600);

		final GraphicsContext gc = canvas.getGraphicsContext2D();

		(new AnimationTimer() {

			@Override
			public void handle(long now)
			{
				gc.clearRect(0, 0, 800, 600);
				gc.fillText(String.format("current time %d", now), 10, 50);
			}

		}).start();

		view.getChildren().add(canvas);
	}
}
