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
