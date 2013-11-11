package pl.xesenix.experiments.experiment02.components.list;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import pl.xesenix.experiments.experiment02.vo.IPathPoint;

public class PointListCellFactory implements Callback<ListView<IPathPoint>, ListCell<IPathPoint>>
{
	private String format;


	public PointListCellFactory(String format)
	{
		this.format = format;
	}
	
	
	@Override
	public ListCell<IPathPoint> call(ListView<IPathPoint> param)
	{
		return new PointListItemCell(format);
	}
}