package computergraphics.framework.math.curve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.basefunction.BaseFunction;
import computergraphics.framework.math.curve.basefunction.BaseFunctions;

public class HermiteCurve extends AbstractCurve implements Curve {
	public HermiteCurve(Vector p0, Vector m0, Vector m1, Vector p1) {
		this(Arrays.asList(p0, m0, m1, p1));
	}
	
	private HermiteCurve(List<Vector> controlPoints) {
		super(controlPoints, BaseFunctions.createHermiteFunctions3());
	}
	
	@Override
	public Vector calculatePoint(double t) {
		Vector result = new Vector(_controlPoints.get(0).getDimension());
		for(int i=0; i<_controlPoints.size(); i++) {
			Vector ci = _controlPoints.get(i);
			BaseFunction Bi = _baseFunctions.get(i);
//			if(i==0 || i==_controlPoints.size()-1) {
				result = result.add(ci.multiply(Bi.solve(t)));
//			} else {
//				result = result.add(ci.multiply(Bi.derive(t)));
//			}
		}
		return result;
	}
	
	@Override
	public List<Vector> getControlPoints() {
		List<Vector> controlPoints = new ArrayList<>();
		int n = _controlPoints.size()-1;
		for(int i=0; i<=n; i++) {
			if(i==0 || i==n) {
				controlPoints.add(_controlPoints.get(i));
			} else {
				Vector ci = _controlPoints.get(i);
				ci = ci.add(_controlPoints.get(i<_controlPoints.size()/2 ? 0 : n));
				controlPoints.add(ci);
			}
		}
		return controlPoints;
	}
}
