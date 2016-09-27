package computergraphics.framework.scenegraph.nodes.composites;

import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.InnerNode;
import computergraphics.framework.scenegraph.nodes.animation.AnimatedRotationNode;
import computergraphics.framework.scenegraph.nodes.primitives.RectangleNode;
import computergraphics.framework.scenegraph.nodes.primitives.SphereNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

public class CarNode extends InnerNode {
	private static final double SIZE = 0.1;
	
	public CarNode() {
		RectangleNode karosserie = new RectangleNode(SIZE, SIZE*3, SIZE/2);
		
		RectangleNode kabine = new RectangleNode(SIZE, SIZE*2, SIZE/2);
		TranslationNode kabineTranslation = new TranslationNode(new Vector(0, 0, SIZE/2));
		kabineTranslation.addChild(kabine);
		
		TranslationNode reifen1Translation = new TranslationNode(new Vector(SIZE, SIZE*2, -SIZE/2));
		TranslationNode reifen2Translation = new TranslationNode(new Vector(-SIZE, SIZE*2, -SIZE/2));
		TranslationNode reifen3Translation = new TranslationNode(new Vector(SIZE, -SIZE*2, -SIZE/2));
		TranslationNode reifen4Translation = new TranslationNode(new Vector(-SIZE, -SIZE*2, -SIZE/2));
		AnimatedRotationNode reifen1Animation = new AnimatedRotationNode(new Vector(1, 0, 0), 5);
		AnimatedRotationNode reifen2Animation = new AnimatedRotationNode(new Vector(1, 0, 0), 5);
		AnimatedRotationNode reifen3Animation = new AnimatedRotationNode(new Vector(1, 0, 0), 5);
		AnimatedRotationNode reifen4Animation = new AnimatedRotationNode(new Vector(1, 0, 0), 5);
		reifen1Animation.addChild(new SphereNode(SIZE/4, 20));
		reifen2Animation.addChild(new SphereNode(SIZE/4, 20));
		reifen3Animation.addChild(new SphereNode(SIZE/4, 20));
		reifen4Animation.addChild(new SphereNode(SIZE/4, 20));
		reifen1Translation.addChild(reifen1Animation);
		reifen2Translation.addChild(reifen2Animation);
		reifen3Translation.addChild(reifen3Animation);
		reifen4Translation.addChild(reifen4Animation);
		
		TranslationNode translation = new TranslationNode(new Vector(0, 0, SIZE/2+SIZE/4));
		translation.addChild(karosserie);
		translation.addChild(kabineTranslation);
		translation.addChild(reifen1Translation);
		translation.addChild(reifen2Translation);
		translation.addChild(reifen3Translation);
		translation.addChild(reifen4Translation);
		
		addChild(translation);
	}
}
