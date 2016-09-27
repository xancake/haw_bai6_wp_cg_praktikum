package computergraphics.framework.scenegraph.nodes.composites;

import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.InnerNode;
import computergraphics.framework.scenegraph.nodes.primitives.RectangleNode;
import computergraphics.framework.scenegraph.nodes.primitives.SphereNode;
import computergraphics.framework.scenegraph.nodes.transformation.ScaleNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

public class MenschNode extends InnerNode {
	public MenschNode() {
		SphereNode kopf           = new SphereNode(0.025, 20);
		RectangleNode hals        = new RectangleNode( 0.01,  0.01, 0.02);
		RectangleNode torso       = new RectangleNode( 0.03,  0.02, 0.04);
		RectangleNode linkerArm   = new RectangleNode(0.005, 0.005, 0.05);
		RectangleNode rechterArm  = new RectangleNode(0.005, 0.005, 0.05);
		RectangleNode linkesBein  = new RectangleNode( 0.01,  0.01, 0.05);
		RectangleNode rechtesBein = new RectangleNode( 0.01,  0.01, 0.05);
		
		ScaleNode kopfToOval = new ScaleNode(new Vector(1, 1, 1.2));
		
		TranslationNode kopfTranslation        = new TranslationNode(new Vector( 0,     0, 0.22));
		TranslationNode halsTranslation        = new TranslationNode(new Vector( 0,     0, 0.20));
		TranslationNode torsoTranslation       = new TranslationNode(new Vector( 0,     0, 0.14));
		TranslationNode linkerArmTranslation   = new TranslationNode(new Vector( 0.035, 0, 0.20));
		TranslationNode rechterArmTranslation  = new TranslationNode(new Vector(-0.035, 0, 0.20));
		TranslationNode linkesBeinTranslation  = new TranslationNode(new Vector( 0.018, 0, 0.05));
		TranslationNode rechtesBeinTranslation = new TranslationNode(new Vector(-0.018, 0, 0.05));
		
		kopfToOval.addChild(kopf);
		
		kopfTranslation.addChild(kopfToOval);
		halsTranslation.addChild(hals);
		torsoTranslation.addChild(torso);
		linkerArmTranslation.addChild(linkerArm);
		rechterArmTranslation.addChild(rechterArm);
		linkesBeinTranslation.addChild(linkesBein);
		rechtesBeinTranslation.addChild(rechtesBein);
		
		addChild(kopfTranslation);
		addChild(halsTranslation);
		addChild(torsoTranslation);
		addChild(linkerArmTranslation);
		addChild(rechterArmTranslation);
		addChild(linkesBeinTranslation);
		addChild(rechtesBeinTranslation);
		
		Vector hautfarbe = new Vector(1, 0.85, 0.6, 1);
		kopf.setColor(hautfarbe);
		torso.setColor(new Vector(0.1, 0.65, 1, 1));
		hals.setColor(hautfarbe);
		linkerArm.setColor(hautfarbe);
		rechterArm.setColor(hautfarbe);
		linkesBein.setColor(hautfarbe);
		rechtesBein.setColor(hautfarbe);
	}
}
