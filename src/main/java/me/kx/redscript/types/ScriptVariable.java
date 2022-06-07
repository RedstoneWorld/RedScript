package me.kx.redscript.types;

import java.util.Arrays;
import java.util.List;

public class ScriptVariable {
	private final String value;
	public ScriptVariable(String value) {
		this.value = value;
	}

	public int asInt() {
		return Integer.parseInt(value);
	}

	public String asString() {
		return value;
	}

	public boolean asBoolean() {
		return Boolean.parseBoolean(value);
	}

	public double asDouble() {
		return Double.parseDouble(value);
	}

	public float asFloat() {
		return Float.parseFloat(value);
	}

	public long asLong() {
		return Long.parseLong(value);
	}

	public List<String> asList(String delimiter) {
		return Arrays.asList(value.split(delimiter));
	}

	public List<String> asList() {
		return asList(",");
	}
}
