package org.haw.cg.lnielsen.particle.color.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.color.ParticleColorizer;
import org.haw.cg.lnielsen.util.Numbers;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.CGUtils;

public class GradientColorizer implements ParticleColorizer {
	private GradientPercentageSupplier _supplier;
	private SortedMap<Double, Vector> _colorMap;
	private boolean _fadeOut;
	
	public GradientColorizer() {
		this(new LifePercentageSupplier());
	}
	
	public GradientColorizer(GradientPercentageSupplier supplier) {
		_supplier = Objects.requireNonNull(supplier);
		_colorMap = new TreeMap<>();
	}
	
	public void setFadeOut(boolean fadeOut) {
		_fadeOut = fadeOut;
	}
	
	public boolean isFadeOut() {
		return _fadeOut;
	}
	
	public void addControlPoint(double lifetimePercentage, Vector color) {
		Numbers.require(lifetimePercentage).greaterThanOrEqual(0, "Der Lebenszeitwert muss im Wertebereich von 0 bis 1 liegen.");
		Numbers.require(lifetimePercentage).lessThanOrEqual(1, "Der Lebenszeitwert muss im Wertebereich von 0 bis 1 liegen.");
		_colorMap.put(lifetimePercentage, CGUtils.checkColorVector(color));
	}
	
	@Override
	public void updateColor(Particle p) {
		double percentage = _supplier.supplyPercentage(p);
		
		Vector color = p.getColor();
		calculateColor(p, color, percentage);
		
		if(_fadeOut) {
			color.set(3, percentage);
		}
	}
	
	private void calculateColor(Particle p, Vector color, double percentage) {
		if(_colorMap.size() <= 0) {
			return;
		}
		
		// Die zwei Kontrollpunkte finden, die um alifePercentage herum liegen.
		double control1 = 0; // Muss 0 sein, da es sein kann, dass es keinen Kontrollpunkt für 0 gibt.
		double control2 = 1; // Muss 1 sein, da es sein kann, dass es keinen Kontrollpunkt für 1 gibt.
		for(Double controlPoint : _colorMap.keySet()) {
			if(percentage > controlPoint || controlPoint == 0) {
				// Das "|| controlPoint == 0" sorgt dafür, dass ein möglicher Kontrollpunkt bei 0 IMMER als erster
				// Kontrollpunkt in Betracht gezogen wird. Andernfalls kann es geschehen, dass er als zweiter
				// Kontrollpunkt misinterpretiert wird was dazu führt, dass in der Berechnung der Anteile NaN entsteht.
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
		double nextColorPercentage = (percentage-control1)/(control2-control1);
		for(int i=0; i<color.getDimension(); i++) {
			color.set(i, nextColorPercentage*color2.get(i) + (1-nextColorPercentage)*color1.get(i));
		}
	}
	
	public static interface GradientPercentageSupplier {
		double supplyPercentage(Particle p);
	}
	
	public static class LifePercentageSupplier implements GradientPercentageSupplier {
		@Override
		public double supplyPercentage(Particle p) {
			return (double)p.getLife() / p.getStartLife();
		}
	}
	
	public static class SpeedPercentageSupplier implements GradientPercentageSupplier {
		private double _min;
		private double _max;
		private boolean _adaptive;
		
		public SpeedPercentageSupplier(double base) {
			this(base, base, true);
		}
		
		public SpeedPercentageSupplier(double min, double max) {
			this(min, max, false);
		}
		
		private SpeedPercentageSupplier(double min, double max, boolean adaptive) {
			_min = min;
			_max = max;
			_adaptive = adaptive;
		}
		
		@Override
		public double supplyPercentage(Particle p) {
			double speed = p.getVelocity().getNorm();
			double speedConstrained = Math.min(Math.max(speed, _min), _max);
			double speedPercentage = speedConstrained/(_max-_min);
			if(_adaptive) {
				if(_min > speed) {
					_min = speed;
				}
				if(_max < speed) {
					_max = speed;
				}
			}
			return 1-speedPercentage;
		}
	}
	
	public static class ControlPointGradientBuilder {
		private GradientPercentageSupplier _supplier;
		private SortedMap<Double, Vector> _colorMap;
		private boolean _fadeOut;
		
		public ControlPointGradientBuilder() {
			_supplier = new LifePercentageSupplier();
			_colorMap = new TreeMap<>();
		}
		
		public ControlPointGradientBuilder withPercentageSupplier(GradientPercentageSupplier supplier) {
			_supplier = Objects.requireNonNull(supplier);
			return this;
		}
		
		public ControlPointGradientBuilder withControlPoint(double lifetimePercentage, Vector color) {
			Numbers.require(lifetimePercentage).greaterThanOrEqual(0, "Der Lebenszeitwert mussim Wertebereich von 0 bis 1 liegen.");
			Numbers.require(lifetimePercentage).lessThanOrEqual(1, "Der Lebenszeitwert mussim Wertebereich von 0 bis 1 liegen.");
			_colorMap.put(lifetimePercentage, CGUtils.checkColorVector(color));
			return this;
		}
		
		public ControlPointGradientBuilder withFadeOut(boolean fadeOut) {
			_fadeOut = fadeOut;
			return this;
		}
		
		public GradientColorizer build() {
			GradientColorizer gradient = new GradientColorizer(_supplier);
			for(Entry<Double, Vector> e : _colorMap.entrySet()) {
				gradient.addControlPoint(e.getKey(), e.getValue());
			}
			gradient.setFadeOut(_fadeOut);
			return gradient;
		}
	}
	
	public static class UniformGradientBuilder {
		private GradientPercentageSupplier _supplier;
		private List<Vector> _colors;
		private boolean _fadeOut;
		
		public UniformGradientBuilder() {
			_supplier = new LifePercentageSupplier();
			_colors = new LinkedList<>();
		}
		
		public UniformGradientBuilder withPercentageSupplier(GradientPercentageSupplier supplier) {
			_supplier = Objects.requireNonNull(supplier);
			return this;
		}
		
		public UniformGradientBuilder appendColor(Vector color) {
			_colors.add(CGUtils.checkColorVector(color));
			return this;
		}
		
		public UniformGradientBuilder withFadeOut(boolean fadeOut) {
			_fadeOut = fadeOut;
			return this;
		}
		
		public GradientColorizer build() {
			GradientColorizer gradient = new GradientColorizer(_supplier);
			for(int i=0; i<_colors.size(); i++) {
				// 1- am Anfang um die Farbreihenfolge zu erhalten in der die Farben mit appendColor angegeben wurden.
				// _colors.size()-1 da der letzte Eintrag bei 0 aufhören soll (i wird maximal _colors.size()-1) 
				gradient.addControlPoint(1-(double)i/(_colors.size()-1), _colors.get(i));
			}
			gradient.setFadeOut(_fadeOut);
			return gradient;
		}
	}
}
