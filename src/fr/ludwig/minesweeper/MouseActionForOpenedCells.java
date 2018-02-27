package fr.ludwig.minesweeper;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseActionForOpenedCells implements MouseListener
{
	private int x;
	private int y;
	private Minesweeper game;

	public MouseActionForOpenedCells(Minesweeper game, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.game = game;
	}

	@Override
	public void mouseClicked(MouseEvent evenement)
	{
		if (evenement.getButton() == MouseEvent.BUTTON2)
		{
			// TODO create middle click method
		}

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// Unused method from interface
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Unused method from interface
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Unused method from interface
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// // Unused method from interface
	}
}
