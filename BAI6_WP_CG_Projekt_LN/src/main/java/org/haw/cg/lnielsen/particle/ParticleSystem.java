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
	public static boolean DEBUG = false;
	
	private long _last;
	private long _curr;
	private boolean _initialized;
	
	private Particle.Builder _builder;
	
	private int _maxParticles;
	private Set<Particle> _lifeParticles;
	private Set<Particle> _deadParticles;
	
	private int _spawnPerSecond;
	private long _lastSpawnTime;	/** Der Zeitpunkt zu dem das letzte Mal ein Partikel erzeugt wurde. */
	private long _spawnCount;		/** Hält fest, wieviele Partikel bisher gespawned wurden. */
	private boolean _spawnCapped;	/** Steuert ob weiterhin Partikel gespawned werden sollen, nachdem die Maximalzahl an Partikeln gespawned wurde. */
	
	public ParticleSystem(Particle.Builder builder, int maxParticles, int spawnPerSecond, boolean spawnCapped) {
		_builder = Objects.requireNonNull(builder);
		_spawnPerSecond = Numbers.require(spawnPerSecond).greaterThanOrEqual(0, "Die Anzahl der pro Sekunde zu spawnenden Partikel muss positiv sein!");
        _maxParticles = Numbers.require(maxParticles).greaterThanOrEqual(0, "Die maximale Anzahl der Partikel muss positiv sein!");
        _lifeParticles = new HashSet<>(_maxParticles);
        _deadParticles = new HashSet<>(_maxParticles);
        _spawnCapped = spawnCapped;
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
		long deltaMS = _curr - _last;
		
		if(DEBUG) {
			System.out.printf("Life: %5d, Dead: %5d, Max: %5d, diff: %4d%n", getLifeParticlesCount(), getDeadParticlesCount(), getMaxParticlesCount(), deltaMS);
		}
		
		spawnParticles(deltaMS);
		updateParticles(deltaMS);
	}
	
	/**
	 * Spawnt Partikel für die übergebene Vergangene Zeit seit dem letzten Update-Zyklus.
	 * <p>Wenn dieses Partikelsystem {@link #isSpawnCapped() spawn-capped} ist, werden nur noch Partikel gespawned
	 * insofern dieses Partikelsystem noch nicht die {@link #getMaxParticlesCount() Maximalzahl} zu spawnender Prtikel
	 * gespawned hat.
	 * @param deltaMS Die vergangene Zeit seit dem letzten Update-Zyklus in Millisekunden
	 */
	private void spawnParticles(long deltaMS) {
		if(!_spawnCapped || _spawnCount < _maxParticles) {
    		Iterator<Particle> dead = _deadParticles.iterator();
    		// TODO: Spawnmechanismus überarbeiten, sodass nicht mehr als _spawnPerSecond Partikel pro Sekunde gespawnt werden können
    		double particlesToSpawn = deltaMS/1000.0 * _spawnPerSecond;
    		for(int i=0; i<particlesToSpawn; i++) {
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
    			_spawnCount++;
    		}
		}
	}
	
	/**
	 * Updatet alle lebendigen Partikel und markiert sie ggf. als tot, wenn sie nach dem Update ihre Lebenszeit
	 * überdauert haben.
	 * @param deltaMS Die vergangene Zeit seit dem letzten Update-Zyklus in Millisekunden
	 * @see #getLifeParticlesCount()
	 */
	private void updateParticles(long deltaMS) {
		for(Iterator<Particle> life=_lifeParticles.iterator(); life.hasNext(); ) {
			Particle p = life.next();
			p.update(deltaMS);
			if(p.isDead()) {
				life.remove();
				_deadParticles.add(p);
			}
		}
	}
	
	/**
	 * Wendet die übergebene Kraft auf alle lebendigen und zukünftigen Partikel dieses Partikelsystems an.
	 * @param force Die Kraft
	 */
	public void applyForce(Vector force) {
		for(Particle p : _lifeParticles) {
			p.applyForce(force);
		}
		_builder.addForce(force);
	}
	
	/**
	 * Entfernt Einwirkung der übergebenen Kraft auf alle lebendigen und zukünfigen Partikel dieses Partikelsystems.
	 * <p>Da intern ausschließlich Berechnungen stattfinden, sollte der Aufrufer selber sicherstellen, dass die zu
	 * entfernende Kraft vorher wirklich auf das Partikelsystem gewirkt hat.
	 * @param force Die Kraft
	 */
	public void removeForce(Vector force) {
		for(Particle p : _lifeParticles) {
			p.removeForce(force);
		}
		_builder.removeForce(force);
	}
	
	/**
	 * Wendet die übergebene Schwerkraft auf alle lebendigen und zukünftigen Partikel dieses Partikelsystems an.
	 * <p>Die Schwerkraft wird auf die Z-Achse übertragen. Soll die Schwerkraft in eine andere Achse gehen, so kann
	 * {@link #applyGravity(Vector)} verwendet werden.
	 * <p>Schwerkraft unterscheidet sich insofern von allgemeinen {@link #applyForce(Vector) Kräften}, als dass die
	 * {@link Particle#getMass() Masse des Partikels} auf sie keinen Einfluss hat.
	 * @param gravity Die Schwerkraft als Skalar
	 * @see #applyGravity(Vector)
	 */
	public void applyGravity(double gravity) {
		for(Particle p : _lifeParticles) {
			p.applyGravity(gravity);
		}
		_builder.addGravity(gravity);
	}
	
	/**
	 * Wendet die übergebene Schwerkraft auf alle lebendigen und zukünftigen Partikel dieses Partikelsystems an.
	 * <p>Schwerkraft unterscheidet sich insofern von allgemeinen {@link #applyForce(Vector) Kräften}, als dass die
	 * {@link Particle#getMass() Masse des Partikels} auf sie keinen Einfluss hat.
	 * @param gravity Die Schwerkraft als Vektor
	 * @see #applyGravity(double)
	 */
	public void applyGravity(Vector gravity) {
		for(Particle p : _lifeParticles) {
			p.applyGravity(gravity);
		}
		_builder.addGravity(gravity);
	}
	
	/**
	 * Entfernt Einwirkung der übergebenen Schwerkraft auf alle lebendigen und zukünfigen Partikel dieses Partikelsystems.
	 * <p>Die Schwerkraft wird auf die Z-Achse übertragen. Soll die Schwerkraft von einer anderen Achse entfernt werden,
	 * so kann {@link #removeGravity(Vector)} verwendet werden.
	 * <p>Schwerkraft unterscheidet sich insofern von allgemeinen {@link #removeForce(Vector) Kräften}, als dass die
	 * {@link Particle#getMass() Masse des Partikels} auf sie keinen Einfluss hat.
	 * <p>Da intern ausschließlich Berechnungen stattfinden, sollte der Aufrufer selber sicherstellen, dass die zu
	 * entfernende Kraft vorher wirklich auf das Partikelsystem gewirkt hat.
	 * @param force Die Kraft
	 * @see #removeGravity(Vector)
	 */
	public void removeGravity(double gravity) {
		for(Particle p : _lifeParticles) {
			p.removeGravity(gravity);
		}
		_builder.removeGravity(gravity);
	}
	
	/**
	 * Entfernt Einwirkung der übergebenen Schwerkraft auf alle lebendigen und zukünfigen Partikel dieses Partikelsystems.
	 * <p>Schwerkraft unterscheidet sich insofern von allgemeinen {@link #removeForce(Vector) Kräften}, als dass die
	 * {@link Particle#getMass() Masse des Partikels} auf sie keinen Einfluss hat.
	 * <p>Da intern ausschließlich Berechnungen stattfinden, sollte der Aufrufer selber sicherstellen, dass die zu
	 * entfernende Kraft vorher wirklich auf das Partikelsystem gewirkt hat.
	 * @param force Die Kraft
	 * @see #removeGravity(double)
	 */
	public void removeGravity(Vector gravity) {
		for(Particle p : _lifeParticles) {
			p.removeGravity(gravity);
		}
		_builder.removeGravity(gravity);
	}
	
	/**
	 * Gibt eine unveränderliche Sammlung aller lebendigen, von diesem Partikelsystem verwalteten Partikel zurück.
	 * @return Eine unveränderliche Sammlung der Partikel
	 */
	public Collection<Particle> getParticles() {
		return Collections.unmodifiableSet(_lifeParticles);
	}
	
	/**
	 * Gibt die maximale Anzahl an Partikel zurück, die von diesem Partikelsystem gleichzeitig verwaltet werden.
	 * @return Die maximale Anzahl Partikel dieses Partikelsystems
	 */
	public int getMaxParticlesCount() {
		return _maxParticles;
	}
	
	/**
	 * Gibt die Anzahl der gerade {@link Particle#isDead() lebendigen Partikel} zurück.
	 * @return Die Anzahl der lebendigen Partikel
	 */
	public int getLifeParticlesCount() {
		return _lifeParticles.size();
	}
	
	/**
	 * Gibt die Anzahl der gerade {@link Particle#isDead() toten Partikel} zurück.
	 * @return Die Anzahl der toten Partikel
	 */
	public int getDeadParticlesCount() {
		return _deadParticles.size();
	}
	
	/**
	 * Gibt zurück, wieviele Partikel von diesem Partikelsystem bereits gespawned wurden.
	 * @return Die Anzahl der bisher gespawnten Partikel
	 */
	public long getParticlesSpawnedCount() {
		return _spawnCount;
	}
	
	/**
	 * Prüft ob dieses Partikelsystem tot ist. Das heißt, dass es keine lebendigen Partikel mehr gibt und dieses
	 * Partikelsystem keine weiteren Partikel mehr spawnen möchte.
	 * @return {@code true} wenn dieses Partikelsystem tot ist, ansonsten {@code false}
	 */
	public boolean isDead() {
		return _spawnCapped && _lifeParticles.isEmpty() && _spawnCount >= _maxParticles;
	}
	
	/**
	 * Gibt zurück ob dieses Partikelsystem weitere Partikel spawned, auch nachdem es bereits die Maximalzahl an
	 * Partikeln gespawned erreicht hat.
	 * @return {@code true} wenn das Partikelsystem Partikel respawned, ansonsten {@code false}
	 * @see #getMaxParticlesCount()
	 * @see #getParticlesSpawnedCount()
	 */
	public boolean isSpawnCapped() {
		return _spawnCapped;
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
