package l1j.server.server.model.item.collection.time;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_DATA_LOAD_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_REGIST_ITEM_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_RESET_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_SELECT_BONUS_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_TIME_COLLECTION_SET_DATA_NOTI;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionAblity;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionStatus;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;
//import l1j.server.server.monitor.Logger.TimeCollectionType;
//import l1j.server.server.serverpackets.S_TimeCollection;

/**
 * 유저에게 할당된 실렉티스 전시회 핸들러
 * @author LinOffice
 */
public class L1TimeCollectionHandler {
	private final L1PcInstance owner;
	private final ConcurrentHashMap<Integer, L1TimeCollectionUser> DATA;// Key: flag, Value: L1TimeCollectionUser
	private int buffSize;
	
	/**
	 * 전체 데이터 조사
	 * @return ConcurrentHashMap<Integer, L1TimeCollectionUser>
	 */
	public ConcurrentHashMap<Integer, L1TimeCollectionUser> getData() {
		return DATA;
	}
	
	/**
	 * 등록된 컬렉션 조사
	 * @param flag
	 * @return L1TimeCollectionUser
	 */
	public L1TimeCollectionUser getUser(int flag) {
		return DATA.get(flag);
	}
	
	/**
	 * 기본 생성자
	 * @param owner
	 */
	public L1TimeCollectionHandler(L1PcInstance owner) {
		this.owner	= owner;
		this.DATA	= new ConcurrentHashMap<>();
		init(owner);
	}
	
	/**
	 * 컬렉션 등록
	 * @param obj
	 * @param item
	 * @param slotIndex
	 * @return boolean
	 */
	public boolean regist(L1PcInstance pc, L1TimeCollection obj, L1ItemInstance item, int slotIndex) {
		L1TimeCollectionUser user = DATA.get(obj.getFlag());
		if (user == null) {
			// 최초 등록시 클래스의 주 버프타입을 설정한다.
			L1TimeCollectionBuffType type = null;
			switch(owner.getType()){
			case 2:// 요정
				type = L1TimeCollectionBuffType.LONG;
				break;
			case 3:// 법사
			case 6:// 환술사
				type = L1TimeCollectionBuffType.MAGIC;
				break;
			default:
				type = L1TimeCollectionBuffType.SHORT;
				break;
			}
			user = new L1TimeCollectionUser(
					owner.getId(), obj.getFlag(), obj.getType(), obj.getCollectionIndex(), 
					new ConcurrentHashMap<Integer, L1ItemInstance>(), 
					false, 0, type, null, obj, 0);
		}
		// 이미 완성하여 버프를 받고 있음
		if (user.getBuffTimer() != null) {
			return false;
		}
		user.putRegistItem(slotIndex, item);

		ConcurrentHashMap<Integer, L1ItemInstance> registItems = user.getRegistItem();
		if (registItems.size() == obj.getSlotSize()) {// 완성
//			System.out.println("패킷확인1");
			user.setRegistComplet(true);
			int sum = 0; 
			int buffDuration = 0;
			L1ItemInstance value = null;
			
			for(Map.Entry<Integer, L1ItemInstance> entry : registItems.entrySet()){
				value			= entry.getValue();
				sum				+= value.getEnchantLevel();
				buffDuration	+= obj.getDuration(entry.getKey(), value.getEnchantLevel());
			}
//			for (L1ItemInstance regiItem : registItems.values()) {
//				sum				+= regiItem.getEnchantLevel();
//				buffDuration	+= obj.getDuration(slotIndex, regiItem.getEnchantLevel());
//			}
			// 합산 강화 수치
			user.setSumEnchant(sum);
			
			// 버프시간 설정
			long currentTime = System.currentTimeMillis();
			if (user.getBuffTime() == null) {
				user.setBuffTime(new Timestamp((long)((buffDuration * 3600000L) + currentTime)));
//				user.getBuffTime().setTime((long)(buffDuration * 3600 * 1000) + currentTime);
//				System.out.println(currentTime);
//				System.out.println(buffDuration);
//				System.out.println(user.getBuffTime());
			} else {
				user.getBuffTime().setTime((long)(buffDuration * 3600000L) + currentTime);
			}
			
			activeAblity(pc, user);
			SC_TIME_COLLECTION_SET_DATA_NOTI.send(pc, user);
//			owner.sendPackets(new S_TimeCollection(owner, user), true);
		}
//		System.out.println("패킷확인2");
		SC_TIME_COLLECTION_REGIST_ITEM_ACK.send(pc, user, slotIndex, item);
//		owner.sendPackets(new S_TimeCollection(owner, user, slotIndex, item), true);
		
		if (L1TimeCollectionUserLoader.getInstance().insert(user)) {
			DATA.put(user.getFlag(), user);
//			LoggerInstance.getInstance().addTimeCollection(TimeCollectionType.REGIST, owner, user, item);
			return true;
		}
		return false;
	}
	
