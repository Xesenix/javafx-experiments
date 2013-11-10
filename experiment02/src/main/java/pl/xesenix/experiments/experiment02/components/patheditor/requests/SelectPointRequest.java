
package pl.xesenix.experiments.experiment02.components.patheditor.requests;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment02.components.patheditor.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class SelectPointRequest extends Service<IPathPoint>
{
	@Inject
	public PathDrawingContext context;


	public IPathPoint point;


	@Override
	protected Task<IPathPoint> createTask()
	{
		return new Task<IPathPoint>() {

			@Override
			protected IPathPoint call() throws Exception
			{
				context.selectPoint(point);

				return context.getEditedPoint();
			}
		};
	}

}
