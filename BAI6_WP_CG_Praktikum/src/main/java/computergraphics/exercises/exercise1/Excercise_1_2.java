package computergraphics.exercises.exercise1;

import computergraphics.framework.Scene;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.animation.AnimatedRotationNode;
import computergraphics.framework.scenegraph.nodes.animation.AnimatedTranslationNode;
import computergraphics.framework.scenegraph.nodes.composites.CarNode;
import computergraphics.framework.scenegraph.nodes.composites.MenschDistributorNode;
import computergraphics.framework.scenegraph.nodes.composites.MenschNode;
import computergraphics.framework.scenegraph.nodes.composites.SkyscraperDistributorNode;
import computergraphics.framework.scenegraph.nodes.primitives.FloorNode;

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
		
		MenschDistributorNode menschen = new MenschDistributorNode(FLOOR_SIZE * 0.9, 20);
		AnimatedRotationNode menschenAnimation = new AnimatedRotationNode(new Vector(0, 0, 1), 5);
		menschenAnimation.addChild(menschen);
		getRoot().addChild(menschenAnimation);
		
		CarNode car = new CarNode();
		AnimatedTranslationNode animation = new AnimatedTranslationNode(FLOOR_SIZE, new Vector(0, 0, 0));
		animation.addChild(car);
		getRoot().addChild(animation);
		
		MenschNode mensch = new MenschNode();
		getRoot().addChild(mensch);
		
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
