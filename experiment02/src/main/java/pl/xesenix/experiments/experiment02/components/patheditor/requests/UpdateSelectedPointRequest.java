
package pl.xesenix.experiments.experiment02.components.patheditor.requests;

import com.google.inject.Inject;

import pl.xesenix.experiments.experiment02.components.patheditor.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;
import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class UpdateSelectedPointRequest extends Service<IPath>
{
	@Inject
	public PathDrawingContext context;


	public double x;


	public double y;


	public double inX;


	public double inY;


	public double outX;


	public double outY;


	@Override
	protected Task<IPath> createTask()
	{
		return new Task<IPath>() {

			@Override
			protected IPath call() throws Exception
			{
				IPathPoint point = context.getEditedPoint();
				
				context.updateSelectedPoint(x, y, inX, inY, outX, outY);
				
				return point.getPath();
			}
		};
	}

}
