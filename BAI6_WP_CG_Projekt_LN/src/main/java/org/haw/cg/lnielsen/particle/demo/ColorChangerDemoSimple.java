package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.color.ParticleColorizerBuilder;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemShowcaseScene;
import computergraphics.framework.math.Vector;

public class ColorChangerDemoSimple {
	public static void main(String[] args) {
		Particle.Builder builder3 = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(1))
        		.withLocation(new Vector(0, 0, 0))
        		.withVelocity(new Vector(0, 2, 0))
        		.withColorizer(ParticleColorizerBuilder.multiColorGradient()
        				.withControlPoint(1,   new Vector(0, 1, 1))
        				.withControlPoint(0.5, new Vector(1, 1, 0))
        				.withControlPoint(0,   new Vector(1, 0, 1))
        				.build());
		
		ParticleSystem system3 = new ParticleSystem(builder3, 100, 100, false);
		
		new ParticleSystemShowcaseScene(system3, 60);
	}
}
