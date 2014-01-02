package pl.xesenix.experiments.experiment04;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.NonIterableChange.SimpleAddChange;
import com.sun.javafx.collections.NonIterableChange.SimpleRemovedChange;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeItem;

public class XmlItem extends TreeItem<Object>
{
	static final Logger log = LoggerFactory.getLogger(XmlItem.class);
	
	
	public XmlItem(Object obj)
	{
		super(obj);
		
		if (obj instanceof Element)
		{
			getChildren().addListener(new ListChangeListener<TreeItem<Object>>() {

				public void onChanged(ListChangeListener.Change<? extends TreeItem<Object>> c)
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
									
									Element parent = (Element) getValue();
									Object obj = addedItem.getValue();
									
									if (obj instanceof Element)
									{
										//((Element) obj).detach();
									//	parent.addContent((Element) obj);
									}
								}
							}
							
							if (c.wasRemoved())
							{
								for (TreeItem<Object> removedItem : c.getRemoved())
								{
									log.debug("removed {}", removedItem);
									
									Element parent = (Element) getValue();
									Object obj = removedItem.getValue();
									
									if (obj instanceof Element)
									{
										parent.removeContent((Element) obj);
									}
								}
							}
						}
					}
				}
				
			});
		}
	}
}
