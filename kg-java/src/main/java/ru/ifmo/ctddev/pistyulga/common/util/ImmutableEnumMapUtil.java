package ru.ifmo.ctddev.pistyulga.common.util;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class ImmutableEnumMapUtil {
	private ImmutableEnumMapUtil() {}
	
	@SuppressWarnings("serial")
	public static <K extends Enum<K>, V> EnumMap<K, V> getInstance(EnumMap<K, V> src) {
		return new EnumMap<K, V>(src) {
			@Override
			public V put(K key, V value) { throw new UnsupportedOperationException(); }
			@Override
			public void putAll(Map<? extends K, ? extends V> m) { throw new UnsupportedOperationException(); }
			@Override
			public V remove(Object key) { throw new UnsupportedOperationException(); }
			@Override
			public void clear() { throw new UnsupportedOperationException(); }
			@Override
			public Set<K> keySet() { return Collections.unmodifiableSet(super.keySet()); }
			@Override
			public Set<Entry<K, V>> entrySet() { return Collections.unmodifiableSet(super.entrySet()); }
			@Override
			public Collection<V> values() { return Collections.unmodifiableCollection(super.values()); }
		};
	}
}
