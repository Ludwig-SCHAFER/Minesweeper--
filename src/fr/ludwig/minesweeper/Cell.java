package fr.ludwig.minesweeper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class Cell extends Object
{

	int x;
	int y;
	boolean trapped;
	CellStatus status;
	int value;
	Minesweeper game;

	public static final String CHAR_TRAP = new String(Character.toChars(128163));

	public Cell(Minesweeper game, int x, int y)
	{
		this.game = game;
		this.x = x;
		this.y = y;
		status = CellStatus.CLOSED;
		trapped = false;
		value = 0;
	}

	public void markAsTrapped()
	{
		trapped = true;
		value = -9;
	}

	public void incrementValue()
	{
		value++;
	}

	public boolean isEmpty()
	{
		return value == 0;
	}

	public String toText()
	{
		if (trapped)
		{
			return CHAR_TRAP;
		}
		else if (value == 0)
		{
			return " ";
		}
		else if (value > 0)
		{
			return Integer.toString(value);
		}
		else
		{
			return status.getText();
		}
	}

	public Component toComponent(int componentX, int componentY, int componentWidth, int componentHeight, Border border)
	{
		if ((status == CellStatus.OPEN) || (status == CellStatus.TRIGGERED) || (status == CellStatus.REVEALED))
		{
			JLabel label = new JLabel();

			label.setBounds(componentX, componentY, componentWidth, componentHeight);
			label.addMouseListener(new MouseActionForOpenedCells(game, x, y));
			label.setBorder(border);
			label.setOpaque(true);

			if (status == CellStatus.TRIGGERED)
			{
				label.setText(status.getText());
				label.setBackground(Color.RED);
				label.setForeground(Color.WHITE);
			}
			else if (status == CellStatus.REVEALED)
			{
				label.setText(CHAR_TRAP);
				label.setBackground(Color.ORANGE);
				label.setForeground(Color.WHITE);
			}
			else if (value > 0)
			{
				label.setText(Integer.toString(value));
				label.setForeground(CellColor.toColor(value));
			}

			label.setHorizontalAlignment(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);
			label.setFont(new Font("Courrier", Font.BOLD, Math.min(componentWidth, componentHeight) / 2));

			return label;

		}
		else
		{
			JButton button = new JButton();

			button.setBounds(componentX, componentY, componentWidth, componentHeight);
			button.addMouseListener(new MouseActionForClosedCells(game, x, y));
			button.setBorder(border);
			button.setOpaque(true);
			button.setBackground(new Color(192, 192, 192));
			if ((status == CellStatus.FLAGGED) || (status == CellStatus.MARKED))
			{
				button.setText(status.toString());
			}
			/******
			 * else if (trapped) { button.setText(CHAR_TRAP); } else if (value >= 0) {
			 * button.setText(Integer.toString(value));
			 * button.setForeground(CellColor.toColor(value)); }
			 * 
			 * button.setHorizontalAlignment(JLabel.CENTER);
			 * button.setVerticalAlignment(JLabel.CENTER); button.setFont(new
			 * Font("Courrier", Font.BOLD, Math.min(componentWidth, componentHeight) / 2));
			 * /
			 *******/

			return button;
		}
	}

	public boolean isTriggered()
	{
		return status == CellStatus.TRIGGERED;
	}

	public boolean isTrapped()
	{
		return trapped;
	}

	public boolean isNotTrapped()
	{
		return !isTrapped();
	}

	public void open()
	{
		if ((status == CellStatus.CLOSED) || (status == CellStatus.FLAGGED) || (status == CellStatus.MARKED))
		{
			if (trapped)
			{
				status = CellStatus.TRIGGERED;
			}
			else
			{
				status = CellStatus.OPEN;
			}
		}
	}

	public void toggleStatus()
	{
		switch (status)
		{
		case CLOSED:
			status = CellStatus.FLAGGED;
			break;
		case FLAGGED:
			status = CellStatus.MARKED;
			break;
		case MARKED:
			status = CellStatus.CLOSED;
			break;
		default:
			break;
		}
	}

	public void reveal()
	{
		if (isMarked() || isMarked() || isClosed())
		{
			status = CellStatus.REVEALED;
		}
	}

	public boolean isOpen()
	{
		return status == CellStatus.OPEN;
	}

	public boolean isNotOpened()
	{
		return !isOpen();
	}

	public boolean isClosed()
	{
		return status == CellStatus.CLOSED;
	}

	public boolean isNotClosed()
	{
		return !isClosed();
	}

	public boolean isFlagged()
	{
		return status == CellStatus.FLAGGED;
	}

	public boolean isMarked()
	{
		return status == CellStatus.MARKED;
	}

}
