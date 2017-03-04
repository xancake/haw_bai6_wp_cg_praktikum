package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.color.ColorChangerBuilder;
import org.haw.cg.lnielsen.particle.color.impl.SpeedVisualizer;
import org.haw.cg.lnielsen.particle.physics.Repeller;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemManagerShowcaseScene;
import computergraphics.framework.math.Vector;

public class ColorChangerDemo {
	public static void main(String[] args) {
		Particle.Builder builder1 = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(-1, 0, 0))
        		.withVelocity(new Vector(-0.1, 0, -0.1), new Vector(0.1, 0, 0.1))
        		.withColorChanger(new SpeedVisualizer(0));
		Particle.Builder builder2 = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(1, 0, 0))
        		.withVelocity(new Vector(-0.1, 0, -0.1), new Vector(0.1, 0, 0.1))
        		.withColorChanger(new SpeedVisualizer(0, 1));
		Particle.Builder builder3 = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(0, 0, 0))
        		.withVelocity(new Vector(-0.1, -0.1, -0.1), new Vector(0.1, 0.1, 0.1))
        		.withColorChanger(ColorChangerBuilder.uniformMultiColorGradient()
        				.appendColor(new Vector(1, 0, 0))
        				.appendColor(new Vector(1, 1, 0))
        				.appendColor(new Vector(0, 1, 0))
        				.appendColor(new Vector(0, 1, 1))
        				.appendColor(new Vector(0, 0, 1))
        				.appendColor(new Vector(1, 0, 1))
        				.build());
		
		ParticleSystem system1 = new ParticleSystem(builder1, 5000, 5000, false);
		system1.applyForce(new Vector(0, 1, 0));
		system1.addRepeller(new Repeller(new Vector(-1, 1.5, 0), 0.5, 20));
		
		ParticleSystem system2 = new ParticleSystem(builder2, 5000, 5000, false);
		system2.applyForce(new Vector(0, 1, 0));
		system2.addRepeller(new Repeller(new Vector(1, 1.5, 0), 0.5, 20));
		
		ParticleSystem system3 = new ParticleSystem(builder3, 5000, 5000, false);
		system3.applyForce(new Vector(0, -1, 0));
//		system3.addRepeller(new Repeller(new Vector(0, -1.5, 0), 0.5, 20));
		
		ParticleSystemManagerShowcaseScene scene = new ParticleSystemManagerShowcaseScene(60);
		scene.addParticleSystem(system1);
		scene.addParticleSystem(system2);
		scene.addParticleSystem(system3);
	}
}
