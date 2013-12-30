package pl.xesenix.experiments.experiment04;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.NonIterableChange.SimpleAddChange;
import com.sun.javafx.collections.NonIterableChange.SimpleRemovedChange;

import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

public class XmlItem extends TreeItem<Object>
{
	static final Logger log = LoggerFactory.getLogger(XmlTreeViewMediator.class);
	
	
	public XmlItem(Object obj)
	{
		super(obj);
		
		if (obj instanceof Element)
		{
			getChildren().addListener(new ListChangeListener<TreeItem<Object>>() {

				public void onChanged(ListChangeListener.Change<? extends TreeItem<Object>> c)
				{
					log.debug("list of children change {}", c);
					
					// THIS API SUCKS
					
					if (!(c instanceof NonIterableChange))
					{
						while (c.next())
						{
							if (!c.wasPermutated() || !c.wasUpdated())
							{
								if (c.wasAdded())
								{
									for (TreeItem<Object> addedItem : c.getAddedSubList())
									{
										log.debug("added {}", addedItem);
									}
								}
								
								if (c.wasRemoved())
								{
									for (TreeItem<Object> removedItem : c.getRemoved())
									{
										log.debug("removed {}", removedItem);
									}
								}
							}
						}
					}
				}
				
			});
			
			addEventHandler(Event.ANY, new EventHandler<Event>() {

				public void handle(Event event)
				{
					log.debug("item event {}", event.getEventType());
				}
			});
		}
	}
}
