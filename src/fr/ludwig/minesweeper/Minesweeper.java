package fr.ludwig.minesweeper;

import java.awt.Color;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

public class Minesweeper extends JFrame
{
	/*
	 * ENTRY POINT
	 */

	public static void main(String... args) throws ExceptionMinesweeperInitializationFailed
	{
		new Minesweeper(16, 16, 40);
	}

	/*
	 * ATTIBUTES
	 */
	private static final long serialVersionUID = 1;

	final boolean DEBUG = false;

	private Cell[][] cells;

	private int width;
	private int height;
	private int traps;

	private GameStatus gameStatus;

	/*
	 * CONSTRUCTOR
	 */

	public Minesweeper(int width, int height, int traps) throws ExceptionMinesweeperInitializationFailed
	{
		this.width = width;
		this.height = height;
		this.traps = traps;

		if ((width <= 0))
		{
			throw new ExceptionMinesweeperInitializationFailed("WIDTH TOO SMALL (" + width + ")");
		}
		else if ((height <= 0))
		{
			throw new ExceptionMinesweeperInitializationFailed("HEIGHT TOO SMALL (" + height + ")");
		}
		else if (traps >= (width * height))
		{
			throw new ExceptionMinesweeperInitializationFailed("TOO MANY TRAPS (" + traps + ") for only " + Long.toString(width * height) + " cells");
		}

		initializeJFrame();

		cells = new Cell[height][width];

		intializeCells();

		setTraps();
		if (DEBUG)
		{
			dispGameInConsole();
		}
		gameStatus = GameStatus.NEW;
		refresh();
	}

	/*
	 * METHODS
	 */

