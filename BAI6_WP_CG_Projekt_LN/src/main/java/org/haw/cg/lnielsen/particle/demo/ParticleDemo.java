package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemShowcaseScene;
import computergraphics.framework.math.Vector;

public class ParticleDemo {
	public static void main(String[] args) throws InterruptedException {
		Particle.Builder builder = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(-1, -1, 0), new Vector(1, -1, 0))
        		.withVelocity(new Vector(0, 0, 0.5))
        		.withAcceleration(new Vector(-0.5, 0, -0.5), new Vector(0.5, 0, 0.5))
//        		.withMass(0, 2)
        		.withColorStart(new Vector(1, 0.1, 0, 1))
        		.withColorEnd(new Vector(1, 0.9, 0, 0.1))
        		.withFadeOut(false);
		
		ParticleSystem system = new ParticleSystem(builder, 10000, 5000);
		system.applyForce(new Vector(0, 1, 0));     // Feuer aufsteigend
		system.applyForce(new Vector(0, 0, -0.25)); // Wind
		
		new ParticleSystemShowcaseScene(system, 60);
		
		// TODO: Das wäre echt schön... Vielleicht Partikelsystem threadsafe machen, damit das geht?
//		Vector wind = new Vector(0, 0, -1);
//		system.applyForce(wind);
//		Thread.sleep(1000);
//		system.removeForce(wind);
	}
}
