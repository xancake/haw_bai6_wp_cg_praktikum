package computergraphics.framework.math.curve;

import java.util.Arrays;
import java.util.List;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.basefunction.BaseFunction;
import computergraphics.framework.math.curve.basefunction.BaseFunctions;

public class BezierCurve extends AbstractCurve implements Curve {
	public BezierCurve(Vector... controlPoints) {
		this(Arrays.asList(controlPoints));
	}
	
	public BezierCurve(List<Vector> controlPoints) {
		super(controlPoints, BaseFunctions.createBernsteinPolynome(controlPoints.size()-1));
	}
	
	@Override
	public Vector calculatePoint(double t) {
		Vector result = new Vector(_controlPoints.get(0).getDimension());
		for(int i=0; i<_controlPoints.size(); i++) {
			Vector ci = _controlPoints.get(i);
			BaseFunction Bi = _baseFunctions.get(i);
			result = result.add(ci.multiply(Bi.solve(t)));
		}
		return result;
	}
}
