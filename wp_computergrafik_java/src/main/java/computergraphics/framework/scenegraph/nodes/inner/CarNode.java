package computergraphics.framework.scenegraph.nodes.inner;

import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.leaf.RectangleNode;
import computergraphics.framework.scenegraph.nodes.leaf.SphereNode;

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
		reifen1Translation.addChild(new SphereNode(SIZE/4, 20));
		reifen2Translation.addChild(new SphereNode(SIZE/4, 20));
		reifen3Translation.addChild(new SphereNode(SIZE/4, 20));
		reifen4Translation.addChild(new SphereNode(SIZE/4, 20));
		
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
