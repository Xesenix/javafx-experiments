
package pl.xesenix.experiments.experiment02.components.patheditor.views;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;


public class PathView extends Group
{
	private static final Logger log = LoggerFactory.getLogger(PathView.class);
	
	
	@Inject
	private Injector injector;


	private ObjectProperty<Path> path = new SimpleObjectProperty(this, "path");


	private ListProperty<PathPointView> points = new SimpleListProperty(this, "points", FXCollections.<PathPointView> observableArrayList());


	private ObservableMap<IPathPoint, PathPointView> pointsToViewMap = FXCollections.<IPathPoint, PathPointView> observableHashMap();


	private Group pathLayer;


	private Group pointsLayer;


	public PathView()
	{
		pathLayer = new Group();
		pathLayer.setMouseTransparent(true);
		pointsLayer = new Group();
		pathLayer.setPickOnBounds(true);

		Path path = new Path();
		path.setStroke(Color.web("#fff"));
		path.setStrokeWidth(2);

		this.path.set(path);

		pathLayer.getChildren().add(path);

		getChildren().add(pathLayer);
		getChildren().add(pointsLayer);
	}


	public void update(IPath pathModel)
	{
		Path path = this.path.get();
		ObservableList<PathElement> segments = path.getElements();

		segments.clear();

		points.clear();
		pointsLayer.getChildren().clear();

		log.debug("pathModel: [{}]", pathModel);

		IPathPoint previousPoint = null;

		for (IPathPoint point : pathModel.getPathPoints())
		{
			PathPointView pathPointView = getPointView(point);

			points.add(pathPointView);

			path.getElements().add(preparePathSegment(previousPoint, point));

			previousPoint = point;
		}

		pointsLayer.getChildren().addAll(points);
		
		setPickOnBounds(true);
		
		setUserData(pathModel);
	}


	private PathPointView getPointView(IPathPoint point)
	{
		PathPointView pathPointView;
		
		if (pointsToViewMap.containsKey(point))
		{
			log.debug("retriving point [{}]", point);

			pathPointView = pointsToViewMap.get(point);
		}
		else
		{
			log.debug("adding point [{}]", point);

			pathPointView = injector.getInstance(PathPointView.class);
			
			pointsToViewMap.put(point, pathPointView);
		}
		
		pathPointView.update(point);
		
		return pathPointView;
	}


	private PathElement preparePathSegment(IPathPoint previousPoint, IPathPoint point)
	{
		if (previousPoint == null)
		{
			return new MoveTo(point.getX(), point.getY());
		}

		return new CubicCurveTo(
			previousPoint.getOutX() + previousPoint.getX(),
			previousPoint.getOutY() + previousPoint.getY(),
			point.getInX() + point.getX(),
			point.getInY() + point.getY(),
			point.getX(),
			point.getY());
	}

}
