package computergraphics.framework.math.curve.basefunction;

/**
 * Schnittstelle für Basisfunktionen.
 */
public interface BaseFunction {
	/**
	 * Berechnet den Wert dieser Basisfunktion an dem Parameter {@code t}.
	 * @param t Der Parameter
	 * @return Der Wert dieser Basisfunktion
	 */
	double solve(double t);
}
