package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.physics.Repeller;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemManagerShowcaseScene;
import computergraphics.framework.math.Vector;

public class RepellerParticleDemo {
	public static void main(String[] args) {
		double massFrom = 0.5;
		double massTo   = 2;
		
		Particle.Builder builder1 = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(-2, 0, 0))
        		.withVelocity(new Vector(-0.1, 0, -0.1), new Vector(0.1, 0, 0.1))
        		.withColor(new Vector(1, 0, 0))
        		.withMass(massFrom, massTo);
		Particle.Builder builder2 = new Particle.Builder()
    			.withStartLife(TimeUnit.SECONDS.toMillis(1))
        		.withLocation(new Vector(-0.5, -1, 0), new Vector(0.5, -1, 0))
        		.withVelocity(new Vector(-0.1, 0.5, -0.1), new Vector(0.1, 2, 0.1))
        		.withColor(new Vector(0, 1, 1))
        		.withMass(massFrom, massTo);
		Particle.Builder builder3 = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(1.5, 0, 0), new Vector(2.5, 0, 0))
        		.withVelocity(new Vector(-0.1, 0.5, -0.1), new Vector(0.1, 2, 0.1))
        		.withColor(new Vector(1, 1, 0))
        		.withMass(massFrom, massTo);
		
		ParticleSystem system1 = new ParticleSystem(builder1, 5000, 5000, false);
		system1.addRepeller(new Repeller(new Vector(-2, 1, 0), 1, -10));
		system1.addRepeller(new Repeller(new Vector(-2, 2, 0), 1, 1));
		
		ParticleSystem system2 = new ParticleSystem(builder2, 5000, 5000, false);
		system2.addRepeller(new Repeller(new Vector(0, 0, 0), 1, 10));
		
		ParticleSystem system3 = new ParticleSystem(builder3, 5000, 5000, false);
		system3.addRepeller(new Repeller(new Vector(2, 1, 0), 1, -10));
		
		ParticleSystemManagerShowcaseScene scene = new ParticleSystemManagerShowcaseScene(60);
		scene.addParticleSystem(system1);
		scene.addParticleSystem(system2);
		scene.addParticleSystem(system3);
	}
}
