package computergraphics.framework.math.curve;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import computergraphics.framework.math.Vector;
import computergraphics.framework.math.curve.basefunction.BaseFunction;

public abstract class AbstractCurve implements Curve {
	protected List<Vector> _controlPoints;
	protected List<? extends BaseFunction> _baseFunctions;
	
	public AbstractCurve(List<Vector> controlPoints, List<? extends BaseFunction> baseFunctions) {
		Objects.requireNonNull(controlPoints);
		Vector.checkDimensionEqual(controlPoints.toArray(new Vector[0]));
		if(controlPoints.size() != baseFunctions.size()) {
			throw new IllegalArgumentException("Für jeden Kontrollpunkt wird eine Basisfunktion benötigt!");
		}
		_controlPoints = controlPoints;
		_baseFunctions = baseFunctions;
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
	public final int getDegree() {
		return _controlPoints.size()-1;
	}
	
	@Override
	public double getMaxT() {
		return 1;
	}
}
