package computergraphics.framework.datastructures;

import java.util.Objects;

public class Pair<K, V> {
	private K _key;
	private V _value;
	
	public Pair(K key, V value) {
		_key = Objects.requireNonNull(key);
		_value = Objects.requireNonNull(value);
	}
	
	public static <K, V> Pair<K, V> pair(K key, V value) {
		return new Pair<K, V>(key, value);
	}
	
	public K getKey() {
		return _key;
	}
	
	public V getValue() {
		return _value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_key == null) ? 0 : _key.hashCode());
		result = prime * result + ((_value == null) ? 0 : _value.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof Pair)) {
			return false;
		}
		Pair<?, ?> other = (Pair<?, ?>)obj;
		if(_key == null) {
			if(other._key != null) {
				return false;
			}
		} else if(!_key.equals(other._key)) {
			return false;
		}
		if(_value == null) {
			if(other._value != null) {
				return false;
			}
		} else if(!_value.equals(other._value)) {
			return false;
		}
		return true;
	}
}
