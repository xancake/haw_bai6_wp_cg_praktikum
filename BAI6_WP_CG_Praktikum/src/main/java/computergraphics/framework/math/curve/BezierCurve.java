package computergraphics.framework.math.curve;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.basefunction.BernsteinPolynome;

public class BezierCurve implements Curve {
	private List<Vector> _controlPoints;
	private List<BernsteinPolynome> _baseFunctions;
	
	public BezierCurve(Vector... controlPoints) {
		this(Arrays.asList(controlPoints));
	}
	
	public BezierCurve(List<Vector> controlPoints) {
		Objects.requireNonNull(controlPoints);
		Vector.checkDimensionEqual(controlPoints.toArray(new Vector[0]));
		
		_controlPoints = controlPoints;
		_baseFunctions = BernsteinPolynome.createPolynome(_controlPoints.size()-1);
	}
	
	@Override
	public Vector calculatePoint(double t) {
		Vector result = new Vector(_controlPoints.get(0).getDimension());
		for(int i=0; i<_controlPoints.size(); i++) {
			Vector ci = _controlPoints.get(i);
			BernsteinPolynome Bi = _baseFunctions.get(i);
			result = result.add(ci.multiply(Bi.solve(t)));
		}
		return result;
	}
	
	@Override
	public Vector calculateTangent(double t) {
		double h = 0.00001;
		return calculatePoint(t+h).subtract(calculatePoint(t)).multiply(1/h).getNormalized();
	}
	
	@Override
	public List<Vector> getControlPoints() {
		return Collections.unmodifiableList(_controlPoints);
	}
	
	@Override
	public int getDegree() {
		return _controlPoints.size()-1;
	}
}
