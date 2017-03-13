package org.haw.cg.lnielsen.particle.demo;

import java.util.concurrent.TimeUnit;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.color.ParticleColorizer;
import org.haw.cg.lnielsen.particle.color.impl.GradientColorizer;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemShowcaseScene;
import computergraphics.framework.math.Vector;

public class ColorizerDemoSimple {
	public static void main(String[] args) {
		Particle.Builder builder3 = new Particle.Builder()
				.withStartLife(TimeUnit.SECONDS.toMillis(1))
				.withLocation(new Vector(0, 0, 0))
				.withVelocity(new Vector(0, 4, 0));
		
		ParticleColorizer colorizer = new GradientColorizer.UniformGradientBuilder()
				// Bezieht den Farbverlauf auf das Alter der Partikel 
//				.withFunction(new GradientColorizer.LifeFunction())
				// Bezieht den Farbverlauf auf die Geschwindigkeit und "lernt" die Min- und Max-Geschwindigkeit der Partikel adaptiv
//				.withFunction(new GradientColorizer.SpeedFunction(0))
				// Bezieht den Farbverlauf auf die Geschwindigkeit, die Abstufung geschieht zwischen Geschwindigkeit=2 und 4
//				.withFunction(new GradientColorizer.SpeedFunction(2, 4))
				// Bezieht den Farbverlauf auf die Position auf der Y-Achse (hoch runter), die Abstufung geschieht zwischen Y=1 und Y=2
				.withFunction(new GradientColorizer.GradientPropertyFunction(
						p -> 1,                     // Minimum
						p -> 2,                     // Maximum
						p -> p.getLocation().get(1) // tatsächlicher Wert
				))
				.appendColor(new Vector(0, 0, 1))
				.appendColor(new Vector(0, 1, 1))
				.appendColor(new Vector(0, 1, 0))
				.appendColor(new Vector(1, 1, 0))
				.appendColor(new Vector(1, 0, 0))
				.withFadeOut(false)
				.build();
		
		ParticleSystem system3 = new ParticleSystem(builder3, 10, 10, false);
		system3.applyForce(new Vector(0, -4, 0));
		system3.setParticleColorizer(colorizer);
		
		new ParticleSystemShowcaseScene(system3, 60);
	}
}
