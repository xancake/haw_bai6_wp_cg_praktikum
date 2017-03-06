package org.haw.cg.lnielsen.particle.color.impl;

import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.color.AbstractParticleColorizer;
import org.haw.cg.lnielsen.particle.color.ParticleColorizer;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.CGUtils;

/**
 * Ein {@link ParticleColorizer} der Partikel über ihre Lebenszeit entsprechend eines Farbverlaufs von einer Farbe
 * zu einer anderen Farbe umsetzt. Die Farben können über Setter gesetzt werden. Standardmäßig sind beide Farben
 * schwarz.
 */
public class TwoColorGradient extends AbstractParticleColorizer implements ParticleColorizer {
	private Vector _colorStart   = new Vector(0,0,0,1);
	private Vector _colorEnd     = new Vector(0,0,0,1);
	
	public void setStartColor(Vector color) {
		_colorStart = CGUtils.checkColorVector(color);
	}
	
	public Vector getStartColor() {
		return _colorStart;
	}
	
	public void setEndColor(Vector color) {
		_colorEnd = CGUtils.checkColorVector(color);
	}
	
	public Vector getEndColor() {
		return _colorEnd;
	}
	
	@Override
	protected void calculateColor(Particle p, Vector color, double alifePercentage) {
		for(int i=0; i<color.getDimension(); i++) {
			color.set(i, alifePercentage*_colorStart.get(i) + (1-alifePercentage)*_colorEnd.get(i));
		}
	}
	
	public static class Builder {
		private Vector _colorStart;
		private Vector _colorEnd;
		private boolean _fadeOut;
		
		public Builder withStartColor(Vector color) {
			_colorStart = CGUtils.checkColorVector(color);
			return this;
		}
		
		public Builder withEndColor(Vector color) {
			_colorEnd = CGUtils.checkColorVector(color);
			return this;
		}
		
		public Builder withFadeOut(boolean fadeOut) {
			_fadeOut = fadeOut;
			return this;
		}
		
		public TwoColorGradient build() {
			TwoColorGradient gradient = new TwoColorGradient();
			gradient.setStartColor(_colorStart);
			gradient.setEndColor(_colorEnd);
			gradient.setFadeOut(_fadeOut);
			return gradient;
		}
	}
}
