package pl.xesenix.experiments.experiment02.requests;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment02.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CreatePathRequest extends Service<IPath>
{
	@Inject
	public PathDrawingContext context;

	@Override
	protected Task<IPath> createTask()
	{
		return new Task<IPath>() {

			@Override
			protected IPath call() throws Exception
			{
				IPath path = context.createPath();
				
				return path;
			}
			
		};
	}

}
