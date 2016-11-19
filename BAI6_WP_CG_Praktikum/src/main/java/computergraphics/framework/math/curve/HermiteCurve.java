package computergraphics.framework.math.curve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.basefunction.BaseFunctions;

public class HermiteCurve extends AbstractCurve implements Curve {
	public HermiteCurve(Vector p0, Vector m0, Vector m1, Vector p1) {
		super(Arrays.asList(p0, m0, m1, p1), BaseFunctions.createHermiteFunctions3());
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
