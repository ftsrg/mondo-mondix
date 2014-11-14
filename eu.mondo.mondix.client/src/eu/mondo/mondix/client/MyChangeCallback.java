package eu.mondo.mondix.client;

import java.util.List;

import eu.mondo.mondix.live.IChangeCallback;
import eu.mondo.mondix.live.ILiveView;

public class MyChangeCallback implements IChangeCallback {
	StringBuilder notificationsStr = new StringBuilder();
	
	@Override
	public void changed(ILiveView view, boolean inserted,	List<?> changedTuple) {
		String message = "View: " + view.hashCode() + "; inserted: " + inserted + "; tuple: " + changedTuple.toString() + "\n";
		notificationsStr.append(message);
	}
	
	public StringBuilder getNotificationsStr() {
		return notificationsStr;
	}
}
