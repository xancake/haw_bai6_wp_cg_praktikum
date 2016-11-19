package computergraphics.framework.math.curve;

import java.util.Arrays;
import java.util.List;
import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.basefunction.BaseFunctions;

public class BezierCurve extends AbstractCurve implements Curve {
	public BezierCurve(Vector... controlPoints) {
		this(Arrays.asList(controlPoints));
	}
	
	public BezierCurve(List<Vector> controlPoints) {
		super(controlPoints, BaseFunctions.createBernsteinPolynome(controlPoints.size()-1));
	}
}
