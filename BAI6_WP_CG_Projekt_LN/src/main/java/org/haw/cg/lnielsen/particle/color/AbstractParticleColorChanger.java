package org.haw.cg.lnielsen.particle.color;

import org.haw.cg.lnielsen.particle.Particle;
import computergraphics.framework.math.Vector;

public abstract class AbstractParticleColorChanger implements ParticleColorChanger {
	private boolean _fadeOut;
	
	public void setFadeOut(boolean fadeOut) {
		_fadeOut = fadeOut;
	}
	
	public boolean isFadeOut() {
		return _fadeOut;
	}
	
	@Override
	public final void updateColor(Particle p) {
		double alifePercentage = (double)p.getLife() / p.getStartLife();
		Vector color = p.getColor();
		
		calculateColor(p, color, alifePercentage);
		
		if(_fadeOut) {
			color.set(3, alifePercentage);
		}
	}
	
	protected abstract void calculateColor(Particle p, Vector color, double alifePercentage);
}
