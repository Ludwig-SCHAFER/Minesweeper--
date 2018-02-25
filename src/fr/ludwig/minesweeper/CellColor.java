package fr.ludwig.minesweeper;

import java.awt.Color;

public enum CellColor
{
	EMPTY(0, new Color(255, 255, 255)),
	BLUE(1, new Color(0, 0, 128)),
	GREEN(2, new Color(0, 128, 0)),
	RED(3, new Color(255, 0, 0)),
	DARKBLUE(4, new Color(0, 0, 80)),
	BROWN(5, new Color(160, 80, 40)),
	CYAN(6, new Color(0, 128, 128)),
	BLACK(7, new Color(0, 0, 0)),
	GRAY(8, new Color(80, 80, 80));

	private int value;

	private Color color;

	CellColor(int value, Color color)
	{
		this.value = value;
		this.color = color;
	}

	@Override
	public String toString()
	{
		return Integer.toString(value);
	}

	public static Color toColor(int value)
	{
		for (CellColor cellColor : CellColor.values())
		{
			if (value == cellColor.getValue())
			{
				return cellColor.getColor();
			}
		}
		return new Color(255, 0, 0);
	}

	public int getValue()
	{
		return value;
	}

	public Color getColor()
	{
		return color;
	}
}
