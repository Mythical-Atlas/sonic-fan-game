package main;

import java.awt.event.KeyEvent;

public class MenuScene extends Scene {
	private boolean changingScene = false;
	private float timeToChangeScene = 2;
	
	public MenuScene() {}
	
	@Override
	public void update(float dt) {
		if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {changingScene = true;}
		
		if(changingScene && timeToChangeScene > 0) {
			timeToChangeScene -= dt;
			Window.get().r -= dt * 5.0f;
			Window.get().g -= dt * 5.0f;
			Window.get().b -= dt * 5.0f;
		}
		else if(changingScene) {Window.changeScene(1);}
	}
}