	/**
	 * 컬렉션 제거
	 * @param obj
	 * @return boolean
	 */
	public boolean delete(L1PcInstance pc, L1TimeCollection obj, L1TimeCollectionStatus status) {
		L1TimeCollectionUser user = DATA.remove(obj.getFlag());
		if (user == null) {
			System.out.println(String.format(
					"[L1TimeCollectionHandler] DELETE RESON(user == null) NAME(%s) FLAG(%d)", 
					owner.getName(), obj.getFlag()));
			return false;
		}
		
		if (user.isBuffActive()) {
			user.getAblity().ablity(owner, false);// 옵션 제거
			user.setBuffTime(null);
			user.setSumEnchant(0);
			user.getRegistItem().clear();
			user.getBuffTimer().cancel();
			user.setAblity(null);
			user.setBuffTimer(null);
			user.setRegistComplet(false);
			user.setBuffIndex(--buffSize);
			user.setRefill_count(0);
			SC_TIME_COLLECTION_BUFF_NOTI.send(pc, user, status);
//			owner.sendPackets(new S_TimeCollection(user, status), true);
		}
		
		if (L1TimeCollectionUserLoader.getInstance().delete(user)) {
			SC_TIME_COLLECTION_RESET_ACK.send(pc, user);
//			owner.sendPackets(new S_TimeCollection(S_TimeCollection.DELETE, user), true);
//			LoggerInstance.getInstance().addTimeCollection(TimeCollectionType.DELETE, owner, user, null);
			return true;
		}
		return false;
	}
	
	/**
	 * 버프를 가동 시킨다.
	 * @param user
	 */
	public void activeAblity(L1PcInstance pc, L1TimeCollectionUser user) {
		if (!user.isRegistComplet()) {
			return;
		}
		long interval = user.restBuffTime();
		if (interval <= 0L) {
			return;
		}
		
		L1TimeCollectionAblity ablity = user.getObj().getAblity(user.getSumEnchant(), user.getBuffType());// 사용될 버프 옵션
		if (ablity == null) {
			System.out.println(String.format(
					"[L1TimeCollectionHandler] ABLITY NOT FOUND : SUM_ENCHANT(%d), BUFF_TYPE(%s), NAME(%s)",
					user.getSumEnchant(), user.getBuffType().getName(), owner.getName()));
			return;
		}
		user.setAblity(ablity);
		ablity.ablity(owner, true);// 옵션 부여
		
		// 버프 타이머 없음
		if (user.getBuffTimer() == null)
		{
			user.setBuffIndex(++buffSize);
			user.setBuffTimer(new L1TimeCollectionTimer(owner, ablity));// 타이머 생성
			GeneralThreadPool.getInstance().schedule(user.getBuffTimer(), interval);// 타이머 가동
		} 
		// 버프 타이머 존재
		else 
		{
			if (!user.getBuffTimer().isEquals(ablity)) {
				user.getBuffTimer().trans(ablity);// 타이머의 옵션설정 변경
			}
		}
		SC_TIME_COLLECTION_BUFF_NOTI.send(pc, user, L1TimeCollectionStatus.START);
//		owner.sendPackets(new S_TimeCollection(user, L1TimeCollectionStatus.START), true);
	}
	
	/**
	 * 최초 생성시 데이터 세팅
	 */
	private void init(L1PcInstance pc) {
		ArrayList<L1TimeCollectionUser> list = L1TimeCollectionUserLoader.getUserList(owner.getId());
		if (list != null && !list.isEmpty()) {
			for (L1TimeCollectionUser obj : list) {
				DATA.put(obj.getFlag(), obj);
				activeAblity(pc, obj);
			}
		}
		sendLoginPacket(pc);
	}
	
	/**
	 * 로그인 출력 패킷
	 */
	private void sendLoginPacket(L1PcInstance pc) {
		SC_TIME_COLLECTION_DATA_LOAD_NOTI.send(pc, this);
//		owner.sendPackets(new S_TimeCollection(owner, this), true);
	}
	
	/**
	 * 메모리 정리
	 */
	public void dispose() {
		if(DATA != null && !DATA.isEmpty()){
			for (L1TimeCollectionUser user : DATA.values()) {
				if (user.getBuffTimer() != null) {
					user.getBuffTimer().cancel();
					user.setBuffTimer(null);
				}
			}
		}
		DATA.clear();
	}
}
