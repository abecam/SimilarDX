package com.tgb.game.similardx;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.tgb.game.similardx.Similar2DEmb.CoordXY;
import com.tgb.game.similardx.Similar2DEmb.Zone;

import se.hgo.mmroutils.LogManager;

public class SimilarDX extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	OrthographicCamera camera;
	
	LogManager myLog;

	static final int majorVersion = 3;
	static final int minorVersion = 0;

	private Music music;
    private Sound badSnd;
    private Sound okSnd;
    private Sound fallSnd;
    private Sound bigSnd;
    private Sound endSnd;

	// Position of the "menu" parts, relatives. The menu image in 800*600, so the relative place is divided by 800 on w and 600 on h
	// Sizes
	static final double VERYSMALLLEFT = 20.0 / 800.0;
	static final double VERYSMALLRIGHT = 180.0 / 800.0;
	static final double SIZEUP = 203.0 / 600.0; // Valid for all size choices (on a row)
	static final double SIZEDOWN = 242.0 / 600.0;

	static final double SMALLLEFT = 214.0 / 800.0;
	static final double SMALLRIGHT = 296.0 / 800.0;

	static final double MEDIUMLEFT = 339.0 / 800.0;
	static final double MEDIUMRIGHT = 452.0 / 800.0;

	static final double BIGLEFT = 514.0 / 800.0;
	static final double BIGRIGHT = 566.0 / 800.0;

	static final double VERYBIGLEFT = 615.0 / 800.0;
	static final double VERYBIGRIGHT = 748.0 / 800.0;

	// Nb of colors
	static final double TWOLEFT = 172.0 / 800.0;
	static final double TWORIGHT = 200.0 / 800.0;
	static final double COLORSUP = 370.0 / 600.0; // Valid for all colors choices (on a row)
	static final double COLORSDOWN = 404.0 / 600.0;

	static final double THREELEFT = 242.0 / 800.0;
	static final double THREERIGHT = 267.0 / 800.0;

	static final double FOURLEFT = 313.0 / 800.0;
	static final double FOURRIGHT = 338.0 / 800.0;

	static final double FIVELEFT = 382.0 / 800.0;
	static final double FIVERIGHT = 407.0 / 800.0;

	static final double SIXLEFT = 453.0 / 800.0;
	static final double SIXRIGHT = 474.0 / 800.0;

	static final double SEVENLEFT = 520.0 / 800.0;
	static final double SEVENRIGHT = 545.0 / 800.0;

	static final double EIGHTLEFT = 590.0 / 800.0;
	static final double EIGHTRIGHT = 616.0 / 800.0;

	// New seed
	static final double NSEEDLEFT = 312.0 / 800.0;
	static final double NSEEDRIGHT = 460.0 / 800.0;
	static final double NSEEDUP = 500.0 / 600.0;
	static final double NSEEDDOWN = 534.0 / 600.0;

	// Play
	static final double PLAYLEFT = 298.0 / 800.0;
	static final double PLAYRIGHT = 474.0 / 800.0;
	static final double PLAYUP = 437.0 / 600.0;
	static final double PLAYDOWN = 496.0 / 600.0;

	boolean inMenu = true;
	boolean loading = true;

	int sizeGrid = 2; // Medium

	int nbColors = 4; // How much colors are we playing with

	boolean newSeed = false; // If new seed is selected

	int nbBlocX = 40;
	int nbBlocY = 30;

	//static final int nbBlocX = 240;
	//static final int nbBlocY = 230;

	static final int voidBloc = 100000; // the number of the void bloc

	static final int nbUndos = 5;
	//RotateBehavior myRotator;
	//SelfRotateBehavior mySelfRotator;

	String nameOfOurCountry;
	Integer scoreOfOurCountry;
	Integer posOfOurCountry;

	boolean univOn = false; // Is the universe ON (initialised)

	//UpdateInfos myUpdater;

	boolean endAsked = false;
	boolean frameDrawn = false; // Lock the application (cannot exit) until the frame is painted

	StringBuffer playerName = null;
	int currentPosition = 0; // Position in score.

	private int[][] tabOfBlocs;

	private int[][] undos;
	int posUndo = 0; // Current undo position
	int scoreUndo; // Save the score
	// For deep undos...
	//int bottomUndo=0; // Bottom of the pile of undos

	int score = 0;
	boolean win;

	int xMin, xMax; // Range of the selected zone

	int tempoLoad; // Some temporisation(french ?) for loading

	int intTemp; // temporary integerer for score calculation

	// Some colors
	Color greyColor;
	Color scoreBG;
	Color scoreFG;
	Color greyTans;

	CoordXY blocSelected;

	private File backFolder;

	private File[] allBackgroundsFiles;
	
	Rectangle theLimits;

	class CoordXY
	{
		int x, y;

		/**
		 * @return the x
		 */
		public int getX()
		{
			return x;
		}

		/**
		 * @param x the x to set
		 */
		public void setX(int x)
		{
			this.x = x;
		}

		/**
		 * @return the y
		 */
		public int getY()
		{
			return y;
		}

		/**
		 * @param y the y to set
		 */
		public void setY(int y)
		{
			this.y = y;
		}

		/**
		 * @param x
		 * @param y
		 */
		public CoordXY(int x, int y)
		{
			this.x = x;
			this.y = y;
		}

		public CoordXY()
		{
			;
		}
	}

	class Zone
	{
		java.util.Vector<CoordXY> mesCoords; // My elements
		int nbZone; // The zone number I belong to

		/**
		 * @return the mesCoords
		 */
		public java.util.Vector<CoordXY> getMesCoords()
		{
			return mesCoords;
		}

		/**
		 * @param mesCoords the mesCoords to set
		 */
		public void addCoord(CoordXY oneCoord)
		{
			this.mesCoords.add(oneCoord);
		}

		/**
		 * @return the nbZone
		 */
		public int getNbZone()
		{
			return nbZone;
		}

		/**
		 * @param nbZone the nbZone to set
		 */
		public void setNbZone(int nbZone)
		{
			this.nbZone = nbZone;
		}

		/**
		 * @param mesCoords
		 * @param nbZone
		 */
		public Zone(int nbZone)
		{
			this.mesCoords = new Vector<CoordXY>();
			this.nbZone = nbZone;
		}

	}

	java.util.Vector<CoordXY> zoneSelected;
	java.util.HashMap<Integer, Zone> zones;

	// Selected zone selection, from Lode's Computer Graphics Tutorial, http://student.kuleuven.be/~m0216922/CG/floodfill.html
	java.util.Stack<CoordXY> stackOfCoords;

	//  The scanline floodfill algorithm using our own stack routines, faster
	private synchronized void findSelectedZone(CoordXY startPoint)
	{
		int x = startPoint.getX();
		int y = startPoint.getY();
		int newColor = 100000;
		int oldColor = tabOfBlocs[x][y];
		CoordXY recoveredCoord;
		boolean freeOnY;
		stackOfCoords.clear();
		zoneSelected.clear();

		int y1;
		boolean spanLeft, spanRight;
		xMin = x;
		xMax = x;
		stackOfCoords.push(new CoordXY(x, y));

		while (!stackOfCoords.empty())
		{
			recoveredCoord = stackOfCoords.pop();
			x = recoveredCoord.getX();
			y = recoveredCoord.getY();
			y1 = y;

			freeOnY = true;

			while (freeOnY)
			{
				if (y1 >= 0)
				{
					if (tabOfBlocs[x][y1] == oldColor)
						y1--;
					else
						freeOnY = false;
				}
				else
					freeOnY = false;
			}

			y1++;
			spanLeft = spanRight = false;
			freeOnY = true;
			while (freeOnY)
			{
				if (y1 < nbBlocY)
				{
					if (tabOfBlocs[x][y1] == oldColor)
					{
						tabOfBlocs[x][y1] = newColor;
						zoneSelected.add(new CoordXY(x, y1));
						if (x < xMin)
						{
							xMin = x;
						}
						if (x > xMax)
						{
							xMax = x;
						}
						if (!spanLeft && x > 0 && tabOfBlocs[x - 1][y1] == oldColor)
						{
							stackOfCoords.push(new CoordXY(x - 1, y1));
							spanLeft = true;
						}
						else if (spanLeft && x > 0 && tabOfBlocs[x - 1][y1] != oldColor)
						{
							spanLeft = false;
						}
						if (!spanRight && x < nbBlocX - 1 && tabOfBlocs[x + 1][y1] == oldColor)
						{
							stackOfCoords.push(new CoordXY(x + 1, y1));
							spanRight = true;
						}
						else if (spanRight && x < nbBlocX - 1 && tabOfBlocs[x + 1][y1] != oldColor)
						{
							spanRight = false;
						}
						y1++;
					}
					else
						freeOnY = false;
				}
				else
					freeOnY = false;
			}
		}
		if (zoneSelected.size() >= 2)
		{
			Iterator<CoordXY> i = zoneSelected.iterator();

			// Reset the flooding (the zone is known now)
			// Now remove the zone! (no double click mechanisms...)
			while (i.hasNext())
			{
				CoordXY currentCoord = (CoordXY) i.next();
				//tabOfBlocs[currentCoord.getX()][currentCoord.getY()]=oldColor;
				tabOfBlocs[currentCoord.getX()][currentCoord.getY()] = voidBloc;
			}
		}
		else
		{
			tabOfBlocs[startPoint.getX()][startPoint.getY()] = oldColor;
		}
	}

	private void fallZone(int x)
	{
		// On a column, see the elements falling!
		// voidBloc
		int yTopOfBase = 0;
		int yStartOfNextZone;
		int yWork = nbBlocY - 1;
		int yCopy = 0;
		int yFrom = 0;
		//boolean remainSomething=true;
		boolean inVoid;
		// First look for a void zone (with a "filled zone" up)
		//while (remainSomething)
		{
			inVoid = false;
			//remainSomething=false;
			for (; yWork >= 0; yWork--)
			{
				if (!inVoid && (tabOfBlocs[x][yWork] == voidBloc))
				{
					inVoid = true;
					yTopOfBase = yWork;
				}
				if (inVoid && (tabOfBlocs[x][yWork] != voidBloc))
				{
					//remainSomething=true;
					yStartOfNextZone = yWork;
					yFrom = yStartOfNextZone;
					// Now we copy all the column
					for (yCopy = yTopOfBase; yCopy >= 0; yCopy--)
					{
						if (yFrom >= 0)
							tabOfBlocs[x][yCopy] = tabOfBlocs[x][yFrom];
						else
							tabOfBlocs[x][yCopy] = voidBloc;
						yFrom--;
					}
					// Then we start again to look up
					yWork = yTopOfBase;
					inVoid = false;
				}
			}
		}
	}

	public void checkWinLose()
	{
		boolean notEmpty = false;
		int nbSinglesBlocs = 0;
		for (int xWL = 0; xWL < nbBlocX - 1; xWL++)
		{
			for (int yWL = 0; yWL < nbBlocY; yWL++)
			{
				if (tabOfBlocs[xWL][yWL] != voidBloc)
				{
					if (tabOfBlocs[xWL][yWL] == tabOfBlocs[xWL + 1][yWL])
					{
						notEmpty = true;
						break;
					}
					if (yWL < nbBlocY - 1)
					{
						if (tabOfBlocs[xWL][yWL] == tabOfBlocs[xWL][yWL + 1])
						{
							notEmpty = true;
							break;
						}
					}
					nbSinglesBlocs++;
				}
			}
		}
		if (!notEmpty)
		{
			posUndo = 0;

			if (nbSinglesBlocs == 0) score += 10000 * (nbColors - 1) * (sizeGrid + 1);
			if (nbSinglesBlocs <= 10) score += 1000 * (nbColors - 1) * (sizeGrid + 1);
			endSnd.play();
			// Update GFX ?
			win = true;
			//    		currentPosition=DBSupport.findPos(score, nbColors, sizeGrid)+1;
		}
	}

	public void shiftToLeft(int xStart)
	{
		int xLeft;
		int yWork;

		for (xLeft = xStart; xLeft < nbBlocX; xLeft++)
		{
			for (yWork = 0; yWork < nbBlocY; yWork++)
			{
				if (xLeft == nbBlocX - 1)
					tabOfBlocs[xLeft][yWork] = voidBloc;
				else
					tabOfBlocs[xLeft][yWork] = tabOfBlocs[xLeft + 1][yWork];
			}
		}
	}
	
	DateFormat myDateFormat;

	public String getFormattedTime(long timeToFormat)
	{
		//myDateFormat = new SimpleDateFormat("'Time elapsed' yyyy 'years ' MM'months, 'dd' days, 'H'h'm'm's's'");

		//GregorianCalendar c=new GregorianCalendar();
		//c.setTime(new Date(timeToFormat));
		//String output= myDateFormat.format(new Date(new Date().getTime() - timeToFormat));
		//String output= String.format("Time elapsed: %tT", c);
		long secs, mins, hours, days, months, years;

		//lastTime= timeToFormat;

		long timeDone = new Date().getTime() - timeToFormat;
		secs = timeDone / 1000;
		mins = secs / 60;
		hours = mins / 60;
		days = hours / 24;
		months = days / 31;
		years = months / 12;
		secs -= mins * 60;
		mins -= hours * 60;
		hours -= days * 24;
		days -= months * 31;
		months -= years * 12;

		String output = String.format("Time elapsed %d years, %d months, %d days, %dh%dm%ds", years, months, days, hours, mins, secs);

		return output;
	}

	public void InitAll()
	{
		myLog = new se.hgo.mmroutils.LogManager();
		//myLog = new mmroutils.LogManager();
		myLog.initLog("Similar - Version " + majorVersion + "." + minorVersion);
		myLog.setLevelExc(4); // All exception printed
		myLog.setLevelGen(2); // Important messages printed
		myLog.add2Log(1, "");
		myLog.add2Log(1, "");
		myLog.add2Log(1, "");
		myLog.add2Log(1, "------------------------------------------");

		backFolder = new File("Gfx/Backgrounds/");

		if (backFolder.isDirectory())
		{
			allBackgroundsFiles = backFolder.listFiles();
		}
		else
		{
			myLog.add2Log(0, backFolder + " is not a folder");
		}

		blocSelected = new CoordXY();

		playerName = new StringBuffer();

		nbColors = 4;
		win = false;

		setGrid();

		zoneSelected = new java.util.Vector<CoordXY>();
		zones = new java.util.HashMap<Integer, Zone>();
		stackOfCoords = new java.util.Stack<CoordXY>();
		//System.out.println(stackOfCoords.capacity());
		stackOfCoords.ensureCapacity(160 * 120);
		//System.out.println(stackOfCoords.capacity());
		// Initialize the GUI components

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);

		//used=true;

		myLog.add2Log(1, "Initialised");

		initGraphics();		
		
		initSounds();
		
		return;
	}

	public void setGrid()
	{
		scoreUndo = 0;
		tabOfBlocs = null;
		java.util.Random myRandomGen = new java.util.Random();

		tabOfBlocs = new int[nbBlocX][nbBlocY];
		undos = new int[nbBlocX][nbBlocY];

		for (int x = 0; x < nbBlocX; x++)
		{
			for (int y = 0; y < nbBlocY; y++)
			{
				tabOfBlocs[x][y] = myRandomGen.nextInt(nbColors);
			}
		}
	}

	public void pushBoard()
	{
		scoreUndo = score;
		for (int xWL = 0; xWL < nbBlocX; xWL++)
		{
			for (int yWL = 0; yWL < nbBlocY; yWL++)
			{
				undos[xWL][yWL] = tabOfBlocs[xWL][yWL];
			}
		}
		posUndo = 1;
	}

	public void pullBoard()
	{
		if (posUndo == 1)
		{
			score = scoreUndo;
			for (int xWL = 0; xWL < nbBlocX; xWL++)
			{
				for (int yWL = 0; yWL < nbBlocY; yWL++)
				{
					tabOfBlocs[xWL][yWL] = undos[xWL][yWL];
				}
			}
			// Update Gfx
			posUndo = 0;
		}
	}

	public void clearUndo()
	{
		posUndo = 0;
	}

	public void leftMouseClicked(Vector3 onPos)
	{
		if (!inMenu && !win)
		{
			float stepBlocX = theLimits.width / nbBlocX;
			float stepBlocY = theLimits.height / nbBlocY;
			blocSelected.setX((int )(onPos.x / stepBlocX));
			blocSelected.setY((int )(onPos.y / stepBlocY));
			//if (firstTimeInClick)
			// First a simple selection, but don't accept a 1 bloc zone
			//myLog.add2Log("One block selected in " + blocSelected.getX() + " : " + blocSelected.getY());
			//zoneSelected.clear();
			//zoneSelected.add(blocSelected);
			pushBoard(); // Save for the undo

			if (tabOfBlocs[blocSelected.getX()][blocSelected.getY()] != voidBloc)
				findSelectedZone(blocSelected);
			else
				zoneSelected.clear();

			if (zoneSelected.size() >= 2)
			{

				//System.out.println("Size for big: "+nbBlocX*4/nbColors);
				if (zoneSelected.size() > nbBlocX * 4 / nbColors)
				{
					// Big area selected, points!!!
					bigSnd.play();
				}
				else
					okSnd.play();

				// Score calculation!
				/*for (int iScore = 0; iScore < zoneSelected.size(); iScore++)
					 {
					 score += iScore;
					 }*/
				intTemp = zoneSelected.size() - 1;

				score += intTemp * (intTemp + 1) / 2;

				for (int xToRemove = xMin; xToRemove <= xMax; xToRemove++)
				{
					//myLog.add2Log("Falling in " + xToRemove);
					fallZone(xToRemove);
					fallSnd.play();
					if (tabOfBlocs[xToRemove][nbBlocY - 1] == voidBloc)
					{
						shiftToLeft(xToRemove);
						xToRemove--;
						xMax--;
					}
				}
				zoneSelected.clear();
				checkWinLose();
			}
			else
			{
				badSnd.play();
				zoneSelected.clear();
				clearUndo(); // Save for the undo
			}
		}
		else
		{
			if (!win)
			{
				// Menu!
				double widthD = (double) theLimits.width;
				double heightD = (double) theLimits.height;

				if ((onPos.x >= (int) (VERYSMALLLEFT * widthD)) && (onPos.y >= (int) (SIZEUP * heightD)) && (onPos.x <= (int) (VERYSMALLRIGHT * widthD)) && (onPos.y <= (int) (SIZEDOWN * heightD)))
				{
					sizeGrid = 0;
					nbBlocX = 20;
					nbBlocY = 15;
					setGrid();
				}
				if ((onPos.x >= (int) (SMALLLEFT * widthD)) && (onPos.y >= (int) (SIZEUP * heightD)) && (onPos.x <= (int) (SMALLRIGHT * widthD)) && (onPos.y <= (int) (SIZEDOWN * heightD)))
				{
					sizeGrid = 1;
					nbBlocX = 32;
					nbBlocY = 24;
					setGrid();
				}
				if ((onPos.x >= (int) (MEDIUMLEFT * widthD)) && (onPos.y >= (int) (SIZEUP * heightD)) && (onPos.x <= (int) (MEDIUMRIGHT * widthD)) && (onPos.y <= (int) (SIZEDOWN * heightD)))
				{
					sizeGrid = 2;
					nbBlocX = 40;
					nbBlocY = 30;
					setGrid();
				}
				if ((onPos.x >= (int) (BIGLEFT * widthD)) && (onPos.y >= (int) (SIZEUP * heightD)) && (onPos.x <= (int) (BIGRIGHT * widthD)) && (onPos.y <= (int) (SIZEDOWN * heightD)))
				{
					sizeGrid = 3;
					nbBlocX = 80;
					nbBlocY = 60;
					setGrid();
				}
				if ((onPos.x >= (int) (VERYBIGLEFT * widthD)) && (onPos.y >= (int) (SIZEUP * heightD)) && (onPos.x <= (int) (VERYBIGRIGHT * widthD)) && (onPos.y <= (int) (SIZEDOWN * heightD)))
				{
					sizeGrid = 4;
					nbBlocX = 160;
					nbBlocY = 120;
					setGrid();
				}

				if ((onPos.x >= (int) (TWOLEFT * widthD)) && (onPos.y >= (int) (COLORSUP * heightD)) && (onPos.x <= (int) (TWORIGHT * widthD)) && (onPos.y <= (int) (COLORSDOWN * heightD)))
				{
					nbColors = 2;
					setGrid();
				}

				if ((onPos.x >= (int) (THREELEFT * widthD)) && (onPos.y >= (int) (COLORSUP * heightD)) && (onPos.x <= (int) (THREERIGHT * widthD)) && (onPos.y <= (int) (COLORSDOWN * heightD)))
				{
					nbColors = 3;
					setGrid();
				}

				if ((onPos.x >= (int) (FOURLEFT * widthD)) && (onPos.y >= (int) (COLORSUP * heightD)) && (onPos.x <= (int) (FOURRIGHT * widthD)) && (onPos.y <= (int) (COLORSDOWN * heightD)))
				{
					nbColors = 4;
					setGrid();
				}

				if ((onPos.x >= (int) (FIVELEFT * widthD)) && (onPos.y >= (int) (COLORSUP * heightD)) && (onPos.x <= (int) (FIVERIGHT * widthD)) && (onPos.y <= (int) (COLORSDOWN * heightD)))
				{
					nbColors = 5;
					setGrid();
				}

				if ((onPos.x >= (int) (SIXLEFT * widthD)) && (onPos.y >= (int) (COLORSUP * heightD)) && (onPos.x <= (int) (SIXRIGHT * widthD)) && (onPos.y <= (int) (COLORSDOWN * heightD)))
				{
					nbColors = 6;
					setGrid();
				}

				if ((onPos.x >= (int) (SEVENLEFT * widthD)) && (onPos.y >= (int) (COLORSUP * heightD)) && (onPos.x <= (int) (SEVENRIGHT * widthD)) && (onPos.y <= (int) (COLORSDOWN * heightD)))
				{
					nbColors = 7;
					setGrid();
				}

				if ((onPos.x >= (int) (EIGHTLEFT * widthD)) && (onPos.y >= (int) (COLORSUP * heightD)) && (onPos.x <= (int) (EIGHTRIGHT * widthD)) && (onPos.y <= (int) (COLORSDOWN * heightD)))
				{
					nbColors = 8;
					setGrid();
				}

				if ((onPos.x >= (int) (NSEEDLEFT * widthD)) && (onPos.y >= (int) (NSEEDUP * heightD)) && (onPos.x <= (int) (NSEEDRIGHT * widthD)) && (onPos.y <= (int) (NSEEDDOWN * heightD)))
				{
					newSeed = true;
					setGrid();
					newSeed = false;
				}

				if ((onPos.x >= (int) (PLAYLEFT * widthD)) && (onPos.y >= (int) (PLAYUP * heightD)) && (onPos.x <= (int) (PLAYRIGHT * widthD)) && (onPos.y <= (int) (PLAYDOWN * heightD)))
				{
					pickImageBackground();
					inMenu = false;
				}
			}
			else
			{
				// In win (game over) mode
				/*inMenu=true;
					 score=0;
					 setGrid();
					 win=false;*/
			}
		}
	}
	
	public void rightMouseClicked()
	{
		if (!inMenu && !win) pullBoard();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed()
	{
		if (win)
		{
			//myLog.add2Log(3, "Key pressed: " + e.getKeyChar());
			if (true) // KeyEvent.VK_BACK_SPACE)
			{
				if (playerName.length() > 0)
				{
					playerName.deleteCharAt(playerName.length() - 1);
				}
			}
			if (true) // KeyEvent.VK_ENTER)
			{
				// Append
				if (playerName.length() == 0)
				{
					// No name entered-> Anonymous!
					playerName.append("Anonymous");
				}
				//				DBSupport.addScore(playerName.toString(), score, nbColors, sizeGrid);
				inMenu = true;
				score = 0;
				setGrid();
				win = false;
			}
		}
		else
		{
			if (true) // KeyEvent.VK_BACK_SPACE)
			{
				pullBoard();
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped()
	{
		if (win)
		{
			if (playerName.length() < 20)
			{
				playerName.append("");
			}
		}
	}
	
	BitmapFont font;
	ShaderProgram fontShader;
	/*Font ourInfosFont;
	Font scoresFont;
	Font titleFont;
	Font infosFont;
	Font startingFont;
	 */
	int frameNb = 0;
	int indAnim = 0; // Nb of frame for the small animation
	int indNbAnim = 0; // Indice of current animation

	Image bFlagImage;

	Image bAnimImage;

	Image bSatImage;

	Color brickColors[];

	public String getMyFormattedTime(long timeToFormat)
	{
		long secs, mins, hours, days, months, years;

		long timeDone = new Date().getTime() - timeToFormat;
		secs = timeDone / 1000;
		mins = secs / 60;
		hours = mins / 60;
		days = hours / 24;
		months = days / 31;
		years = months / 12;
		secs -= mins * 60;
		mins -= hours * 60;
		hours -= days * 24;
		days -= months * 31;
		months -= years * 12;
		String output = String.format("Time elapsed %d years, %d months, %d days, %dh%dm%ds", years, months, days, hours, mins, secs);

		return output;
	}

	/* (non-Javadoc)
	 * @see javax.media.j3d.Canvas3D#postRender()
	 */
	public void postRender(Graphics g)
	{
		if (!this.getBounds().isEmpty())
		{
			clear(g);
			
			frameDrawn = false;

			theLimits = this.getBounds();

			/*
			ourInfosFont = new Font("Arial", Font.PLAIN, theLimits.width / 40);
			scoresFont = new Font("Arial", Font.PLAIN, theLimits.width / 19);
			titleFont = new Font("Arial", Font.PLAIN, theLimits.width / 20);
			infosFont = new Font("Arial", Font.PLAIN, theLimits.width / 50);
			startingFont = new Font("Arial", Font.PLAIN, theLimits.width / 10);
			 */
			
			Image myOffScreenImage = this.createImage(theLimits.width, theLimits.height);

			Graphics tempGraphics2D = myOffScreenImage.getGraphics();

			tempGraphics2D.setColor(greyColor);

			if (bSatImage != null) // Draw the flag if it exists, but do not crash elsewhere.
				tempGraphics2D.drawImage(bSatImage, 0, 0, theLimits.width, theLimits.height, Similar2DEmb.this.getParent());

			int stepBlocX = theLimits.width / nbBlocX;
			int stepBlocY = theLimits.height / nbBlocY;
			int xBloc = 0;
			int yBloc = 0;

			for (int x = 0; x < nbBlocX; x++)
			{
				for (int y = 0; y < nbBlocY; y++)
				{
					if (tabOfBlocs[x][y] != voidBloc)
					{
						if ((bAnimImage != null) && (bAnimImage.getHeight(this) > 0))
							tempGraphics2D.drawImage(bAnimImage, xBloc, yBloc, xBloc + stepBlocX, yBloc + stepBlocY, tabOfBlocs[x][y] * 32, 0, 31 + tabOfBlocs[x][y] * 32, 31, Similar2DEmb.this.getParent());
						else
						{
							tempGraphics2D.setColor(brickColors[tabOfBlocs[x][y]]);
							tempGraphics2D.fillRect(xBloc, yBloc, stepBlocX, stepBlocY);
						}
					}
					yBloc += stepBlocY;
				}
				yBloc = 0;
				xBloc += stepBlocX;
			}

			Iterator i = zoneSelected.iterator();
			tempGraphics2D.setColor(Color.black);

			while (i.hasNext())
			{
				CoordXY currentCoord = (CoordXY) i.next();
				tempGraphics2D.drawRect(currentCoord.getX() * stepBlocX, currentCoord.getY() * stepBlocY, stepBlocX, stepBlocY);
			}

			if (inMenu)
			{
				tempGraphics2D.setColor(greyTans);

				tempGraphics2D.fillRect(0, 0, theLimits.width, theLimits.height);

				tempGraphics2D.setColor(brickColors[sizeGrid]);

				double widthD = (double) theLimits.width;
				double heightD = (double) theLimits.height;

				switch (sizeGrid)
				{
					case 0:
						tempGraphics2D.fillRect((int) (VERYSMALLLEFT * widthD), (int) (SIZEUP * heightD), (int) ((VERYSMALLRIGHT - VERYSMALLLEFT) * widthD), (int) ((SIZEDOWN - SIZEUP) * heightD));
					break;

					case 1:
						tempGraphics2D.fillRect((int) (SMALLLEFT * widthD), (int) (SIZEUP * heightD), (int) ((SMALLRIGHT - SMALLLEFT) * theLimits.width), (int) ((SIZEDOWN - SIZEUP) * heightD));
					break;

					case 2:
						tempGraphics2D.fillRect((int) (MEDIUMLEFT * widthD), (int) (SIZEUP * heightD), (int) ((MEDIUMRIGHT - MEDIUMLEFT) * widthD), (int) ((SIZEDOWN - SIZEUP) * heightD));
					break;

					case 3:
						tempGraphics2D.fillRect((int) (BIGLEFT * widthD), (int) (SIZEUP * heightD), (int) ((BIGRIGHT - BIGLEFT) * theLimits.width), (int) ((SIZEDOWN - SIZEUP) * heightD));
					break;

					case 4:
						tempGraphics2D.fillRect((int) (VERYBIGLEFT * widthD), (int) (SIZEUP * heightD), (int) ((VERYBIGRIGHT - VERYBIGLEFT) * widthD), (int) ((SIZEDOWN - SIZEUP) * heightD));
					break;

				}

				tempGraphics2D.setColor(brickColors[nbColors - 2]);

				switch (nbColors)
				{
					case 2:
						tempGraphics2D.fillRect((int) (TWOLEFT * widthD), (int) (COLORSUP * heightD), (int) ((TWORIGHT - TWOLEFT) * widthD), (int) ((COLORSDOWN - COLORSUP) * heightD));
					break;

					case 3:
						tempGraphics2D.fillRect((int) (THREELEFT * widthD), (int) (COLORSUP * heightD), (int) ((THREERIGHT - THREELEFT) * theLimits.width), (int) ((COLORSDOWN - COLORSUP) * heightD));
					break;

					case 4:
						tempGraphics2D.fillRect((int) (FOURLEFT * widthD), (int) (COLORSUP * heightD), (int) ((FOURRIGHT - FOURLEFT) * widthD), (int) ((COLORSDOWN - COLORSUP) * heightD));
					break;

					case 5:
						tempGraphics2D.fillRect((int) (FIVELEFT * widthD), (int) (COLORSUP * heightD), (int) ((FIVERIGHT - FIVELEFT) * theLimits.width), (int) ((COLORSDOWN - COLORSUP) * heightD));
					break;

					case 6:
						tempGraphics2D.fillRect((int) (SIXLEFT * widthD), (int) (COLORSUP * heightD), (int) ((SIXRIGHT - SIXLEFT) * widthD), (int) ((COLORSDOWN - COLORSUP) * heightD));
					break;

					case 7:
						tempGraphics2D.fillRect((int) (SEVENLEFT * widthD), (int) (COLORSUP * heightD), (int) ((SEVENRIGHT - SEVENLEFT) * widthD), (int) ((COLORSDOWN - COLORSUP) * heightD));
					break;

					case 8:
						tempGraphics2D.fillRect((int) (EIGHTLEFT * widthD), (int) (COLORSUP * heightD), (int) ((EIGHTRIGHT - EIGHTLEFT) * widthD), (int) ((COLORSDOWN - COLORSUP) * heightD));
					break;
				}

				tempGraphics2D.setColor(brickColors[1]);

				if (newSeed)
				{
					tempGraphics2D.fillRect((int) (NSEEDLEFT * widthD), (int) (NSEEDUP * heightD), (int) ((NSEEDRIGHT - NSEEDLEFT) * widthD), (int) ((NSEEDDOWN - NSEEDUP) * heightD));
				}

				if (bFlagImage != null) // Draw the flag if it exists, but do not crash elsewhere.
					tempGraphics2D.drawImage(bFlagImage, 0, 0, theLimits.width, theLimits.height, Similar2DEmb.this.getParent());

			}

			if (!inMenu && !win)
			{
				tempGraphics2D.setFont(titleFont);
				tempGraphics2D.setColor(scoreBG);
				tempGraphics2D.drawString("   Score: " + score, theLimits.width / 4 + theLimits.width / 16 + 2, theLimits.height / 16 + 2);
				tempGraphics2D.setFont(titleFont);
				tempGraphics2D.setColor(scoreFG);
				tempGraphics2D.drawString("   Score: " + score, theLimits.width / 4 + theLimits.width / 16, theLimits.height / 16);
				
				batch.setShader(fontShader);
				font.draw(batch, "Hello smooth world!", 10, 10);
				batch.setShader(null);
			}

			if (win)
			{
				tempGraphics2D.setColor(scoreFG);
				tempGraphics2D.setFont(startingFont);
				tempGraphics2D.drawString("Game over", theLimits.width / 2 - theLimits.width / 4, theLimits.height / 2 - theLimits.height / 6);
				tempGraphics2D.setFont(titleFont);
				tempGraphics2D.setColor(scoreBG);
				tempGraphics2D.drawString("   Score: " + score, theLimits.width / 4 + theLimits.width / 16 + 2, theLimits.height / 2 - theLimits.height / 3 + 2);
				tempGraphics2D.drawString("   Position: " + currentPosition, theLimits.width / 4 + theLimits.width / 16 + 2, theLimits.height / 2 + 2);
				tempGraphics2D.setFont(titleFont);
				tempGraphics2D.setColor(scoreFG);
				tempGraphics2D.drawString("   Score: " + score, theLimits.width / 4 + theLimits.width / 16, theLimits.height / 2 - theLimits.height / 3);
				tempGraphics2D.drawString("   Position: " + currentPosition, theLimits.width / 4 + theLimits.width / 16, theLimits.height / 2);

				tempGraphics2D.setColor(scoreFG);
				tempGraphics2D.drawString("Enter your name", theLimits.width / 4 + theLimits.width / 16 + 2, theLimits.height / 2 + theLimits.height / 3);
				tempGraphics2D.drawString("- " + playerName + " -", theLimits.width / 4 + theLimits.width / 16 + 2, theLimits.height / 2 + theLimits.height / 2 - theLimits.height / 12);
			}
			if (loading)
			{
				tempGraphics2D.setColor(greyTans);

				tempGraphics2D.fillRect(0, 0, theLimits.width, theLimits.height);
				tempGraphics2D.setColor(scoreFG);
				tempGraphics2D.setFont(startingFont);
				tempGraphics2D.drawString("Loading...", theLimits.width / 2 - theLimits.width / 4, theLimits.height / 2 - theLimits.height / 6);
			}

			g.drawImage(myOffScreenImage, 0, 0, null);

			frameDrawn = true;
		}
		else
			frameDrawn = true;
	}

	public void initGraphics()
	{
		loading = true;
		
		File flagImageFile = new File("Gfx/MenuPage.png");

		System.out.println("Looking in " + flagImageFile.getAbsolutePath());
		try
		{
			bFlagImage = ImageIO.read(flagImageFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			myLog.add2Log(1, e);
			System.err.println("Error, menuPage not found");
		}
		if (bFlagImage == null)
		{
			myLog.add2Log(1, "MenuPage not found");
			System.err.println("MenuPage not found");
			//System.exit(1);
		}
		///BufferedImage = new BufferedImage(bgImage);

		File satImageFile = new File("Gfx/EarthMap6.jpg");

		try
		{
			bSatImage = ImageIO.read(satImageFile);
		}
		catch (Exception e)
		{
			myLog.add2Log(1, e);
			System.err.println("Earth image not found ");
		}
		if (bSatImage == null)
		{
			myLog.add2Log(1, "Earth image not found ");
			System.err.println("Earth image not found ");
			//System.exit(1);
		}

		File testBlockImage = new File("Gfx/TestBlocs.png");

		try
		{
			bAnimImage = ImageIO.read(testBlockImage);
		}
		catch (Exception e)
		{
			myLog.add2Log(1, e);
			System.err.println("Anim image not found ");
		}
		if (bAnimImage == null)
		{
			myLog.add2Log(1, "Anim image not found ");
			System.err.println("Anim image not found ");
			//System.exit(1);
		}

		brickColors = new Color[8];
		brickColors[0] = new Color(0.9f, 0.78f, 0.39f, 1f);
		brickColors[1] = new Color(0.345f, 0.525f, 0.945f, 1f);
		brickColors[2] = new Color(0.38f, 0.8f, 0.42f, 1f);
		brickColors[3] = new Color(0.968f, 0.207f, 0.2274f, 1f);
		brickColors[4] = new Color(0.39f, 0.39f, 0.78f, 1f);
		brickColors[5] = new Color(0.1176f, 0.1176f, 0.78f, 1f);
		brickColors[6] = new Color(0.9f, 0.39f, 0.78f, 1f);
		brickColors[7] = new Color(0.509f, 0.78f, 0.945f, 1f);

		greyColor = new Color(0.78f, 0.78f, 0.78f, 1f);
		greyTans = new Color(0, 0, 0, 0.78f);
		scoreBG = new Color(0.039f, 0.039f, 0.039f, 1f);
		scoreFG = new Color(0.98f, 0.235f, 0.235f, 1f);

		Texture textureFont = new Texture(Gdx.files.internal("ArialSimilar.png"));
		textureFont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		font = new BitmapFont(Gdx.files.internal("ArialSimilar.fnt"), new TextureRegion(textureFont), false);

		ShaderProgram fontShader = new ShaderProgram(Gdx.files.internal("font.vert"), Gdx.files.internal("font.frag"));
		if (!fontShader.isCompiled()) {
		    Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
		}

		
		loading = false;
		//this.update(this.getGraphics());
	}

	public void pickImageBackground()
	{
		loading = true;

		java.util.Random myRandomGen = new java.util.Random();
		int nbBackgroundImage = myRandomGen.nextInt(allBackgroundsFiles.length);

		File nextBackground = allBackgroundsFiles[nbBackgroundImage];

		try
		{
			bSatImage = ImageIO.read(nextBackground);
		}
		catch (IOException e)
		{
			myLog.add2Log(1, e);
			System.err.println("Exception: backgrounds/BackImage" + nbBackgroundImage + ".jpg image not recovered ");
		}

		//        	java.util.Random myRandomGen= new java.util.Random();
		//        	int nbBackgroundImage= myRandomGen.nextInt(nbBckImages);
		//            satImage = Resources.getResource("Gfx/Backgrounds/BackImage"+nbBackgroundImage+".jpg");
		//          
		//            try
		//            {
		//                bSatImage = theApplet.getImage(satImage);
		//            }
		//            catch (Exception e)
		//            {
		//                myLog.add2Log(1, e);
		//                System.err.println("Exception: backgrounds/BackImage"+nbBackgroundImage+".jpg image not recovered ");
		//            }
		//            if (bSatImage == null)
		//            {
		//                myLog.add2Log(1,"Earth image not found ");
		//                System.err.println("backgrounds/BackImage"+nbBackgroundImage+".jpg image not found ");
		//                //System.exit(1);
		//            }
		//            loading=false;
		loading = false;

		this.update(this.getGraphics());
	}

	public void initSounds()
	{
		loading = true;	

        music = Gdx.audio.newMusic(Gdx.files.internal("music/A_Mission.mp3"));
        
		badSnd = Gdx.audio.newSound(Gdx.files.internal("Sounds/badSnd.wav"));

		okSnd = Gdx.audio.newSound(Gdx.files.internal("Sounds/okSnd.wav"));

		fallSnd = Gdx.audio.newSound(Gdx.files.internal("Sounds/fallSnd.wav"));

		bigSnd = Gdx.audio.newSound(Gdx.files.internal("Sounds/bigSnd.wav"));

		endSnd = Gdx.audio.newSound(Gdx.files.internal("Sounds/endSnd.wav"));

		loading = false;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
		
		if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Vector3 actualPos = camera.unproject(touchPos);
		}
	}
	
	@Override
	public void dispose () {
		myLog.add2Log(1, "Leaving cleanly");
		
		batch.dispose();
		img.dispose();
	}
}
