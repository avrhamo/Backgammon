package game;

import java.util.Vector;

import org.newdawn.slick.Image;

public class GameAlgo { 
	
	int value;
	int xpos;
	int ypos;
	Vector <Chip> chips;
	Image triangle;
	String chipColor;
	boolean visableT;
	
	public GameAlgo(int _value, int _xpos, int _ypos, Image _triangle, String chipColor) {
		value = _value;
		xpos = _xpos;
		ypos = _ypos;
		triangle = _triangle;
		visableT = false;
		chips = new Vector<Chip>();
		for (int i = 0; i < Math.abs(value); i++) {
			if(value < 0)
				chips.add(new Chip(xpos,ypos,"BlackChip"));
			else
				chips.add(new Chip(xpos,ypos,"WhiteChip"));	
		}
	}
		
	public GameAlgo(int _value, int _xpos, int _ypos, Image _triangle) {
		value = _value;
		xpos = _xpos;
		ypos = _ypos;
		triangle = _triangle;
		chips = new Vector<Chip>();
		visableT = false;
	}
	
	

}
