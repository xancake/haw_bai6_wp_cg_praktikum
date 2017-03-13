package org.haw.cg.lnielsen.particle.physics;

import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.util.Numbers;
import computergraphics.framework.math.Vector;

public class Repeller {
	private Vector _location;
	private double _range;
	private double _power;
	
	public Repeller(Vector location, double range, double power) {
		setLocation(location);
		setRange(range);
		setPower(power);
	}
	
	public boolean isInRange(Particle p) {
		Vector direction = _location.subtract(p.getLocation());
		double distance = direction.getNorm();
		return distance <= _range;
	}
	
	public Vector calculateRepellForce(Particle p) {
		Vector direction = _location.subtract(p.getLocation());
		double distance = direction.getNorm();
		if(distance <= _range) {
			distance = Math.max(1, Math.min(distance, 100));
			direction.normalize();
			double force = -1 * _power / (distance * distance);
			direction.multiplySelf(force);
			return direction;
		} else {
			return new Vector(0, 0, 0);
		}
	}
	
	public Vector getLocation() {
		return new Vector(_location);
	}
	
	public double getRange() {
		return _range;
	}
	
	public double getPower() {
		return _power;
	}
	
	public void setLocation(Vector location) {
		_location = Vector.checkDimension(location, 3);
	}
	
	public void setRange(double range) {
		_range = Numbers.require(range).greaterThanOrEqual(0, "Die Reichweite eines Repellers muss positiv sein!");
	}
	
	public void setPower(double power) {
		_power = power;
	}
}
