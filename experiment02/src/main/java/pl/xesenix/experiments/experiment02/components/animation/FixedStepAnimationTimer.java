package pl.xesenix.experiments.experiment02.components.animation;

import javafx.animation.AnimationTimer;

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