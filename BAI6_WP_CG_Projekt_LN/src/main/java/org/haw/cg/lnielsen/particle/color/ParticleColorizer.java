package org.haw.cg.lnielsen.particle.color;

import org.haw.cg.lnielsen.particle.Particle;

/**
 * Schnittstelle für austauschbare Farbeffekte von Partikelsystemen.
 */
public interface ParticleColorizer {
	/**
	 * Aktualisiert die Farbe des übergebenen Partikels.
	 * <p>Implementationen dieser Methode sind dazu angehalten das Farb-Objekt des Partikels {@link Particle#getColor()}
	 * durch mutable Operationen zu verändern, da so der Speicherverbrauch reduziert werden kann. Aus diesem Grund
	 * besitzt die Methode auch nicht über einen Rückgabewert.
	 * @param p Der Partikel
	 */
	void updateColor(Particle p);
}
