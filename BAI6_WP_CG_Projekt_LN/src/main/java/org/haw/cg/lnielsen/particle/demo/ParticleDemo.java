package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemShowcaseScene;
import computergraphics.framework.math.Vector;

public class ParticleDemo {
	public static void main(String[] args) {
		Particle.Builder builder = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(-1, -1, 0), new Vector(1, -1, 0))
        		.withAcceleration(new Vector(-0.5, 0, -0.5), new Vector(0.5, 0, 0.5))
//        		.withMass(0, 2)
        		.withColorStart(new Vector(1, 0.1, 0, 1))
        		.withColorEnd(new Vector(1, 0.9, 0, 0.1))
        		.withFadeOut(false);
		
		ParticleSystem system = new ParticleSystem(builder, 10000, 5000);
		system.applyForce(new Vector(0, 1, 0));     // Feuer aufsteigend
		system.applyForce(new Vector(0, 0, -0.25)); // Wind
		
		new ParticleSystemShowcaseScene(system, 60);
	}
}
