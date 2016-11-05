package computergraphics.framework.datastructures.implicit_functions;

import static java.lang.Math.*;

import computergraphics.framework.math.Vector;

public class SteinerscheRoemischeFlaecheFunction extends AbstractImplicitFunction implements ImplicitFunction {
	public SteinerscheRoemischeFlaecheFunction(Vector center) {
		super(center, 0);
	}
	
	@Override
	protected double calculateValue(Vector v) {
		double x = v.x();
		double y = v.y();
		double z = v.z();
		return pow(x, 2)*pow(y, 2) + pow(x, 2)*pow(z, 2) + pow(z, 2)*pow(y, 2) + x*y*z;
	}
}
