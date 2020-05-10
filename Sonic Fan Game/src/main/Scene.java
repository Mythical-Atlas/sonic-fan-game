package main;

public abstract class Scene {
	protected Camera camera;
	
	public Scene() {}
	
	public abstract void init();
	public abstract void update(float dt);
}
