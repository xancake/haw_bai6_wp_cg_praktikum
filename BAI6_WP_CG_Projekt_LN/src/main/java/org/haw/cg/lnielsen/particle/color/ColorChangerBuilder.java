package org.haw.cg.lnielsen.particle.color;

/**
 * Einstiegspunkt für unterschiedliche {@link ParticleColorChanger}-Builder.
 */
public final class ColorChangerBuilder {
	/**
	 * Liefert einen Builder für einen {@link TwoColorGradient} zurück. Es können eine Start- und Endfarbe definiert
	 * werden, zwischen denen über den Lebenszyklus von Partikeln interpoliert werden soll.
	 * @return Der Builder
	 */
	public static TwoColorGradient.Builder twoColorGradient() {
		return new TwoColorGradient.Builder();
	}
	
	/**
	 * Liefert einen Builder für {@link SpeedVisualizer} zurück. Er visualisiert farblich kodiert die aktuelle
	 * Geschwindigkeit von Partikeln. Es können eine minimale und maximale Geschwindigkeit und entsprechende Farben dazu
	 * definiert werden. Werte die dazwischen liegen werden interpoliert.
	 * @return Der Builder
	 */
	public static SpeedVisualizer.BoundedBuilder boundedSpeedVisualizer() {
		return new SpeedVisualizer.BoundedBuilder();
	}
	
	/**
	 * Liefert einen Builder für {@link SpeedVisualizer} zurück. Er visualisiert farblich kodiert die aktuelle
	 * Geschwindigkeit von Partikeln. Es kann eine Basisgeschwindigkeit definiert werden, von welchen aus der
	 * {@link SpeedVisualizer} seine minimale und maximale Geschwindigkeit anpasst, wenn Partikel mit extremeren Werten
	 * auftreten. Werte die zwischen den Grenzen liegen werden interpoliert.
	 * @return Der Builder
	 */
	public static SpeedVisualizer.AdaptiveBuilder adaptiveSpeedVisualizer() {
		return new SpeedVisualizer.AdaptiveBuilder();
	}
	
	/**
	 * Liefert einen Builder für {@link MultiColorGradient} zurück. Es können Kontrollpunkte auf der Lebensleiste von
	 * Partikeln festgelegt werden zu denen ein Partikel eine bestimmte Farbe haben soll. Dieser Builder bietet sich an,
	 * wenn der Farbverlauf nicht gleichmäßig geschehen soll. Für gleichmäßige Farbverläufe ist
	 * {@link #uniformMultiColorGradient()} zu empfehlen.
	 * @return Der Builder
	 */
	public static MultiColorGradient.ControlPointGradientBuilder multiColorGradient() {
		return new MultiColorGradient.ControlPointGradientBuilder();
	}
	
	/**
	 * Liefert einen Builder für {@link MultiColorGradient} zurück. Es können Farben definiert werden über die
	 * gleichmäßig interpoliert wird. Somit braucht im Gegensatz zu {@link #multiColorGradient()} kein Punkt auf der
	 * Lebensleiste von Partikeln definiert werden, da die Umrechnung intern geschieht.
	 * @return Der Builder
	 */
	public static MultiColorGradient.UniformGradientBuilder uniformMultiColorGradient() {
		return new MultiColorGradient.UniformGradientBuilder();
	}
}
