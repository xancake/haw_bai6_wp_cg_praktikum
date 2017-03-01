package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemManagerShowcaseScene;
import computergraphics.framework.math.Vector;

public class PhysicsParticleDemo {
	public static void main(String[] args) throws InterruptedException {
		ParticleSystemManagerShowcaseScene scene = new ParticleSystemManagerShowcaseScene(60);
		
		// Partikelsystem mit Schwerkraft
		scene.addParticleSystem(new ParticleSystem(
				new Particle.Builder()
        				.withStartLife(TimeUnit.SECONDS.toMillis(2))
        				.withLocation(new Vector(-1, 0, 0))
        				.withVelocity(new Vector(-0.1, 1, -0.1), new Vector(0.1, 1, 0.1))
        				.withColor(new Vector(1, 0, 0))
        				.withMass(0.5, 2)
        				.addGravity(new Vector(0, -1, 0)),
				1000, 1000, false
		));
		
		// Partikelsystem mit Schwerkraft als reine Kraft
		scene.addParticleSystem(new ParticleSystem(
				new Particle.Builder()
        				.withStartLife(TimeUnit.SECONDS.toMillis(2))
        				.withLocation(new Vector(1, 0, 0))
        				.withVelocity(new Vector(-0.1, 1, -0.1), new Vector(0.1, 1, 0.1))
        				.withColor(new Vector(0, 1, 1))
        				.withMass(0.5, 2)
        				.addForce(new Vector(0, -1, 0)),
				1000, 1000, false
		));
	}
}
