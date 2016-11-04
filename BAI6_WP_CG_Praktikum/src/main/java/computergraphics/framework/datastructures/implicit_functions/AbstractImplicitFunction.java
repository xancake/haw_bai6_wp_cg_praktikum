package computergraphics.framework.datastructures.implicit_functions;

import java.util.Objects;

import computergraphics.framework.math.Vector;

public abstract class AbstractImplicitFunction implements ImplicitFunction {
	private Vector _center;
	
	public AbstractImplicitFunction(Vector center) {
		_center = Objects.requireNonNull(center);
	}
	
	@Override
	public final double getValue(double x, double y, double z) {
		return getValue(new Vector(x, y, z));
	}

	@Override
	public final double getValue(Vector v) {
		return calculateValue(_center.add(v));
	}
	
	protected abstract double calculateValue(Vector scaledVector);
}
