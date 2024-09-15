package l1j.server.MJWebServer.ws;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelId;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.collection.MapEventListener;
import l1j.server.MJTemplate.collection.ObservableMap;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.MJWebServer.ws.protocol.MJWebSockSendModel;

public class MJWebSockRequestGroup implements Iterable<MJWebSockRequest>{
	private static final AtomicInteger newGroupId = new AtomicInteger(0);
	
	private final String name;
	private final int capacity;
	private final MJWebSockInactiveListener inactiveListener;
	private ObservableMap<ChannelId, MJWebSockRequest> groups;




	/**
	 * 構造函數
	 * @param name 組的名稱
	 * @param capacity 組的基本分配大小
	 **/
	public MJWebSockRequestGroup(String name, int capacity){
		this.name = name;
		this.capacity = capacity;
		inactiveListener = new MJWebSockInactiveListener(){
			@Override
			public void onInactive(MJWebSockRequest request) {
				if(request != null){
					groups.remove(request.channelId());
				}
			}
		};
		allocateGroup();
	}
	
	
	
	
	private void allocateGroup(){
		groups = ObservableMap.newConcurrentMap(capacity);
		addListener(new GroupMapEventListener());
	}




	/**
	 * 返回組的名稱。
	 * @return String
	 **/
	public String name(){
		return name;
	}




	/**
	 * 註冊接收組成員變更事件的監聽器。
	 * @param listener {@code MapEventListener<ChannelId, MJWebSockRequest>}
	 * @see MapEventListener
	 * @see ChannelId
	 * @see l1j.server.MJWebServer.ws.MJWebSockRequest
	 **/
	public void addListener(MapEventListener<ChannelId, MJWebSockRequest> listener){
		groups.addListener(listener);
	}




	/**
	 * 刪除接收組成員變更事件的監聽器。
	 * @param listener {@code MapEventListener<ChannelId, MJWebSockRequest>}
	 * @see MapEventListener
	 * @see ChannelId
	 * @see l1j.server.MJWebServer.ws.MJWebSockRequest
	 **/
	public void removeListener(MapEventListener<ChannelId, MJWebSockRequest> listener){
		groups.removeListener(listener);
	}




	/**
	 * 返回組成員的數量。
	 * @return int
	 **/
	public int size() {
		return groups.size();
	}




	/**
	 * 判斷組是否為空。
	 * @return boolean 如果為空，返回true。
	 **/
	public boolean isEmpty() {
		return groups.isEmpty();
	}




	/**
	 * 初始化組。
	 **/
	public void clear(){
		ObservableMap<ChannelId, MJWebSockRequest> oldGroups = groups;
		allocateGroup();
		if(oldGroups != null){
			for(MJWebSockRequest request : oldGroups.values()){
				request.removeInactiveListener(inactiveListener);
			}
			oldGroups.clear();
		}
	}




	/**
	 * 判斷組中是否存在指定的 channelId。
	 * @param channelId {@link ChannelId}
	 * @return boolean 如果存在，返回 true。
	 **/
	public boolean contains(ChannelId channelId){
		return groups.containsKey(channelId);
	}

	/**
	 * 判斷組中是否存在指定的 MJWebSockRequest。
	 * @param request {@link l1j.server.MJWebServer.ws.MJWebSockRequest}
	 * @return boolean 如果存在，返回 true。
	 **/
	public boolean contains(MJWebSockRequest request){
		return contains(request.channelId());
	}

	/**
	 * 向組中添加一個頻道。
	 * @param request {@link l1j.server.MJWebServer.ws.MJWebSockRequest}
	 **/
	public boolean add(MJWebSockRequest request) {
		groups.put(request.channelId(), request);
		return true;
	}

	/**
	 * 從組中查找並返回指定的 request。
	 * @return {@link l1j.server.MJWebServer.ws.MJWebSockRequest}
	 **/
	public MJWebSockRequest get(ChannelId channelId) {
		return groups.get(channelId);
	}




	/**
	 * 從組中刪除指定的頻道。
	 * @param channelId {@link ChannelId}
	 **/
	public boolean remove(ChannelId channelId) {
		return groups.remove(channelId) != null;
	}

	/**
	 * 從組中刪除指定的頻道。
	 * @param request {@link TcpChannelContext}
	 **/
	public boolean remove(MJWebSockRequest request){
		return remove(request.channelId());
	}

	/**
	 * 從組中刪除符合 matcher 條件的頻道，並返回刪除的頻道組。
	 * @param matcher {@code Matcher<MJWebSockRequest>}
	 * @return MJWebSockRequestGroup 刪除的頻道組
	 * @see Matcher
	 * @see MJWebSockRequest
	 **/
	public MJWebSockRequestGroup remove(Matcher<MJWebSockRequest> matcher){
		MJWebSockRequestGroup newGroup =
				new MJWebSockRequestGroup(MJString.concat(name, String.valueOf(newGroupId.incrementAndGet())), groups.size());

		for(Iterator<MJWebSockRequest> itg = groups.values().iterator(); itg.hasNext();){
			MJWebSockRequest request = itg.next();
			if(matcher.matches(request)){
				newGroup.add(request);
				itg.remove();
			}
		}
		return newGroup;
	}