	private void intializeCells()
	{
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				cells[i][j] = new Cell(this, i, j);
			}
		}
	}

	private void dispGameInConsole()
	{
		dispFancyLineInConsole();
		for (Cell[] line : cells)
		{
			for (Cell cell : line)
			{
				System.out.print("|" + cell.toText());
			}
			System.out.println("|");
		}

		dispFancyLineInConsole();
		System.out.println("");
	}

	private void dispFancyLineInConsole()
	{
		for (int i = 0; i < width; i++)
		{
			System.out.print("--");
		}
		System.out.println("-");
	}

	private void setTraps()
	{
		final Random random = new Random();

		int x;
		int y;

		for (int i = 0; i < traps; i++)
		{
			do
			{
				x = random.nextInt(height);
				y = random.nextInt(width);
			}
			while (cells[x][y].isTrapped());

			cells[x][y].markAsTrapped();

			updateAdjacentCellsCounter(x, y);
		}

	}

	private void updateAdjacentCellsCounter(int x, int y)
	{
		int adjacentX;
		int adjacentY;

		for (int j = -1; j < 2; j++)
		{
			for (int i = -1; i < 2; i++)
			{
				adjacentX = x + i;
				adjacentY = y + j;

				if (!cellExists(adjacentX, adjacentY) || cells[adjacentX][adjacentY].isTrapped())
				{
					continue;
				}
				cells[adjacentX][adjacentY].incrementValue();
			}
		}

	}

	private boolean cellExists(int x, int y)
	{
		return ((x >= 0) && (x < height) && (y >= 0) && (y < width));
	}

	private void initializeJFrame()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Minesweeper (" + width + "," + height + ")");

		int winwdowWidth = (width * PIXELS.BUTTON_WIDTH) + ((width - 1) * PIXELS.BUTTON_SPACER) + (PIXELS.HORIZONTAL_PADDING * 2) + (2 * PIXELS.WINDOW_BORDER) + 14;
		int windowHeight = (height * PIXELS.BUTTON_HEIGHT) + ((height - 1) * PIXELS.BUTTON_SPACER) + (PIXELS.VERTICAL_PADDING * 2) + PIXELS.WINDOW_BAR + PIXELS.WINDOW_BORDER + 17;

		setSize(winwdowWidth, windowHeight);

		setLayout(null);

		setVisible(true);
		setResizable(false);

		setLocationRelativeTo(null);
	}

	void refresh()
	{
		final Border border = BorderFactory.createLineBorder(Color.black);

		getContentPane().removeAll();
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int componentX = (j * (PIXELS.BUTTON_HEIGHT + PIXELS.BUTTON_SPACER)) + PIXELS.VERTICAL_PADDING;
				int componentY = (i * (PIXELS.BUTTON_WIDTH + PIXELS.BUTTON_SPACER)) + PIXELS.HORIZONTAL_PADDING;
				add(cells[i][j].toComponent(componentX, componentY, PIXELS.BUTTON_WIDTH, PIXELS.BUTTON_HEIGHT, border));

			}
		}
		repaint();

		if (gameStatus != GameStatus.LOST)
		{
			if (checkVictoryConditions())
			{
				gameStatus = GameStatus.WON;
			}
		}

		if ((gameStatus == GameStatus.WON) || (gameStatus == GameStatus.LOST))
		{
			showPopup();
		}
	}

	void openCell(int x, int y)
	{
		if (gameStatus == GameStatus.NEW)
		{
			gameStatus = GameStatus.PLAYING;
		}

		if (cells[x][y].isClosed())
		{
			cells[x][y].open();
		}

		if (cells[x][y].isTriggered())
		{
			gameStatus = GameStatus.LOST;
			revealAllTraps();
		}
		else if (cells[x][y].isEmpty())
		{
			automaticDiscovery(x, y);
		}

		refresh();
	}

	private void showPopup()
	{
		final String popupTitle = ((gameStatus == GameStatus.WON) ? "Congratulations" : (gameStatus == GameStatus.LOST) ? "Oops" : "ERROR");
		final String popupMessage = ((gameStatus == GameStatus.WON) ? "Start a new game?" : (gameStatus == GameStatus.LOST) ? "Try again" : "ERROR");
		final int popupType = ((gameStatus == GameStatus.WON) ? JOptionPane.INFORMATION_MESSAGE : (gameStatus == GameStatus.LOST) ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE);
		final int popupConfirmation = ((gameStatus == GameStatus.WON) ? JOptionPane.YES_NO_OPTION : (gameStatus == GameStatus.LOST) ? JOptionPane.YES_NO_OPTION : JOptionPane.OK_CANCEL_OPTION);

		final int answer = JOptionPane.showConfirmDialog(this, popupMessage, popupTitle, popupConfirmation, popupType);

		if (answer == JOptionPane.YES_NO_OPTION)
		{
			// TODO : restart
			try
			{
				main();
				dispose();
			}
			catch (ExceptionMinesweeperInitializationFailed e)
			{
			}
		}
		else
		{
			dispose();
		}
	}

	boolean checkVictoryConditions()
	{
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				if ((cells[i][j].isNotTrapped() && cells[i][j].isNotOpened()) || cells[i][j].isMarked())
				{
					return false;
				}
			}
		}
		return true;
	}

	private void automaticDiscovery(int x, int y)
	{
		if (!cellExists(x, y))
		{
			return;
		}

		if (cells[x][y].isFlagged() || cells[x][y].isMarked())
		{
			return;
		}

		if (cells[x][y].isEmpty())
		{
			cells[x][y].open();
			for (int i = -1; i <= 1; i++)
			{
				for (int j = -1; j <= 1; j++)
				{
					int next_x = x + i;
					int next_y = y + j;
					if (cellExists(next_x, next_y) && cells[next_x][next_y].isClosed())
					{
						automaticDiscovery(next_x, next_y);
					}
				}
			}
		}
		else
		{
			cells[x][y].open();
		}
	}

	private void revealAllTraps()
	{
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				if (cells[i][j].isTrapped())
				{
					cells[i][j].reveal();
				}
			}
		}
	}

	void toggleCellStatus(int x, int y)
	{
		cells[x][y].toggleStatus();
	}

	// NOP
	public void nop()
	{
		;
	}
}
