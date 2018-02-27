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
		new Minesweeper(9, 9, 5);
	}

	/*
	 * ATTIBUTES
	 */
	private static final long serialVersionUID = 1;

	private transient Cell[][] cells;

	private int cellWidth;
	private int cellHeight;
	private int traps;

	private GameStatus gameStatus;

	/*
	 * CONSTRUCTOR
	 */

	public Minesweeper(int width, int height, int traps) throws ExceptionMinesweeperInitializationFailed
	{
		cellWidth = width;
		cellHeight = height;
		this.traps = traps;

		if ((width <= 0))
		{
			throw new ExceptionMinesweeperInitializationFailed("WIDTH TOO SMALL (" + width + ")");
		} else if ((height <= 0))
		{
			throw new ExceptionMinesweeperInitializationFailed("HEIGHT TOO SMALL (" + height + ")");
		} else if (traps >= (width * height))
		{
			throw new ExceptionMinesweeperInitializationFailed("TOO MANY TRAPS (" + traps + ") for only " + Integer.toString(width * height) + " cells");
		}

		initializeJFrame();

		cells = new Cell[height][width];

		intializeCells();

		setTraps();

		gameStatus = GameStatus.NEW;
		refresh();
	}

	/*
	 * METHODS
	 */

	private void intializeCells()
	{
		for (int i = 0; i < cellHeight; i++)
		{
			for (int j = 0; j < cellWidth; j++)
			{
				cells[i][j] = new Cell(this, i, j);
			}
		}
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
				x = random.nextInt(cellHeight);
				y = random.nextInt(cellWidth);
			} while (cells[x][y].isTrapped());

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
		return ((x >= 0) && (x < cellHeight) && (y >= 0) && (y < cellWidth));
	}

	private void initializeJFrame()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Minesweeper (" + cellWidth + "," + cellHeight + ")");

		int winwdowWidth = (cellWidth * PIXELS.BUTTON_WIDTH) + ((cellWidth - 1) * PIXELS.BUTTON_SPACER) + (PIXELS.HORIZONTAL_PADDING * 2)
				+ (2 * PIXELS.WINDOW_BORDER) + 14;
		int windowHeight = (cellHeight * PIXELS.BUTTON_HEIGHT) + ((cellHeight - 1) * PIXELS.BUTTON_SPACER) + (PIXELS.VERTICAL_PADDING * 2) + PIXELS.WINDOW_BAR
				+ PIXELS.WINDOW_BORDER + 17;

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
		for (int i = 0; i < cellHeight; i++)
		{
			for (int j = 0; j < cellWidth; j++)
			{
				int componentX = (j * (PIXELS.BUTTON_HEIGHT + PIXELS.BUTTON_SPACER)) + PIXELS.VERTICAL_PADDING;
				int componentY = (i * (PIXELS.BUTTON_WIDTH + PIXELS.BUTTON_SPACER)) + PIXELS.HORIZONTAL_PADDING;
				add(cells[i][j].toComponent(componentX, componentY, PIXELS.BUTTON_WIDTH, PIXELS.BUTTON_HEIGHT, border));

			}
		}
		repaint();

		if ((gameStatus != GameStatus.LOST) && checkVictoryConditions())
		{
			gameStatus = GameStatus.WON;
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
		} else if (cells[x][y].isEmpty())
		{
			automaticDiscovery(x, y);
		}

		refresh();
	}

	private void showPopup()
	{
		String popupTitle = null;
		String popupMessage = null;
		int popupType = 0;
		int popupConfirmation = 0;

		switch (gameStatus)
		{
		case WON:
			popupTitle = "Congratulations";
			popupMessage = "Start a new game?";
			popupType = JOptionPane.INFORMATION_MESSAGE;
			popupConfirmation = JOptionPane.YES_NO_OPTION;
			break;
		case LOST:
			popupTitle = "Oops";
			popupMessage = "Try again";
			popupType = JOptionPane.WARNING_MESSAGE;
			popupConfirmation = JOptionPane.YES_NO_OPTION;
			break;
		default:
			break;
		}

		final int answer = JOptionPane.showConfirmDialog(this, popupMessage, popupTitle, popupConfirmation, popupType);

		if (answer == JOptionPane.YES_NO_OPTION)
		{
			try
			{
				main();
				dispose();
			} catch (ExceptionMinesweeperInitializationFailed e)
			{
				return;
			}
		} else
		{
			dispose();
		}
	}

	boolean checkVictoryConditions()
	{
		for (int i = 0; i < cellWidth; i++)
		{
			for (int j = 0; j < cellHeight; j++)
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
					int nextX = x + i;
					int nextY = y + j;
					if (cellExists(nextX, nextY) && cells[nextX][nextY].isClosed())
					{
						automaticDiscovery(nextX, nextY);
					}
				}
			}
		} else
		{
			cells[x][y].open();
		}
	}

	private void revealAllTraps()
	{
		for (int i = 0; i < cellWidth; i++)
		{
			for (int j = 0; j < cellHeight; j++)
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

	void previewAdjacentCells(int x, int y)
	{
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				int nextX = x + i;
				int nextY = y + j;
				if (cellExists(nextX, nextY))
				{
					cells[nextX][nextY].toString();
				}
			}
		}
	}
}