	/**
	 * 向組成員發送封包。
	 * @param model {@link MJWebSockSendModel}
	 **/
	public void write(MJWebSockSendModel<?> model){
		write(Matchers.all(), model);
	}

	/**
	 * 向符合 matcher 條件的組成員發送封包。
	 * @param matcher {@code Matcher<MJWebSockRequest> matcher}
	 * @param model {@link MJWebSockSendModel}
	 * @see Matcher
	 * @see l1j.server.MJWebServer.ws.MJWebSockRequest
	 **/
	public void write(Matcher<MJWebSockRequest> matcher, MJWebSockSendModel<?> model){
		for(MJWebSockRequest request : groups.values()){
			if(matcher.matches(request)){
				request.write(model);
			}
		}
	}




	/**
	 * 斷開組內所有成員的連接。
	 **/
	public void disconnect(){
		disconnect(Matchers.all());
	}

	/**
	 * 只斷開符合 matcher 條件的組成員的連接。
	 * @param matcher {@code Matcher<MJWebSockRequest>}
	 * @see Matcher
	 * @see l1j.server.MJWebServer.ws.MJWebSockRequest
	 **/
	public void disconnect(Matcher<MJWebSockRequest> matcher){
		for(MJWebSockRequest request : groups.values()){
			if(matcher.matches(request)){
				request.disconnect();
			}
		}
	}

	/**
	 * 關閉組內所有成員的連接。
	 **/
	public void close(){
		close(Matchers.all());
	}

	/**
	 * 只關閉符合 matcher 條件的組成員的連接。
	 * @param matcher {@code Matcher<MJWebSockRequest>}
	 * @see Matcher
	 * @see l1j.server.MJWebServer.ws.MJWebSockRequest
	 **/
	public void close(Matcher<MJWebSockRequest> matcher){
		for(MJWebSockRequest request : groups.values()){
			if(matcher.matches(request)){
				request.close();
			}
		}
	}




	/**
	 * 將組中符合 matcher 條件的成員收集到新組並返回。
	 * @param matcher {@code Matcher<MJWebSockRequest>}
	 * @see Matcher
	 * @see l1j.server.MJWebServer.ws.MJWebSockRequest
	 **/
	public MJWebSockRequestGroup subGroup(Matcher<MJWebSockRequest> matcher){
		return subGroup(
				MJString.concat(name, String.valueOf(newGroupId.incrementAndGet())),
				matcher);
	}

	/**
	 * 將組中符合 matcher 條件的成員收集到新組並返回。
	 * @param newName 新組的名稱 String
	 * @param matcher {@code Matcher<MJWebSockRequest>}
	 * @see Matcher
	 * @see TcpChannelContext
	 **/
	public MJWebSockRequestGroup subGroup(String newName, Matcher<MJWebSockRequest> matcher){
		MJWebSockRequestGroup newGroup = new MJWebSockRequestGroup(newName, groups.size());
		for(MJWebSockRequest request : groups.values()){
			if(matcher.matches(request)){
				newGroup.add(request);
			}
		}
		return newGroup;
	}

	/**
	 * {&#064;inheritdoc }
     **/
	@override
	public Iterator<MJWebSockRequest> iterator() {
		return new RequestIterator();
	}
	
	
	
	
	private class RequestIterator implements Iterator<MJWebSockRequest>{
		private Iterator<MJWebSockRequest> itr = groups.values().iterator();
		@Override
		public boolean hasNext() {
			return itr.hasNext();
		}

		@Override
		public MJWebSockRequest next() {
			return itr.next();
		}
	}
	
	
	
	
	private class GroupMapEventListener implements MapEventListener<ChannelId, MJWebSockRequest>{

		@Override
		public void onPut(Map<ChannelId, MJWebSockRequest> map,
				l1j.server.MJTemplate.collection.MapEventListener.ChangeNode<ChannelId, MJWebSockRequest> newNode,
				l1j.server.MJTemplate.collection.MapEventListener.ChangeNode<ChannelId, MJWebSockRequest> oldNode) {
			if(newNode.value != null){
				newNode.value.addInactiveListener(inactiveListener);
			}
			if(oldNode.value != null){
				oldNode.value.removeInactiveListener(inactiveListener);
			}
		}

		@Override
		public void onPutAll(Map<ChannelId, MJWebSockRequest> map,
				Map<? extends ChannelId, ? extends MJWebSockRequest> putsMap) {
			for(MJWebSockRequest request : putsMap.values()){
				request.addInactiveListener(inactiveListener);
			}
		}

		@Override
		public void onRemove(Map<ChannelId, MJWebSockRequest> map,
				l1j.server.MJTemplate.collection.MapEventListener.ChangeNode<ChannelId, MJWebSockRequest> removeNode) {
			if(removeNode.value != null){
				removeNode.value.removeInactiveListener(inactiveListener);
			}
		}

		@Override
		public void onClear(Map<ChannelId, MJWebSockRequest> map) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
