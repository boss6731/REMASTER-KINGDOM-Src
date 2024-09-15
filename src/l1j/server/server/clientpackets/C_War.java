package l1j.server.server.clientpackets;
import l1j.server.Config;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;


public class C_War extends ClientBasePacket {

	private static final String C_WAR = "[C] C_War";

	public C_War(byte abyte0[], GameClient clientthread) throws Exception {		
		super(abyte0);
		try {
		int type = readC();
		String s = readS();
		L1PcInstance player = clientthread.getActiveChar();
		if ( player == null)
			return;
			if (type == 0) {
				if (s.equalsIgnoreCase("紅色騎士團")) {
					int castleId = L1CastleLocation.getCastleIdByArea(player);
					if (castleId == 0) {
						player.sendPackets(new S_SystemMessage("攻城戰尚未開始。"));
						return;
					}
					MJCastleWar war = MJCastleWarBusiness.getInstance().get(castleId);
					if (war == null) {
						player.sendPackets(new S_SystemMessage("攻城戰尚未開始。"));
						return;
					}
					MJCastleWarBusiness.getInstance().proclaim(player, castleId);
					return;
				}
			
			MJCastleWar war = MJCastleWarBusiness.getInstance().findWar(s);
			L1Clan c = L1World.getInstance().findClan(s);
			if(war != null){
				MJCastleWarBusiness.getInstance().proclaim(player, war.getCastleId());
			}else if(c != null){				
				MJCastleWarBusiness.getInstance().proclaim(player, c.getCastleId());
			}else{
				player.sendPackets(new S_SystemMessage(String.format("無法找到目標血盟。 [%s]", s)));
			}
				return;
			}

			String playerName = player.getName();
			String clanName = player.getClanname();
			int clanId = player.getClanid();
			if (player.getRedKnightClanId() != 0) {
				L1Clan clan = L1World.getInstance().getClan(player.getRedKnightClanId());
				if (clan == null) {
					player.sendPackets(new S_SystemMessage("無法找到目標血盟。"), true);
					player.setRedKnightClanId(0);
					return;
				}
			
			clanName 	= clan.getClanName();
			clanId		= clan.getClanId();
		}

			if (!player.isCrown() && player.getRedKnightClanId() == 0) { // 非君主
				player.sendPackets(new S_ServerMessage(478)); // \f1只有王子和公主才能宣戰。
				return;
			}
			if (clanId == 0) { // 無血盟
				player.sendPackets(new S_ServerMessage(272)); // \f1必須先創建血盟才能宣戰。
				return;
			}

			L1Clan clan = L1World.getInstance().getClan(clanId);
			if (clan == null) {
				S_SystemMessage sm = new S_SystemMessage("無法找到目標血盟。");
				player.sendPackets(sm);
				sm = null;
				return;
			}

			if (player.getId() != clan.getLeaderId() && player.getRedKnightClanId() == 0) { // 非血盟主
				player.sendPackets(new S_ServerMessage(478)); // \f1只有王子和公主才能宣戰。
				return;
			}

			if (clanName.toLowerCase().equals(s.toLowerCase())) { // 已指定自己血盟
				return;
			}

		L1Clan enemyClan = null;
		String enemyClanName = null;
		for (L1Clan checkClan : L1World.getInstance().getAllClans()) { // 크란명을 체크
			if (checkClan.getClanName().toLowerCase().equals(s.toLowerCase())) {
				enemyClan = checkClan;
				enemyClanName = checkClan.getClanName();
				break;
			}
		}
			if (enemyClan == null) {
				S_SystemMessage sm = new S_SystemMessage("無法找到目標血盟。");
				player.sendPackets(sm);
				sm = null;
				return;
			}

			boolean inWar = false;
			MJWar war = clan.getCurrentWar();
			if (war != null) {
				if (type == 0) { // 宣戰
					player.sendPackets(new S_ServerMessage(234)); // \f1您的血盟已經在戰爭中。
					return;
				}
				inWar = true;
			}

			if (!inWar && (type == 2 || type == 3)) { // 如果不是在戰爭中，嘗試投降或者結束戰爭
				return;
			}

			if (clan.getCastleId() != 0) { // 如果血盟已經擁有城堡
				if (type == 0) { // 宣戰
					player.sendPackets(new S_ServerMessage(474)); // 您已經擁有一個城堡，無法攻佔其他城堡。
					return;
				} else if (type == 2 || type == 3) { // 投降或結束戰爭
					return;
				}
			}

			// 對方血盟不是城主，並且角色等級低於 52
			if (enemyClan.getCastleId() == 0 && player.getLevel() < 52) {
				player.sendPackets(new S_ServerMessage(232)); // \f1等級低於15的君主不能宣戰。
				return;
			}

			// NPC 點擊宣戰時
			if (enemyClan.getCastleId() != 0 && player.getLevel() < Config.ServerAdSetting.WARMINLEVEL) {
				player.sendPackets(new S_SystemMessage("\\aA王子/公主等級\\aG[" + Config.ServerAdSetting.WARMINLEVEL + "]\\aA 以上才可以宣戰。"));
				return;
			}

			if (player.getRedKnightClanId() == 0) {
				if (clan.getOnlineClanMember().length <= Config.ServerAdSetting.WARPLAYER) {
					player.sendPackets(new S_SystemMessage("當在線的血盟成員達到 " + Config.ServerAdSetting.WARPLAYER + " 人以上時才可以宣戰。"));
					return;
				}
			}

		MJWar enemyWar = enemyClan.getCurrentWar();
			if (enemyClan.getCastleId() != 0) { // 對方血盟是城主
			} else { // 對方血盟不是城主
				boolean enemyInWar = false;
				if (enemyClan.getCurrentWar() != null) {
					if (type == 0) { // 宣戰
						player.sendPackets(new S_ServerMessage(236, enemyClanName)); // \f1%0血盟拒絕了與您的血盟戰爭。
						return;
					} else if (type == 2 || type == 3) { // 投降或者結束戰爭
						if (war.getId() != enemyWar.getId()) { // 自己的血盟和對方血盟處於不同的戰爭中
							return;
						}
					}
					enemyInWar = true;
				}

				if (!enemyInWar && (type == 2 || type == 3)) { // 對方血盟沒有處於戰爭中，嘗試投降或者結束戰爭
					return;
				}

				// 如果不是攻城戰，則需要對方血盟主的批准
				L1PcInstance enemyLeader = L1World.getInstance().getPlayer(enemyClan.getLeaderName());

				if (enemyLeader == null) { // 無法找到對方的血盟主
					player.sendPackets(new S_ServerMessage(218, enemyClanName)); // \f1%0 血盟的君主目前不在伺服器上。
					return;
				}

				if (type == 0) { // 宣戰
					enemyLeader.setTempID(player.getId()); // 保存對方的對象 ID
					enemyLeader.sendPackets(new S_Message_YN(217, clanName, playerName)); // %0血盟的%1希望與你的血盟開戰。是否應戰？(Y/N)
				} else if (type == 2) { // 投降
					enemyLeader.setTempID(player.getId()); // 保存對方的對象 ID
					enemyLeader.sendPackets(new S_Message_YN(221, clanName)); // %0血盟希望投降。是否接受？(Y/N)
				} else if (type == 3) { // 終止
					enemyLeader.setTempID(player.getId()); // 保存對方的對象 ID
					enemyLeader.sendPackets(new S_Message_YN(222, clanName)); // %0血盟希望結束戰爭。是否結束？(Y/N)
			}
		}
		}catch(Exception e) {
			
		}finally{
			clear();
		}
	}

	@Override
	public String getType() {
		return C_WAR;
	}

}
