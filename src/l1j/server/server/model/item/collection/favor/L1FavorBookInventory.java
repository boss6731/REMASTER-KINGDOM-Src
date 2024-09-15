package l1j.server.server.model.item.collection.favor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.InvenBonusItem.InvenBonusItemInfo;
import l1j.server.InvenBonusItem.InvenBonusItemLoader;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookTypeObject;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.item.collection.favor.construct.L1FavorBookInventoryStatus;
import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
import l1j.server.server.serverpackets.S_FavorBook;
import l1j.server.server.serverpackets.ServerBasePacket;
//import l1j.server.server.monitor.Logger.FavorType;

/**
 * 유저에게 할당된 성물 인벤토리
 * @author LinOffice
 */
public class L1FavorBookInventory {
	private final L1PcInstance owner;
	private final ConcurrentHashMap<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>>> mapData;
	private final ArrayList<L1FavorBookUserObject> listData;
	private final ConcurrentHashMap<L1FavorBookListType, ServerBasePacket> packets;

	private L1ItemInstance craftBoxOpenResult;
	public L1ItemInstance getCraftBoxOpenResult(){
		return craftBoxOpenResult;
	}
	public void setCraftBoxOpenResult(L1ItemInstance item){
		craftBoxOpenResult = item;
	}
	
	/**
	 * 생성자
	 * @param owner
	 */
	public L1FavorBookInventory(L1PcInstance owner) {
		this.owner		= owner;
		this.mapData	= new ConcurrentHashMap<>();
		this.listData	= new ArrayList<>();
		this.packets	= new ConcurrentHashMap<>();
		init();
	}
	
	/**
	 * 목록 패킷 반환
	 * @param listType
	 * @return ServerBasePacket
	 */
	public ServerBasePacket getListPacket(L1FavorBookListType listType){
		return packets.get(listType);
	}
	
	/**
	 * 등록되어 있는 모든 성물 정보를 조사한다.
	 * @return ArrayList<L1FavorBookUser>
	 */
	public ArrayList<L1FavorBookUserObject> getList(){
		return listData;
	}
	
	/**
	 * 전체 성물 데이터를 조사한다.
	 * @return map
	 */
	public ConcurrentHashMap<L1FavorBookListType, ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>>> getData(){
		return mapData;
	}
	
	/**
	 * 목록 타입별 성물 목록을 조사한다.
	 * @param listType
	 * @return map
	 */
	public ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> getListTypeMap(L1FavorBookListType listType){
		return mapData.get(listType);
	}
	
	/**
	 * 가호 타입별 성물 목록을 조사한다.(축복의 성물 or 강화의 성물)
	 * @param listType
	 * @param type
	 * @return map
	 */
	public ConcurrentHashMap<Integer, L1FavorBookUserObject> getTypeMap(L1FavorBookListType listType, L1FavorBookTypeObject type){
		ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> listMap = getListTypeMap(listType);
		if (listMap == null) {
			return null;
		}
		return listMap.get(type);
	}
	
	/**
	 * 등록된 성물을 조사한다.
	 * @param listType
	 * @param type
	 * @param index
	 * @return L1FavorBookUser
	 */
	public L1FavorBookUserObject getFavorUser(L1FavorBookListType listType, L1FavorBookTypeObject type, int index){
		ConcurrentHashMap<Integer, L1FavorBookUserObject> typeMap = getTypeMap(listType, type);
		if (typeMap == null) {
			return null;
		}
		return typeMap.get(index);
	}
	
	/**
	 * 성물 인벤토리에 새 데이터을 추가한다.
	 * @param favor
	 * @return L1FavorBookUserObject
	 */
	private L1FavorBookUserObject create(L1FavorBookObject favor){
		L1FavorBookUserObject user = new L1FavorBookUserObject(favor.getListType(), favor.getType(), favor.getIndex(), favor);
		ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> listTypeMap = mapData.get(user.getListType());
		if (listTypeMap == null) {
			listTypeMap = new ConcurrentHashMap<>();
			mapData.put(user.getListType(), listTypeMap);
		}
		
		ConcurrentHashMap<Integer, L1FavorBookUserObject> typeMap = listTypeMap.get(user.getType());
		if (typeMap == null) {
			typeMap = new ConcurrentHashMap<>();
			listTypeMap.put(user.getType(), typeMap);
		}
		typeMap.put(user.getIndex(), user);
		listData.add(user);
		return user;
	}
	
	/**
	 * 성물 인벤토리에 아이템을 적재한다.
	 * 등록된 성물에 대한 패킷을 보낸다.
	 * 성물 인벤토리 적재 로그를 남긴다.
	 * @param favor
	 * @param item
	 * @return boolean
	 */
	public boolean registFavor(L1FavorBookObject favor, L1ItemInstance item){
		// TODO 성물 인벤토리 등록(실제 유저 인벤토리에는 생성하지 않는다)
		if (!Config.ServerAdSetting.FavorSystem) {
			return false;
		}
		L1FavorBookUserObject user = getFavorUser(favor.getListType(), favor.getType(), favor.getIndex());
		if (user == null) {
			user = create(favor);
		}
		// 봉인 아이템
		if (item.getBless() >= 128) {
			item.setBless(item.getBless() - 128);
		}
		item.setIdentified(true);
		user.setCurrentItem(item);
		if (L1FavorBookUserLoader.getInstance().insert(owner, user)) {
			owner.sendPackets(new S_FavorBook(owner, user, L1FavorBookInventoryStatus.STORE), true);
//			LoggerInstance.getInstance().addFavorBook(FavorType.REGIST, owner, user);
			updateListPacket();// 목록 패킷 갱신
			return true;
		}
		return false;
	}
	
