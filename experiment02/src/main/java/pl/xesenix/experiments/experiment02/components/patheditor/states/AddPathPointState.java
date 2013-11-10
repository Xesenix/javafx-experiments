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

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AddPathPointState extends BaseState
{
	@Override
	public void manageCanvasMouseEvent(MouseEvent event)
	{
		if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED) && event.getButton().equals(MouseButton.PRIMARY))
		{
			Point2D point = pathsEditor.getCanvas().sceneToLocal(event.getSceneX(), event.getSceneY());
			log.debug("=== REQUESTING CREATE POINT AT {} ===", point);
			pathsEditor.getMediator().createPoint(point.getX(), point.getY());
			event.consume();
		}
		else
		{
			super.manageCanvasMouseEvent(event);
		}
	}
}
