
package pl.xesenix.experiments.experiment02.components.patheditor.requests;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.xesenix.experiments.experiment02.components.patheditor.services.PathDrawingContext;
import pl.xesenix.experiments.experiment02.vo.IPath;

import com.google.inject.Inject;


public class CreatePointRequest extends Service<IPath>
{
	@Inject
	public PathDrawingContext context;
	
	
	public double x;
	
	
	public double y;


	@Override
	protected Task<IPath> createTask()
	{
		// TODO Auto-generated method stub
		return new Task<IPath>() {

			@Override
			protected IPath call() throws Exception
			{
				context.createPoint(x, y);
				
				return context.getEditedPath();
			}
		};
	}

}
