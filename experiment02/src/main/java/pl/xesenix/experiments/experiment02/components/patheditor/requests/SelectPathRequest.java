
package pl.xesenix.experiments.experiment02.components.patheditor.requests;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment02.components.patheditor.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;

import com.google.inject.Inject;


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
