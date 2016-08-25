package game;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState{
	
	String mouse = "";	
	Image menuPicture ,playNow ,instructions ,Exit ,dice , GlowLine;
	boolean GlowPlay , GlowInstruction , GlowExit;
	
	public Menu(int state){}

	public int getID() {
		return 0;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		GlowPlay = false;
		GlowInstruction = false;
		GlowExit = false;
		menuPicture = new Image("res/backgammon.png");
		playNow = new Image("res/playNow.png");
		instructions = new Image("res/instructions.png");
		Exit = new Image("res/exit.png");
		dice = new Image("res/dice.png");
		GlowLine = new Image("res/GlowLine.png");
		
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		Input input = gc.getInput();
		int xpos = Mouse.getX();
		int ypos = 600 - Mouse.getY();
		//mouse = "x = "+xpos+"\ny = "+ypos;
		
		//Play Button
		if( (xpos >= 90 && xpos <= 210) && (ypos >= 245 && ypos <= 265) 
				&& input.isMousePressed(0)){
			sbg.enterState(1);
		}
		
		//Lighting up 'Play Button'
		if( (xpos >= 90 && xpos <= 210) && (ypos >= 245 && ypos <= 265)) {
			GlowPlay = true;
		}else {
			GlowPlay = false;
		}
		
		//Lighting up 'Instruction Button'
		if( (xpos >= 75 && xpos <= 220) && (ypos >= 285 && ypos <= 305) ){
			GlowInstruction = true;
		}else {
			
			GlowInstruction = false;
		}
		
		//Lighting up 'Exit Button'
		if ((xpos >= 120 && xpos <= 170) && (ypos >= 310 && ypos <= 330)) {
			 GlowExit = true;
		 }else {
			 
			 GlowExit = false;
		 }
		
		//Instruction Button
		if( (xpos >= 75 && xpos <= 220) && (ypos >= 285 && ypos <= 305) 
				&& input.isMousePressed(0)){
			URI ur;
			try {
				ur = new URI("http://www.bkgm.com/rules.html");
				open(ur);
			}catch (URISyntaxException e) {	e.printStackTrace();}
		}
		
		//Exit Button
		if( (xpos >= 120 && xpos <= 170) && (ypos >= 310 && ypos <= 330) 
				&& input.isMousePressed(0))System.exit(0);
		
	}
	

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		g.drawImage(menuPicture,0,0);
		g.drawImage(playNow,90,245);
		g.drawImage(instructions,75,280);
		g.drawImage(Exit,120,310);
		g.drawImage(dice,100,470);
		g.drawString("B A C K G A M M O N !", 40, 400);
		g.drawString("Written By:\nAvrham Ayela.", 40, 420);
		g.drawString(mouse , 50 , 500);
		
		if(GlowPlay) {
			g.drawImage(GlowLine, 5, 180);
		}
		if(GlowInstruction) {
			g.drawImage(GlowLine, 5, 215);
		}
		if(GlowExit) {
			g.drawImage(GlowLine, 5, 246);
		}
	}
	
	private void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) {  
	    	  e.printStackTrace();
	    	  }
	    } 
	  }
	

}
