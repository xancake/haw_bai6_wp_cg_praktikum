package org.haw.cg.lnielsen.particle.color.impl;

import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.color.AbstractParticleColorizer;
import org.haw.cg.lnielsen.particle.color.ParticleColorizer;
import org.haw.cg.lnielsen.util.Numbers;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.CGUtils;

/**
 * Ein {@link ParticleColorizer} der Partikel abhängig ihrer Geschwindigkeit einfärbt. Diese Klasse arbeitet mit
 * minimal und maximal Geschwindigkeiten, zu denen die Partikel mit entsprechenden Farben eingefärbt werden. Sie kann
 * adaptiv genutzt werden, wodurch die Grenzwerte sich anpassen wenn extremere Werte auftreten.
 */
public class SpeedVisualizer extends AbstractParticleColorizer implements ParticleColorizer {
	private double _speedMin;
	private double _speedMax;
	private boolean _adaptive;
	private Vector _colorMin;
	private Vector _colorMax;
	
	/**
	 * Dieser Konstruktor initialisiert das Objekt adaptiv. Die Grenzen werden mit dem übergebenen Wert initialisiert,
	 * passen sich aber größeren und kleineren Werten von Partikeln an.
	 * @param base Der Basisgrenzwert von dem aus die Schranken wachsen
	 */
	public SpeedVisualizer(double base) {
		this(base, base, true);
	}
	
	/**
	 * Dieser Konstruktor verwendet harte Grenzwerte, wobei Partikel die schneller oder langsamer sind mit der Farbe
	 * eingefärbt werden, die dem betroffenen Grenzwert entsprechen.
	 * @param speedMin Der Minimalwert
	 * @param speedMax Der Maximalwert
	 */
	public SpeedVisualizer(double speedMin, double speedMax) {
		this(speedMin, speedMax, false);
	}
	
	private SpeedVisualizer(double speedMin, double speedMax, boolean adaptive) {
		Numbers.require(speedMin).lessThanOrEqual(speedMax, "Die Minimalgeschwindigkeit muss geringer sein als die Maximalgeschwindigkeit!");
		_speedMin = speedMin;
		_speedMax = speedMax;
		_colorMin = new Vector(0.25, 1, 0, 1);
		_colorMax = new Vector(1, 0.25, 0, 1);
		_adaptive = adaptive;
	}
	
	@Override
	protected void calculateColor(Particle p, Vector color, double alifePercentage) {
		double speed = p.getVelocity().getNorm();
		double speedConstrained = Math.min(speed, _speedMax);
		double speedPercentage = (speedConstrained-_speedMin)/(_speedMax-_speedMin);
		for(int i=0; i<color.getDimension(); i++) {
			color.set(i, speedPercentage*_colorMax.get(i) + (1-speedPercentage)*_colorMin.get(i));
		}
		if(_adaptive) {
			if(_speedMin > speed) {
				_speedMin = speed;
			}
			if(_speedMax < speed) {
				_speedMax = speed;
			}
		}
	}
	
	public static class BoundedBuilder {
		private double _speedMin;
		private double _speedMax;
		private Vector _colorMin = new Vector(0.25, 1, 0, 1);
		private Vector _colorMax = new Vector(1, 0.25, 0, 1);
		private boolean _fadeOut;
		
		public BoundedBuilder withMinSpeed(double speed) {
			_speedMin = speed;
			return this;
		}
		
		public BoundedBuilder withMaxSpeed(double speed) {
			_speedMax = speed;
			return this;
		}
		
		public BoundedBuilder withMinColor(Vector color) {
			_colorMin = CGUtils.checkColorVector(color);
			return this;
		}
		
		public BoundedBuilder withMaxColor(Vector color) {
			_colorMax = CGUtils.checkColorVector(color);
			return this;
		}
		
		public BoundedBuilder withFadeOut(boolean fadeOut) {
			_fadeOut = fadeOut;
			return this;
		}
		
		public SpeedVisualizer build() {
			SpeedVisualizer changer = new SpeedVisualizer(_speedMin, _speedMax);
			changer._colorMin = _colorMin;
			changer._colorMax = _colorMax;
			changer.setFadeOut(_fadeOut);
			return changer;
		}
	}
	
	public static class AdaptiveBuilder {
		private double _speedBase;
		private Vector _colorMin = new Vector(0.25, 1, 0, 1);
		private Vector _colorMax = new Vector(1, 0.25, 0, 1);
		private boolean _fadeOut;
		
		public AdaptiveBuilder withBaseSpeed(double speed) {
			_speedBase = speed;
			return this;
		}
		
		
		public AdaptiveBuilder withMinColor(Vector color) {
			_colorMin = CGUtils.checkColorVector(color);
			return this;
		}
		
		public AdaptiveBuilder withMaxColor(Vector color) {
			_colorMax = CGUtils.checkColorVector(color);
			return this;
		}
		
		public AdaptiveBuilder withFadeOut(boolean fadeOut) {
			_fadeOut = fadeOut;
			return this;
		}

		public SpeedVisualizer build() {
			SpeedVisualizer changer = new SpeedVisualizer(_speedBase);
			changer._colorMin = _colorMin;
			changer._colorMax = _colorMax;
			changer.setFadeOut(_fadeOut);
			return changer;
		}
	}
}
