package computergraphics.framework.math.implicit_functions;

import computergraphics.framework.math.Vector;

public class TorusFunction extends AbstractImplicitFunction implements ImplicitFunction {
	private double _innerRadius;
	private double _outerRadius;
	
	public TorusFunction(double innerRadius, double outerRadius, Vector center) {
		super(center, 0);
		_innerRadius = innerRadius;
		_outerRadius = outerRadius;
	}

	@Override
	public double calculateValue(Vector v) {
		Vector squared = v.power(2);
		return sqr(squared.sum() + sqr(_outerRadius) - sqr(_innerRadius))
				- 4 * sqr(_outerRadius) * (squared.x() + squared.y());
	}

	private double sqr(double x) {
		return Math.pow(x, 2);
	}
}
