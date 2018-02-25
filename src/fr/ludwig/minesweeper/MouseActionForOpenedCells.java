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
			game.nop();
			y = 0 + y;
			x = 0 + x;

		}

	}

	@Override
	public void mousePressed(MouseEvent evenement)
	{
	}

	@Override
	public void mouseReleased(MouseEvent evenement)
	{
	}

	@Override
	public void mouseEntered(MouseEvent evenement)
	{
	}

	@Override
	public void mouseExited(MouseEvent evenement)
	{
	}
}
