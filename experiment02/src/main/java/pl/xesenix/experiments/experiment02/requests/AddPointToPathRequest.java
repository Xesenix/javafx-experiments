
package pl.xesenix.experiments.experiment02.requests;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment02.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


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
