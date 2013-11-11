/*******************************************************************************
 * Copyright (c) 2013 Paweł Kapalla, Xessenix. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the GNU
 * Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors: Paweł Kapalla, Xessenix - initial API and implementation
 ******************************************************************************/

package pl.xesenix.experiments.experiment02;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment02.components.animation.FPSRendererAnimator;
import pl.xesenix.experiments.experiment02.components.list.PointListCellFactory;
import pl.xesenix.experiments.experiment02.components.patheditor.PathsEditor;
import pl.xesenix.experiments.experiment02.vo.IPath;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class Controller
{
	static final Logger log = LoggerFactory.getLogger(Controller.class);


	@Inject
	private PathsEditor editor;


	@FXML
	private ResourceBundle resources;


	@FXML
	private URL location;


	@FXML
	private AnchorPane editorContainer;


	@FXML
	private ListView<IPath> pathsList;


	@FXML
	private ListView<IPathPoint> pointsList;


	@FXML
	private AnchorPane view;


	public Controller()
	{
		log.debug("construct");
	}


	public AnchorPane getView()
	{
		return view;
	}


	@FXML
	public void initialize()
	{
		log.debug("initializing");

		assert editorContainer != null : "fx:id=\"editorContainer\" was not injected: check your FXML file 'app.fxml'.";
		assert pathsList != null : "fx:id=\"pathsList\" was not injected: check your FXML file 'app.fxml'.";
		assert pointsList != null : "fx:id=\"pointsList\" was not injected: check your FXML file 'app.fxml'.";
		assert view != null : "fx:id=\"view\" was not injected: check your FXML file 'app.fxml'.";

		pointsList.setCellFactory(new PointListCellFactory("position: (${x}, ${y}), in: (${inX}, ${inY}), out: (${outX}, ${outY}), path: ${path}"));
		
		pathsList.itemsProperty().bind(editor.pathsProperty());
		pointsList.itemsProperty().bind(editor.pointsProperty());
		
		editorContainer.getChildren().add(editor);

		AnchorPane.setTopAnchor(editor, (double) 0f);
		AnchorPane.setBottomAnchor(editor, (double) 0f);
		AnchorPane.setRightAnchor(editor, (double) 0f);
		AnchorPane.setLeftAnchor(editor, (double) 0f);

		Canvas canvas = new Canvas(200, 200);
		canvas.setMouseTransparent(true);

		editor.getCanvasContainer().getChildren().add(canvas);

		final GraphicsContext gc = canvas.getGraphicsContext2D();
		int fps = 10;

		AnimationTimer animator = (new FPSRendererAnimator(fps, gc));

		animator.start();
	}
}
