package org.haw.cg.lnielsen.particle.color;

import org.haw.cg.lnielsen.particle.Particle;
import computergraphics.framework.math.Vector;

public class NullParticleColorChanger extends AbstractParticleColorChanger implements ParticleColorChanger {
	@Override
	protected void calculateColor(Particle p, Vector color, double alifePercentage) {}
}
