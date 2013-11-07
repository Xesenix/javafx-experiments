
package pl.xesenix.experiments.experiment02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
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
		int fps = 2;

		AnimationTimer animator = (new FixedStepAnimationTimer(fps) {
			private FPS fps = new FPS();
			
			@Override
			public void render(long stepMiliseconds)
			{
				fps.update();
				
				gc.clearRect(0, 0, 800, 600);
				
				gc.fillText(String.format("animation fps %d", fps.fps), 10, 20);
				gc.fillText(String.format("frame fps %.2f", 1000 / (float)stepMiliseconds), 10, 40);
				gc.fillText(String.format("step time %.3f", stepMiliseconds / 1000f), 10, 60);
			}
			
			@Override
			public void start()
			{
				fps.start();
				super.start();
			}
		});
		
		animator.start();

		view.getChildren().add(canvas);
	}
	
	
	public abstract class FixedStepAnimationTimer extends AnimationTimer
	{
		private final long step;


		private long lastAnimationFrameTime;


		public FixedStepAnimationTimer(long fps)
		{
			this.step = 1000 / fps;
		}


		public void handle(long now)
		{
			long currentFrameTime = System.nanoTime() / 1000000;
			long interval = currentFrameTime - lastAnimationFrameTime;
			
			if (interval >= step)
			{
				render(step);
				
				lastAnimationFrameTime = currentFrameTime - interval + step; // time overflow control
			}
		}


		public abstract void render(long stepMiliseconds);


		@Override
		public void start()
		{
			lastAnimationFrameTime = System.nanoTime() / 1000000;
			
			super.start();
		}
	}


	public static class FPS {
		public int fps;

		private int mesurements;
		
		private long lastFpsCalculationTime;
		
		
		public void update()
		{
			update(System.nanoTime() / 1000000);
		}
		
		public void update(long currentFrameTime)
		{
			mesurements ++;
			
			if (currentFrameTime - lastFpsCalculationTime > 1000)
			{
				log.debug("fps:{} interval: {}", fps, currentFrameTime - lastFpsCalculationTime);
				fps = mesurements;
				lastFpsCalculationTime = currentFrameTime;
				mesurements = 0;
			}
		}
		
		public void start()
		{
			lastFpsCalculationTime = System.nanoTime() / 1000000;
			mesurements = 0;
		}
	}
}
