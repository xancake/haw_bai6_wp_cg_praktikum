package org.haw.cg.lnielsen.particle.color.impl;

import java.util.Objects;
import org.haw.cg.lnielsen.particle.Particle;
import org.haw.cg.lnielsen.particle.color.impl.GradientColorizer.GradientPercentageSupplier;

public class PropertyPercentageSupplier implements GradientPercentageSupplier {
	private PropertySupplier _minSupplier;
	private PropertySupplier _maxSupplier;
	private PropertySupplier _valueSupplier;
	private boolean _reverse;
	
	public PropertyPercentageSupplier(PropertySupplier maxSupplier, PropertySupplier valueSupplier) {
		this(null, maxSupplier, valueSupplier, false);
	}
	
	public PropertyPercentageSupplier(PropertySupplier minSupplier, PropertySupplier maxSupplier, PropertySupplier valueSupplier, boolean reverse) {
		_minSupplier = minSupplier;
		_maxSupplier = Objects.requireNonNull(maxSupplier);
		_valueSupplier = Objects.requireNonNull(valueSupplier);
		_reverse = reverse;
	}
	
	@Override
	public double supplyPercentage(Particle p) {
		double min   = _minSupplier != null ? _minSupplier.getProperty(p) : 0;
		double max   = _maxSupplier.getProperty(p);
		double value = _valueSupplier.getProperty(p);
		double constrainedValue = Math.min(Math.max(value, min), max);
		double percentage = constrainedValue/(max-min);
		return _reverse ? 1-percentage : percentage;
	}
	
	public static interface PropertySupplier {
		double getProperty(Particle p);
	}
	
	public static class Builder {
		private PropertySupplier _minSupplier;
		private PropertySupplier _maxSupplier   = (p -> p.getStartLife());
		private PropertySupplier _valueSupplier = (p -> p.getLife());
		private boolean _reverse;
		
		public Builder withMin(PropertySupplier min) {
			_maxSupplier = min;
			return this;
		}
		
		public Builder withMax(PropertySupplier max) {
			_maxSupplier = Objects.requireNonNull(max);
			return this;
		}
		
		public Builder withValue(PropertySupplier value) {
			_valueSupplier = Objects.requireNonNull(value);
			return this;
		}
		
		public Builder reverseOrder(boolean reverse) {
			_reverse = reverse;
			return this;
		}
		
		public GradientPercentageSupplier build() {
			return new PropertyPercentageSupplier(_minSupplier, _maxSupplier, _valueSupplier, _reverse);
		}
	}
}
