package me.kx.redscript.utils;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class SafeList<A> extends ArrayList<A> {
	private static final long serialVersionUID = 1L;

	public SafeList(List<A> list) {
		super(list);
	}

	public A getOrDefault(int index, A defaultValue) {
		if(index < 0 || index >= size()) {
			return defaultValue;
		}
		return get(index);
	}
}
