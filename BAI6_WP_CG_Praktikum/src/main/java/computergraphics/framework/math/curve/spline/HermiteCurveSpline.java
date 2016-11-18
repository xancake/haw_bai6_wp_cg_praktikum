package computergraphics.framework.math.curve.spline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
import computergraphics.framework.math.curve.HermiteCurve;

public class HermiteCurveSpline implements Curve {
	private List<Vector> _controlPoints;
	private List<Curve> _curves;
	
	public HermiteCurveSpline(Vector... controlPoints) {
		this(Arrays.asList(controlPoints));
	}
	
	public HermiteCurveSpline(List<Vector> controlPoints) {
		_controlPoints = Objects.requireNonNull(controlPoints);
		_curves = new ArrayList<>();
		int n = controlPoints.size()-1;
		for(int i=0; i<n; i++) {
			Vector p0 = controlPoints.get(i);
			Vector p1 = controlPoints.get(i+1);
			Vector m0 = (i==0)   ? new Vector(3) : p1.subtract(controlPoints.get(i-1)).getNormalized();
			Vector m1 = (i==n-1) ? new Vector(3) : controlPoints.get(i+2).subtract(p0).getNormalized();
			_curves.add(new HermiteCurve(p0, m0, m1, p1));
		}
	}
	
	@Override
	public Vector calculatePoint(double t) {
		int i = (int)(t>=_curves.size() ? t-1 : t);
		Curve curve = _curves.get(i);
		double relativeT = t-i;
		return curve.calculatePoint(relativeT);
	}
	
	@Override
	public Vector calculateTangent(double t) {
		double h = 0.00001;
		return calculatePoint(t+h).subtract(calculatePoint(t)).multiply(1/h).getNormalized();
	}
	
	@Override
	public List<Vector> getControlPoints() {
		return _controlPoints;
//		List<Vector> controlPoints = new ArrayList<>();
//		for(Curve curve : _curves) {
//			controlPoints.addAll(curve.getControlPoints());
//		}
//		return controlPoints;
	}
	
	@Override
	public int getDegree() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double getMaxT() {
		return _curves.size();
	}
}
