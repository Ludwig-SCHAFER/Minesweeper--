package fr.ludwig.minesweeper;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseActionForClosedCells implements MouseListener
{
	private int x;
	private int y;
	private Minesweeper game;

	public MouseActionForClosedCells(Minesweeper game, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.game = game;
	}

	@Override
	public void mouseClicked(MouseEvent evenement)
	{
		if (evenement.getButton() == MouseEvent.BUTTON1)
		{
			game.openCell(x, y);
		}

		if (evenement.getButton() == MouseEvent.BUTTON3)
		{
			game.toggleCellStatus(x, y);
			game.refresh();
		}

		if (evenement.getButton() == MouseEvent.BUTTON2)
		{
			// game.explorerAutourDelaCase(x, y);
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
		evenement.getComponent().setBackground(new Color(160, 160, 160));
	}

	@Override
	public void mouseExited(MouseEvent evenement)
	{
		evenement.getComponent().setBackground(new Color(192, 192, 192));// exact color from windows game was #c0c0c0 !!
	}
}
