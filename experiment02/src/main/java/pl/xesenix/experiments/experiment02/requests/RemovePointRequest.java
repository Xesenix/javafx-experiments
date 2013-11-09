
package pl.xesenix.experiments.experiment02.requests;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment02.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class RemovePointRequest extends Service<Void>
{
	@Inject
	public PathDrawingContext context;


	public IPathPoint point;


	@Override
	protected Task<Void> createTask()
	{
		// TODO Auto-generated method stub
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception
			{
				context.removePoint(point);

				return null;
			}
		};
	}

}
