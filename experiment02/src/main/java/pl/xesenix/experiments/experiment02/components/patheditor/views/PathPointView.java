/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/
package pl.xesenix.experiments.experiment02.components.patheditor.views;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import pl.xesenix.experiments.experiment02.cursors.CursorProvider;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

import com.google.inject.Inject;

public class PathPointView extends Group
{
	public static final String POINT_HANDLE = "pointHandle";


	public static final String IN_HANDLE = "inHandle";
	
	
	public static final String OUT_HANDLE = "outHandle";


	@Inject
	protected CursorProvider cursorProvider;


	private Circle pointHandle;


	private Circle inHandle;


	private Circle outHandle;

	
	public PathPointView()
	{
		pointHandle = CircleBuilder.create()
			.radius(15)
			.fill(Color.web("#8fff00"))
			.userData(POINT_HANDLE)
			.build();
		
		inHandle = CircleBuilder.create()
			.radius(5)
			.fill(Color.web("#00f"))
			.userData(IN_HANDLE)
			.build();

		outHandle = CircleBuilder.create()
			.radius(5)
			.fill(Color.web("#f00"))
			.userData(OUT_HANDLE)
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
		
		setPickOnBounds(true);
		setOpacity(0.5f);
		
	}

	public void update(IPathPoint point)
	{
		inHandle.setCenterX(point.getInX());
		inHandle.setCenterY(point.getInY());
		
		outHandle.setCenterX(point.getOutX());
		outHandle.setCenterY(point.getOutY());
		
		setTranslateX(point.getX());
		setTranslateY(point.getY());
		
		setUserData(point);
	}
}
