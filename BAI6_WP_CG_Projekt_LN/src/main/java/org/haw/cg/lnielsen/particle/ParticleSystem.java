package org.haw.cg.lnielsen.particle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import computergraphics.framework.math.Vector;

/**
 * Diese Klasse repräsentiert ein Partikelsystem.
 * <p>Ein Partikelsystem verwaltet mehrere Partikel über ihren Lebenszyklus hinweg.
 */
public class ParticleSystem {
	private Particle.Builder _builder;
	private Particle _particle;
	
	private long _last;
	private long _curr;
	
	public ParticleSystem() {
		_builder = new Particle.Builder()
        		.withStartLife(TimeUnit.SECONDS.toMillis(2))
        		.withLocation(new Vector(-1,-1,0), new Vector(1,-1,0))
        		.withVelocity(new Vector(0,0,0))
        		.withAcceleration(new Vector(0,1,0))
        		.withColor(new Vector(0,1,1,1))
        		.withFadeOut(true);
		
		_particle = _builder.build();
	}
	
	/**
	 * Updatet alle von diesem Partikelsystem verwalteten Partikel und sorgt für die Berechnung der vergangenen Zeit
	 * seit dem letzten Update.
	 */
	public void update() {
		_last = _curr;
		_curr = System.currentTimeMillis();
		_particle.update(_curr - _last);
		if(_particle.isDead()) {
			_builder.initialize(_particle);
		}
	}
	
	/**
	 * Gibt eine unveränderliche Liste aller von diesem Partikelsystem verwalteten Partikel zurück.
	 * @return Eine unveränderliche Liste der Partikel
	 */
	public List<Particle> getParticles() {
		return Collections.unmodifiableList(Arrays.asList(_particle));
	}
}
