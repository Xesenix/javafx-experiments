
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
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.VLineTo;
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
		int fps = 30;
		
		final Path path = new Path();
		path.setSmooth(true);
		
		double x = Math.random() * 800;
		double y = Math.random() * 600;
		double dir = Math.random() * Math.PI;
		
		path.getElements().add(new MoveTo(x, y));
		
		for (int i = 0; i < 100; i++)
		{
			dir = Math.random() * Math.PI;
			
			x = Math.random() * 800;
			y = Math.random() * 600;
			
			path.getElements().add(new QuadCurveTo(x + 50 * Math.cos(dir), y - 50 * Math.sin(dir), x, y));
		}

		//gc.setEffect(new Glow());
		
		AnimationTimer animator = (new FixedStepAnimationTimer(fps) {
			private FPS animationFps = new FPS();


			private FPS logicFps = new FPS();


			private int timeElapsed;


			private long logicSteps;


			private long frameRenderSteps;


			@Override
			public void dologic(long stepMiliseconds)
			{
				logicSteps ++;
				timeElapsed += stepMiliseconds;
				logicFps.update(timeElapsed); // it`s requires to increment some variable on every logic step (measurement ++)
				
				for (PathElement el : path.getElements())
				{
					if (el instanceof QuadCurveTo)
					{
						QuadCurveTo point = (QuadCurveTo) el;
						point.setX(point.getX() + 2 - Math.random() * 4);
						point.setY(point.getY() + 2 - Math.random() * 4);
					}
					else if (el instanceof MoveTo)
					{
						MoveTo point = (MoveTo) el;
						point.setX(point.getX() + 2 - Math.random() * 4);
						point.setY(point.getY() + 2 - Math.random() * 4);
					}
				}
			}


			@Override
			public void render(long stepMiliseconds)
			{
				frameRenderSteps ++;
				
				animationFps.update();

				
				gc.clearRect(0, 0, 800, 600);
				
				
				
				gc.fillText(String.format("animation fps %d", animationFps.fps), 10, 20);
				gc.fillText(String.format("logic fps %d", logicFps.fps), 10, 40);
				gc.fillText(String.format("frame fps %.2f", 1000 / (float) stepMiliseconds), 10, 60);
				gc.fillText(String.format("step time %.3fs", stepMiliseconds / 1000f), 10, 80);
				gc.fillText(String.format("time elapsed %.2fs", timeElapsed / 1000f), 10, 100);
				gc.fillText(String.format("logic steps %d", logicSteps), 10, 120);
				gc.fillText(String.format("frame render steps %d", frameRenderSteps), 10, 140);
			}


			@Override
			public void start()
			{
				logicSteps = 0;
				timeElapsed = 0;
				frameRenderSteps = 0;
				
				logicFps.start(timeElapsed);
				animationFps.start();

				super.start();
			}
		});

		animator.start();

		view.getChildren().add(canvas);
		view.getChildren().add(path);
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
				long frameTime = 0;
				
				// subframe logic handling for fps higher than available 
				
				while (interval >= step)
				{
					interval -= step;
					frameTime += step;
					
					dologic(step);
				}
				
				render(frameTime);
				
				lastAnimationFrameTime = currentFrameTime - interval; // time overflow control
			}
		}

		
		public abstract void dologic(long stepMiliseconds);


		public abstract void render(long stepMiliseconds);


		@Override
		public void start()
		{
			lastAnimationFrameTime = System.nanoTime() / 1000000;

			super.start();
		}
	}

	public static class FPS
	{
		public int fps;


		private int measurements;


		private long lastFpsCalculationTime;


		public void update()
		{
			update(System.nanoTime() / 1000000);
		}


		public void update(long currentFrameTime)
		{
			measurements++;

			if (currentFrameTime - lastFpsCalculationTime >= 1000)
			{
				log.debug("fps:{} interval: {}", fps, currentFrameTime - lastFpsCalculationTime);
				fps = measurements;
				lastFpsCalculationTime = currentFrameTime;
				measurements = 0;
			}
		}


		public void start()
		{
			start(System.nanoTime() / 1000000);
		}


		public void start(long time)
		{
			lastFpsCalculationTime = time;
			measurements = 0;
		}
	}
}
