package pl.xesenix.experiments.experiment04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

public class XmlTreeItem extends TreeItem<Object>
{
	static final Logger log = LoggerFactory.getLogger(XmlTreeItem.class);
	
	
	public XmlTreeItem(Object value)
	{
		super(value);
		
		valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue)
			{
				log.debug("{} {}", oldValue, newValue);
			}
		});
		
		addEventHandler(Event.ANY, new EventHandler<Event>() {

			@Override
			public void handle(Event event)
			{
				log.debug("{} {}", event, event.getEventType());
			}
		});
	}
}
