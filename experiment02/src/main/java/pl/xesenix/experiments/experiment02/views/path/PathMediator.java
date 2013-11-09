
package pl.xesenix.experiments.experiment02.views.path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.xesenix.experiments.experiment02.requests.AddPointToPathRequest;
import pl.xesenix.experiments.experiment02.requests.CreatePathRequest;
import pl.xesenix.experiments.experiment02.requests.CreatePointRequest;
import pl.xesenix.experiments.experiment02.requests.IRequestProvider;
import pl.xesenix.experiments.experiment02.requests.RemovePointRequest;
import pl.xesenix.experiments.experiment02.requests.SelectPathRequest;
import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

import com.google.inject.Inject;

/**
 * Mediator but maybe we should use it to manage drawing state pattern
 * 
 * @author Xesenix
 */
public class PathMediator implements IPathMediator
{
	private static final Logger log = LoggerFactory.getLogger(PathMediator.class);
	
	
	@Inject
	private IRequestProvider requestProvider;


	private IPathView view;


	@Override
	public void registerView(IPathView pathView)
	{
		log.debug("registring view for path: [{}]", pathView);
		
		view = pathView;
	}


	@Override
	public void createPath()
	{
		log.debug("requesting create path");
		
		CreatePathRequest request = requestProvider.get(CreatePathRequest.class);
		request.setOnSucceeded(new CreatePathEventHandler());
		request.start();
	}


	@Override
	public void setCurrentPath(IPath path)
	{
		log.debug("requesting select as current path: [{}]", path);
		
		SelectPathRequest request = requestProvider.get(SelectPathRequest.class);
		request.setOnSucceeded(new SelectPathEventHandler());
		request.path = path;
		request.start();
	}


	public void createPoint(double x, double y)
	{
		log.debug("requesting create point at: ({}, {})", x, y);
		
		CreatePointRequest request = requestProvider.get(CreatePointRequest.class);
		request.setOnSucceeded(new UpdatePathViewEventHandler());
		request.x = x;
		request.y = y;
		request.start();
	}


	@Override
	public void addPointToPath(IPathPoint point)
	{
		log.debug("requesting add point to current path: [{}]", point);
		
		AddPointToPathRequest request = requestProvider.get(AddPointToPathRequest.class);
		request.setOnSucceeded(new UpdatePathViewEventHandler());
		request.point = point;
		request.start();
	}


	@Override
	public void removePoint(IPathPoint point)
	{
		log.debug("requesting remove point: [{}]", point);
		
		RemovePointRequest request = requestProvider.get(RemovePointRequest.class);
		request.setOnSucceeded(new UpdateViewEventHandler());
		request.point = point;
		request.start();
	}


	private class CreatePathEventHandler implements EventHandler<WorkerStateEvent>
	{
		@Override
		public void handle(WorkerStateEvent event)
		{
			IPath path = (IPath) event.getSource().getValue();
			
			log.debug("created path: [{}]", path);
			
			view.createPathView(path);
		}
	}


	private class SelectPathEventHandler implements EventHandler<WorkerStateEvent>
	{
		@Override
		public void handle(WorkerStateEvent event)
		{
			IPath path = (IPath) event.getSource().getValue();
			
			log.debug("selected path: [{}]", path);
			
			view.focusPath(path);
		}
	}


	private class UpdatePathViewEventHandler implements EventHandler<WorkerStateEvent>
	{
		@Override
		public void handle(WorkerStateEvent event)
		{
			IPath path = (IPath) event.getSource().getValue();
			
			log.debug("updated path: [{}]", path);
			
			view.updatePath(path);
		}
	}


	private class UpdateViewEventHandler implements EventHandler<WorkerStateEvent>
	{
		@Override
		public void handle(WorkerStateEvent event)
		{
			view.update();
		}
	}

}
