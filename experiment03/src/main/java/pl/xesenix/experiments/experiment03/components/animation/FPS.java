package pl.xesenix.experiments.experiment03.components.animation;

public class FPS
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