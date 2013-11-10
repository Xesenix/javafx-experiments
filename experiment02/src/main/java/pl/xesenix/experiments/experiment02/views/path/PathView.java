package pl.xesenix.experiments.experiment02.views.path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

public class PathView extends Pane
{
	private static final Logger log = LoggerFactory.getLogger(PathView.class);
	
	
	private ObjectProperty<Path> path = new SimpleObjectProperty(this, "path", new Path());
	
	
	private ListProperty<PathPointView> points = new SimpleListProperty(this, "points", FXCollections.<PathPointView>observableArrayList());


	private Pane pathLayer;


	private Pane pointsLayer;
	
	
	public PathView(IPath pathModel)
	{
		pathLayer = new Pane();
		pointsLayer = new Pane();
		
		getChildren().add(pathLayer);
		getChildren().add(pointsLayer);
		
		update(pathModel);
	}


	public void update(IPath pathModel)
	{
		Path path = this.path.get();
		ObservableList<PathElement> segments = path.getElements();
		
		segments.clear();
		
		points.clear();
		pointsLayer.getChildren().clear();
		
		log.debug("pathModel: [{}]", pathModel);
		
		for (IPathPoint point : pathModel.getPathPoints())
		{
			log.debug("adding point [{}]", point);
			
			PathPointView pathPointView = new PathPointView(point);
			
			points.add(pathPointView);
		}
		
		pointsLayer.getChildren().addAll(points);
	}
	
	
}
