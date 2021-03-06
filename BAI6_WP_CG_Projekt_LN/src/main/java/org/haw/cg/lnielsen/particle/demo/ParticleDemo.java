package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.color.ParticleColorizerBuilder;
import org.haw.cg.lnielsen.particle.physics.Repeller;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemShowcaseScene;
import computergraphics.framework.math.Vector;

public class ParticleDemo {
	public static void main(String[] args) throws InterruptedException {
		Particle.Builder builder = new Particle.Builder()
				.withStartLife(TimeUnit.SECONDS.toMillis(2))
				.withLocation(new Vector(-1, -1, 0), new Vector(1, -1, 0))
				.withVelocity(new Vector(-0.5, 0.5, -0.5), new Vector(0.5, 1, 0.5));
		
		ParticleSystem system = new ParticleSystem(builder, 10000, 5000, false);
		system.setParticleColorizer(ParticleColorizerBuilder.twoColorGradient()
						.withStartColor(new Vector(1, 0.1, 0, 1))
						.withEndColor(new Vector(1, 0.9, 0, 0.1))
						.withFadeOut(true)
						.build());
		
		ParticleSystem.DEBUG = true;
		ParticleSystemShowcaseScene scene = new ParticleSystemShowcaseScene(system, 60);
		scene.setRepeller(new Repeller(new Vector(-1, 0, -1), 1, 10));
	}
}
