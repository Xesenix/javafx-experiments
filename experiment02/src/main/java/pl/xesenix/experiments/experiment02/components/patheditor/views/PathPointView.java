package pl.xesenix.experiments.experiment02.components.patheditor.views;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

public class PathPointView extends Pane
{
	private Circle pointHandle;
	private Circle inHandle;
	private Circle outHandle;

	public PathPointView(IPathPoint point)
	{
		pointHandle = CircleBuilder.create()
			.radius(15)
			.fill(Color.web("#8fff00"))
			.build();
		
		inHandle = CircleBuilder.create()
			.radius(5)
			.fill(Color.web("#00f"))
			.build();

		outHandle = CircleBuilder.create()
			.radius(5)
			.fill(Color.web("#f00"))
			.build();
		
		Line inLine = LineBuilder.create()
			.stroke(Color.web("#0ff"))
			.strokeWidth(1)
			.build();
		
		inLine.startXProperty().bind(pointHandle.centerXProperty());
		inLine.startYProperty().bind(pointHandle.centerYProperty());
		inLine.endXProperty().bind(inHandle.centerXProperty());
		inLine.endYProperty().bind(inHandle.centerYProperty());
		
		Line outLine = LineBuilder.create()
			.stroke(Color.web("#0ff"))
			.strokeWidth(1)
			.build();
		
		outLine.startXProperty().bind(pointHandle.centerXProperty());
		outLine.startYProperty().bind(pointHandle.centerYProperty());
		outLine.endXProperty().bind(outHandle.centerXProperty());
		outLine.endYProperty().bind(outHandle.centerYProperty());

		getChildren().add(inLine);
		getChildren().add(outLine);
		getChildren().add(pointHandle);
		getChildren().add(inHandle);
		getChildren().add(outHandle);
		
		setTranslateX(point.getX());
		setTranslateY(point.getY());
		
		setOpacity(0.5f);
		setUserData(point);
		
	}

	public void update(IPathPoint point)
	{
		inHandle.setCenterX(point.getInX());
		inHandle.setCenterY(point.getInY());
		
		outHandle.setCenterX(point.getOutX());
		outHandle.setCenterY(point.getOutY());
	}
}
