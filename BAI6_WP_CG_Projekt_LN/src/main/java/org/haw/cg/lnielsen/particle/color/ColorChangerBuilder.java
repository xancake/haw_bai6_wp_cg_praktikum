package org.haw.cg.lnielsen.particle.color;

public final class ColorChangerBuilder {
	public static TwoColorGradient.Builder twoColorGradient() {
		return new TwoColorGradient.Builder();
	}
	
	public static SpeedVisualizer.Builder speedVisualizer() {
		return new SpeedVisualizer.Builder();
	}
	
	public static MultiColorGradient.Builder multiColorGradient() {
		return new MultiColorGradient.Builder();
	}
}
