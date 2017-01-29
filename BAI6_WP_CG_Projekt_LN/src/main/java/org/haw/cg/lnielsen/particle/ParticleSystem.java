package org.haw.cg.lnielsen.particle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.haw.cg.lnielsen.util.Numbers;
import computergraphics.framework.math.Vector;

/**
 * Diese Klasse repräsentiert ein Partikelsystem.
 * <p>Ein Partikelsystem verwaltet mehrere Partikel über ihren Lebenszyklus hinweg.
 */
public class ParticleSystem {
	private long _last;
	private long _curr;
	private boolean _initialized;
	
	private Particle.Builder _builder;
	
	private int _maxParticles;
	private Set<Particle> _lifeParticles;
	private Set<Particle> _deadParticles;
	
	private int _spawnPerSecond;
	private long _lastCreatedTime; /** Der Zeitpunkt zu dem das letzte Mal ein Partikel erzeugt wurde. */
	
	public ParticleSystem(Particle.Builder builder, int maxParticles, int spawnPerSecond) {
		_builder = Objects.requireNonNull(builder);
		_spawnPerSecond = Numbers.require(spawnPerSecond).greaterThanOrEqual(0, "Die Anzahl der pro Sekunde zu spawnenden Partikel muss positiv sein!");
        _maxParticles = Numbers.require(maxParticles).greaterThanOrEqual(0, "Die maximale Anzahl der Partikel muss positiv sein!");
        _lifeParticles = new HashSet<>(_maxParticles);
        _deadParticles = new HashSet<>(_maxParticles);
	}
	
	/**
	 * Initialisiert die Zeitpunkte beim ersten Aufruf von {@link #update()}, damit beim ersten Aufruf keine riesigen
	 * Sprünge auftreten. Das hätte nämlich zur Folge, dass bereits alle Partikel erzeugt werden, auch wenn lange nicht
	 * soviele benötigt werden.
	 */
	private void initTime() {
		if(!_initialized) {
			_initialized = true;
			_last = _curr = System.currentTimeMillis();
		}
	}
	
	/**
	 * Updatet alle von diesem Partikelsystem verwalteten Partikel und sorgt für die Berechnung der vergangenen Zeit
	 * seit dem letzten Update.
	 */
	public void update() {
		initTime();
		
		_last = _curr;
		_curr = System.currentTimeMillis();
		
		long timeMS = _curr - _last;
		double timeS  = timeMS / 1000.0;
		
		System.out.printf("Life: %5d, Dead: %5d, Max: %5d, diff: %4d%n", getLifeParticlesCount(), getDeadParticlesCount(), getMaxParticlesCount(), timeMS);
		
		// TODO: Spawnmechanismus überarbeiten, sodass nicht mehr als _spawnPerSecond Partikel pro Sekunde gespawnt werden können
		
		// Partikel (re-)spawnen
		Iterator<Particle> dead = _deadParticles.iterator();
		for(int i=0; i<timeS*_spawnPerSecond; i++) {
			if(dead.hasNext()) {
				// Wenn wir noch Partikel haben, die wir wiederverwenden können, dann tun wir das
				Particle p = dead.next();
				_builder.initialize(p);
				dead.remove();
				_lifeParticles.add(p);
			} else if(_lifeParticles.size() + _deadParticles.size() < _maxParticles) {
				// Wenn die Maximalzahl an Partikeln noch nicht erreich ist, erzeugen wir neue Partikel
				_lifeParticles.add(_builder.build());
			} else {
				// Ansonsten erzeugen wir keine weiteren Partikel mehr
				break;
			}
		}
		
		// Partikel updaten und ggf. als Tot markieren
		for(Iterator<Particle> life=_lifeParticles.iterator(); life.hasNext(); ) {
			Particle p = life.next();
			p.update(timeMS);
			if(p.isDead()) {
				life.remove();
				_deadParticles.add(p);
			}
		}
	}
	
	public void applyForce(Vector force) {
		for(Particle p : _lifeParticles) {
			p.applyForce(force);
		}
		_builder.addForce(force);
	}
	
	/**
	 * Gibt eine unveränderliche Sammlung aller lebendigen, von diesem Partikelsystem verwalteten Partikel zurück.
	 * @return Eine unveränderliche Sammlung der Partikel
	 */
	public Collection<Particle> getParticles() {
		return Collections.unmodifiableSet(_lifeParticles);
	}
	
	public int getMaxParticlesCount() {
		return _maxParticles;
	}
	
	public int getLifeParticlesCount() {
		return _lifeParticles.size();
	}
	
	public int getDeadParticlesCount() {
		return _deadParticles.size();
	}
	
	/**
	 * Gibt den intern verwendeten {@link Particle.Builder} zurück, über den die von diesem Partikelsystem erzeugten
	 * {@link Particle Partikel} konfiguriert werden können.
	 * @return Der Builder
	 */
	public Particle.Builder getParticleBuilder() {
		return _builder;
	}
}
