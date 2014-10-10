package eu.mondo.mondix.implementation.hashmap;

/**
 * Abstract row interface for the generic implementation.
 *
 */
public interface AbstractRow {
	
	/**
	 * Access row values by their attribute.
	 * 
	 * @param key name of attribute
	 * @return attribute value of the row
	 */
	Object getValue(String key);
	
}
