package main;

public class MainScene extends Scene {
	public MainScene() {
		Window.get().r = 1.0f;
		Window.get().g = 1.0f;
		Window.get().b = 1.0f;
	}
	
	public void init() {}
	
	public void update(float dt) {
		System.out.println("" + (1.0f / dt) + " FPS");
	}
}
