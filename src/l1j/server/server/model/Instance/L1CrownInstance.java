package l1j.server.server.model.Instance;


import java.sql.Timestamp;

import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1CrownInstance extends L1NpcInstance {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public L1CrownInstance(L1Npc template) {
		super(template);
	}

	@Override
	public synchronized void onAction(L1PcInstance player) {
		if(_destroyed)
			return;
		try{
			MJBotAI ai = player.getAI();
			L1Clan clan = player.getClan();
			if(clan == null || clan.getCurrentWar() == null){
				return;
			}
			
			if(ai != null && (ai.getBotType() == MJBotType.REDKNIGHT || ai.getBotType() == MJBotType.PROTECTOR)){
				clan.getCurrentWar().updateDefense(clan);
				deleteMe();
				return;
			}
			
			if(player.getClanid() == 0 || !player.isCrown())
				return;
			
			clan = L1World.getInstance().getClan(player.getClanid());
			if(clan == null || clan.getLeaderId() != player.getId())
				return;
			else if(clan.getCastleId() != 0){
				player.sendPackets(new S_ServerMessage(474));	// 이미 성을 보유
				return;
			}
			if(!checkRange(player))
				return;

			// 從王冠的座標獲取 castle_id
			MJCastleWar war = (MJCastleWar)clan.getCurrentWar();
			int castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());
			if (war == null || war.getDefenseClan().getCastleId() != castle_id) {
				player.sendPackets(new S_SystemMessage("尚未宣告戰爭。"));
				return;
			}
			
			clan.setCastleDate(new Timestamp(System.currentTimeMillis()));
			war.updateDefense(clan);
			deleteMe();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		removeAllKnownObjects();
	}

	private boolean checkRange(L1PcInstance pc) {
		return (getX() - 1 <= pc.getX() && pc.getX() <= getX() + 1 && getY() - 1 <= pc.getY()
				&& pc.getY() <= getY() + 1);
	}
}
