package computergraphics.framework.scenegraph.nodes.composites;

import computergraphics.framework.math.Vector;
import computergraphics.framework.scenegraph.nodes.InnerNode;
import computergraphics.framework.scenegraph.nodes.primitives.RectangleNode;
import computergraphics.framework.scenegraph.nodes.transformation.TranslationNode;

public class SkyscraperNode extends InnerNode {
	private static final double WINDOW_SIZE = 0.1;
	private static final double WINDOW_SPACING = WINDOW_SIZE / 2;
	private static final double CHIMNEY_SIZE = 0.1;
	
	private int _etagen;
	private int _fensterInDerBreite;
	private int _fensterInDerLaenge;
	private boolean _schornstein;
	
	public SkyscraperNode(int fensterInDerBreite, int fensterInDerLaenge, int etagen, boolean schornstein) {
		if(etagen <= 0) {
			throw new IllegalArgumentException("Das Hochhaus muss aus mindestens einer Etage bestehen!");
		}
		if(fensterInDerBreite <= 0) {
			throw new IllegalArgumentException("Das Hochhaus muss mindestens ein Fenster in der Breite pro Etage haben!");
		}
		if(fensterInDerLaenge <= 0) {
			throw new IllegalArgumentException("Das Hochhaus muss mindestens ein Fenster in der L�nge pro Etage haben!");
		}
		_etagen = etagen;
		_fensterInDerBreite = fensterInDerBreite;
		_fensterInDerLaenge = fensterInDerLaenge;
		_schornstein = schornstein;
		
		
		
		double skyscraperWidth  = _fensterInDerBreite*WINDOW_SIZE + (_fensterInDerBreite+1)*WINDOW_SPACING;
		double skyscraperLength = _fensterInDerLaenge*WINDOW_SIZE + (_fensterInDerLaenge+1)*WINDOW_SPACING;
		double skyscraperHeight = _etagen*WINDOW_SIZE + (_etagen+1)*WINDOW_SPACING;
		RectangleNode skyscraper = new RectangleNode(skyscraperWidth, skyscraperLength, skyscraperHeight);
		TranslationNode translation = new TranslationNode(new Vector(0, 0, skyscraperHeight / 2));
		translation.addChild(skyscraper);
		
		if(_schornstein) {
			double chimneyHeight = CHIMNEY_SIZE*2;
			RectangleNode chimney = new RectangleNode(CHIMNEY_SIZE, CHIMNEY_SIZE, chimneyHeight);
			TranslationNode chimneyTranslation = new TranslationNode(new Vector(0, 0, chimneyHeight + skyscraperHeight));
			chimneyTranslation.addChild(chimney);
			translation.addChild(chimneyTranslation);
		}
		addChild(translation);
	}
}
