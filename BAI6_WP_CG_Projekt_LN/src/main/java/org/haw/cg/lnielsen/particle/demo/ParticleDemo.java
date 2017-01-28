package org.haw.cg.lnielsen.particle.demo;

import org.haw.cg.lnielsen.particle.ParticleSystem;
import org.haw.cg.lnielsen.particle.scenegraph.ParticleSystemShowcaseScene;

public class ParticleDemo {
	public static void main(String[] args) {
		ParticleSystem system = new ParticleSystem();
		new ParticleSystemShowcaseScene(system, 60);
	}
}
