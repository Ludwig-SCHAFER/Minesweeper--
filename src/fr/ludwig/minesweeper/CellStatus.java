package fr.ludwig.minesweeper;

public enum CellStatus
{
	CLOSED(" "),
	OPEN(""),
	FLAGGED(new String(Character.toChars(128681))) /* unicode for <triangular flag> */,
	MARKED("?"),
	TRIGGERED(new String(Character.toChars(128163))),
	REVEALED(new String(Character.toChars(128163)));

	private String text;

	CellStatus(String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		return text;
	}

	public String getText()
	{
		return text;
	}
}
