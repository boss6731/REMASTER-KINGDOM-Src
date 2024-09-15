package l1j.server.server;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.server.datatables.NpcShopSpawnTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.templates.L1NpcShop;
import l1j.server.server.templates.L1ShopItem;

public class NpcShopSystem {

	private static NpcShopSystem _instance;

	private boolean _power = false;

	public static NpcShopSystem getInstance() {
		if (_instance == null) {
			_instance = new NpcShopSystem();
		}
		return _instance;
	}

	private static ArrayList<L1NpcInstance> _shops = new ArrayList<L1NpcInstance>(20);

	private static void shopRefill(L1NpcInstance npc){
		try{
			L1Shop shop = NpcShopTable.getInstance().get(npc.getNpcId());
			if(shop == null)
				return;
			
			List<L1ShopItem> list = shop.getSellingItems();
			if(list == null)
				return;
			
			int size = list.size();
			MJDShopItem ditem 	= null;
			for(int i=0; i<size; i++){
				ditem = MJDShopItem.create(list.get(i), i, false);
				npc.addSellings(ditem);
			}
			
			list = shop.getBuyingItems();
			if(list == null)
				return;
			
			size = list.size();
			for(int i=0; i<size; i++){
				ditem = MJDShopItem.create(list.get(i), i, true);
				npc.addPurchasings(ditem);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	static class NpcShopTimer implements Runnable {

		public NpcShopTimer() {
		}

		public void run() {
			try {
				ArrayList<L1NpcShop> list = NpcShopSpawnTable.getInstance().getList();
				for (int i = 0; i < list.size(); i++) {
					L1NpcShop shop = list.get(i);
					L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(shop.getNpcId());
					npc.setId(IdFactory.getInstance().nextId());
					npc.setMap(shop.getMapId());
					npc.getLocation().set(shop.getX(), shop.getY(), shop.getMapId());
					npc.getLocation().forward(5);
					npc.setHomeX(npc.getX());
					npc.setHomeY(npc.getY());
					npc.setHeading(shop.getHeading());
					npc.setName(shop.getName());
					npc.setTitle(shop.getTitle());
					L1NpcShopInstance obj = (L1NpcShopInstance) npc;
					obj.setShopName(shop.getShopName());
					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
					npc.getLight().turnOnOffLight();
					Thread.sleep(30);
					obj.setState(1);
					Broadcaster.broadcastPacket(npc, new S_DoActionShop(npc.getId(), ActionCodes.ACTION_Shop, shop.getShopName()));
					_shops.add(npc);
					Thread.sleep(10);
				}
				// list.clear();

			} catch (Exception exception) {
                return new l1j.server.server.model.Instance.L1PcInstance[0];
			}finally{
				push_shop_info();
			}
            return new l1j.server.server.model.Instance.L1PcInstance[0];
        }
	}

	public void npcShopStart() {
		NpcShopTimer ns = new NpcShopTimer();
		GeneralThreadPool.getInstance().execute(ns);
		_power = true;
	}

	public void npcShopStop() {
		_power = false;
		int size = _shops.size();
		for(int i=0; i<size; i++){
			L1NpcInstance npc = _shops.get(i);
			if(npc == null)
				continue;
			
			GeneralThreadPool.getInstance().execute(new MJDShopStorage(npc, true));
			npc.deleteMe();
		}
		_shops.clear();
	}	
	
	public static void push_shop_info() {
		NpcShopTable.reloding();
		int size = _shops.size();
		for(int i=0; i<size; i++)
			shopRefill(_shops.get(i));
		
		MJDShopStorage.updateProcess(_shops);
	}

	public boolean isPower() {
		return _power;
	}
}
