package computergraphics.framework.math.implicit_functions;

import computergraphics.framework.math.Vector;

import static java.lang.Math.*;

public class EllipsoidFunction extends AbstractImplicitFunction implements ImplicitFunction {
	private double _alpha;
	private double _beta;
	private double _gamma;
	
	public EllipsoidFunction(double alpha, double beta, double gamma, Vector center) {
		super(center, 1);
		_alpha = alpha;
		_beta = beta;
		_gamma = gamma;
	}
	
	@Override
	protected double calculateValue(Vector v) {
		return pow(v.x(), 2) / pow(_alpha, 2)
				+ pow(v.y(), 2) / pow(_beta, 2)
				+ pow(v.z(), 2) / pow(_gamma, 2);
	}
}
