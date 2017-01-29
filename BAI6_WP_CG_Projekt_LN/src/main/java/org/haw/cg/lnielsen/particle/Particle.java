package org.haw.cg.lnielsen.particle;

import org.haw.cg.lnielsen.util.Numbers;
import computergraphics.framework.math.Vector;
import computergraphics.framework.rendering.CGUtils;

/**
 * Diese Klasse repräsentiert ein Partikel.
 */
public class Particle {
	private Vector _location     = new Vector(0,0,0);
	private Vector _velocity     = new Vector(0,0,0);
	private Vector _acceleration = new Vector(0,0,0);
	
	private double _mass;
	
	private Vector _color        = new Vector(0,0,0,1);
	private boolean _fadeOut;
	
	private long _startLife;
	private long _life;
	
	/**
	 * Aktualisiert dieses Partikel. Dabei wird das {@link #getLife() Leben} des Partikels reduziert, es wird die neue
	 * {@link #getVelocity() Geschwindigkeit} und {@link #getLocation() Position} aktualisiert und wenn das Partikel
	 * {@link #isFadeOut() ausblenden} soll wird der Alpha-Wert der {@link #getColor() Farbe} reduziert.
	 * @param millis Die seit dem letzten Update vergangenen Millisekunden
	 */
	public void update(long millis) {
		_life -= millis;
		
		double updateSeconds = millis / 1000.0;
		_velocity = _velocity.add(_acceleration.multiply(updateSeconds));
		_location = _location.add(_velocity.multiply(updateSeconds));
		
		if(_fadeOut) {
			_color.set(3, (double)getLife() / getStartLife());
		}
	}
	
	/**
	 * Lässt die übergebene Kraft auf diesen Partikel einwirken. Dabei wird die aktuell auf das Partikel einwirkende
	 * Beschleunigung mit der übergebenen Kraft verrechnet.
	 * @param force Die Kraft
	 */
	public void applyForce(Vector force) {
		_acceleration = _acceleration.add(force.multiply(1/_mass));
	}
	
	/**
	 * Sorgt dafür, dass die übergebene Kraft nicht mehr auf diesen Partikel einwirkt. Dabei wird die aktuell auf das
	 * Partikel einwirkende Beschleunigung mit der übergebenen Kraft verrechnet.
	 * <p>Da intern ausschließlich Berechnungen stattfinden, sollte der Aufrufer selber sicherstellen, dass die zu
	 * entfernende Kraft vorher wirklich auf das Partikel gewirkt hat.
	 * @param force Die Kraft
	 */
	public void removeForce(Vector force) {
		_acceleration = _acceleration.subtract(force.multiply(1/_mass));
	}
	
	public Vector getLocation() {
		return _location;
	}
	
	public Vector getVelocity() {
		return _velocity;
	}
	
	public Vector getAcceleration() {
		return _acceleration;
	}
	
	public double getMass() {
		return _mass;
	}
	
	public Vector getColor() {
		return _color;
	}
	
	public boolean isFadeOut() {
		return _fadeOut;
	}
	
	public long getStartLife() {
		return _startLife;
	}
	
	public long getLife() {
		return _life;
	}
	
	public boolean isDead() {
		return _life < 0;
	}
	
	public void setLocation(Vector location) {
		_location = Vector.checkDimension(location, 3);
	}
	
	public void setVelocity(Vector velocity) {
		_velocity = Vector.checkDimension(velocity, 3);
	}
	
	public void setAcceleration(Vector acceleration) {
		_acceleration = Vector.checkDimension(acceleration, 3);
	}
	
	public void setMass(double mass) {
		_mass = Numbers.require(mass).greaterThanOrEqual(0, "Die Masse eines Partikels muss positiv sein!");
	}
	
	public void setColor(Vector color) {
		_color = CGUtils.checkColorVector(color);
	}
	
	public void setFadeOut(boolean fadeOut) {
		_fadeOut = fadeOut;
	}
	
	public void setStartLife(long lifeMS) {
		_startLife = Numbers.require(lifeMS).greaterThanOrEqual(0, "Die Lebenszeit eines Partikels muss positiv sein!");
	}
	
	public void setLife(long lifeMS) {
		if(lifeMS < 0) {
			throw new IllegalArgumentException("Die Lebenszeit eines Partikels muss positiv sein!");
		}
		if(_startLife < lifeMS) {
			_startLife = lifeMS;
		}
		_life = lifeMS;
	}
	
