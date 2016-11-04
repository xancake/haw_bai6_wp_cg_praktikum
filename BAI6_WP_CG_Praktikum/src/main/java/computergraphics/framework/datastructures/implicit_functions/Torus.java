package computergraphics.framework.datastructures.implicit_functions;

import computergraphics.framework.math.Vector;

public class Torus implements ImplicitFunction {

	private double innerRadius;
	private double outerRadius;
	private Vector center;
	
	public Torus(double innerRadius, double outerRadius, Vector center) {
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.center = center;
	}

	@Override
	public double getValue(double x, double y, double z) {
		return getValue(new Vector(x, y, z));
	}

	@Override
	public double getValue(Vector v) {
		Vector squared = center.add(v).power(2);
		return sqr(squared.sum() + sqr(outerRadius) - sqr(innerRadius))
				- 4 * sqr(outerRadius) * (squared.x() + squared.y());
	}

	private double sqr(double x) {
		return Math.pow(x, 2);
	}
}
