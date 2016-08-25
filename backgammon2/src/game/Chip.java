package game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Chip {

	
	int xpos;
	int ypos;
	Image chip;
	boolean mark;
	public Chip(int x, int y, String color) {
		xpos = x;
		ypos = y;
		mark = false;
		try {
			chip = new Image("res/"+color+".png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
}
