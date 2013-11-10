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
package pl.xesenix.experiments.experiment02.cursors;

import java.util.HashMap;

import javafx.scene.Cursor;

import com.google.inject.Singleton;

@Singleton
public class CursorProvider
{
	HashMap<Cursor, Cursor> map = new HashMap<Cursor, Cursor>();
	
	
	public CursorProvider()
	{
		map.put(Cursor.OPEN_HAND, Cursor.cursor(getClass().getResource("/images/open_hand_cursor.png").toExternalForm()));
		map.put(Cursor.CLOSED_HAND, Cursor.cursor(getClass().getResource("/images/closed_hand_cursor.png").toExternalForm()));
	}
	
	
	public Cursor get(Cursor type)
	{
		if (map.containsKey(type))
		{
			return map.get(type);
		}
		
		return type;
	}
}
