package eu.mondo.mondix.client;

import java.util.List;

import eu.mondo.mondix.live.IChangeCallback;
import eu.mondo.mondix.live.ILiveQueryInstance;

public class MyChangeCallback implements IChangeCallback {
	StringBuilder notificationsStr = new StringBuilder();
	
	@Override
	public void changed(ILiveQueryInstance query, boolean inserted,	List<?> changedTuple) {
		String message = "Query: " + query.hashCode() + "; inserted: " + inserted + "; tuple: " + changedTuple.toString() + "\n";
		notificationsStr.append(message);
	}
	
	public StringBuilder getNotificationsStr() {
		return notificationsStr;
	}
}
