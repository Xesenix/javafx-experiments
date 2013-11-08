
package pl.xesenix.experiments.experiment02;

import javafx.animation.AnimationTimer;
import javafx.beans.property.Property;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;


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

		view.getChildren().add(canvas);

		final GraphicsContext gc = canvas.getGraphicsContext2D();
		int fps = 120;
		
		final Path path = new Path();
		path.setSmooth(true);
		path.setStroke(Color.web("#000"));
		
		view.getChildren().add(path);
		
		preparePath(path);
		preparePathUI(path);

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
					if (el instanceof CubicCurveTo)
					{
						CubicCurveTo point = (CubicCurveTo) el;
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
	}


	private void preparePath(final Path path)
	{
		double x = Math.random() * 800;
		double y = Math.random() * 600;
		
		MoveTo mt = new MoveTo(x, y);
		
		path.getElements().add(mt);
		
		for (int i = 0; i < 10; i++)
		{
			x = Math.random() * 800;
			y = Math.random() * 600;
			
			CubicCurveTo cct = new CubicCurveTo(x, y, x, y, x, y);

			path.getElements().add(cct);
		}
	}


	private void preparePathUI(final Path path)
	{
		EventHandler<MouseEvent> dragHandler = new DragHandler();
		EventHandler<MouseEvent> clickHandler = new ClickHandler();
		
		Property<Number> pointPropertyX = null;
		Property<Number> pointPropertyY = null;
		Property<Number> previousPointPropertyX = null;
		Property<Number> previousPointPropertyY = null;
		Property<Number> handleInPropertyX = null;
		Property<Number> handleInPropertyY = null;
		Property<Number> handleOutPropertyX = null;
		Property<Number> handleOutPropertyY = null;
		
		Circle handle1 = null;
		Circle handle2 = null;
		Line line1 = null;
		Line line2 = null;
		
		for (PathElement pathPoint : path.getElements())
		{
			previousPointPropertyX = pointPropertyX;
			previousPointPropertyY = pointPropertyY;
			
			if (pathPoint instanceof MoveTo)
			{
				pointPropertyX = ((MoveTo) pathPoint).xProperty();
				pointPropertyY = ((MoveTo) pathPoint).yProperty();
			}
			else if (pathPoint instanceof CubicCurveTo)
			{
				pointPropertyX = ((CubicCurveTo) pathPoint).xProperty();
				pointPropertyY = ((CubicCurveTo) pathPoint).yProperty();
				handleInPropertyX = ((CubicCurveTo) pathPoint).controlX1Property();
				handleInPropertyY = ((CubicCurveTo) pathPoint).controlY1Property();
				handleOutPropertyX = ((CubicCurveTo) pathPoint).controlX2Property();
				handleOutPropertyY = ((CubicCurveTo) pathPoint).controlY2Property();
			}
			
			Circle point = new Circle(20, Color.web("#f00"));
			point.setOpacity(0.2);
			point.setUserData(pathPoint);
			
			point.setOnMousePressed(dragHandler);
			point.setOnMouseDragged(dragHandler);
			point.setOnDragDetected(dragHandler);
			point.setOnMouseClicked(clickHandler);
			
			point.centerXProperty().bindBidirectional(pointPropertyX);
			point.centerYProperty().bindBidirectional(pointPropertyY);
			
			if (handleInPropertyX != null)
			{
				handle1 = new Circle(10, Color.web("#0f0"));
				handle1.setOpacity(0.4);
				
				handle1.setOnMousePressed(dragHandler);
				handle1.setOnMouseDragged(dragHandler);
				handle1.setOnDragDetected(dragHandler);
				
				handle1.centerXProperty().bindBidirectional(handleInPropertyX);
				handle1.centerYProperty().bindBidirectional(handleInPropertyY);
			}
			
			if (handleOutPropertyX != null)
			{
				handle2 = new Circle(10, Color.web("#0f0"));
				handle2.setOpacity(0.4);
				
				handle2.setOnMousePressed(dragHandler);
				handle2.setOnMouseDragged(dragHandler);
				handle2.setOnDragDetected(dragHandler);
				
				handle2.centerXProperty().bindBidirectional(handleOutPropertyX);
				handle2.centerYProperty().bindBidirectional(handleOutPropertyY);
			}
			
			if (previousPointPropertyX != null && handleInPropertyX != null)
			{
				line1 = new Line();
				line1.setOpacity(0.2);
				
				line1.startXProperty().bind(previousPointPropertyX);
				line1.startYProperty().bind(previousPointPropertyY);
				line1.endXProperty().bind(handleInPropertyX);
				line1.endYProperty().bind(handleInPropertyY);
			}
			
			if (pointPropertyX != null && handleOutPropertyX != null)
			{
				line2 = new Line();
				line2.setOpacity(0.2);
				
				line2.startXProperty().bind(pointPropertyX);
				line2.startYProperty().bind(pointPropertyY);
				line2.endXProperty().bind(handleOutPropertyX);
				line2.endYProperty().bind(handleOutPropertyY);
			}

			if (line1 != null)
			{
				view.getChildren().add(line1);
			}
			
			if (line2 != null)
			{
				view.getChildren().add(line2);
			}
			
			view.getChildren().add(point);
			
			if (handle1 != null)
			{
				view.getChildren().add(handle1);
			}
			
			if (handle2 != null)
			{
				view.getChildren().add(handle2);
			}
		}
	}


	public class ClickHandler implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent event)
		{
			EventType<? extends Event> eventType = event.getEventType();
			
			log.debug("{}", event);
			
			if (eventType.equals(MouseEvent.MOUSE_CLICKED) && event.getClickCount() == 2)
			{
				if (event.getTarget() instanceof Circle)
				{
					PathElement pathPoint = (PathElement) ((Circle) event.getTarget()).getUserData();
					
					if (pathPoint instanceof CubicCurveTo)
					{
						CubicCurveTo cct = (CubicCurveTo) pathPoint;
						cct.controlX1Property().set(cct.getX());
						cct.controlY1Property().set(cct.getY());
						cct.controlX2Property().set(cct.getX());
						cct.controlY2Property().set(cct.getY());
					}
				}
			}
		}
	}

	public class DragHandler implements EventHandler<MouseEvent>
	{
		private double offsetX;


		private double offsetY;


		@Override
		public void handle(MouseEvent event)
		{
			EventType<? extends Event> eventType = event.getEventType();
			
			log.debug("{}", event);
			
			if (eventType.equals(MouseEvent.MOUSE_PRESSED))
			{
				if (event.getTarget() instanceof Circle)
				{
					offsetX = event.getScreenX() - ((Circle) event.getTarget()).getCenterX();
					offsetY = event.getScreenY() - ((Circle) event.getTarget()).getCenterY();
				}
			}
			else if (eventType.equals(MouseEvent.MOUSE_DRAGGED))
			{
				if (event.getTarget() instanceof Circle)
				{
					((Circle) event.getTarget()).setCenterX(event.getScreenX() - offsetX);
					((Circle) event.getTarget()).setCenterY(event.getScreenY() - offsetY);
				}
			}
		}
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
