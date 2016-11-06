package computergraphics.exercises.exercise1;

import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.Shader;
import computergraphics.framework.scenegraph.Scene;
import computergraphics.framework.scenegraph.nodes.INode.RenderMode;
import computergraphics.framework.scenegraph.nodes.animation.AnimatedRotationNode;
import computergraphics.framework.scenegraph.nodes.animation.AnimatedTranslationNode;
import computergraphics.framework.scenegraph.nodes.composites.CarNode;
import computergraphics.framework.scenegraph.nodes.composites.MenschDistributorNode;
import computergraphics.framework.scenegraph.nodes.composites.MenschNode;
import computergraphics.framework.scenegraph.nodes.primitives.FloorNode;
import computergraphics.framework.scenegraph.nodes.primitives.SphereNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

@SuppressWarnings("serial")
public class Excercise_1_2 extends Scene {
	
	private static final double FLOOR_SIZE = 4;

	public Excercise_1_2() {
		super(100, Shader.ShaderMode.PHONG, RenderMode.REGULAR);

		getRoot().setLightPosition(new Vector(1, 1, 5));
		getRoot().setAnimated(true);
		getRoot().getCamera().setEye(new Vector(0, 0, 5));
		getRoot().getCamera().setRef(new Vector(0, 0, 0));
		
		
		// Nur einkommentieren, wenn Sie Hochhäuser haben wollen, die wie auf den Boden gefallenes Eis am Stiel aussehen.
//		SkyscraperDistributorNode skyscrapers = new SkyscraperDistributorNode(FLOOR_SIZE * 0.9, 10);
//		getRoot().addChild(skyscrapers);
		
		MenschDistributorNode tanzendeMenschen = new MenschDistributorNode(FLOOR_SIZE * 0.9, 20);
		AnimatedRotationNode tanzAnimation = new AnimatedRotationNode(new Vector(0, 0, 1), 5);
		tanzAnimation.addChild(tanzendeMenschen);
		getRoot().addChild(tanzAnimation);
		
		MenschDistributorNode springendeMenschen = new MenschDistributorNode(FLOOR_SIZE * 0.9, 20);
		TranslationNode springendeMenschenTranslation = new TranslationNode(new Vector(0, 0, 0.05));
		AnimatedTranslationNode springAnimation = new AnimatedTranslationNode(new Vector(0, 0, 0.05), new Vector(0, 0, 0.05));
		springendeMenschenTranslation.addChild(springendeMenschen);
		springAnimation.addChild(springendeMenschenTranslation);
		getRoot().addChild(springAnimation);
		
		CarNode car = new CarNode();
		AnimatedTranslationNode carAnimation = new AnimatedTranslationNode(new Vector(0, 0.01, 0), new Vector(0, FLOOR_SIZE, 0));
		carAnimation.addChild(car);
		getRoot().addChild(carAnimation);
		
		MenschNode mensch = new MenschNode();
		getRoot().addChild(mensch);
		
		SphereNode discoBall = new SphereNode(0.3, 100);
		discoBall.setColor(new Vector(0.9, 0.9, 0.9, 1));
		TranslationNode discoBallTranslation = new TranslationNode(new Vector(0, 0, 2));
		AnimatedRotationNode discoBallAnimation = new AnimatedRotationNode(new Vector(0, 0, 1), 5);
		discoBallAnimation.addChild(discoBall);
		discoBallTranslation.addChild(discoBallAnimation);
		getRoot().addChild(discoBallTranslation);
		
		
		FloorNode discoFloor = new FloorNode(FLOOR_SIZE, FLOOR_SIZE);
		getRoot().addChild(discoFloor);
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
