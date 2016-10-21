package computergraphics.framework.datastructures;

import java.util.Objects;

public class Pair<K, V> {
	private K _key;
	private V _value;
	
	public Pair(K key, V value) {
		_key = Objects.requireNonNull(key);
		_value = Objects.requireNonNull(value);
	}
	
	public K getKey() {
		return _key;
	}
	
	public V getValue() {
		return _value;
	}
}
