package eu.mondo.mondix.implementation.hashmap;

import java.util.ArrayList;

public interface AbstractRow {
	Object getValue(String key);
	ArrayList<String> getColumns();
}
