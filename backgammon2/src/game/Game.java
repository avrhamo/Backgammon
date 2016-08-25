package game;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;


public class Game extends StateBasedGame {

	public static final String gameName = "B a c k g a m m o n";
	public final int menu = 0;
	public final int play = 1;
	
	

	public Game(String name) {
			super(name);
			this.addState(new Menu(menu));
			this.addState(new Play(play));
		}

	public void initStatesList(GameContainer gc)throws SlickException{
		this.getState(menu).init(gc,this);
		this.getState(play).init(gc,this);
		
		this.enterState(menu);
		}
	
	public static void main(String[] args) {
		
		AppGameContainer appgc;
		try{
			appgc = new AppGameContainer(new Game(gameName));
			appgc.setDisplayMode(848, 600, false);
			appgc.start();
			
		}catch(SlickException e){
			e.printStackTrace();
		}
	}

	

}
