package computergraphics.framework.rendering;

import java.util.Objects;
import computergraphics.framework.math.Vector;

public final class CGUtils {
	private CGUtils() {}
	
	/**
	 * Prüft ob der übergebene Vektor einen gültigen Farbvektor beschreibt und gibt eine Kopie des Vektors zurück,
	 * wenn das der Fall ist. Ansonsten wird eine {@link IllegalArgumentException} geworfen.
	 * <p>Ein gültiger Farbvektor hat entweder drei (R,G,B) oder vier (R,G,B,A) Dimensionen.
	 * @param color Der zu prüfende Farbvektor
	 * @return Eine Kopie des erfolgreich geprüften Vektors
	 * @throws IllegalArgumentException Wenn der Vektor nicht drei- oder vierdimensional ist
	 */
	public static Vector checkColorVector(Vector color) {
		switch(Objects.requireNonNull(color).getDimension()) {
			case 3: return new Vector(color.x(), color.y(), color.z(), 1);
			case 4: return new Vector(color);
			default:
				throw new IllegalArgumentException("Die Farbe muss als drei- oder vierdimensionaler Vektor übergeben werden!");
		}
	}
}
