import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("serial")
public class Quinto extends Applet implements ActionListener, ItemListener
{
	// local color constants
	static final Color black = Color.black;
	static final Color myRed = new Color(255, 60, 49);
	static final Color myYellow = new Color(255, 255, 102);
	static final Color myGreen = new Color(107, 202, 65);
	static final Color white = Color.white;
	static final Color gray = new Color(225, 225, 225);

	// instance variables
	Component header, lower;
	QuintoCanvas boardCanvas;
	Label movesDisplay;

	public void init ()
	// initialize the apple with instance variables
	{   
		header = setHeader();
		boardCanvas = new QuintoCanvas(this, 3, false);
		boardCanvas.addMouseListener(boardCanvas);
		lower = setLower();

		this.setFont(new Font("Verdana", Font.BOLD, 18));
		this.setLayout(new BorderLayout());
		this.setBackground(gray);
		this.add("North", header);
		this.add("Center", boardCanvas);
		this.add("South", lower);
	}  
	public Component setHeader()
	// define the header component
	{
		Panel headerPanel = new Panel();
		headerPanel.setLayout(new FlowLayout());
		headerPanel.setBackground(myRed);
		Label header = new Label("Lights Out!");
		header.setForeground(white);
		header.setFont(new Font("Verdana", Font.BOLD, 36));
		headerPanel.add(header);

		return headerPanel;
	}
	public Component setLower()
	// define the lower panel component
	{
		Panel lowerPanel = new Panel();
		lowerPanel.setLayout(new FlowLayout());
		Label sizeLabel = new Label("Size:");
		Choice sizeMenu = sizeMenu();
		// define the standard game button w/action listener
		Button standButton = new Button("Standard Game");
		standButton.setBackground(myGreen);
		standButton.addActionListener(this);
		// random game button
		Button randButton = new Button("Random Game");
		randButton.setBackground(myGreen);
		randButton.addActionListener(this);
		// move label and initialize move count
		Label movesLabel = new Label("Moves:");
		movesDisplay = new Label("0");
		lowerPanel.add(sizeLabel);    	
		lowerPanel.add(sizeMenu);
		lowerPanel.add(standButton);
		lowerPanel.add(randButton);
		lowerPanel.add(movesLabel);
		lowerPanel.add(movesDisplay);
		return lowerPanel;
	}
	public Choice sizeMenu()
	// define the board size drop-down menu
	{
		Choice sizeMenu = new Choice();
		sizeMenu.add("3x3");
		sizeMenu.add("4x4");
		sizeMenu.add("5x5");
		sizeMenu.add("6x6");
		sizeMenu.addItemListener(this);
		return sizeMenu;
	}

	public void actionPerformed(ActionEvent e)
	// handles standard and random game button presses
	{
		if (e.getSource() instanceof Button) 
		{
			String label = ((Button)e.getSource()).getLabel();
			if (label.equals("Standard Game"))
			{
				boardCanvas.random_init = false;
				newGame(boardCanvas.n);
			}
			if (label.equals("Random Game"))
			{
				boardCanvas.random_init = true;
				newGame(boardCanvas.n);
			}
		}
	}
	public void itemStateChanged(ItemEvent e) 
	// handles drop-down menu changes
	{
		if (e.getSource() instanceof Choice)
		{

			String label = ((Choice)e.getSource()).getSelectedItem();
			if (label.equals("3x3"))
			{
				newGame(3);
			}
			else if (label.equals("4x4"))
			{
				newGame(4);

			}
			else if (label.equals("5x5"))
			{
				newGame(5);
			}
			else if (label.equals("6x6"))
			{
				newGame(6);
			}	
		}
	}
	public void newGame(int n)
	// called when the board size or initial board state is changed
	{
		// resets instance variables
		boardCanvas.n = n;
		boardCanvas.moves = 0;
		movesDisplay.setText("0");
		boardCanvas.board = new boolean [boardCanvas.n][boardCanvas.n];
		if (boardCanvas.random_init)
		{
			boardCanvas.randomBoard();
		}
		else
		{
			boardCanvas.yellowBoard();
		}
		boardCanvas.repaint();
	}
	
