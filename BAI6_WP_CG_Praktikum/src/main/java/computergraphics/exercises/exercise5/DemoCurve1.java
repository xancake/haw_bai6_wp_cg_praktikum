package computergraphics.exercises.exercise5;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.BezierCurve;
import computergraphics.framework.math.curve.Curve;

public class DemoCurve1 {
	public static void main(String[] args) {
		Curve curve = new BezierCurve(
				new Vector(0, 0, 0),
				new Vector(0.25, 1, 0),
				new Vector(0.75, 0, 0),
				new Vector(1, 1, 0)
		);
		new CurveShowcaseScene(curve);
	}
}
