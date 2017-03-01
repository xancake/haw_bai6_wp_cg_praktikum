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
		Particle.Builder gravityBuilder = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(-1, 0, 0))
        		.withVelocity(new Vector(-0.1, 1, -0.1), new Vector(0.1, 1, 0.1))
        		.withColor(new Vector(1, 0, 0))
        		.withMass(0.5, 2);
		Particle.Builder forceBuilder = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(0, 0, 0))
        		.withVelocity(new Vector(-0.1, 1, -0.1), new Vector(0.1, 1, 0.1))
        		.withColor(new Vector(0, 1, 1))
        		.withMass(0.5, 2);
		Particle.Builder noForceBuilder = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(1, 0, 0))
        		.withVelocity(new Vector(-0.1, 1, -0.1), new Vector(0.1, 1, 0.1))
        		.withColor(new Vector(1, 1, 0))
        		.withMass(0.5, 2);
		Particle.Builder attractorRepellerBuilder = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(0, 0, -1))
        		.withVelocity(new Vector(-0.1, 1, -0.1), new Vector(0.1, 1, 0.1))
        		.withColor(new Vector(1, 0, 1))
        		.withMass(0.5, 2);
		
		ParticleSystem gravitySystem = new ParticleSystem(gravityBuilder, 1000, 1000, false);
		ParticleSystem forceSystem   = new ParticleSystem(forceBuilder,   1000, 1000, false);
		ParticleSystem noForceSystem = new ParticleSystem(noForceBuilder, 1000, 1000, false);
		ParticleSystem attractorRepellerSystem = new ParticleSystem(attractorRepellerBuilder, 1000, 1000, false);
		
		gravitySystem.applyGravity(new Vector(0, -1, 0));
		forceSystem.applyForce(new Vector(0, -1, 0));
		
		scene.addParticleSystem(gravitySystem);
		scene.addParticleSystem(forceSystem);
		scene.addParticleSystem(noForceSystem);
		scene.addParticleSystem(attractorRepellerSystem);
	}
}
