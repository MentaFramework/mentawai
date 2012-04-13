package org.mentawai.rule;

import java.util.HashMap;
import java.util.Map;


/**
 * A rule to validate doubles.
 *
 * You can also specify min and max values.
 *
 * @author Helio Frota
 *
 * (idea by Emanuel Cordeiro)
 */
public class DoubleRule extends BasicRule {

	/**
	 * Attribute noMin of DoubleRule.
	 */
	private boolean noMin = true;
	/**
	 * Attribute noMax of DoubleRule.
	 */
	private boolean noMax = true;
	/**
	 * Attribute min of DoubleRule.
	 */
	private double min;
	/**
	 * Attribute max of DoubleRule.
	 */
	private double max;
	/**
	 * Attribute tokens of DoubleRule.
	 */
	private Map<String, String> tokens = new HashMap<String, String>();
	/**
	 * Attribute cache of DoubleRule.
	 */
	private static final Map<String, DoubleRule> cache = new HashMap<String, DoubleRule>();

	/**
	 * Default constructor.
	 */
	public DoubleRule() { }

    /**
     * Creates a IntegerRule with a min value.
     *
     * @param min The minimum value for the integer.
     */
	public DoubleRule(double min) {
		noMin = false;
		this.min = min;
		tokens.put("min", String.valueOf(min));
	}

    /**
     * Creates a IntegerRule with a min and max values.
     *
     * @param min The minium value for the integer.
     * @param max The maximum value for the integer.
     */
	public DoubleRule(double min, double max) {
		this(min);
		noMax = false;
		this.max = max;
		tokens.put("max", String.valueOf(max));
	}

	public static DoubleRule getInstance() {

		String key = "null_null";

		DoubleRule ir = cache.get(key);

		if (ir != null) return ir;

		ir = new DoubleRule();

		cache.put(key, ir);

		return ir;
	}

	/**
	 * Gets the instance.
	 * @param min double
	 * @return DoubleRule
	 */
	public static DoubleRule getInstance(double min) {

		StringBuilder sb = new StringBuilder(16);

		sb.append(min).append("_null");

		String key = sb.toString();

		DoubleRule ir = cache.get(key);

		if (ir != null) return ir;

		ir = new DoubleRule(min);

		cache.put(key, ir);

		return ir;
	}

	/**
	 * Gets the instance.
	 * @param min double
	 * @param max double
	 * @return DoubleRule
	 */
	public static DoubleRule getInstance(int min, int max) {

		StringBuilder sb = new StringBuilder(16);

		sb.append(min).append('_').append(max);

		String key = sb.toString();

		DoubleRule ir = cache.get(key);

		if (ir != null) return ir;

		ir = new DoubleRule(min, max);

		cache.put(key, ir);

		return ir;
	}

	public Map<String, String> getTokens() {
		return tokens;
	}

	/**
	 * Do the check.
	 */
	public boolean check(String value) {

		try {

			double x = Double.parseDouble(value);

			if (!noMin && x < min) return false;

			if (!noMax && x > max) return false;

			return true;

		} catch(Exception e) {

			return false;
		}
	}
}
