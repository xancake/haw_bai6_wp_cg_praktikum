package computergraphics.exercises;

import computergraphics.framework.Scene;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.inner.AnimatedTranslationNode;
import computergraphics.framework.scenegraph.nodes.inner.CarNode;
import computergraphics.framework.scenegraph.nodes.inner.SkyscraperDistributorNode;
import computergraphics.framework.scenegraph.nodes.leaf.FloorNode;

@SuppressWarnings("serial")
public class Excercise_1_2 extends Scene {
	
	private static final double FLOOR_SIZE = 4;

	public Excercise_1_2() {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 5));
		getRoot().setAnimated(true);
		getRoot().getCamera().setEye(new Vector(0, 0, 5));
		getRoot().getCamera().setRef(new Vector(0, 0, 0));
		
		
		SkyscraperDistributorNode skyscrapers = new SkyscraperDistributorNode(FLOOR_SIZE * 0.9, 10);
		getRoot().addChild(skyscrapers);
		
		CarNode car = new CarNode();
		AnimatedTranslationNode animation = new AnimatedTranslationNode(FLOOR_SIZE, new Vector(0, 0, 0));
		animation.addChild(car);
		getRoot().addChild(animation);
		
		FloorNode floor = new FloorNode(FLOOR_SIZE, FLOOR_SIZE);
		getRoot().addChild(floor);
	}

	@Override
	public void keyPressed(int keyCode) {
		// Key pressed event
	}

	@Override
	public void timerTick(int counter) {
		getRoot().timerTick(counter);
	}
	
	public static void main(String[] args) {
		new Excercise_1_2();
	}
}
