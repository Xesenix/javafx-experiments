package pl.xesenix.experiments.experiment02.components.patheditor.requests;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment02.components.patheditor.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;

import com.google.inject.Inject;

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
