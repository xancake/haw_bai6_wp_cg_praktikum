package computergraphics.framework.math;

import java.util.Objects;

public class HessescheEbene {
	private Vector _base;
	private Vector _normal;
	
	public HessescheEbene(Vector base, Vector normal) {
		_base = Objects.requireNonNull(base);
		_normal = Objects.requireNonNull(normal);
	}
	
	/**
	 * Berechnet das Lambda für den Schnitt eines Strahls mit dieser Ebene.
	 * @param origin Der Ursprungspunkt des Strahls
	 * @param direction Der Richtungsvektor des Strahls
	 * @return Das berechnete Lambda oder {@link Double#NaN}, wenn der Strahl diese Ebene nicht schneidet
	 */
	public double calculateLambda(Vector origin, Vector direction) {
		double divisor = _normal.multiply(direction);
		if(divisor == 0) {
			return Double.NaN;
		}
		return (_normal.multiply(_base) - _normal.multiply(origin)) / divisor;
	}
}
