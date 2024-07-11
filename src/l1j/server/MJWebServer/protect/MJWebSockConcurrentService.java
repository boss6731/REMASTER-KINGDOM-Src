package l1j.server.MJWebServer.protect;

import java.util.HashMap;

import l1j.server.MJTemplate.MJReadWriteLock;
import l1j.server.MJWebServer.protect.MJWebProtectInfo.WebSocketAccessInfo;

class MJWebSockConcurrentService {
	private static final MJWebSockConcurrentService service = new MJWebSockConcurrentService();
	static MJWebSockConcurrentService service(){
		return service;
	}
	
	private final MJReadWriteLock lock;
	private final HashMap<String, AddressAccess> accesses;
	private MJWebSockConcurrentService(){
		lock = new MJReadWriteLock();
		accesses = new HashMap<>();
	}
	
	AddressAccess safeAccess(String address){
		AddressAccess access = null;
		try{
			lock.writeLock();
			access = accesses.get(address);
			if(access == null){
				access = new AddressAccess(address);
				accesses.put(address, access);
			}
		}finally{
			lock.writeUnlock();
		}
		return access;
	}
	
	boolean containsAccess(String address){
		try{
			lock.readLock();
			return accesses.containsKey(address);
		}finally{
			lock.readUnlock();
		}
	}
	
	boolean onActive(String address){
		WebSocketAccessInfo accessInfo = MJWebProtectService.service().protectDefine().websockAccess();
		AddressAccess access = safeAccess(address);
		synchronized(access){
			access.onActive();
			if(access.accessCount >= accessInfo.concurrentAccessCountByAddress()){
				System.out.println(String.format("[MJWebSockConcurrentService]訪問過多\r\n%s", access.toString()));
				return false;
			}
		}
		return true;
	}
	
	void onInActive(String address){
		if(!containsAccess(address)){
			return;
		}
		
		AddressAccess access = safeAccess(address);
		synchronized(access){
			access.onInActive();
		}
	}
	
	private static class AddressAccess{
		String address;
		int accessCount;
		AddressAccess(String address){
			this.address = address;
			this.accessCount = 0;
		}
		
		void onActive(){
			++accessCount;
		}
		
		void onInActive(){
			--accessCount;
		}
		
		@Override
		public String toString(){
			return new StringBuilder(32)
					.append(" -IP位址： ").append(address).append("\r\n")
					.append(" -訪問次數： ").append(accessCount)
					.toString();
		}
	}
}
