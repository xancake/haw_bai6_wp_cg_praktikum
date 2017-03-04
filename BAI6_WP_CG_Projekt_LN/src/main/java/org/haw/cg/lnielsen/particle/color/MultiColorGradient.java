package org.haw.cg.lnielsen.particle.color;

import java.util.SortedMap;
import java.util.TreeMap;
import org.haw.cg.lnielsen.particle.Particle;
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
		Numbers.require(lifetimePercentage).greaterThanOrEqual(0, "Der Lebenszeitwert mussim Wertebereich von 0 bis 1 liegen.");
		Numbers.require(lifetimePercentage).lessThanOrEqual(1, "Der Lebenszeitwert mussim Wertebereich von 0 bis 1 liegen.");
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
	
	/**
	 * Dieser Builder verhält sich anders als übliche Builder. Während andere Builder die registrierten Eigenschaften
	 * behalten, nachdem sie ein Objekt erzeugt haben, werden sie bei diesem Builder zurückgesetzt.
	 */
	public static class Builder {
		private MultiColorGradient _gradient;
		
		public Builder() {
			_gradient = new MultiColorGradient();
		}
		
		public Builder withControlPoint(double lifetimePercentage, Vector color) {
			_gradient.addControlPoint(lifetimePercentage, color);
			return this;
		}
		
		public Builder withFadeOut(boolean fadeOut) {
			_gradient.setFadeOut(fadeOut);
			return this;
		}
		
		public MultiColorGradient build() {
			MultiColorGradient gradient = _gradient;
			_gradient = new MultiColorGradient();
			return gradient;
		}
	}
}