	/**
	 * Ein Builder zum Erzeugen und Wiederverwenden von Partikel-Objekten. Über die {@code withXXX}-Methoden können die
	 * gewünschten Eigenschaften zu erzeugender Partikel vorgegeben werden. Mit {@link #build()} wird ein Partikel
	 * erzeugt und durch {@link #initialize(Particle)} kann ein bereits existierender Partikel erneut initialisiert
	 * werden um ihn so weiterzuverwenden.
	 */
	public static class Builder {
		private Vector _locationFrom     = new Vector(0,0,0);
		private Vector _locationTo       = new Vector(0,0,0);
		private Vector _velocityFrom     = new Vector(0,0,0);
		private Vector _velocityTo       = new Vector(0,0,0);
		private Vector _accelerationFrom = new Vector(0,0,0);
		private Vector _accelerationTo   = new Vector(0,0,0);
		
		private double _massFrom         = 1;
		private double _massTo           = 1;
		
		private Vector _force            = new Vector(0,0,0);
		
		private Vector _colorFrom        = new Vector(0,0,0,1);
		private Vector _colorTo          = new Vector(0,0,0,1);
		private boolean _fadeOut;
		
		private long _startLifeFrom;
		private long _startLifeTo;
		
		public Builder() {}
		
		public Builder withStartLife(long startLifeFrom, long startLifeTo) {
			_startLifeFrom = Numbers.require(startLifeFrom).greaterThanOrEqual(0, "Die Lebenszeit eines Partikels muss positiv sein!");
			_startLifeTo   = Numbers.require(startLifeTo).greaterThanOrEqual(0, "Die Lebenszeit eines Partikels muss positiv sein!");
			return this;
		}
		
		public Builder withLocation(Vector locationFrom, Vector locationTo) {
			_locationFrom = Vector.checkDimension(locationFrom, 3);
			_locationTo   = Vector.checkDimension(locationTo,   3);
			return this;
		}
		
		public Builder withVelocity(Vector velocityFrom, Vector velocityTo) {
			_velocityFrom = Vector.checkDimension(velocityFrom, 3);
			_velocityTo   = Vector.checkDimension(velocityTo,   3);
			return this;
		}
		
		public Builder withAcceleration(Vector accelerationFrom, Vector accelerationTo) {
			_accelerationFrom = Vector.checkDimension(accelerationFrom, 3);
			_accelerationTo   = Vector.checkDimension(accelerationTo,   3);
			return this;
		}
		
		public Builder withMass(double massFrom, double massTo) {
			_massFrom = Numbers.require(massFrom).greaterThanOrEqual(0, "Die Masse eines Partikels muss positiv sein!");
			_massTo   = Numbers.require(massTo).greaterThanOrEqual(0, "Die Masse eines Partikels muss positiv sein!");
			return this;
		}
		
		public Builder withColor(Vector colorFrom, Vector colorTo) {
			_colorFrom = CGUtils.checkColorVector(colorFrom);
			_colorTo   = CGUtils.checkColorVector(colorTo);
			return this;
		}
		
		public Builder withStartLife(long startLife) {
			return withStartLife(startLife, startLife);
		}
		
		public Builder withLocation(Vector location) {
			return withLocation(location, location);
		}
		
		public Builder withVelocity(Vector velocity) {
			return withVelocity(velocity, velocity);
		}
		
		public Builder withAcceleration(Vector acceleration) {
			return withAcceleration(acceleration, acceleration);
		}
		
		public Builder withMass(double mass) {
			return withMass(mass, mass);
		}
		
		public Builder withColor(Vector color) {
			return withColor(color, color);
		}
		
		public Builder withFadeOut(boolean fadeOut) {
			_fadeOut = fadeOut;
			return this;
		}
		
		public Builder addForce(Vector force) {
			_force = _force.add(force);
			return this;
		}
		
		/**
		 * Erzeugt ein Partikel und initialisiert es zufällig im Rahmen der an diesem Builder konfigurierten Parameter.
		 * @return Das erzeugte Partikel
		 */
		public Particle build() {
			Particle particle = new Particle();
			initialize(particle);
			return particle;
		}
		
		/**
		 * Initialisiert das übergebene Partikel zufällig im Rahmen der an diesem Builder konfigurierten Parameter.
		 * Das erlaubt die Wiederverwendung von z.B. gestorbenen Partikeln um Speicherplatz zu sparen.
		 * @param particle Das zu initialisierende Partikel
		 */
		public void initialize(Particle particle) {
			long lifeMS = (long)(Math.random() * (_startLifeTo - _startLifeFrom) + _startLifeFrom);
			particle.setStartLife(lifeMS);
			particle.setLife(lifeMS);
			particle.setLocation(Vector.random(_locationFrom, _locationTo));
			particle.setVelocity(Vector.random(_velocityFrom, _velocityTo));
			particle.setAcceleration(Vector.random(_accelerationFrom, _accelerationTo));
			particle.setMass(Math.random() * (_massTo - _massFrom) + _massTo);
			particle.setColor(Vector.random(_colorFrom, _colorTo));
			particle.setFadeOut(_fadeOut);
			particle.applyForce(_force);
		}
	}
}
