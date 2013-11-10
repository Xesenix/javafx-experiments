
package pl.xesenix.experiments.experiment02.components.patheditor.requests;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment02.components.patheditor.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

import com.google.inject.Inject;


public class AddPointToPathRequest extends Service<IPath>
{
	@Inject
	public PathDrawingContext context;


	public IPathPoint point;


	@Override
	protected Task<IPath> createTask()
	{
		// TODO Auto-generated method stub
		return new Task<IPath>() {

			@Override
			protected IPath call() throws Exception
			{
				IPath path = context.getEditedPath();

				path.addPathPoint(point);

				return path;
			}
		};
	}

}
