package me.kx.redscript.types;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableStorage {
	private final HashMap<UUID, HashMap<String, ScriptVariable>> variables = new HashMap<>();

	public ScriptVariable getVariable(UUID player, String variable) {
		if (!variables.containsKey(player) || !variables.get(player).containsKey(variable)) {
			return null;
		}
		return variables.get(player).get(variable);
	}

	public void clearVariable(UUID player, String variable) {
		if (!variables.containsKey(player)) {
			return;
		}
		variables.get(player).remove(variable);
	}

	public void clearVariables(UUID player) {
		if (!variables.containsKey(player)) {
			return;
		}
		variables.remove(player);
	}

	public void setVariable(UUID player, String variable, String value) {
		if (!variables.containsKey(player)) {
			variables.put(player, new HashMap<>());
		}
		variables.get(player).put(variable, new ScriptVariable(value));
	}

	public String getVariables(UUID player) {
		if (!variables.containsKey(player)) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (String variable : variables.get(player).keySet()) {
			builder.append(variable).append("=").append(variables.get(player).get(variable).asString()).append("\n");
		}
		return builder.toString();
	}

	public String fillVariables(UUID player, String command) {
		// Matching variable definitions forwarding the form: "[<variable>=<value>]"
		Pattern pattern = Pattern.compile("\\$.+?\\$", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(command);
		
		while (matcher.find()) {
			String input = matcher.group();
			ScriptVariable variable = getVariable(player, input.substring(1, input.length() - 1));
			
			if (variable == null) continue;
			String value = variable.asString();
			
			command = command.replace(input, value);
		}
		return command;
	}
}
