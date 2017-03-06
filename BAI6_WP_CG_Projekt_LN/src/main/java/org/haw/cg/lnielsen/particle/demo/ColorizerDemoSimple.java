package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.color.impl.GradientColorizer;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemShowcaseScene;
import computergraphics.framework.math.Vector;

public class ColorizerDemoSimple {
	public static void main(String[] args) {
		Particle.Builder builder3 = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(1))
        		.withLocation(new Vector(0, 0, 0))
        		.withVelocity(new Vector(0, 2, 0))
        		.withColorizer(new GradientColorizer.UniformGradientBuilder()
        				.withPercentageSupplier(new GradientColorizer.LifePercentageSupplier())
        				.appendColor(new Vector(0, 0, 1))
        				.appendColor(new Vector(0, 1, 1))
        				.appendColor(new Vector(0, 1, 0))
        				.appendColor(new Vector(1, 1, 0))
        				.appendColor(new Vector(1, 0, 0))
        				.withFadeOut(false)
        				.build());
		
		ParticleSystem system3 = new ParticleSystem(builder3, 10, 10, false);
		
		new ParticleSystemShowcaseScene(system3, 60);
	}
}
