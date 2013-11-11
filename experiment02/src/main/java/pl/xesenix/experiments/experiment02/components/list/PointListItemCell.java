
package pl.xesenix.experiments.experiment02.components.list;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;


public class PointListItemCell extends ListCell<IPathPoint>
{
	private String format;


	public PointListItemCell(String format)
	{
		this.format = format;
	}


	protected void updateItem(IPathPoint point, boolean empty)
	{
		if (!empty)
		{
			try
			{
				Map<String, Object> properties = BeanUtils.describe(point);
				StrSubstitutor formatter = new StrSubstitutor(properties);
				
				setText(formatter.replace(format));
			}
			catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}

		super.updateItem(point, empty);
	}
}