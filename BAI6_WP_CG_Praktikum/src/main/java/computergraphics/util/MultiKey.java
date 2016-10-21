package computergraphics.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MultiKey {

	private Set<Object> _keys;

	public MultiKey(Object... keys) {
		_keys = new HashSet<>(Arrays.asList(keys));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_keys == null) ? 0 : _keys.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiKey other = (MultiKey) obj;
		if (_keys == null) {
			if (other._keys != null)
				return false;
		} else if (!_keys.equals(other._keys))
			return false;
		return true;
	}
}