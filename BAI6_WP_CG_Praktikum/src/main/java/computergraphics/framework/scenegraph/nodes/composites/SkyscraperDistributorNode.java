package computergraphics.framework.scenegraph.nodes.composites;

import java.util.Random;
import java.util.stream.DoubleStream;

import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.InnerNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

public class SkyscraperDistributorNode extends InnerNode {
	private double _area;
	private int _numberOfObjects;
	
	public SkyscraperDistributorNode(double area, int numberOfObjects) {
		_area = area;
		_numberOfObjects = numberOfObjects;
		distributeSkyscrapers();
	}
	
	private void distributeSkyscrapers() {
		DoubleStream stream = new Random().doubles(_numberOfObjects * 2, -_area, _area);
		double[] d = stream.toArray();
		for (int i = 0; i < _numberOfObjects; i++) {
			SkyscraperNode scraper = new SkyscraperNode(2, 1, 4, true);
			TranslationNode scraperTranslation = new TranslationNode(new Vector(d[i], d[d.length-i-1], 0));
			scraperTranslation.addChild(scraper);
			addChild(scraperTranslation);
		}
	}
}