	class QuintoCanvas extends Canvas implements MouseListener
	// QuintoCanvas class handles the representation of the game board
	{
		// instance variables
		protected int n;
		protected boolean board [][];
		protected int size;
		protected int buffer = 20;
		protected boolean random_init;
		protected int moves;
		protected Quinto parent;
		
		//constructor
		public QuintoCanvas(Quinto q, int n, boolean random)
		{
			parent = q;
			this.n = n;
			this.random_init= random;
			this.moves = 0;
			this.board = new boolean [n][n];
			this.yellowBoard();
		}

		//instance methods
		public void paint(Graphics g)
		// draws the game board
		{
			for (int rowI = 0; rowI < n; rowI++) 
			{
				for (int colI = 0; colI < n; colI++)
				{
					if (board[rowI][colI])
						g.setColor(myYellow);
					else
						g.setColor(Color.black);
					this.size = (this.getWidth() - (n+1)*buffer)/n;
					int x = (rowI * size) + buffer;
					int y = (colI * size) + buffer;
					g.fillRect(x, y, size, size);
					g.setColor(Color.gray);
					g.drawRect(x, y, size, size);
				}
			}
		}
		public void winPaint(Graphics g)
		// draws the win message
		{
			g.setColor(white);
			g.drawRect(0, 0, this.getWidth(), this.getHeight());
			g.setFont(new Font ("Verdana", Font.BOLD, 48));
			// ensures the string is centered on the canvas
			int stringWidth = g.getFontMetrics().stringWidth("You win!");
			g.drawString("You win!", this.getWidth()/2 - stringWidth/2-buffer,
						 this.getHeight()/2+buffer);
		}
		public void yellowBoard()
		// makes all the tiles of the board yellow (true)
		{
			for (int rowI = 0; rowI<n; rowI++)
			{
				for (int colI = 0; colI<n; colI++)
					board[rowI][colI] = true;		
			}
		}
		public void randomBoard()
		// makes the tiles of the board randomly yellow (true) or black (false)
		{
			// creates a random number generator object
			Random randomGen = new Random();
			for (int rowI = 0; rowI<n; rowI++)
			{
				for (int colI = 0; colI<n; colI++)
				{
					// nextBoolean() has 50/50 chance of being true or false
					board[rowI][colI] = randomGen.nextBoolean();
				}
			}
		}
		public boolean allTrue()
		// returns true if all entries of 2d boolean array are false
		{
			for (int rowI = 0; rowI<n; rowI++)
			{
				for (int colI = 0; colI<n; colI++)
				{
					if (board[rowI][colI])
						return false;
				}
			}
			return true;
		}
		
		public void isWin()
		// if the board is in a winning state, draws win message
		{
			if(this.allTrue())
				this.winPaint(this.getGraphics());

		}
		public void invert(Graphics g, int row, int col)
		// inverts color of tile at row, col, inverts color of adjacent tiles
		// updates moves
		{
			int x1 = (col * size) + buffer;
			int y1 = (row * size) + buffer;
			// (x1, y1) are coordinates of selected tile
			int x0 = ((col-1) * size) + buffer;
			int y0 = ((row-1) * size) + buffer;
			// (x0, y0): tile to "northwest" of selected tile
			int x2 = ((col+1) * size) + buffer;
			int y2 = ((row+1) * size) + buffer;
			// (x2, y2): "southeast" of selected tile
			
			// all tiles in 3x3 grid around selected tile can be accessed
			// using combination of these 3 coordinate pairs
			
			// center cell
			board[row][col] = !board[row][col];
			if (board[row][col])
				g.setColor(myYellow);
			else
				g.setColor(Color.black);
			g.fillRect(x1, y1, size, size);
			g.setColor(Color.gray);
			g.drawRect(x1, y1, size, size);
			// west cell
			if (col-1 >= 0)
			{
				board[row][col-1] = !board[row][col-1];
				if (board[row][col-1])
					g.setColor(myYellow);
				else
					g.setColor(Color.black);

				g.fillRect(x0, y1, size, size);
				g.setColor(Color.gray);
				g.drawRect(x0, y1, size, size);
			}
			// east cell
			if (col+1 < n)
			{
				board[row][col+1] = !board[row][col+1];
				if (board[row][col+1])
					g.setColor(myYellow);
				else
					g.setColor(Color.black);
				g.fillRect(x2, y1, size, size);
				g.setColor(Color.gray);
				g.drawRect(x2, y1, size, size);
			}
			// north cell
			if (row-1 >= 0)
			{
				board[row-1][col] = !board[row-1][col];
				if (board[row-1][col])
					g.setColor(myYellow);
				else
					g.setColor(Color.black);
				g.fillRect(x1, y0, size, size);
				g.setColor(Color.gray);
				g.drawRect(x1, y0, size, size);
			}
			//south cell
			if (row+1 < n)
			{
				board[row+1][col] = !board[row+1][col];
				if (board[row+1][col])
					g.setColor(myYellow);
				else
					g.setColor(Color.black);
				g.fillRect(x1, y2, size, size);
				g.setColor(Color.gray);
				g.drawRect(x1, y2, size, size);
			}
			// increments move count, updates moves display label
			moves++;
			parent.movesDisplay.setText(Integer.toString(moves));
			
		}
		public void mousePressed(MouseEvent event)
		// handle mouse events and calls invert and isWin
		{
			Point p = event.getPoint();

			int x = p.x - buffer;
			int y = p.y - buffer;
			if (x >= 0 && x < n*size &&
				y >= 0 && y < n*size) 
			{
				int col = x / size;
				int row = y / size;
				invert(this.getGraphics(), row, col);
				this.isWin();
			}
		}
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
}