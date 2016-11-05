package computergraphics.framework.datastructures.implicit_functions;

import static java.lang.Math.*;

import computergraphics.framework.math.Vector;

public class BoyscheFlaecheFunction extends AbstractImplicitFunction implements ImplicitFunction {
	public BoyscheFlaecheFunction(Vector center) {
		super(center, 0);
	}
	
	@Override
	public double calculateValue(Vector v) {
		double x = v.x();
		double y = v.y();
		double z = v.z();
		
		return    64 * cube(1-z) * cube(z)
				- 48 * sqr(1-z) * sqr(z) * (3*sqr(x) + 3*sqr(y) + 2*sqr(z))
				+ 12 * (1-z) * z * (
						27 * sqr(sqr(x) + sqr(y))
						- 24 * sqr(z) * (sqr(x) + sqr(y))
						+ 36 * sqrt(2) * y * z * (sqr(y) - 3*sqr(x))
						+ 4 * quad(z)
				)
				+ (9*sqr(x) + 9*sqr(y) - 2*sqr(z)) * (
						-81 * sqr(sqr(x) + sqr(y))* -72 * sqr(z) * (sqr(x) + sqr(y))
						+ 108 * sqrt(2) * x * z * (sqr(x) - 3*sqr(y)) + 4*quad(z)
				);
	}
	
	private double sqr(double x) {
		return pow(x, 2);
	}
	
	private double cube(double x) {
		return pow(x, 3);
	}
	
	private double quad(double x) {
		return pow(x, 4);
	}
}
