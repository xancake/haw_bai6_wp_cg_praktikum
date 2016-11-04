package computergraphics.framework.datastructures.implicit_functions;

import java.util.Objects;

import computergraphics.framework.math.Vector;

public class Kugel implements ImplicitFunction {
	
	private double radius;
	
	private Vector center;
	
	public Kugel(double radius, Vector center) {
		this.radius = radius;
		this.center = Objects.requireNonNull(center);
	}

	@Override
	public double getValue(double x, double y, double z) {
		return getValue(new Vector(x, y, z));
	}

	@Override
	public double getValue(Vector v) {
		return center.add(v).power(2).sum() - Math.pow(radius, 2);
	}
}
