package l1j.server.server.model.item.function;

import static l1j.server.server.model.skill.L1SkillId.MASS_TELEPORT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import l1j.server.MJTemplate.Chain.Action.MJTellBookChain;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1BookMark;

public class Telbookitem {

	public static void clickItem(L1PcInstance pc, int itemId, int bookmark_x,int bookmark_y,short bookmark_mapid, L1ItemInstance l1iteminstance) {
//		System.out.println("확인2");
		if (bookmark_x != 0) {
			boolean isGetBookMark = false;
			for (L1BookMark book : pc.getBookMarkArray()) {
				if (book.getLocX() == bookmark_x && book.getLocY() == bookmark_y && book.getMapId() == bookmark_mapid) {
					isGetBookMark = true;
					break;
				}
			}
			if (isGetBookMark && (pc.getMap().isEscapable() || pc.isGm())) {
				if (itemId == 40086) {
					for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc)) {
						if (pc.getLocation().getTileLineDistance(member.getLocation()) <= 3
								&& member.getClanid() == pc.getClanid() && pc.getClanid() != 0
								&& member.getId() != pc.getId() && !member.isPrivateShop()) {
							member.start_teleport(bookmark_x, bookmark_y, bookmark_mapid, member.getHeading(), 18339, true, false);
						}
					}
				}
				pc.start_teleport(bookmark_x, bookmark_y, bookmark_mapid, pc.getHeading(), 18339, true, false);
				pc.getInventory().removeItem(l1iteminstance, 1);
			} else {
				if(itemId == 40100){
					clickItem(pc, itemId, 0, 0,(short)0, l1iteminstance);
				}
				else{
					pc.sendPackets(new S_ServerMessage(79));	
				}
			}
		} else {
			if(pc.getMapId()>=101 && pc.getMapId()<=110) {
				int find_item_ids[] = {
						830022, 	// 1층
						830023, 	// 2층 
						830024, 	// 3층 
						830025, 	// 4층 
						830026, 	// 5층 
						830027, 	// 6층 
						830028, 	// 7층 
						830029, 	// 8층 
						830030, 	// 9층
						830031   	// 10층
				};
				L1ItemInstance findItem = pc.getInventory().findItemId(find_item_ids[pc.getMapId() - 101]);
				L1ItemInstance findItem1 = pc.getInventory().findItemId(560028);
				if(findItem != null || findItem1 != null )
					toActive(pc, itemId, l1iteminstance, 0);
				else
					pc.sendPackets(new S_ServerMessage(276));
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
			} else if(pc.getMapId()>=12852 && pc.getMapId()<=12861) {
				int find_item_ids[] = {
						830022, 	// 1층
						830023, 	// 2층
						830024, 	// 3층
						830025, 	// 4층
						830026, 	// 5층
						830027, 	// 6층
						830028, 	// 7층
						830029, 	// 8층
						830030, 	// 9층
						830031   	// 10층
				};
				L1ItemInstance findItem = pc.getInventory().findItemId(find_item_ids[pc.getMapId() - 12852]);
				L1ItemInstance findItem1 = pc.getInventory().findItemId(4100135); // 환상지배의탑 지배부적
				L1ItemInstance findItem2 = pc.getInventory().findItemId(560028);
				if(findItem != null || findItem1 != null || findItem2 != null)
					toActive(pc, itemId, l1iteminstance, 0);
				else
					pc.sendPackets(new S_ServerMessage(276));
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
			} else if(pc.getMapId() == 111) { //오만 정상
//				System.out.println(pc.is_dominion_tel());
				if (pc.is_dominion_tel()){
					toActive(pc, itemId, l1iteminstance, 0);
				} else{
					pc.sendPackets(new S_ServerMessage(276));
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
				}
			} else if(pc.getMapId() == 12862) { //지배 정상
				if (pc.is_dominion_tel()){
					toActive(pc, itemId, l1iteminstance, 0);
				} else{
					pc.sendPackets(new S_ServerMessage(276));
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
				}
//				L1ItemInstance findItem = pc.getFavorBook().getItemId(900111);
//						pc.getInventory().findItemId(30001149); // 사신의 가호석
//				if(findItem != null )
//					toActive(pc, itemId, l1iteminstance, 0);
//				else
//					pc.sendPackets(new S_ServerMessage(276));
//					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
			} else {
				L1Map map = pc.getMap();
				if(map.isTeleportable() || pc.isGm() || (pc.getInventory().checkItem(900111)) && map.isRuler()){
					toActive(pc, itemId, l1iteminstance, 0);
				}else{
					pc.sendPackets(new S_ServerMessage(276));
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
				}
			}
		}
		// TODO 40100 類型變更後在禁用傳送的區域嘗試傳送時解決手掌問題
		pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
		pc.cancelAbsoluteBarrier();
	}

	public static void toActive(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance, int skillId) {
//        System.out.println("確認3");
		L1Location newLocation = pc.getLocation().randomLocation(500, true);
		int newX = newLocation.getX();
		int newY = newLocation.getY();

		short mapId = (short) newLocation.getMapId();
		// SC_CROWD_CONTROL_NOTI有傳送回城的bug，所以暫時這樣處理
		// 搜索SC_CROWD_CONTROL_NOTI可以找到使用的源代碼。
		// 檢查傳送類型的道具和例程，如果可能，應使用SC_CROWD_CONTROL_NOTI進行處理是正統做法...
		// 沒有時間，所以暫時這樣。
		if(MJTellBookChain.getInstance().handle(pc, itemId, l1iteminstance, skillId, mapId, newX, newY)) {
			pc.sendPackets(6648);
//			pc.sendPackets(new S_Paralysis(7, false));
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
			return;
		}
			
		Iterator<L1PcInstance> iter = null;
		L1PcInstance member = null;
		
		if (itemId == 40086) {  //-- 매스 텔레포터 주문서
			iter = L1World.getInstance().getVisiblePlayer(pc).iterator();
			
			while (iter.hasNext()) {
				member = iter.next();
				if (member == null) {
					continue;
				}
				if (pc.getLocation().getTileLineDistance(member.getLocation()) <= 3 && member.getClanid() == pc.getClanid() && pc.getClanid() != 0 && member.getId() != pc.getId() && !member.isPrivateShop()) {
					member.start_teleport(newX, newY, mapId, member.getHeading(), 18339, true, true);
				}
			}  
		}
		if (skillId == MASS_TELEPORT) {
			Random random = new Random();
			L1Map map = L1WorldMap.getInstance().getMap(mapId);
			int newX2 = newX + random.nextInt(3) + 1;
			int newY2 = newY + random.nextInt(3) + 1;
			
			iter = L1World.getInstance().getVisiblePlayer(pc, 3).iterator();
			
			start_action(pc);
			
			while (iter.hasNext()) {
				member = iter.next();
				if (member == null) {
					continue;
				}
				
				if (pc.getClanid() != 0 && member.getClanid() == pc.getClanid() && member.getId() != pc.getId() && !member.isPrivateShop()) {
					if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
						member.start_teleport(newX2, newY2, mapId, member.getHeading(), 18339, true, true);
					} else {
						member.start_teleport(newX, newY, mapId, member.getHeading(), 18339, true, true);
					}
				}
			}
//			System.out.println("確認4");
			
			if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2)) {
				pc.start_teleport(newX2, newY2, mapId, pc.getHeading(), 18339, true, true);
			} else {
				pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, true);
			}
		} else if (skillId == L1SkillId.TELEPORT) {
			start_action(pc);
			pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, true);
		} else {
			pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
		}
		
		if (l1iteminstance != null) {
			pc.getInventory().removeItem(l1iteminstance, 1);
		}
	}
	
	private static void start_action(L1PcInstance pc) {
		S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), 19);
		pc.sendPackets(gfx);
		pc.broadcastPacket(gfx);
	}
	
}