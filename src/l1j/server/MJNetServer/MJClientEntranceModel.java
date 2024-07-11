package l1j.server.MJNetServer;

import l1j.server.MJTemplate.MJRnd;

class MJClientEntranceModel {
	int waitLowSeconds;
	int waitHighSeconds;
	int waitQueueLowCount;
	int waitQueueHighCount;
	boolean useWaitQueue;
	MJClientEntranceModel(){
		waitLowSeconds = 3;
		waitHighSeconds = 5;
		waitQueueLowCount = 50;
		waitQueueHighCount = 300;
		useWaitQueue = true;
	}
	
	void setUseWaitQueue(boolean flag) {
		useWaitQueue = flag;
	}
	
	int nextSeconds() {
		return MJRnd.next(waitLowSeconds, waitHighSeconds);
	}
	
	int nextQueueCount() {
		return nextQueueCount(waitQueueHighCount);
	}
	
	int nextQueueCount(int current) {
		return nextQueueCount(waitQueueLowCount, current);
	}
	
	int nextQueueCount(int low, int high) {	
		return MJRnd.next(low, high);
	}
}
