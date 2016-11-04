package computergraphics.framework.datastructures.implicit_functions;

import computergraphics.framework.math.Vector;

public interface ImplicitFunction {
	double getValue(double x, double y, double z);
	
	double getValue(Vector v);
}
