package l1j.server.server.server.clientpackets;


import l1j.server.server.server.model.Instance.L1PcInstance;

public class C_ClanMatching extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_CLANMATCHING = "[C] C_ClanMatching";

	public <GameClient> C_ClanMatching(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		/**
		 * 類型
		 * 0: 登錄, 修改                        ' 完成 '
		 * 1: 註銷, 只對君主                    ' 完成 '
		 * 2: 推薦血盟, 刷新                    ' 完成 '
		 * 3: 申請列表, 刷新                    ' 完成 '
		 * 4: 請求列表, 刷新                    ' 完成 '
		 * 5: 申請. clanobjid                  ' 完成 '
		 * 6: 類型 1: 批准, 2: 拒絕, 3: 刪除    ' 完成 '
		 */
		/*int type = readC();
		int objid = 0;
		String text = null;
		int htype = 0;
		if (type == 0) {
			L1ClanMatching cml = L1ClanMatching.getInstance();
			htype = readC();
			text = readS();
			if (!cml.isClanMatchingList(pc.getClanname())) {
				cml.writeClanMatching(pc.getClanname(), text, htype);
			} else {
				cml.updateClanMatching(pc.getClanname(), text, htype);
			}

		} else if (type == 1) {
			L1ClanMatching cml = L1ClanMatching.getInstance();
			if (cml.isClanMatchingList(pc.getClanname())) {
				cml.deleteClanMatching(pc);
			}
		} else if (type == 4) {
			L1ClanMatching cml = L1ClanMatching.getInstance();
			if (pc.getClanid() == 0) {
				if (!pc.isCrown()) {
					cml.loadClanMatchingApcList_User(pc);
				}
			} else {
				switch (pc.getClanRank()) {
					case 4:	case 6: case 9:case 10: // 副君主, 血盟君主, 守護騎士
						cml.loadClanMatchingApcList_Crown(pc);
					break; 
				}
			}
		
		} else if (type == 5) {
			objid = readD();
			L1Clan clan = getClan(objid);
			if (clan != null && !pc.getCMAList().contains(clan.getClanName())) {
				L1ClanMatching cml = L1ClanMatching.getInstance();
				cml.writeClanMatchingApcList_User(pc, clan);
			}
		} else if (type == 6) {
			objid = readD();
			htype = readC(); // 1: 批准, 2: 拒絕, 3: 刪除
			L1ClanMatching cml = L1ClanMatching.getInstance();
			if (htype == 1) {
				L1Object target = L1World.getInstance().findObject(objid);
				if (target != null & target instanceof L1PcInstance) {
					L1PcInstance user = (L1PcInstance) target;
					if (!pc.getCMAList().contains(user.getName())) {
						pc.sendPackets(new S_SystemMessage("取消申請的用戶。"));
					} else {
						if (L1ClanJoin.getInstance().ClanJoin(pc, user)) {
							cml.deleteClanMatchingApcList(user);
						}
					}
				} else if (target == null) {
					pc.sendPackets(new S_SystemMessage("用戶不在線。"));
				}
			} else if (htype == 2) {
				L1Object target = L1World.getInstance().findObject(objid);
				if (target != null) {
					if (target instanceof L1PcInstance) {
						L1PcInstance user = (L1PcInstance) target;
						user.removeCMAList(pc.getName());
						pc.removeCMAList(user.getName());
						cml.deleteClanMatchingApcList(user, user.getId(), pc.getClan());
					}
				} else {
					cml.deleteClanMatchingApcList(null, objid, pc.getClan());
				}
			} else if (htype == 3) {
				L1Clan clan = getClan(objid);
				if (clan != null && pc.getCMAList().contains(clan.getClanName())) {
					cml.deleteClanMatchingApcList(pc, clan);
				}
			}
		}
		pc.sendPackets(new S_ClanMatching(pc, type, objid, text, htype));*/
	}

//	private L1Clan getClan(int objid) {
//		L1Clan clan = null;
//		for (L1Clan c : L1World.getInstance().getAllClans()) {
//			if (c.getClanId() == objid) {
//				clan = c;
//				break;
//			}
//		}
//		return clan;
//	}


	@Override
	public String getType() {
		return C_CLANMATCHING;
	}

}
