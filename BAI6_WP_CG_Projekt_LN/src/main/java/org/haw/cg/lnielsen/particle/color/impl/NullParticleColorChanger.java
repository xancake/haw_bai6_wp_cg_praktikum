package org.haw.cg.lnielsen.particle.color.impl;

import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.color.AbstractParticleColorChanger;
import org.haw.cg.lnielsen.particle.color.ParticleColorChanger;
import computergraphics.framework.math.Vector;

public class NullParticleColorChanger extends AbstractParticleColorChanger implements ParticleColorChanger {
	@Override
	protected void calculateColor(Particle p, Vector color, double alifePercentage) {}
}
