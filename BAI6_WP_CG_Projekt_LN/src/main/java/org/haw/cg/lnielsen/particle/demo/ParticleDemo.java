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
        		.withLocation(new Vector(-1,-1,0), new Vector(1,-1,0))
        		.withVelocity(new Vector(0,0,0))
        		.withAcceleration(new Vector(0,1,0))
        		.withColor(new Vector(1,0,0,1))
        		.withFadeOut(true);
		
		ParticleSystem system = new ParticleSystem(builder, 10, 1);
		system.applyForce(new Vector(0,0,-0.25));
		
		new ParticleSystemShowcaseScene(system, 60);
	}
}
