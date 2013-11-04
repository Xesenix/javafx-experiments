package pl.xesenix.experiments.experiment01.utils;

import javafx.util.StringConverter;

public class IntegerStringConverter extends StringConverter<Number>
{
	@Override
	public Integer fromString(String value)
	{
		return Integer.parseInt(value);
	}


	@Override
	public String toString(Number value)
	{
		return String.valueOf((Integer) value);
	}
}