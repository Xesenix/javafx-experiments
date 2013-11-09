
package pl.xesenix.experiments.experiment02.requests;

import pl.xesenix.experiments.experiment02.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;

import com.google.inject.Inject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class SelectPathRequest extends Service<IPath>
{
	@Inject
	public PathDrawingContext context;


	public IPath path;


	@Override
	protected Task<IPath> createTask()
	{
		return new Task<IPath>() {

			@Override
			protected IPath call() throws Exception
			{
				context.setEditedPath(path);

				return context.getEditedPath();
			}
		};
	}

}
