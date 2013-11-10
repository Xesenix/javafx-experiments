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
import javafx.scene.input.MouseEvent;
import pl.xesenix.experiments.experiment02.components.patheditor.views.PathPointView;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;


public class EditPathState extends BaseState
{
	private Node draggedObject;


	private double cos;


	private double sin;


	@Override
	public void manageCanvasMouseEvent(MouseEvent event)
	{
		Node target = (Node) event.getTarget();

		if (draggedObject != null)
		{
			target = draggedObject;
		}

		Node parent = target.getParent();

		if (parent instanceof PathPointView)
		{
			Object handleType = target.getUserData();
			PathPointView pathPointView = (PathPointView) parent;
			IPathPoint point = (IPathPoint) pathPointView.getUserData();

			if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED))
			{
				startDragX = event.getSceneX() - event.getX() + pathsEditor.getCanvas().getTranslateX();
				startDragY = event.getSceneY() - event.getY() + pathsEditor.getCanvas().getTranslateY();

				mouseCursor = ((Node) event.getSource()).getCursor();

				double angle = Math.atan2(point.getInY(), point.getInX()) - Math.atan2(point.getOutY(), point.getOutX());
				double din = Math.sqrt(Math.pow(point.getInX(), 2) + Math.pow(point.getInY(), 2));
				double dout = Math.sqrt(Math.pow(point.getOutX(), 2) + Math.pow(point.getOutY(), 2));
				double scale;

				switch (handleType.toString())
				{
					case PathPointView.IN_HANDLE:
						scale = dout == din ? 1 : (din != 0 ? dout / din : 0);
						cos = Math.cos(angle) * scale;
						sin = Math.sin(angle) * scale;
						break;

					case PathPointView.OUT_HANDLE:
						scale = dout == din ? 1 : (dout != 0 ? din / dout : 0);
						cos = Math.cos(-angle) * scale;
						sin = Math.sin(-angle) * scale;
						break;
				}

				pathsEditor.getMediator().selectPoint(point);

				event.consume();
			}
			else if (event.getEventType().equals(MouseEvent.DRAG_DETECTED))
			{
				if (handleType.equals(PathPointView.POINT_HANDLE) && !event.isControlDown())
				{
					((Node) event.getSource()).setCursor(cursorProvider.get(Cursor.CLOSED_HAND));
					draggedObject = target;
				}
			}
			else if (event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
			{
				((Node) event.getSource()).setCursor(mouseCursor);
				draggedObject = null;
			}
			else if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED))
			{
				double x = event.getSceneX() - startDragX;
				double y = event.getSceneY() - startDragY;

				if (event.isControlDown())
				{
					log.debug("event: [{}]", event);

					double dx = x - point.getX();
					double dy = y - point.getY();

					switch (handleType.toString())
					{
						case PathPointView.POINT_HANDLE:
							pathsEditor.getMediator().updatePoint(point.getX(), point.getY(), dx, dy, -dx, -dy);
							event.consume();
							break;

						case PathPointView.IN_HANDLE:
							pathsEditor.getMediator().updatePoint(point.getX(),
								point.getY(),
								dx,
								dy,
								dx * cos + dy * sin,
								-dx * sin + dy * cos);
							event.consume();
							break;

						case PathPointView.OUT_HANDLE:
							pathsEditor.getMediator().updatePoint(point.getX(),
								point.getY(),
								dx * cos + dy * sin,
								-dx * sin + dy * cos,
								dx,
								dy);
							event.consume();
							break;
					}
				}
				else
				{
					switch (handleType.toString())
					{
						case PathPointView.POINT_HANDLE:
							pathsEditor.getMediator().updatePoint(x,
								y,
								point.getInX(),
								point.getInY(),
								point.getOutX(),
								point.getOutY());
							event.consume();
							break;

						case PathPointView.IN_HANDLE:
							pathsEditor.getMediator().updatePoint(point.getX(),
								point.getY(),
								x - point.getX(),
								y - point.getY(),
								point.getOutX(),
								point.getOutY());
							event.consume();
							break;

						case PathPointView.OUT_HANDLE:
							pathsEditor.getMediator().updatePoint(point.getX(),
								point.getY(),
								point.getInX(),
								point.getInY(),
								x - point.getX(),
								y - point.getY());
							event.consume();
							break;
					}
				}
			}
		}
		else
		{
			super.manageCanvasMouseEvent(event);
		}
	}
}
