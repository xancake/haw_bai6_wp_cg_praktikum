package computergraphics.framework.math.curve;

import java.util.List;
import computergraphics.framework.math.Vector;

/**
 * Schnittstelle für Kurven.
 */
public interface Curve {
	/**
	 * Berechnet die Koordinate des Kurvenpunktes an dem Parameter {@code t}.
	 * @param t Der Parameter. Muss im Intervall 0 bis {@link #getMaxT()} liegen.
	 * @return Die Koordinate des Kurvenpunktes
	 */
	Vector calculatePoint(double t);
	
	/**
	 * Berechnet den Tangentialvektor an dem Parameter {@code t}.
	 * @param t Der Parameter. Muss im Intervall 0 bis {@link #getMaxT()} liegen.
	 * @return Der Tangentialvektor
	 */
	Vector calculateTangent(double t);
	
	/**
	 * Gibt eine Liste der Kontrollpunkte dieser Kurve zurück.
	 * @return Eine unveränderliche Liste der Kontrollpunkte
	 */
	List<Vector> getControlPoints();
	
	/**
	 * Gibt den Grad der Kurve zurück.
	 * @return Der Grad
	 */
	int getDegree();
	
	/**
	 * Gibt den maximalen Parameterwert für {@code t} zurück, der für diese Kurve erlaubt ist.
	 * @return Der Parameterwert für {@code t}
	 */
	double getMaxT();
}
