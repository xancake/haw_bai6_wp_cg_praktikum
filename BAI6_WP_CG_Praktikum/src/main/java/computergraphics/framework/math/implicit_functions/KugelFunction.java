package computergraphics.framework.math.implicit_functions;

import computergraphics.framework.math.Vector;

public class KugelFunction extends AbstractImplicitFunction implements ImplicitFunction {
	private double _radius;
	
	public KugelFunction(double radius, Vector center) {
		super(center, 0);
		this._radius = radius;
	}
	
	@Override
	public double calculateValue(Vector v) {
		return v.power(2).sum() - Math.pow(_radius, 2);
	}
}
