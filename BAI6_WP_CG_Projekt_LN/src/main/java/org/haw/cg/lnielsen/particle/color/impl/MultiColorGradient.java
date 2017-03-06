package org.haw.cg.lnielsen.particle.color.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.color.AbstractParticleColorChanger;
import org.haw.cg.lnielsen.particle.color.ParticleColorChanger;
import org.haw.cg.lnielsen.util.Numbers;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.CGUtils;

/**
 * Ein {@link ParticleColorChanger} der es ermöglicht Kontrollpunkte auf der Lebensleiste eines Partikels zu setzen und
 * mit einer Farbe zu assoziieren. Dieser {@link ParticleColorChanger} interpoliert die Farbe zwischen den
 * Kontrollpunkten, sodass ein Farbverlauf entsteht.
 */
public class MultiColorGradient extends AbstractParticleColorChanger implements ParticleColorChanger {
	private SortedMap<Double, Vector> _colorMap;
	
	public MultiColorGradient() {
		_colorMap = new TreeMap<>();
	}
	
	public void addControlPoint(double lifetimePercentage, Vector color) {
		Numbers.require(lifetimePercentage).greaterThanOrEqual(0, "Der Lebenszeitwert muss im Wertebereich von 0 bis 1 liegen.");
		Numbers.require(lifetimePercentage).lessThanOrEqual(1, "Der Lebenszeitwert muss im Wertebereich von 0 bis 1 liegen.");
		_colorMap.put(lifetimePercentage, CGUtils.checkColorVector(color));
	}
	
	@Override
	protected void calculateColor(Particle p, Vector color, double alifePercentage) {
		if(_colorMap.size() <= 0) {
			return;
		}
		
		// Die zwei Kontrollpunkte finden, die um alifePercentage herum liegen.
		double control1 = 0; // Muss 0 sein, da es sein kann, dass es keinen Kontrollpunkt für 0 gibt.
		double control2 = 1; // Muss 1 sein, da es sein kann, dass es keinen Kontrollpunkt für 1 gibt.
		for(Double controlPoint : _colorMap.keySet()) {
			if(alifePercentage >= controlPoint) {
				control1 = controlPoint;
			} else {
				control2 = controlPoint;
				break;
			}
		}
		
		// Ermitteln der Farben für die gefundenen Kontrollpunkte.
		Vector color1 = _colorMap.get(control1);
		Vector color2 = _colorMap.get(control2);
		
		// Da es sein kann, dass es in eine der beiden Richtungen keinen Kontrollpunkt gibt, wird angenommen, dass die
		// Farbe des anderen Kontrollpunkts hier einfach weitergeht. Da diese Methode abbricht wenn es gar keine
		// Kontrollpunkte gibt kann es nicht vorkommen, dass sowohl color1 als auch color2 gleichzeitig null sind.
		if(color1 == null) {
			color1 = color2;
		}
		if(color2 == null) {
			color2 = color1;
		}
		
		// Wendet die beiden ausgewählten Farben zu dem Prozentsatz
		double percentage = (alifePercentage-control1)/(control2-control1);
		for(int i=0; i<color.getDimension(); i++) {
			color.set(i, percentage*color2.get(i) + (1-percentage)*color1.get(i));
		}
	}
	
	public static class ControlPointGradientBuilder {
		private SortedMap<Double, Vector> _colorMap;
		private boolean _fadeOut;
		
		public ControlPointGradientBuilder() {
			_colorMap = new TreeMap<>();
		}
		
		public ControlPointGradientBuilder withControlPoint(double lifetimePercentage, Vector color) {
			Numbers.require(lifetimePercentage).greaterThanOrEqual(0, "Der Lebenszeitwert muss im Wertebereich von 0 bis 1 liegen.");
			Numbers.require(lifetimePercentage).lessThanOrEqual(1, "Der Lebenszeitwert muss im Wertebereich von 0 bis 1 liegen.");
			_colorMap.put(lifetimePercentage, CGUtils.checkColorVector(color));
			return this;
		}
		
		public ControlPointGradientBuilder withFadeOut(boolean fadeOut) {
			_fadeOut = fadeOut;
			return this;
		}
		
		public MultiColorGradient build() {
			MultiColorGradient gradient = new MultiColorGradient();
			for(Entry<Double, Vector> e : _colorMap.entrySet()) {
				gradient.addControlPoint(e.getKey(), e.getValue());
			}
			gradient.setFadeOut(_fadeOut);
			return gradient;
		}
	}
	
	public static class UniformGradientBuilder {
		private List<Vector> _colors;
		private boolean _fadeOut;
		
		public UniformGradientBuilder() {
			_colors = new LinkedList<>();
		}
		
		public UniformGradientBuilder appendColor(Vector color) {
			_colors.add(CGUtils.checkColorVector(color));
			return this;
		}
		
		public UniformGradientBuilder withFadeOut(boolean fadeOut) {
			_fadeOut = fadeOut;
			return this;
		}
		
		public MultiColorGradient build() {
			MultiColorGradient gradient = new MultiColorGradient();
			for(int i=0; i<_colors.size(); i++) {
				Vector color = _colors.get(i);
				// 1- am Anfang um die Farbreihenfolge zu erhalten in der die Farben mit appendColor angegeben wurden.
				// _colors.size()-1 da der letzte Eintrag bei 0 aufhören soll (i wird maximal _colors.size()-1) 
				gradient.addControlPoint(1-(double)i/(_colors.size()-1), color);
			}
			gradient.setFadeOut(_fadeOut);
			return gradient;
		}
	}
}
