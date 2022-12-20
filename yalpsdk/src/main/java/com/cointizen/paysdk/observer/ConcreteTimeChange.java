package com.cointizen.paysdk.observer;

import java.util.ArrayList;
import java.util.List;

public class ConcreteTimeChange implements TimeChange {

	private List<SecondsWatcher> watcherList = new ArrayList<SecondsWatcher>();
	
	@Override
	public void noticeCurrentSeconds(String seconds) {
		for (SecondsWatcher secondsWatcher : watcherList) {
			secondsWatcher.updateSeconds(seconds);
		}
	}

	@Override
	public void addWatcher(SecondsWatcher seconds) {
		watcherList.add(seconds);
	}

	@Override
	public void removeWatcher(SecondsWatcher seconds) {
		watcherList.remove(seconds);
		
	}

	@Override
	public void removeAllWatcher() {
		for (SecondsWatcher secondsWatcher : watcherList) {
			watcherList.remove(secondsWatcher);
		}
	}
}