	/**
	 * 성물 인벤토리에서 등록된 아이템을 꺼낸다.(제작재료, 기간제한)
	 * @param user
	 * @param favorItem
	 * @param pollCount(꺼낼 수량 : 현재 1개씩이지만 추후 여러개로 변경시 처리된다.)
	 */
	public void pollFavor(L1FavorBookUserObject user, L1ItemInstance favorItem, int pollCount){
		int afterCount = favorItem.getCount() - pollCount;
		if (afterCount > 0) {
			favorItem.setCount(afterCount);
			return;
		}
		// 등록된 성물의 옵션을 제거한다.
		InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(favorItem.getItemId());
        if (info != null) {
        	InvenBonusItemInfo.inven_option(owner, info.get_item_id(), false);
        }
        if (L1FavorBookUserLoader.getInstance().delete(owner, user)) {
        	ConcurrentHashMap<Integer, L1FavorBookUserObject> map = getTypeMap(user.getListType(), user.getType());
        	if (map == null) {
        		return;
        	}
        	map.remove(user.getIndex());
	        listData.remove(user);
//	        LoggerInstance.getInstance().addFavorBook(FavorType.DELETE, owner, user);
	        updateListPacket();// 목록 패킷 갱신
        }
	}
	
	/**
	 * 성물 인벤토리에서 등록된 성물을 제거한다.
	 * @param user
	 */
	public void deleteFavor(L1FavorBookUserObject user){
		L1ItemInstance favorItem = user.getCurrentItem();
		if (favorItem != null) {
			// 등록된 성물의 옵션을 제거한다.
			InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(favorItem.getItemId());
	        if (info != null) {
	        	InvenBonusItemInfo.inven_option(owner, info.get_item_id(), false);
	        }
		}
		if (L1FavorBookUserLoader.getInstance().delete(owner, user)) {
        	ConcurrentHashMap<Integer, L1FavorBookUserObject> map = getTypeMap(user.getListType(), user.getType());
        	if (map == null) {
        		return;
        	}
        	map.remove(user.getIndex());
	        listData.remove(user);
//	        LoggerInstance.getInstance().addFavorBook(FavorType.DELETE, owner, user);
	        updateListPacket();// 목록 패킷 갱신
        }
	}
	
	/**
	 * 인벤토리에 변화가 발생시 목록 패킷을 최신으로 갱신한다.
	 */
	private void updateListPacket(){
		clearListPacket();
		packets.put(L1FavorBookListType.ALL,	new S_FavorBook(owner, this, L1FavorBookListType.ALL));
		packets.put(L1FavorBookListType.RELIC,	new S_FavorBook(owner, this, L1FavorBookListType.RELIC));
		packets.put(L1FavorBookListType.EVENT,	new S_FavorBook(owner, this, L1FavorBookListType.EVENT));
	}
	
	/**
	 * 목록 패킷을 제거한다.
	 */
	private void clearListPacket(){
		ServerBasePacket pck = packets.get(L1FavorBookListType.ALL);
		if (pck != null) {
			pck.clear();
			pck = null;
		}
		pck	= packets.get(L1FavorBookListType.RELIC);
		if (pck != null) {
			pck.clear();
			pck = null;
		}
		pck	= packets.get(L1FavorBookListType.EVENT);
		if (pck != null) {
			pck.clear();
			pck = null;
		}
		packets.clear();
	}
	
	/**
	 * 최초 생성시 default 처리
	 */
	private void init(){
		// TODO 로그인 처리
		ArrayList<L1FavorBookUserObject> userList = L1FavorBookUserLoader.getFavorUserList(owner.getId());// 등록되어 있는 성물 정보
		if (userList != null && !userList.isEmpty()) {
			for (L1FavorBookUserObject user : userList) {
				ConcurrentHashMap<L1FavorBookTypeObject, ConcurrentHashMap<Integer, L1FavorBookUserObject>> listTypeMap = mapData.get(user.getListType());
				if (listTypeMap == null) {
					listTypeMap = new ConcurrentHashMap<>();
					mapData.put(user.getListType(), listTypeMap);
				}
				
				ConcurrentHashMap<Integer, L1FavorBookUserObject> typeMap = listTypeMap.get(user.getType());
				if (typeMap == null) {
					typeMap = new ConcurrentHashMap<>();
					listTypeMap.put(user.getType(), typeMap);
				}
				
				// 등록된 성물의 옵션을 부여한다.
				InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(user.getCurrentItem().getItemId());
				if (info != null) {
					InvenBonusItemInfo.inven_option(owner, info.get_item_id(), true);
				}
				
				typeMap.put(user.getIndex(), user);
				listData.add(user);
			}
		}
		updateListPacket();
		sendLoginPacket();
	}
	
	/**
	 * 로그인 출력 패킷
	 */
	private void sendLoginPacket(){
		if (!listData.isEmpty()) {
			for (L1FavorBookUserObject user : listData) {
				owner.sendPackets(new S_FavorBook(owner, user, L1FavorBookInventoryStatus.LIST), true);// 등록되어 있는 인벤토리 정보
			}
		}
	}
	
	/**
	 * 메모리 정리
	 */
	public void dispose(){
		mapData.clear();
		listData.clear();
		clearListPacket();
	}
}
