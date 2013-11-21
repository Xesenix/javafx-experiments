package pl.xesenix.experiments.experiment03.components.viewport.states;


public class SimpleViewportStateManager implements IViewportStateManager
{
	protected IViewportState state = new SimpleViewportState();


	@Override
	public IViewportState getDefaultState()
	{
		return state;
	}

}