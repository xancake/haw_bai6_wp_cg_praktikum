package computergraphics.framework.scenegraph.nodes.composites;

import java.util.Random;
import java.util.stream.DoubleStream;
import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.InnerNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

public class MenschDistributorNode extends InnerNode {
	private double _area;
	private int _numberOfObjects;
	
	public MenschDistributorNode(double area, int numberOfObjects) {
		_area = area;
		_numberOfObjects = numberOfObjects;
		distributeMenschen();
	}
	
	private void distributeMenschen() {
		DoubleStream stream = new Random().doubles(_numberOfObjects * 2, -_area, _area);
		double[] d = stream.toArray();
		for (int i = 0; i < _numberOfObjects; i++) {
			MenschNode mensch = new MenschNode();
			TranslationNode translation = new TranslationNode(new Vector(d[i], d[d.length-i-1], 0));
			translation.addChild(mensch);
			addChild(translation);
		}
	}
}
