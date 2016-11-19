package computergraphics.exercises.exercise5;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.Curve;
import computergraphics.framework.math.curve.spline.HermiteCurveSpline;

public class HermiteCurveSplineDemo {
	public static void main(String[] args) {
		Curve curve = new HermiteCurveSpline(
				new Vector(-2, -0.5, 0),
				new Vector(-1,  0.5, 0),
				new Vector( 0, -0.5, 0),
				new Vector( 1,  0.5, 0),
				new Vector( 2, -0.5, 0)
		);
		
		new CurveShowcaseScene(curve);
	}
}
