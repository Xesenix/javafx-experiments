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
package pl.xesenix.experiments.experiment02.components.patheditor.states;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.xesenix.experiments.experiment02.components.patheditor.PathsEditor;
import pl.xesenix.experiments.experiment02.cursors.CursorProvider;

import com.google.inject.Inject;


public class BaseState implements IPathEditorState
{
	protected static final Logger log = LoggerFactory.getLogger(BaseState.class);


	@Inject
	protected CursorProvider cursorProvider;


	protected PathsEditor pathsEditor;


	protected double startDragX;


	protected double startDragY;


	protected Cursor mouseCursor;


	public void initialize(PathsEditor pathsEditor)
	{
		this.pathsEditor = pathsEditor;
	}


	@Override
	public void manageCanvasMouseEvent(MouseEvent event)
	{
		if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED))
		{
			startDragX = event.getSceneX() - pathsEditor.getCanvas().getTranslateX();
			startDragY = event.getSceneY() - pathsEditor.getCanvas().getTranslateY();
			mouseCursor = ((Node) event.getSource()).getCursor();
			event.consume();
		}
		else
		{
			if (event.getButton().equals(MouseButton.SECONDARY))
			{
				if (event.getEventType().equals(MouseEvent.DRAG_DETECTED))
				{
					((Node) event.getSource()).setCursor(cursorProvider.get(Cursor.OPEN_HAND));
				}
				else
				{
					if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
					{
						((Node) event.getSource()).setCursor(mouseCursor);
					}
					else
					{
						if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED))
						{
							double x = event.getSceneX() - startDragX;
							double y = event.getSceneY() - startDragY;
							pathsEditor.getCanvas().setTranslateX(x);
							pathsEditor.getCanvas().setTranslateY(y);
							pathsEditor.getCanvasContainer().setStyle("-fx-background-position:" + x + " " + y);
							event.consume();
						}
					}
				}
			}
		}

	}
}
