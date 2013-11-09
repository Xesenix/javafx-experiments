package pl.xesenix.experiments.experiment02.vo;

import javafx.beans.property.DoubleProperty;

public interface IPathPoint
{
	DoubleProperty xProperty();


	double getX();


	void setX(double value);


	DoubleProperty yProperty();


	double getY();


	void setY(double value);


	DoubleProperty inXProperty();


	double getInX();


	void setInX(double value);


	DoubleProperty inYProperty();


	double getInY();


	void setInY(double value);


	DoubleProperty outXProperty();


	public double getOutX();


	void setOutX(double value);


	DoubleProperty outYProperty();


	double getOutY();


	void setOutY(double value);
	
	
	IPath getPath();
	
	
	void addToPath(IPath path);


	void removeFromPath(IPath path);
}
