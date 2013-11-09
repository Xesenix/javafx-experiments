package pl.xesenix.experiments.experiment02.views.path;

import pl.xesenix.experiments.experiment02.vo.IPathPoint;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;

public class PathPointView extends Pane
{
	private Circle pointHandle;

	public PathPointView(IPathPoint point)
	{
		pointHandle = CircleBuilder.create()
			.radius(10)
			.fill(Color.web("#f0f"))
			.build();
		
		getChildren().add(pointHandle);

		setTranslateX(point.getX());
		setTranslateY(point.getY());
	}
}
