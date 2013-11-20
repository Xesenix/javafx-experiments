package pl.xesenix.experiments.experiment03.components.animation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FPSRendererAnimator extends FixedStepAnimationTimer
{
	private final GraphicsContext gc;


	private FPS animationFps = new FPS();


	private FPS logicFps = new FPS();


	private int timeElapsed;


	private long logicSteps;


	private long frameRenderSteps;


	public FPSRendererAnimator(long fps, GraphicsContext gc)
	{
		super(fps);
		this.gc = gc;
	}


	@Override
	public void dologic(long stepMiliseconds)
	{
		logicSteps++;
		timeElapsed += stepMiliseconds;
		logicFps.update(timeElapsed); // it`s requires to increment some variable on every logic step (measurement ++)

	}


	@Override
	public void render(long stepMiliseconds)
	{
		frameRenderSteps++;

		animationFps.update();

		gc.clearRect(0, 0, 200, 200);

		gc.setFill(Color.rgb(255, 255, 255, 0.75));
		gc.fillRect(0, 0, 140, 160);

		gc.setFill(Color.web("000"));
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
}