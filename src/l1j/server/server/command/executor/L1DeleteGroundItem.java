package l1j.server.server.server.command.executor;

import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.server.datatables.LetterTable;
import l1j.server.server.server.datatables.PetTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1DeleteGroundItem implements L1CommandExecutor {
	
	private L1DeleteGroundItem() {	}

	public static L1CommandExecutor getInstance() {
		return new L1DeleteGroundItem();
	}


    @Override
	public <L1FurnitureInstance> void execute(L1PcInstance pc, String cmdName, String arg) {
		l1j.server.server.model.Instance.L1ItemInstance l1iteminstance = null;
	//	ArrayList<L1PcInstance> players = null;
	try{
		StringTokenizer st = new StringTokenizer(arg);
		String type = st.nextToken();
		if(type.equalsIgnoreCase("îïÝ»")){
			L1Inventory groundInventory= null;
			LetterTable lettertable = null;
			L1FurnitureInstance furniture = null;
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if (l1object instanceof l1j.server.server.model.Instance.L1ItemInstance) {
					l1iteminstance = (l1j.server.server.model.Instance.L1ItemInstance) l1object;
					if (l1iteminstance.getX() == 0 && l1iteminstance.getY() == 0) { // ÜôãÀò¢Øüß¾îÜÚªù¡£¬ãÀÙ»ìÑîÜá¶êóÚª
						continue;
					}
					//
				//	players = L1World.getInstance().getVisiblePlayer(l1iteminstance, 2);
				//	if (0 == players.size()) {
						groundInventory = L1World.getInstance().getInventory(l1iteminstance.getX(), l1iteminstance.getY(), l1iteminstance.getMapId());
						int itemId = l1iteminstance.getItem(). getItemId();
					if (itemId == 40314 || itemId == 40316) { // õÁÚªîÜûÞãóÝ¬
						PetTable.getInstance().deletePet(l1iteminstance.getId());
					} else if (itemId >= 49016 && itemId <= 49025) { // ãáòµ
						lettertable = new LetterTable();
						lettertable.deleteLetter(l1iteminstance.getId());
					} else if (itemId >= 41383 && itemId <= 41400) { // Ê«Îý
						if (l1object instanceof L1FurnitureInstance) {
							furniture = (L1FurnitureInstance) l1object;
							if (furniture.getItemObjId() == l1iteminstance.getId()) { // ì«Ìèö¢õóîÜÊ«Îý
									FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
								}
							}
						}
						groundInventory.deleteItem(l1iteminstance);
						L1World.getInstance().removeVisibleObject(l1iteminstance);
						L1World.getInstance().removeObject(l1iteminstance);
					}
			}
		}else if(type.equalsIgnoreCase("ò¢Óñ")){
			L1Inventory groundInventory= null;
			LetterTable lettertable = null;
			L1FurnitureInstance furniture = null;
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if(l1object.getMapId() == pc.getMapId()){
					if (l1object instanceof l1j.server.server.model.Instance.L1ItemInstance) {
						l1iteminstance = (l1j.server.server.model.Instance.L1ItemInstance) l1object;
						if (l1iteminstance.getX() == 0 && l1iteminstance.getY() == 0) { // ÜôãÀò¢Øüß¾îÜÚªù¡£¬ãÀÙ»ìÑîÜá¶êóÚª
							continue;
						}
				
							groundInventory = L1World.getInstance().getInventory(l1iteminstance.getX(), l1iteminstance.getY(), l1iteminstance.getMapId());
							int itemId = l1iteminstance.getItem(). getItemId();
						if (itemId == 40314 || itemId == 40316) { // õÁÚªîÜûÞãóÝ¬
							PetTable.getInstance().deletePet(l1iteminstance.getId());
						} else if (itemId >= 49016 && itemId <= 49025) { // ãáòµ
							lettertable = new LetterTable();
							lettertable.deleteLetter(l1iteminstance.getId());
						} else if (itemId >= 41383 && itemId <= 41400) { // Ê«Îý
							if (l1object instanceof L1FurnitureInstance) {
								furniture = (L1FurnitureInstance) l1object;
								if (furniture.getItemObjId() == l1iteminstance.getId()) { // ì«Ìèö¢õóîÜÊ«Îý
										FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
									}
								}
							}
							groundInventory.deleteItem(l1iteminstance);
							L1World.getInstance().removeVisibleObject(l1iteminstance);
							L1World.getInstance().removeObject(l1iteminstance);
						}
				}
			}
		}else{
			pc.sendPackets(String.valueOf(new S_SystemMessage("?×â [ò¢Óñ, îïÝ»]")));
		}
//			L1World.getInstance().broadcastServerMessage("á¦Í£ò¢Óñß¾îÜÚªù¡ì«ù¬GMß¢ð¶¡£");
	} catch (Exception e) {
		pc.sendPackets(String.valueOf(new S_SystemMessage("?×â [ò¢Óñ, îïÝ»]")));
	}
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {

	}
}
