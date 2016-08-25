package game;

import java.util.Vector;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Play extends BasicGameState{

	final int NUMBER_OF_CHIPS = 15;
	GameAlgo [][]ga;
	int dice1Result, dice2Result,pcdice1Result;
	int findLast, pcdice2Result, doubleMoves,pcDouble;
	String mouse = "";	
	Vector <Chip> BlackPenaltyChips;
	Vector <Chip> WhitePenaltyChips;
	Image gameBoard, quitMenu,gameMenu,BTU,BTD,STU,STD;
	Image Dice[], GlowingSelection, EndGameMessage;
	Animation dice1, dice2;
	boolean leaving,pcTurn,userTurn,Double,BPenalty,WPenalty,lightYes,lightNo;

	public Play(int state){}

	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		Double = false;
		BPenalty = false;
		WPenalty = false;
		userTurn = true;
		pcTurn = false;
		leaving = false;
		lightNo = false;
		lightYes = false;
		doubleMoves = -1;
		findLast = 6;
		pcDouble = 0;
		gameBoard = new Image("res/gameBoard.png");
		gameMenu = new Image("res/gameMenu.png");
		EndGameMessage = new Image("res/EndGameMessage.png");
		GlowingSelection = new Image("res/GlowingSelection.png");
		Dice = new Image[6];
		BTU = new Image("res/BigTringleHighLightUp.png");
		BTD = new Image("res/BigTringleHighLightDown.png");
		STU = new Image("res/SmallTringleHighLightUp.png");
		STD = new Image("res/SmallTringleHighLightDown.png");
		WhitePenaltyChips = new Vector<Chip>();
		BlackPenaltyChips = new Vector<Chip>();
		Dice[0] = new Image("res/dice1.png");
		Dice[1] = new Image("res/dice2.png");
		Dice[2] = new Image("res/dice3.png");
		Dice[3] = new Image("res/dice4.png");
		Dice[4] = new Image("res/dice5.png");
		Dice[5] = new Image("res/dice6.png");
		dice1 = new Animation(Dice , 300);
		dice2 = new Animation(Dice , 300);
		dice1.setPingPong(true);
		dice2.setPingPong(true);
		dice1Result = -1;
		dice2Result = -1;
		ga = new GameAlgo[2][12];
		initGameMatrix();

	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos = 600 - Mouse.getY();
		mouse = "x = "+xpos+"\ny = "+ypos;

		if(xpos >= 770 && xpos <= 820 && ypos >= 550 && ypos <= 600 
				&& input.isMousePressed(0)) {
			init(gc, sbg);
		}

		
		if(xpos >= 110 && xpos <= 485 && ypos >= 550 && ypos <= 600 
				&& input.isMousePressed(0)) {
			leaving = true;
		}
		
		if(leaving && xpos >= 495 && xpos <= 550 && ypos >= 295 && ypos <= 310){
			lightYes = true;
			
		}else {
			lightYes = false;
		}
		
		if (leaving && xpos >= 640 && xpos <= 680 && ypos >= 295 && ypos <= 315){
			lightNo = true;
			
		}else {
			lightNo = false;
		}

		if(leaving && xpos >= 495 && xpos <= 550 && ypos >= 295 && ypos <= 310
				&& input.isMousePressed(0)){
			init(gc, sbg);
			sbg.enterState(0);
		
		}else if (leaving && xpos >= 640 && xpos <= 680 && ypos >= 295 && ypos <= 315 
				&& input.isMousePressed(0)){
		
			leaving = false;
		}

		if(xpos >= 490 && xpos <= 550 && ypos >= 550 && ypos <= 600 
				&& userTurn && input.isMousePressed(0) ) {
			dice1Result = (int) ( ((Math.random() * 10) % 6) + 1 );
			dice2Result = (int) ( ((Math.random() * 10) % 6) + 1 );
			if(dice1Result == dice2Result) {
				Double = true;
				doubleMoves = 4;
			}
		}	
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		gameBoard.draw(0,0);
		gameMenu.draw(0,550);
		g.drawString(mouse, 10, 550);
		drawingBoard();
		drawDices();
		userGamePlay(gc);
		if(userTurn) {

			if(!pcTurn && dice1Result == 0 && dice2Result == 0) {
				dice1Result = -1;
				dice2Result = -1;
				userTurn = false;
				pcTurn = false;
			}
		}else if(!userTurn) {
			pcdice1Result = (int) ( ((Math.random() * 10) % 6) + 1 );
			pcdice2Result = (int) ( ((Math.random() * 10) % 6) + 1 );
			pcNextMove();
			userTurn = true;
		}
		if(leaving) {
			EndGameMessage.draw(300 , 150);
		}
		if(lightYes) {
			GlowingSelection.draw(475,295);
		}
		if(lightNo) {
			GlowingSelection.draw(615,295);
		}
	}

	/**
	 * Drawing dices on the screen.
	 * And deleting them when user finish with one of them or more.
	 * */
	private void drawDices() {
		if(dice1Result == -1 && dice2Result == -1) {}
		else if(dice1Result == 0 && dice2Result != 0){
			dice2.getImage(dice2Result - 1).draw(650, 555);

		}else if(dice1Result != 0 && dice2Result == 0){
			dice1.getImage(dice1Result - 1).draw(650, 555);

		}else if(dice1Result == 0 && dice2Result == 0) {}
		else {
			dice1.getImage(dice1Result - 1).draw(600, 555);
			dice2.getImage(dice2Result - 1).draw(650, 555);
		}

	}

	/**
	 * This function make the PC next move 
	 * according to the PC dices.
	 * */
	public void pcNextMove() {
		if(pcdice1Result == pcdice2Result) {
			pcDouble = pcdice1Result;
		}
		while((pcdice1Result != 0 || pcdice2Result != 0)) {
			System.out.println("1: "+pcdice1Result+"\t2: "+pcdice2Result);
			if(!BPenalty && !checkPCGame()) {
				System.out.println("1: "+pcdice1Result+"\t2: "+pcdice2Result);

				if((pcdice1Result != 0 || pcdice2Result != 0) && hit())
				{System.out.println("hit ---> 1: "+pcdice1Result+"\t2: "+pcdice2Result);}

				else if((pcdice1Result != 0 || dice2Result != 0) && build())
				{System.out.println("cover ---> 1: "+pcdice1Result+"\t2: "+pcdice2Result);}

				else if((pcdice1Result != 0 || pcdice2Result != 0) && cover())
				{System.out.println("bulid---> 1: "+pcdice1Result+"\t2: "+pcdice2Result);}

				else if((pcdice1Result != 0 || pcdice2Result != 0) && allow())
				{System.out.println("allow --> 1: "+pcdice1Result+"\t2: "+pcdice2Result);}
				else {
					pcdice1Result = 0;
					pcdice2Result = 0;				
				}

			}else if(BPenalty && (pcdice1Result != 0 || dice2Result != 0) && getOut()){
				System.out.println("getOut --> 1: "+pcdice1Result+"\t2: "+pcdice2Result);

			}else if(checkPCGame()) {
				pcFinshing();
			}else {
				pcdice1Result = 0;
				pcdice2Result = 0;
			} 
			if(pcDouble != 0 && pcdice1Result == 0 && pcdice2Result == 0) {
				pcdice1Result = pcDouble;
				pcdice2Result = pcDouble;
				pcDouble = 0;
			}
		}
	}

	/**
	 * Taking care of the user game-play interface
	 * */
	public void userGamePlay(GameContainer gc) {
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos = 600 - Mouse.getY();


		if(!WPenalty && (dice1Result > 0 || dice2Result > 0)) {
			for (int i = 0; i < ga.length && (dice1Result != 0 || dice2Result != 0) ; i++) {
				for (int j = 0; j < ga[0].length && (dice1Result != 0 || dice2Result != 0); j++) {

					if(i == 0 && ga[i][j].chips.size() != 0 && ga[i][j].value > 0) {

						int firstChipX = ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).xpos; 
						int firstChipY = ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).ypos;

						if(xpos >= firstChipX && xpos <= firstChipX + 50 && 
								ypos >= firstChipY && ypos <= firstChipY +250
								&& allClear()
								&& (dice1Result != 0 || dice2Result != 0)
								&& input.isMousePressed(0)) {


							if((j - dice1Result) >= 0 
									&& dice1Result != 0
									&& ga[i][j - dice1Result].value >= -1) {
								ga[i][j - dice1Result].visableT = true;
								ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
							}
							if((j - dice2Result) >= 0 
									&& dice2Result != 0
									&& ga[i][j - dice2Result].value >= -1) {
								ga[i][j - dice2Result].visableT = true;
								ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
							}
							if(j - dice1Result - dice2Result >= 0
									&& ga[i][j - dice1Result - dice2Result].value >= -1
									&& (ga[i][j - dice1Result].value >= -1 || ga[i][j - dice2Result].value >= -1)
									&& (!Double || (Double && doubleMoves >= 2))) {
								ga[i][j - dice1Result - dice2Result].visableT = true;
								ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
							}
							if((j - dice1Result) < 0 
									&& dice1Result != 0
									&& ga[i + 1][Math.abs((j-dice1Result) + 1)].value >= -1) {
								ga[i + 1][Math.abs((j-dice1Result) + 1)].visableT = true;
								ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
							}
							if((j - dice2Result) < 0 
									&& dice2Result != 0
									&& ga[i + 1][Math.abs((j-dice2Result) + 1)].value >= -1) {
								ga[i + 1][Math.abs((j-dice2Result) + 1)].visableT = true;
								ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
							}
							if((j - (dice1Result + dice2Result)) < 0 
									&& ga[i + 1][Math.abs((j - (dice1Result + dice2Result)) + 1)].value >= -1) {
								if( ((j - dice2Result) < 0 
										&& (ga[i + 1][Math.abs((j-dice2Result) + 1)].value >= -1))
										|| ( (j - dice1Result) < 0 
												&& (ga[i + 1][Math.abs((j-dice1Result) + 1)].value >= -1)) 
												|| (((j-dice1Result) >= 0) 
														&& ga[i][j-dice1Result].value >= -1)
														|| (((j-dice2Result) >= 0) 
																&& ga[i][j-dice2Result].value >= -1)
																&& !Double || (Double && doubleMoves >= 2)) {
									ga[i + 1][Math.abs((j - (dice1Result + dice2Result)) + 1)].visableT = true;
									ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
								}
							}
							if(Double){
								if((j - 4*dice1Result) < 0 
										&& doubleMoves == 4
										&& Math.abs((j - 4*dice1Result) + 1) <= 11
										&& ga[i + 1][Math.abs((j - 4*dice1Result) + 1)].value >= -1) {
									if( (((j - dice1Result) < 0 
											&& ga[i + 1][Math.abs((j-dice1Result) + 1)].value >= -1)
											|| ((j - dice1Result) >= 0 
											&& (ga[i][j-dice1Result].value >= -1)))
											&& ( ((j - 2*dice1Result) < 0 
													&& (ga[i + 1][Math.abs((j-2*(dice1Result)) + 1)].value >= -1))
													||((j - 2*dice1Result) >= 0 
													&& (ga[i][j-2*dice1Result].value >= -1)) ) 
													&& ( ((j - 3*dice1Result) < 0 
															&& (ga[i + 1][Math.abs((j-3*(dice1Result)) + 1)].value >= -1))
															|| ((j - 3*dice1Result) >= 0 
															&& (ga[i][j-3*dice1Result].value >= -1))) ) {

										ga[i + 1][Math.abs((j - 4*dice1Result) + 1)].visableT = true;
										ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
									}
								}
								if((j - 4*dice1Result) >= 0 
										&& doubleMoves == 4
										&& ga[i][j - dice1Result].value >= -1 
										&& ga[i][j - 2*dice1Result].value >= -1 
										&& ga[i][j - 3*dice1Result].value >= -1 
										&& ga[i][j - 4*dice1Result].value >= -1) {
									ga[i][j - 4*dice1Result].visableT = true;
									ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
								}

								if((j - 3*dice1Result) < 0 
										&& Math.abs((j-3*dice1Result) + 1) <= 11
										&& doubleMoves >= 3
										&& (((j - dice1Result) >= 0
										&& ga[i][j - dice1Result].value >= -1)
										|| ((j - dice1Result < 0)
												&& ga[i+1][Math.abs((j-dice1Result) + 1)].value >= -1))
												&& (((j - 2*dice1Result) >= 0
												&& ga[i][j - 2*dice1Result].value >= -1)
												|| ((j - 2*dice1Result < 0)
														&& ga[i+1][Math.abs((j-2*dice1Result) + 1)].value >= -1))
														&& ga[i + 1][Math.abs((j-3*dice1Result) + 1)].value >= -1) {
									ga[i + 1][Math.abs(j - 3*(dice1Result) + 1)].visableT = true;
									ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
								}
								if((j - 3*(dice1Result)) >= 0 
										&& doubleMoves >= 3
										&& ga[i][j - dice1Result].value >= -1
										&& ga[i][j - 2*dice1Result].value >= -1
										&& ga[i][j - 3*dice1Result].value >= -1) {
									ga[i][j - 3*dice1Result].visableT = true;
									ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
								}
							}
						}
						/////////////////////////////////////////////////////////////////////////////

						if((j - dice1Result) >= 0
								&& dice1Result != 0
								&& ga[i][j - dice1Result].visableT 
								&& ((Double && doubleMoves >= 1)||(!Double && doubleMoves == -1))
								&& !ga[i][j].chips.isEmpty()
								&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
								&& xpos > ga[i][j - dice1Result].xpos + 5
								&& xpos < ga[i][j - dice1Result].xpos + 45 
								&& ypos > ga[i][j - dice1Result].ypos - 45
								&& ypos < ga[i][j - dice1Result].ypos + 250
								&& input.isMousePressed(0)) {
							moveChip(i, j, i, j - dice1Result);
							clearTriangles();
							if(Double && doubleMoves >= 1) {
								doubleMoves--;
							}else
								dice1Result = 0;
						}
						if((j - dice2Result) >= 0 
								&& dice2Result != 0
								&& ga[i][j - dice2Result].visableT
								&& !ga[i][j].chips.isEmpty()
								&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
								//&& ((Double && doubleMoves >= 1)||(!Double && doubleMoves == -1))
								&& xpos > ga[i][j - dice2Result].xpos + 5
								&& xpos < ga[i][j - dice2Result].xpos + 45 
								&& ypos > ga[i][j - dice2Result].ypos - 45
								&& ypos < ga[i][j - dice2Result].ypos + 250
								&& input.isMousePressed(0)) {
							moveChip(i, j, i, j - dice2Result);
							clearTriangles();
							if(Double && doubleMoves >= 1) {
								doubleMoves--;
							}else
								dice2Result = 0;
						}

						if((j - (dice1Result + dice2Result)) >= 0 
								&& dice1Result != 0
								&& dice2Result != 0
								&& ga[i][(j - (dice1Result + dice2Result))].visableT
								&& !ga[i][j].chips.isEmpty()
								&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
								&& ((Double && doubleMoves >= 2)||(!Double && doubleMoves == -1))
								&& xpos > ga[i][(j - (dice1Result + dice2Result))].xpos +5
								&& xpos < ga[i][(j - (dice1Result + dice2Result))].xpos + 45 
								&& ypos > ga[i][(j - (dice1Result + dice2Result))].ypos - 45
								&& ypos < ga[i][(j - (dice1Result + dice2Result))].ypos + 250
								&& input.isMousePressed(0)) {
							moveChip(i, j, i, (j - (dice1Result + dice2Result)) );	
							clearTriangles();
							if(Double && doubleMoves >= 2) {
								doubleMoves-= 2;
							}else{
								dice1Result = 0;
								dice2Result = 0;
							}
						}

						if((j - dice1Result) < 0
								&& dice1Result != 0
								&& Math.abs((j-dice1Result) + 1) <= 11
								&& ga[i + 1][Math.abs((j-dice1Result) + 1)].visableT
								&& !ga[i][j].chips.isEmpty()
								&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
								&& ((Double && doubleMoves >= 1)||(!Double && doubleMoves == -1))
								&& xpos > ga[i + 1][Math.abs((j-dice1Result) + 1)].xpos + 5
								&& xpos < ga[i + 1][Math.abs((j-dice1Result) + 1)].xpos + 45 
								&& ypos < ga[i + 1][Math.abs((j-dice1Result) + 1)].ypos + 45
								&& ypos > ga[i + 1][Math.abs((j-dice1Result) + 1)].ypos - 250
								&& input.isMousePressed(0)) {
							moveChip(i, j, i+1, Math.abs((j-dice1Result) + 1));	
							clearTriangles();
							if(Double && doubleMoves >= 1) {
								doubleMoves--;
							}else
								dice1Result = 0;
						}
						if((j - dice2Result) < 0 
								&& dice2Result != 0
								&& ga[i + 1][Math.abs((j-dice2Result) + 1)].visableT
								&& !ga[i][j].chips.isEmpty()
								&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
								&& ((Double && doubleMoves >= 1)||(!Double && doubleMoves == -1))
								&& xpos > ga[i + 1][Math.abs((j-dice2Result) + 1)].xpos + 5
								&& xpos < ga[i + 1][Math.abs((j-dice2Result) + 1)].xpos + 45 
								&& ypos < ga[i + 1][Math.abs((j-dice2Result) + 1)].ypos + 45
								&& ypos > ga[i + 1][Math.abs((j-dice2Result) + 1)].ypos - 250
								&& input.isMousePressed(0)) {
							moveChip(i, j, i+1, Math.abs((j-dice2Result) + 1));	
							clearTriangles();
							if(Double && doubleMoves >= 1) {
								doubleMoves--;
							}else
								dice2Result = 0;
						}
						if((j - (dice1Result + dice2Result)) < 0 
								&& dice1Result != 0
								&& dice1Result != 0
								&& ga[i][j].chips.size() != 0
								&& !ga[i][j].chips.isEmpty()
								&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
								&& ga[i + 1][Math.abs((j - (dice1Result + dice2Result)) + 1)].visableT
								&& ((Double && doubleMoves >= 2)||(!Double && doubleMoves == -1))
								&& xpos > ga[i + 1][Math.abs((j - (dice1Result + dice2Result)) + 1)].xpos + 5
								&& xpos < ga[i + 1][Math.abs((j - (dice1Result + dice2Result)) + 1)].xpos + 45 
								&& ypos < ga[i + 1][Math.abs((j - (dice1Result + dice2Result)) + 1)].ypos + 45
								&& ypos > ga[i + 1][Math.abs((j - (dice1Result + dice2Result)) + 1)].ypos - 250
								&& input.isMousePressed(0)) {
							moveChip(i, j, i+1, Math.abs((j - (dice1Result + dice2Result)) + 1));	
							clearTriangles();
							if(Double) {
								doubleMoves -= 2;
							}else{
								dice1Result = 0;
								dice2Result = 0;
							}
						}

						if(Double) {
							if((j - dice2Result) >= 0 
									&& dice2Result != 0
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& ga[i][j - dice2Result].visableT
									&& xpos > ga[i][j - dice2Result].xpos + 5
									&& xpos < ga[i][j - dice2Result].xpos + 45 
									&& ypos > ga[i][j - dice2Result].ypos - 45
									&& ypos < ga[i][j - dice2Result].ypos + 250
									&& doubleMoves >= 1
									&& input.isMousePressed(0)) {
								moveChip(i, j, i, j - dice2Result);
								clearTriangles();
								doubleMoves--;
							}
							if((j - dice1Result) < 0
									&& dice1Result != 0
									&& Math.abs((j-dice1Result) + 1) <= 11
									&& ga[i + 1][Math.abs((j-dice1Result) + 1)].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i + 1][Math.abs((j-dice1Result) + 1)].xpos + 5
									&& xpos < ga[i + 1][Math.abs((j-dice1Result) + 1)].xpos + 45 
									&& ypos < ga[i + 1][Math.abs((j-dice1Result) + 1)].ypos + 45
									&& ypos > ga[i + 1][Math.abs((j-dice1Result) + 1)].ypos - 250
									&& doubleMoves >= 1
									&& input.isMousePressed(0)) {
								moveChip(i, j, i+1, Math.abs(Math.abs((j-2*dice1Result) + 1)));	
								clearTriangles();
								doubleMoves--;
							}
							if((j - 2*dice2Result) >= 0 
									&& dice2Result != 0
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& ga[i][j - 2*dice2Result].visableT
									&& xpos > ga[i][j - 2*dice2Result].xpos + 5
									&& xpos < ga[i][j - 2*dice2Result].xpos + 45 
									&& ypos > ga[i][j - 2*dice2Result].ypos - 45
									&& ypos < ga[i][j - 2*dice2Result].ypos + 250
									&& doubleMoves >= 2
									&& input.isMousePressed(0)) {
								moveChip(i, j, i, j - 2*dice2Result);
								clearTriangles();
								doubleMoves = 2;
							}
							if((j - 2*dice1Result) < 0
									&& dice1Result != 0
									&& Math.abs((j-2*dice1Result) + 1) <= 11
									&& ga[i + 1][Math.abs((j-2*dice1Result) + 1)].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i + 1][Math.abs((j-2*dice1Result) + 1)].xpos + 5
									&& xpos < ga[i + 1][Math.abs((j-2*dice1Result) + 1)].xpos + 45 
									&& ypos < ga[i + 1][Math.abs((j-2*dice1Result) + 1)].ypos + 45
									&& ypos > ga[i + 1][Math.abs((j-2*dice1Result) + 1)].ypos - 250
									&& doubleMoves >= 2
									&& input.isMousePressed(0)) {
								moveChip(i, j, i+1, Math.abs(Math.abs((j-2*dice1Result) + 1)));	
								clearTriangles();
								doubleMoves = 2;
							}
							if((j - 3*dice2Result) >= 0 
									&& dice2Result != 0
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& ga[i][j - 3*dice2Result].visableT
									&& xpos > ga[i][j - 3*dice2Result].xpos + 5
									&& xpos < ga[i][j - 3*dice2Result].xpos + 45 
									&& ypos > ga[i][j - 3*dice2Result].ypos - 45
									&& ypos < ga[i][j - 3*dice2Result].ypos + 250
									&& doubleMoves >= 3
									&& input.isMousePressed(0)) {
								moveChip(i, j, i, j - 3*dice2Result);
								clearTriangles();
								doubleMoves = 1;
							}
							if((j - 3*dice1Result) < 0
									&& dice1Result != 0
									&& Math.abs((j-3*dice1Result) + 1) <= 11
									&& ga[i + 1][Math.abs((j-3*dice1Result) + 1)].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i + 1][Math.abs((j-3*dice1Result) + 1)].xpos + 5
									&& xpos < ga[i + 1][Math.abs((j-3*dice1Result) + 1)].xpos + 45 
									&& ypos < ga[i + 1][Math.abs((j-3*dice1Result) + 1)].ypos + 45
									&& ypos > ga[i + 1][Math.abs((j-3*dice1Result) + 1)].ypos - 250
									&& doubleMoves >= 3
									&& input.isMousePressed(0)) {
								moveChip(i, j, i+1, Math.abs(Math.abs((j-3*dice1Result) + 1)));	
								clearTriangles();
								doubleMoves = 1;
							}

							if((j - 4*dice1Result) >= 0 
									&& dice1Result != 0
									&& dice2Result != 0
									&& ga[i][(j - 4*dice1Result)].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i][(j - 4*dice1Result)].xpos +5
									&& xpos < ga[i][(j - 4*dice1Result)].xpos + 45 
									&& ypos > ga[i][(j - 4*dice1Result)].ypos - 45
									&& ypos < ga[i][(j - 4*dice1Result)].ypos + 250
									&& doubleMoves == 4
									&& input.isMousePressed(0)) {
								moveChip(i, j, i, (j - 4*dice1Result) );	
								clearTriangles();
								doubleMoves = 0;
							}

							if((j - 4*dice1Result) < 0 
									&& dice1Result != 0
									&& dice1Result != 0
									&& Math.abs((j - 4*dice1Result) + 1) <= 11
									&& ga[i + 1][Math.abs((j - 4*dice1Result) + 1)].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i + 1][((Math.abs((j - 4*dice1Result) + 1)))].xpos + 5
									&& xpos < ga[i + 1][((Math.abs((j - 4*dice1Result) + 1)))].xpos + 45 
									&& ypos < ga[i + 1][((Math.abs((j - 4*dice1Result) + 1)))].ypos + 45
									&& ypos > ga[i + 1][((Math.abs((j - 4*dice1Result) + 1)))].ypos - 250
									&& doubleMoves == 4
									&& input.isMousePressed(0)) {
								moveChip(i, j, i+1, Math.abs((j - 4*dice1Result) + 1));	
								clearTriangles();
								doubleMoves = 0;
							}
						}
						if(Double && doubleMoves == 0) {
							doubleMoves = -1;
							Double = false;
							dice1Result = 0;
							dice2Result = 0;
						}
					}					
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					else if(i == 1 && ga[i][j].chips.size() != 0 && ga[i][j].value > 0) {

						int firstChipX = ga[i][j].chips.elementAt(0).xpos; 
						int firstChipY = ga[i][j].ypos - (45 * (ga[i][j].value - 1) );

						if(xpos >= firstChipX + 5 && xpos <= firstChipX + 50 && 
								ypos >= firstChipY && ypos <= firstChipY + 50
								&& input.isMousePressed(0)
								&& allClear()
								&& (dice1Result != 0 || dice2Result != 0)) {

							if((j + dice1Result) <= 11 
									&& dice1Result != 0
									&& ga[i][j + dice1Result].value >= -1) {
								ga[i][j + dice1Result].visableT = true;
								ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
							}
							if((j + dice2Result) <= 11 
									&& dice2Result != 0
									&& ga[i][j + dice2Result].value >= -1) {
								ga[i][j + dice2Result].visableT = true;
								ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
							}
							if((j + dice1Result + dice2Result) <= 11 
									&& dice1Result != 0
									&& dice2Result != 0
									&& ga[i][j + dice1Result + dice2Result].value >= -1
									&& ((ga[i][j + dice1Result].value >= -1 ) 
											|| ga[i][j + dice2Result].value >= -1)) {
								ga[i][j + dice1Result + dice2Result].visableT = true;
								ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
							}
							if(Double) {
								if((j + 4*dice1Result) <= 11 
										&& doubleMoves == 4
										&& ga[i][(j + dice1Result)].value >= -1
										&& ga[i][(j + 2*dice1Result)].value >= -1
										&& ga[i][(j + 3*dice1Result)].value >= -1
										&& ga[i][(j + 4*dice1Result)].value >= -1) {
									ga[i][j + 4*dice1Result].visableT = true;
									ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
								}
								if((j + 3*dice1Result) <= 11
										&& doubleMoves >= 3
										&& ga[i][j + dice1Result].value >= -1
										&& ga[i][j + 2*dice1Result].value >= -1
										&& ga[i][j + 3*dice1Result].value >= -1) {
									ga[i][j + 3*dice1Result].visableT = true;
									ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
								}
								if((j + 2*dice1Result) <= 11 
										&& doubleMoves > 0
										&& ga[i][(j + dice1Result)].value >= -1
										&& ga[i][(j + 2*dice1Result)].value >= -1) {
									ga[i][j + 2*dice1Result].visableT = true;
									ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
								}
								if((j + dice1Result) <= 11 
										&& doubleMoves > 0
										&& ga[i][(j + dice1Result)].value >= -1) {
									ga[i][j + dice1Result].visableT = true;
									ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark = true;
								}
							}							

						}
						if(!Double){
							if((j + dice1Result) <= 11
									&& dice1Result != 0
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& ga[i][j + dice1Result].visableT 
									&& xpos > ga[i][j + dice1Result].xpos + 5
									&& xpos < ga[i][j + dice1Result].xpos + 45 
									&& ypos < ga[i][j + dice1Result].ypos + 45
									&& ypos > ga[i][j + dice1Result].ypos - 250
									&& input.isMousePressed(0)) {
								moveChip(i, j, i, j + dice1Result);
								dice1Result = 0;
								clearTriangles();
							}

							if((j + dice2Result) <= 11 
									&& dice2Result != 0
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j + dice2Result].visableT
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i][j + dice2Result].xpos + 5
									&& xpos < ga[i][j + dice2Result].xpos + 45 
									&& ypos < ga[i][j + dice2Result].ypos + 45
									&& ypos > ga[i][j + dice2Result].ypos - 250
									&& input.isMousePressed(0)) {
								moveChip(i, j, i, j + dice2Result);
								clearTriangles();
								dice2Result = 0;
							}

							if((j + dice1Result + dice2Result) <= 11
									&& dice1Result != 0
									&& dice2Result != 0
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& ga[i][j + dice1Result + dice2Result].visableT
									&& xpos > ga[i][j + dice1Result + dice2Result].xpos + 5
									&& xpos < ga[i][j + dice1Result + dice2Result].xpos + 45 
									&& ypos < ga[i][j + dice1Result + dice2Result].ypos + 45
									&& ypos > ga[i][j + dice1Result + dice2Result].ypos - 250
									&& input.isMousePressed(0)) {
								moveChip(i, j, i, j + dice1Result + dice2Result);	
								clearTriangles();
								dice1Result = 0;
								dice2Result = 0;

							}
							if(checkUserGame()){
								if(j + dice1Result == 12 || j + dice2Result == 12){

									if(dice1Result != 0 
											&& j + dice1Result == 12
											&& ga[1][j].value > 0
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										dice1Result = 0;
										clearTriangles();
										ga[1][j].value--;
										ga[1][j].chips.removeElementAt(0);
									} 

									if(dice2Result != 0 
											&& j + dice2Result == 12
											&& ga[1][j].value > 0
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										dice2Result = 0;
										clearTriangles();
										ga[1][j].value--;
										ga[1][j].chips.removeElementAt(0);
									}

									if(dice2Result != 0 
											&& dice1Result != 0 
											&& j + dice1Result + dice2Result == 12
											&& ga[1][j].value > 0
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										dice1Result = 0;
										dice2Result = 0;
										clearTriangles();
										ga[1][j].value--;
										ga[1][j].chips.removeElementAt(0);
									} 
								}else {
									while(findLast < 12) {
										if(ga[1][findLast].value > 0) {
											break;
										}
										findLast++;
									}
									if(dice1Result != 0 
											&& findLast + dice1Result > 12
											&& ga[1][findLast].value > 0
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										dice1Result = 0;
										clearTriangles();
										ga[1][findLast].value--;
										ga[1][findLast].chips.removeElementAt(0);
									}
									if(dice2Result != 0 
											&& findLast + dice2Result > 12
											&& ga[1][findLast].value > 0
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										dice2Result = 0;
										clearTriangles();
										ga[1][findLast].value--;
										ga[1][findLast].chips.removeElementAt(0);
									}
									findLast = 6;
								}
							}
						}
						else{
							if(checkUserGame()) {
								if(j + dice1Result == 12 || j + dice2Result == 12){
									if(dice1Result != 0 
											&& j + dice1Result == 12
											&& ga[1][j].value > 0
											&& doubleMoves >= 1
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										doubleMoves--;
										clearTriangles();
										ga[1][j].value--;
										ga[1][j].chips.removeElementAt(0);
									} 

									if(dice2Result != 0 
											&& j + dice2Result == 12
											&& ga[1][j].value > 0
											&& doubleMoves >= 1
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										doubleMoves--;
										clearTriangles();
										ga[1][j].value--;
										ga[1][j].chips.removeElementAt(0);
									}

									if(dice2Result != 0 
											&& dice1Result != 0 
											&& j + dice1Result + dice2Result == 12
											&& ga[1][j].value > 0
											&& doubleMoves >= 2
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										doubleMoves-=2;
										clearTriangles();
										ga[1][j].value--;
										ga[1][j].chips.removeElementAt(0);
									} 
								}
								else {
									while(findLast < 12) {
										if(ga[1][findLast].value > 0) {
											break;
										}
										findLast++;
									}
									if(dice1Result != 0 
											&& findLast + dice1Result > 12
											&& ga[1][findLast].value > 0
											&& doubleMoves >= 1
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										doubleMoves--;
										clearTriangles();
										ga[1][findLast].value--;
										ga[1][findLast].chips.removeElementAt(0);
									}
									if(dice2Result != 0 
											&& findLast + dice2Result > 12
											&& ga[1][findLast].value > 0
											&& doubleMoves >= 1
											&& xpos >= firstChipX + 5 
											&& xpos <= firstChipX + 50 
											&& ypos >= firstChipY 
											&& ypos <= firstChipY + 50
											&& input.isMousePressed(1)) {
										doubleMoves--;
										clearTriangles();
										ga[1][findLast].value--;
										ga[1][findLast].chips.removeElementAt(0);
									}
									findLast = 6;
								}
							}


							if((j + dice1Result <= 11)
									&& doubleMoves >= 1
									&& ga[i][j + dice1Result].visableT
									&& ga[i][j + dice1Result].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i][j + dice1Result].xpos + 5
									&& xpos < ga[i][j + dice1Result].xpos + 45
									&& ypos < ga[i][j + dice1Result].ypos + 45
									&& ypos > ga[i][j + dice1Result].ypos - 250
									&& input.isMousePressed(0)) {
								moveChip(i , j , i , j + dice1Result);
								clearTriangles();
								doubleMoves --;
							}
							if((j + 2*dice1Result <= 11)
									&& doubleMoves >= 2
									&& ga[i][j + 2*dice1Result].visableT
									&& ga[i][j + dice1Result].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i][j + 2*dice1Result].xpos + 5
									&& xpos < ga[i][j + 2*dice1Result].xpos + 45
									&& ypos < ga[i][j + 2*dice1Result].ypos + 45
									&& ypos > ga[i][j + 2*dice1Result].ypos - 250
									&& input.isMousePressed(0)) {
								moveChip(i , j , i , j + 2*dice1Result);
								clearTriangles();
								doubleMoves -= 2;
							}

							if((j + 3*dice1Result <= 11)
									&& doubleMoves >= 3
									&& ga[i][j + 3*dice1Result].visableT
									&& ga[i][j + 2*dice1Result].visableT
									&& ga[i][j + dice1Result].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i][j + 3*dice1Result].xpos + 5
									&& xpos < ga[i][j + 3*dice1Result].xpos + 45
									&& ypos < ga[i][j + 3*dice1Result].ypos + 45
									&& ypos > ga[i][j + 3*dice1Result].ypos - 250
									&& input.isMousePressed(0)) {
								moveChip(i , j , i , j + 3*dice1Result);
								clearTriangles();
								doubleMoves -= 3;
							}

							if((j + 4*dice1Result <= 11)
									&& doubleMoves == 4
									&& ga[i][j + 4*dice1Result].visableT
									&& ga[i][j + 3*dice1Result].visableT
									&& ga[i][j + 2*dice1Result].visableT
									&& ga[i][j + dice1Result].visableT
									&& !ga[i][j].chips.isEmpty()
									&& ga[i][j].chips.elementAt(ga[i][j].chips.size()-1).mark == true
									&& xpos > ga[i][j + 4*dice1Result].xpos + 5
									&& xpos < ga[i][j + 4*dice1Result].xpos + 45
									&& ypos < ga[i][j + 4*dice1Result].ypos + 45
									&& ypos > ga[i][j + 4*dice1Result].ypos - 250
									&& input.isMousePressed(0)) {
								moveChip(i, j, i, j + 4*dice1Result);
								clearTriangles();
								doubleMoves = 0;

							}
							if(Double && doubleMoves == 0) {
								doubleMoves = -1;
								Double = false;
								dice1Result = 0;
								dice2Result = 0;
							}
						}
					}

					if(Double && doubleMoves == 0) {
						doubleMoves = -1;
						Double = false;
						dice1Result = 0;
						dice2Result = 0;
					}
					if(input.isMouseButtonDown(1)) {
						clearTriangles();
					}
				}
			}
		} else if(WPenalty && dice1Result > 0 || dice2Result > 0){//User in Penalty mode

			if(dice1Result != 0 && ga[0][12-dice1Result].value < -1
					&& dice2Result != 0 && ga[0][12-dice2Result].value < -1) {
				//printError
				System.out.println("11::: "+dice1Result+"  22:::: "+dice2Result);
				Double = false;
				doubleMoves = -1;
				dice1Result = 0;
				dice2Result = 0;
				return;
			}
			if(dice1Result != 0 && ga[0][12-dice1Result].value >= -1) {
				ga[0][12 - dice1Result].visableT = true;

			}
			if(dice2Result != 0 && ga[0][12-dice2Result].value >= -1) {
				ga[0][12 - dice2Result].visableT = true;

			}
			if(dice1Result != 0 && dice2Result != 0 
					&& ga[0][12 - dice1Result - dice2Result].value >= -1
					&& (ga[0][12-dice2Result].value >= -1 
					||ga[0][12-dice1Result].value >= -1)
					&& WhitePenaltyChips.size() == 1) {
				ga[0][12 - dice1Result - dice2Result].visableT = true;

			}

			if(dice1Result != 0 
					&& ga[0][12-dice1Result].value >= -1
					&& ga[0][12-dice1Result].visableT
					&& xpos > ga[0][12-dice1Result].xpos + 5
					&& xpos < ga[0][12-dice1Result].xpos + 45 
					&& ypos > ga[0][12-dice1Result].ypos - 45
					&& ypos < ga[0][12-dice1Result].ypos + 250
					&& input.isMousePressed(0)) {
				moveChip(0, 12-dice1Result, -1, -1);
				if(!Double){
					dice1Result = 0;
				}
				clearTriangles();
				if(Double)doubleMoves--;
				if(WhitePenaltyChips.size() == 0) {
					WPenalty = false;
				}
			}
			if(dice2Result != 0 
					&& ga[0][12-dice2Result].value >= -1
					&& ga[0][12-dice2Result].visableT
					&& xpos > ga[0][12-dice2Result].xpos + 5
					&& xpos < ga[0][12-dice2Result].xpos + 45 
					&& ypos > ga[0][12-dice2Result].ypos - 45
					&& ypos < ga[0][12-dice2Result].ypos + 250
					&& input.isMousePressed(0)) {
				moveChip(0, 12-dice2Result, -1, -1);
				if(!Double){
					dice2Result = 0;
				}
				clearTriangles();
				if(Double)doubleMoves--;
				if(WhitePenaltyChips.size() == 0) {
					WPenalty = false;
				}
			} 
			if(dice1Result != 0
					&& dice2Result != 0
					&& WhitePenaltyChips.size() == 1
					&& ga[0][12-dice1Result - dice2Result].value >= -1
					&& ga[0][12-dice1Result - dice2Result].visableT
					&& xpos > ga[0][12-dice1Result - dice2Result].xpos + 5
					&& xpos < ga[0][12-dice1Result - dice2Result].xpos + 45 
					&& ypos > ga[0][12-dice1Result - dice2Result].ypos - 45
					&& ypos < ga[0][12-dice1Result - dice2Result].ypos + 250
					&& input.isMousePressed(0)) {
				moveChip(0, 12-dice1Result - dice2Result, -1, -1);
				if(!Double){
					dice1Result = 0;
					dice2Result = 0;
				}
				clearTriangles();
				if(Double)doubleMoves = 2;
				if(WhitePenaltyChips.size() == 0) {
					WPenalty = false;
				}
			}
			if(input.isMousePressed(1)) {
				clearTriangles();
			}
		}  

	}

	/**
	 * Drawing the board with all of his elements like so:
	 * map , chips, triangles 
	 * */
	public void drawingBoard() {
		for(int i = 0; i < ga.length; i++) {
			for(int j = 0; j < ga[0].length; j++) {
				for(int k = 0; k < ga[i][j].chips.size(); k++) {
					int y = 0;
					if(i == 0) {
						y = ga[i][j].chips.elementAt(k).ypos + (45 * k);
					}else {
						y = ga[i][j].chips.elementAt(k).ypos - (45 * k);
					}
					int x = ga[i][j].chips.elementAt(k).xpos;
					ga[i][j].chips.elementAt(k).chip.draw(x, y);
				}
				if(ga[i][j].visableT) {
					if(i == 0){
						ga[i][j].triangle.draw(ga[i][j].xpos,ga[i][j].ypos);
					} else {
						if(j % 2 == 0){
							ga[i][j].triangle.draw(ga[i][j].xpos,280);}
						else{
							ga[i][j].triangle.draw(ga[i][j].xpos,320);}		
					}
				}
			}
		}

		for (int i = 0; i < WhitePenaltyChips.size(); i++) {
			WhitePenaltyChips.elementAt(i).chip.draw(WhitePenaltyChips.elementAt(i).xpos,WhitePenaltyChips.elementAt(i).ypos);
		}

		for (int i = 0; i < BlackPenaltyChips.size(); i++) {
			BlackPenaltyChips.elementAt(i).chip.draw(BlackPenaltyChips.elementAt(i).xpos,BlackPenaltyChips.elementAt(i).ypos);
		}

	}

	/**
	 * Clearing all the triangles on screen 
	 * when the user press the right mouse button  
	 * */
	public void clearTriangles() {
		for (int k = 0; k < ga.length; k++) {
			for (int k2 = 0; k2 < ga[0].length; k2++) {
				ga[k][k2].visableT = false;
				if(ga[k][k2].value != 0)
					ga[k][k2].chips.elementAt(ga[k][k2].chips.size() - 1).mark = false;
			}
		}	
	}

	/**
	 * Initialize game matrix to start the game
	 * this function calls once.
	 * */
	private void initGameMatrix() {
		ga[0][0] = new GameAlgo(5, 30, 20, STU, "WhiteChip");		ga[1][0] = new GameAlgo(-5, 30, 480,BTD, "BlackChip");
		ga[0][1] = new GameAlgo(0, 93, 20 ,BTU);					ga[1][1] = new GameAlgo(0, 93, 480,STD);
		ga[0][2] = new GameAlgo(0, 156, 20 ,STU);					ga[1][2] = new GameAlgo(0, 156, 480,BTD);
		ga[0][3] = new GameAlgo(0, 219, 20, BTU);					ga[1][3] = new GameAlgo(0, 219, 480,STD);
		ga[0][4] = new GameAlgo(-3, 282, 20, STU, "BlackChip");		ga[1][4] = new GameAlgo(3, 282, 480,BTD, "WhiteChip");
		ga[0][5] = new GameAlgo(0, 345, 20 ,BTU);					ga[1][5] = new GameAlgo(0, 345, 480,STD);
		ga[0][6] = new GameAlgo(-5, 450, 20 ,STU, "BlackChip");		ga[1][6] = new GameAlgo(5, 450, 480,BTD, "WhiteChip");
		ga[0][7] = new GameAlgo(0, 513, 20, BTU);					ga[1][7] = new GameAlgo(0, 513, 480,STD);
		ga[0][8] = new GameAlgo(0, 576, 20, STU);					ga[1][8] = new GameAlgo(0, 576, 480,BTD);
		ga[0][9] = new GameAlgo(0, 639, 20, BTU);					ga[1][9] = new GameAlgo(0, 639, 480,STD);
		ga[0][10] = new GameAlgo(0, 702, 20,STU);					ga[1][10] = new GameAlgo(0, 702, 480,BTD);
		ga[0][11] = new GameAlgo(2, 765, 20,BTU, "WhiteChip");		ga[1][11] = new GameAlgo(-2, 765, 480,STD, "BlackChip");
	}

	/**
	 * Moving chip according to their values
	 * i1,j1 -> Chip to move from
	 * i2,j2 -> Moving chip to
	 * Concerning all the above to GameAlgo matrix 'ga' 
	 * */
	public void moveChip(int i1 , int j1 , int i2, int j2) {


		//user get's back to the game from penalty
		if(i2 == -1 && j2 == -1) {
			if(ga[i1][j1].value == -1) {
				Chip c1 = WhitePenaltyChips.get(0);
				Chip c2 = ga[i1][j1].chips.get(0);
				ga[i1][j1].chips.remove(c2);
				WhitePenaltyChips.remove(c1);
				c1.xpos = ga[i1][j1].xpos;
				c1.ypos = ga[i1][j1].ypos;
				ga[i1][j1].chips.add(c1);
				ga[i1][j1].value = 1;
				if(BlackPenaltyChips.size() == 0){
					c2.xpos = 400;
					c2.ypos = 50;
					BlackPenaltyChips.add(c2);
				}else {
					c2.xpos = 400;
					c2.ypos = BlackPenaltyChips.elementAt(0).ypos + 50;
					BlackPenaltyChips.add(c2);	
				}
				BPenalty = true;
				return;

			}else{
				Chip c = WhitePenaltyChips.get(0);
				WhitePenaltyChips.remove(c);
				c.xpos = ga[i1][j1].xpos;
				if(ga[i1][j1].value == 0)
				{c.ypos = ga[i1][j1].ypos;}
				else
				{c.ypos = ga[i1][j1].chips.elementAt(0).ypos;}
				ga[i1][j1].chips.add(c);
				ga[i1][j1].value++;
				return;
			}
		}

		//PC get's back to the from penalty
		if(i2 == -2 && j2 == -2) {
			if(ga[i1][j1].value == 1) {
				Chip c1 = BlackPenaltyChips.get(0);
				Chip c2 = ga[i1][j1].chips.get(0);
				ga[i1][j1].chips.remove(c2);
				BlackPenaltyChips.remove(c1);
				c1.xpos = ga[i1][j1].xpos;
				c1.ypos = ga[i1][j1].ypos;
				ga[i1][j1].chips.add(c1);
				ga[i1][j1].value = -1;
				if(WhitePenaltyChips.size() == 0){
					c2.xpos = 400;
					c2.ypos = 500;
					WhitePenaltyChips.add(c2);
				}else {
					c2.xpos = WhitePenaltyChips.elementAt(WhitePenaltyChips.size()-1).xpos;
					c2.ypos = WhitePenaltyChips.elementAt(WhitePenaltyChips.size()-1).ypos - 50;
					WhitePenaltyChips.add(c2);	
				}
				WPenalty = true;
			}else{
				Chip c = BlackPenaltyChips.get(0);
				BlackPenaltyChips.remove(c);
				c.xpos = ga[i1][j1].xpos;
				if(ga[i1][j1].value == 0)
				{c.ypos = ga[i1][j1].ypos;}
				else
				{c.ypos = ga[i1][j1].chips.elementAt(0).ypos;}
				ga[i1][j1].chips.add(c);
				ga[i1][j1].value--;

			}
			if(BlackPenaltyChips.size() == 0) {
				BPenalty = false;
			}

			return;
		}

		//User move his chip and takes out PC chip 
		if(ga[i1][j1].value > 0 && ga[i2][j2].value == -1){
			Chip c1 = ga[i1][j1].chips.get(ga[i1][j1].chips.size() - 1);
			Chip c2 = ga[i2][j2].chips.get(ga[i2][j2].chips.size() - 1);
			ga[i1][j1].chips.removeElementAt(ga[i1][j1].chips.size() - 1);
			ga[i2][j2].chips.removeElementAt(ga[i2][j2].chips.size() - 1);

			ga[i1][j1].value--;
			ga[i2][j2].value = 1;
			c1.xpos = ga[i2][j2].xpos;
			c1.ypos = ga[i2][j2].ypos;
			ga[i2][j2].chips.add(c1);
			c2.xpos = 400;
			if(BlackPenaltyChips.size() == 0){
				c2.ypos = 50;
			}else{
				c2.ypos = BlackPenaltyChips.elementAt(0).ypos + 75;
			}
			BlackPenaltyChips.add(c2);
			BPenalty = true;
			return;
		}

		//PC move his chip and takes out User chip 
		if(ga[i1][j1].value < 0 && ga[i2][j2].value == 1){
			Chip c1 = ga[i1][j1].chips.get(ga[i1][j1].chips.size() - 1);
			Chip c2 = ga[i2][j2].chips.get(ga[i2][j2].chips.size() - 1);
			ga[i1][j1].chips.removeElementAt(ga[i1][j1].chips.size() - 1);
			ga[i2][j2].chips.removeElementAt(ga[i2][j2].chips.size() - 1);

			ga[i1][j1].value++;
			ga[i2][j2].value = -1;
			c1.xpos = ga[i2][j2].xpos;
			c1.ypos = ga[i2][j2].ypos;
			ga[i2][j2].chips.add(c1);
			if(WhitePenaltyChips.size() == 0){
				c2.xpos = 400;
				c2.ypos = 500;
				WhitePenaltyChips.add(c2);
			}else {
				c2.xpos = WhitePenaltyChips.elementAt(WhitePenaltyChips.size()-1).xpos;
				c2.ypos = WhitePenaltyChips.elementAt(WhitePenaltyChips.size()-1).ypos - 50;
				WhitePenaltyChips.add(c2);	
			}
			WPenalty = true;
			return;
		}

		//PC moving chip
		if(ga[i1][j1].value < 0 && ga[i2][j2].value <= 0){
			Chip c = ga[i1][j1].chips.get(ga[i1][j1].chips.size() - 1);
			ga[i1][j1].chips.removeElementAt(ga[i1][j1].chips.size() - 1);
			ga[i1][j1].value++;

			if(ga[i2][j2].value != 0){
				c.xpos = ga[i2][j2].xpos;
				c.ypos = ga[i2][j2].chips.elementAt(0).ypos;
				ga[i2][j2].chips.add(c);
				ga[i2][j2].value--;

			} else {
				c.xpos = ga[i2][j2].xpos;
				c.ypos = ga[i2][j2].ypos;
				ga[i2][j2].value--;
				ga[i2][j2].chips.add(c);
			}
			return;
		}

		//User moving chip
		if(ga[i1][j1].value > 0 && ga[i2][j2].value >= 0){
			Chip c = ga[i1][j1].chips.get(ga[i1][j1].chips.size() - 1);
			ga[i1][j1].chips.removeElementAt(ga[i1][j1].chips.size() - 1);
			ga[i1][j1].value--;

			if(ga[i2][j2].value != 0){
				c.xpos = ga[i2][j2].xpos;
				c.ypos = ga[i2][j2].chips.elementAt(0).ypos;
				ga[i2][j2].chips.add(c);
				ga[i2][j2].value++;

			} else {
				c.xpos = ga[i2][j2].xpos;
				c.ypos = ga[i2][j2].ypos;
				ga[i2][j2].value++;
				ga[i2][j2].chips.add(c);
			}
			return;
		}



	}

	/**
	 * Clearing all of the triangles in the board
	 * Return true if their is no highlight triangles on the screen
	 * **/
	public boolean allClear() {
		for (int i = 0; i < ga.length; i++) {
			for (int j = 0; j < ga[0].length; j++) {
				if(ga[i][j].visableT) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * Return true if possible to hit user chip
	 * false otherwise 
	 * */
	public boolean hit() {
		for (int i = 0; i < ga.length; i++) {
			for (int j = 0; j < ga[0].length; j++) {

				if(i == 0 && ga[i][j].value < 0) {

					if( (j + pcdice1Result) <= 11 
							&& pcdice1Result != 0
							&& (ga[i][j + pcdice1Result].value == 1)) {
						moveChip(i, j, i, j + pcdice1Result);
						pcdice1Result = 0;
						return true;			
					}

					if( (j + pcdice2Result) <= 11  
							&& pcdice2Result != 0
							&& (ga[i][j + pcdice2Result].value == 1)) {
						moveChip(i, j, i, j + pcdice2Result);
						pcdice2Result = 0;
						return true;				
					}

					if( (j + pcdice1Result + pcdice2Result ) <= 11 
							&& pcdice1Result != 0
							&& pcdice2Result != 0
							&& (ga[i][j + pcdice1Result].value <= 1
							|| ga[i][j + pcdice2Result].value <= 1)
							&& (ga[i][j + pcdice1Result + pcdice2Result].value == 1)) {
						if(ga[i][j + pcdice1Result].value == 1) {
							moveChip(i, j, i, j + pcdice1Result);
							moveChip(i, j + pcdice1Result, i, j + pcdice1Result + pcdice2Result);
							
						}else if(ga[i][j + pcdice2Result].value == 1) {
							moveChip(i, j, i, j + pcdice2Result);
							moveChip(i, j + pcdice2Result, i, j + pcdice1Result + pcdice2Result);
						}else {
							moveChip(i, j, i, j + pcdice1Result + pcdice2Result);
						}
						
						pcdice1Result = 0;
						pcdice2Result = 0;
						return true;
					}
				}

				if(i == 1 && ga[i][j].value < 0) {

					if( (j - pcdice1Result) >= 0 
							&& pcdice1Result != 0
							&& (ga[i][j - pcdice1Result].value == 1)) {
						moveChip(i, j, i, j - pcdice1Result);
						pcdice1Result = 0;
						return true;			
					}

					if( (j - pcdice2Result) >= 0 
							&& pcdice2Result != 0
							&& (ga[i][j - pcdice2Result].value == 1)) {
						moveChip(i, j, i, j - pcdice2Result);
						pcdice2Result = 0;
						return true;				
					}

					if( (j - pcdice1Result - pcdice2Result ) >= 0 
							&& pcdice1Result != 0
							&& pcdice2Result != 0
							&& (ga[i][j - (pcdice1Result + pcdice2Result) ].value == 1)) {
						moveChip(i, j, i, j - pcdice1Result - pcdice2Result);
						
						pcdice1Result = 0;
						pcdice2Result = 0;
						return true;
					}

					if( (j - pcdice1Result) < 0 
							&& pcdice1Result != 0
							&& (ga[i][Math.abs(j - pcdice1Result) - 1].value == 1)) {
						moveChip(i, j, i - 1, Math.abs(j - pcdice1Result) - 1);
						pcdice1Result = 0;
						return true;			
					}

					if( (j - pcdice2Result) < 0 
							&& pcdice2Result != 0
							&& (ga[i][Math.abs(j - pcdice2Result) - 1].value == 1)) {
						moveChip(i, j, i - 1, Math.abs(j - pcdice2Result) - 1);
						pcdice2Result = 0;
						return true;				
					}

					if( (j - pcdice1Result - pcdice2Result ) < 0 
							&& pcdice1Result != 0
							&& pcdice2Result != 0
							&& (ga[i][Math.abs(j - (pcdice1Result + pcdice2Result)) - 1].value == 1)) {
						moveChip(i, j, i - 1, Math.abs(j - pcdice1Result - pcdice2Result)  - 1);
						pcdice1Result = 0;
						pcdice2Result = 0;
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 *
	 * Return true if it's possible to 'cover' single PC chip
	 **/
	public boolean cover() {
		int i = 1;
		for (int j = 11; j >= 0; j--) {
			if(ga[i][j].value == -1) {

				if( j + pcdice1Result <= 11
						&& ga[i][j+pcdice1Result].value == -1
						&& pcdice1Result != 0) {
					moveChip( i , j + pcdice1Result , i , j);
					pcdice1Result = 0;
					return true;
				}

				if( j + pcdice2Result <= 11
						&& ga[i][j+pcdice2Result].value == -1
						&& pcdice2Result != 0) {
					moveChip( i , j + pcdice2Result , i , j);
					pcdice2Result = 0;
					return true;
				}

				if( j + pcdice1Result + pcdice2Result <= 11
						&& ga[i][j+pcdice1Result + pcdice2Result].value == -1
						&& (ga[i][j+pcdice2Result].value <= 0
						||ga[i][j+pcdice1Result].value <= 0 )
						&& pcdice1Result != 0
						&& pcdice2Result != 0) {
					moveChip( i , j + pcdice1Result + pcdice2Result , i , j);
					pcdice1Result = 0;
					pcdice2Result = 0;
					return true;
				}

				if( j - pcdice1Result >= 0
						&& ga[i][j - pcdice1Result].value == -1
						&& pcdice1Result != 0) {
					moveChip( i , j , i , j - pcdice1Result);
					pcdice1Result = 0;
					return true;
				}

				if( j - pcdice2Result >= 0
						&& ga[i][j - pcdice2Result].value == -1
						&& pcdice2Result != 0) {
					moveChip( i , j , i , j - pcdice2Result);
					pcdice2Result = 0;
					return true;
				}

				if( j - pcdice1Result - pcdice2Result >= 0
						&& ga[i][j - pcdice1Result - pcdice2Result].value == -1
						&& pcdice1Result != 0
						&& pcdice2Result != 0) {
					moveChip( i , j, i, j - pcdice1Result - pcdice2Result);
					pcdice1Result = 0;
					pcdice2Result = 0;
					return true;
				}

				if( j - pcdice1Result < 0
						&& ga[i][Math.abs(j - pcdice1Result) - 1].value == -1
						&& pcdice1Result != 0) {
					moveChip( i , j , i - 1 , Math.abs(j - pcdice1Result) - 1);
					pcdice1Result = 0;
					return true;
				}

				if( j - pcdice2Result < 0
						&& ga[i][Math.abs(j - pcdice2Result) - 1].value < 0
						&& pcdice2Result != 0) {
					moveChip( i , j , i - 1 , Math.abs(j - pcdice2Result) - 1);
					pcdice2Result = 0;
					return true;
				}

				if( j - pcdice1Result - pcdice2Result < 0
						&& ga[i][Math.abs(j-pcdice1Result - pcdice2Result) - 1].value < 0
						&& pcdice1Result != 0
						&& pcdice2Result != 0) {
					moveChip( i , j, i - 1, Math.abs(j-pcdice1Result - pcdice2Result) - 1);
					pcdice1Result = 0;
					pcdice2Result = 0;
					return true;
				}
			}
		}

		i=0;
		for (int j = 0; j < 11; j++) {
			if(ga[i][j].value == -1) {

				if( j - pcdice1Result >= 0
						&& ga[i][j - pcdice1Result].value == -1
						&& pcdice1Result != 0) {
					moveChip( i , j - pcdice1Result , i , j);
					pcdice1Result = 0;
					return true;
				}

				if( j - pcdice2Result >= 0
						&& ga[i][j - pcdice2Result].value == -1
						&& pcdice2Result != 0) {
					moveChip( i , j - pcdice2Result , i , j);
					pcdice2Result = 0;
					return true;
				}

				if( j - pcdice1Result - pcdice2Result >= 0
						&& ga[i][j - pcdice1Result - pcdice2Result].value == -1
						&& pcdice1Result != 0
						&& pcdice2Result != 0) {
					moveChip( i , j - pcdice1Result - pcdice2Result , i , j);
					pcdice1Result = 0;
					pcdice2Result = 0;
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * If possible PC build a house with his chips.
	 * Return true if possible,
	 * False otherwise.
	 * */
	public boolean build() {
		int a, b, remainder = Math.abs(pcdice1Result - pcdice2Result);
		if(pcdice1Result > pcdice2Result) {
			a = pcdice1Result;
			b = pcdice2Result;
		}else {
			a = pcdice2Result;
			b = pcdice1Result;
		}

		int i = 1;
		for (int j = 11; j <= 0; j--) {

			if((j - a) >= 0) {
				if(ga[i][j].value < 0 && ga[i][j - remainder].value < 0
						&& ga[i][j - a].value <= 0 && ga[i][j - remainder - b].value <= 0) {
					moveChip(i, j, i, j - a);
					moveChip(i, j - remainder, i, j - remainder - b);
					pcdice1Result = 0;
					pcdice2Result = 0;
					return true;
				}

				else if((j - a) < 0 && (j - remainder ) < 0) {
					if(ga[i][j].value < 0 && ga[i][Math.abs(j - remainder) - 1].value < 0
							&& ga[i][Math.abs(j - a) - 1].value <= 0 && ga[i][Math.abs(j - b - remainder) - 1].value <= 0) {
						moveChip(i, j, i, Math.abs(j - a) - 1);
						moveChip(i, Math.abs(j - remainder) - 1, i, Math.abs(j - remainder - b) - 1);
						pcdice1Result = 0;
						pcdice2Result = 0;
						return true;
					}
				}
				else if((j - a) < 0 && (j - remainder ) >= 0) {
					if(ga[i][j].value < 0 && ga[i][j - remainder].value < 0
							&& ga[i][Math.abs(j - a) - 1].value <= 0 ) {
						moveChip(i, j, i, Math.abs(j - a));
						moveChip(i, Math.abs(j - remainder) - 1, i, Math.abs(j - b) - 1);
						pcdice1Result = 0;
						pcdice2Result = 0;
						return true;
					}
				}
			}
		}

		i = 0;
		for (int j = 0; j <= 11; j++) {
			if((j + a) <= 11 && (j + remainder + b <= 11)) {
				if(ga[i][j].value < 0 && ga[i][j + a].value <= 0
						&& ga[i][j + remainder].value < 0) {
					moveChip(i, j, i, j + a);
					moveChip(i, j + remainder, i, j + remainder + b);
					pcdice1Result = 0;
					pcdice2Result = 0;
					return true;
				}
			}
		}	
		return false;
	}
	/**
	 * If possible PC give the a chance to 'eat' his chip.
	 * Return true if possible,
	 * False otherwise.
	 * */
	public boolean allow() {
		int i, j;

		i = 1;
		for (j = 11; j >= 0; j--) {
			if(ga[i][j].value < 0) {

				if(j - pcdice1Result >= 0
						&& ga[i][j - pcdice1Result].value <= 0
						&& pcdice1Result != 0) {
					moveChip(i, j , i, j - pcdice1Result);
					pcdice1Result = 0;
					return true;
				}

				if(j - pcdice2Result >= 0
						&& ga[i][j - pcdice2Result].value <= 0
						&& pcdice2Result != 0) {
					moveChip(i, j , i, j - pcdice2Result);
					pcdice2Result = 0;
					return true;
				}

				if(j - pcdice1Result < 0
						&& ga[i][Math.abs(j - pcdice1Result) - 1].value <= 0
						&& pcdice1Result != 0) {
					moveChip(i, j , i - 1, Math.abs(j - pcdice1Result) - 1);
					pcdice1Result = 0;
					return true;
				}

				if(j - pcdice2Result < 0
						&& ga[i][Math.abs(j - pcdice2Result) - 1].value <= 0
						&& pcdice2Result != 0) {
					moveChip(i, j , i - 1, Math.abs(j - pcdice2Result) - 1);
					pcdice2Result = 0;
					return true;
				}
			}
		}

		i = 0;
		for (j = 0; j < ga[0].length; j++) {
			if(ga[i][j].value < 0) {
				if(j + pcdice1Result <= 11
						&& ga[i][j + pcdice1Result].value <= 0
						&& pcdice1Result != 0) {
					moveChip(i, j , i, j + pcdice1Result);
					pcdice1Result = 0;
					return true;
				}

				if(j + pcdice2Result <= 11
						&& ga[i][j + pcdice2Result].value <= 0
						&& pcdice2Result != 0) {
					moveChip(i, j , i, j + pcdice2Result);
					pcdice2Result = 0;
					return true;
				}

			}
		}
		return false;
	}
	/**
	 * If possible PC get back to the game after penalty.
	 * Return true if possible,
	 * False otherwise.
	 * */
	public boolean getOut() {
		if(BPenalty && (pcdice1Result > 0 || pcdice2Result > 0)) {
			if(pcdice1Result > 0 && ga[1][12 - pcdice1Result].value <= 1) {
				moveChip(1 , (12 - pcdice1Result), -2 , -2);
				pcdice1Result = 0;
				return true;
			}
			else if(pcdice2Result > 0 && ga[1][12 - pcdice2Result].value <= 1) {
				moveChip(1 , (12 - pcdice2Result), -2 , -2);
				pcdice2Result = 0;
				return true;
			}
			else if( pcdice1Result > 0
					&& pcdice2Result > 0
					&& (ga[1][12 - pcdice1Result].value <= 1||ga[1][12 - pcdice2Result].value <= 1)
					&& ga[1][12 - pcdice1Result - pcdice2Result].value <= 1) {
				moveChip(1 , (12 - pcdice1Result - pcdice2Result), -2 , -2);
				pcdice1Result = 0;
				pcdice2Result = 0;
				return true;
			}
		}
		return false;
	}

	/**
	 * If all of the user chips at his 'home'.
	 * Return true if so,
	 * False otherwise.
	 * */
	public boolean checkUserGame() {
		for (int i = 0; i < ga.length; i++) {
			for (int j = 0; j < ga[0].length; j++) {
				if(WPenalty || (ga[i][j].value > 0 && i == 0)
						|| (ga[i][j].value > 0 && i == 1 && j < 6)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * If all of the PC chips at his 'home'.
	 * Return true if possible,
	 * False otherwise.
	 * */
	public boolean checkPCGame() {
		for (int i = 0; i < ga.length; i++) {
			for (int j = 0; j < ga[0].length; j++) {
				if(BPenalty || (ga[i][j].value < 0 && i == 1)
						|| (ga[i][j].value < 0 && i == 0 && j < 6)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Take care of moving chips from PC 'home' arena 
	 * and try to win the game. 
	 * */
	public void pcFinshing() {
		int biggestDice;
		if(pcdice1Result < pcdice2Result){
			biggestDice = pcdice2Result;
			pcdice2Result = pcdice1Result;
			pcdice1Result = biggestDice;
		}

		for (int j = 6; j < 12; j++) {

			if(pcdice1Result != 0
					&& j == 12 - pcdice1Result
					&& ga[0][j].value < 0) {
				ga[0][j].value++;
				ga[0][j].chips.removeElementAt(0);
				pcdice1Result = 0;
			}
			if(pcdice2Result != 0
					&& j == 12 - pcdice2Result
					&& ga[0][j].value < 0) {
				ga[0][j].value++;
				ga[0][j].chips.removeElementAt(0);
				pcdice2Result = 0;
			}
			if(pcdice1Result != 0
					&& pcdice2Result != 0
					&& j == 12 - pcdice1Result - pcdice2Result
					&& ga[0][j].value < 0) {
				ga[0][j].value++;
				ga[0][j].chips.removeElementAt(0);
				pcdice1Result = 0;
				pcdice2Result = 0;
			}
			if(pcdice1Result != 0
					&& j + pcdice1Result > 11
					&& ga[0][j].value < 0) {
				ga[0][j].value++;
				ga[0][j].chips.removeElementAt(0);
				pcdice1Result = 0;
			}
			if(pcdice2Result != 0
					&& j + pcdice2Result > 11
					&& ga[0][j].value < 0) {
				ga[0][j].value++;
				ga[0][j].chips.removeElementAt(0);
				pcdice2Result = 0;
			}
			if(pcdice1Result != 0
					&& pcdice2Result != 0
					&& j + pcdice1Result + pcdice2Result > 11
					&& ga[0][j].value < 0) {
				ga[0][j].value++;
				ga[0][j].chips.removeElementAt(0);
				pcdice1Result = 0;
				pcdice2Result = 0;
			}

		}
		if(pcdice1Result != 0 || pcdice2Result != 0 ) {
			if(hit()){}
			else if(build()){}
			else if(cover()){}
			else if(allow()){}
			else{
				pcdice1Result = 0;
				pcdice2Result = 0;			
			}
		}

	}

	/**
	 * Returning the correct state ID. 
	 * */
	public int getID() {

		return 1;
	}

}

