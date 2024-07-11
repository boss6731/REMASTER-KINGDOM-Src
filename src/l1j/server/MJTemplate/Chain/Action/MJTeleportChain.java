package l1j.server.MJTemplate.Chain.Action;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CHANGE_TEAM_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TEAM_ID_SERVER_NO_MAPPING_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TEAM_ID_SERVER_NO_MAPPING_INFO.MAPPING;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_RemoveObject;

public class MJTeleportChain extends MJAbstractActionChain<MJITeleportHandler> {
	
	public static final int ME_MARK_ID = 0; // 101 你的標記（我們隊伍）（標記你補丁後加入的血印）
	
	public static final int CLAN_MARK_ID = 104; //104 標記所有其他穴位（其他穴位可標記相應標記）

	private static final int[] MARK_ID = new int[] { 106 }; // 不流血是可能的

	public static int select_mark_id(L1PcInstance pc, boolean is_team) {
		if (is_team) {
			return ME_MARK_ID;
		}

		if (pc.getClan() != null) { // 電腦有血
			SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
			noti.set_object_id(pc.getId());
			noti.set_object_team_id(ME_MARK_ID);// 向我顯示的標記
			pc.sendPackets(noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, false);
			return CLAN_MARK_ID;
		}

		// 沒有血 沒有血
		return MARK_ID[pc.getId() % MARK_ID.length];
	}

	private static MJTeleportChain _instance;

	public static MJTeleportChain getInstance() {
		if (_instance == null)
			_instance = new MJTeleportChain();
		return _instance;
	}

	private MJTeleportChain() {
		super();
		add_handler(new ServerCustomHandler());
	}

	public boolean is_teleport(L1PcInstance pc, int next_x, int next_y, int map_id) {
		for (MJITeleportHandler handler : m_handlers) {
			if (handler.is_teleport(pc, next_x, next_y, map_id))
				return true;
		}
		return false;
	}

	public void teleported(L1PcInstance pc, int next_x, int next_y, int map_id, int old_mapid) {
		for (MJITeleportHandler handler : m_handlers) {
			handler.on_teleported(pc, next_x, next_y, map_id, old_mapid);
		}
	}

	static class ServerCustomHandler implements MJITeleportHandler {

		@Override
		public boolean on_teleported(L1PcInstance owner, int next_x, int next_y, int map_id, int old_mapid) {
			if (/* owner.isGm() && */(owner.isInvisble() || owner.isGmInvis())) {
				owner.broadcastPacket(new S_RemoveObject(owner));
			}
			// L1SkillId.onMoveEffectHandle(owner);

			if (SC_WORLD_PUT_OBJECT_NOTI.IS_PRESENTATION_MARK) {
				switch (map_id) {
				case 15871:
				case 15881:
				case 15891:
				case 10500:
				case 10502:
				case 1708:
				case 1709:
				case 1710:
				case 12852:
				case 12853:
				case 12854:
				case 12855:
				case 12856:
				case 12857:
				case 12858:
				case 12859:
				case 12860:
				case 12861:
				case 12862: {
					MJShiftBattleCharacterInfo cInfo = owner.get_battle_info();
					if (cInfo == null) {
						SC_TEAM_ID_SERVER_NO_MAPPING_INFO info = SC_TEAM_ID_SERVER_NO_MAPPING_INFO.newInstance();
						MAPPING mapping = MAPPING.newInstance();
						mapping.set_server_no(0);
						mapping.set_team_id(ME_MARK_ID);
						info.add_mapping_info(mapping);

						mapping = MAPPING.newInstance();
						mapping.set_server_no(0);
						mapping.set_team_id(CLAN_MARK_ID);
						info.add_mapping_info(mapping);

						// 這下面有問題...(解決: 相同血盟標記處理, 無血盟標記處理(註釋時) (問題: 血=血標記處理)
						// 這下面處理沒有血盟時(無血盟)
						for (int mark_id : MARK_ID) {
							mapping = MAPPING.newInstance();
							mapping.set_server_no(0);
							mapping.set_team_id(mark_id);
							info.add_mapping_info(mapping);
						}
						owner.sendPackets(info, MJEProtoMessages.SC_TEAM_ID_SERVER_NO_MAPPING_INFO, true);

						SC_CHANGE_TEAM_NOTI_PACKET noti = SC_CHANGE_TEAM_NOTI_PACKET.newInstance();
						noti.set_object_id(owner.getId());
						noti.set_object_team_id(ME_MARK_ID);// 向我顯示的標記
						owner.sendPackets(noti, MJEProtoMessages.SC_CHANGE_TEAM_NOTI_PACKET, false);
					}
				}
					break;
				}
			}

			switch (old_mapid) {
			case 38: {
				MJShiftBattleCharacterInfo cInfo = owner.get_battle_info();
				if (cInfo == null) {
					switch (map_id) {
					case 38:
						break;
					default:
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(owner, next_x, next_y, map_id, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK);
						break;
					}
				}
			}
				break;
			case 1708:
			case 1709:
			case 1710: {
				MJShiftBattleCharacterInfo cInfo = owner.get_battle_info();
				if (cInfo == null) {
					switch (map_id) {
					case 1708:
					case 1709:
					case 1710:
						break;
					default:
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(owner, next_x, next_y, map_id, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK);
						break;
					}
				}
			}
				break;
			case 15871:
			case 15881:
			case 15891:
			case 10500:
			case 10502:
			case 12852:
			case 12853:
			case 12854:
			case 12855:
			case 12856:
			case 12857:
			case 12858:
			case 12859:
			case 12860:
			case 12861:
			case 12862: {
				MJShiftBattleCharacterInfo cInfo = owner.get_battle_info();
				if (cInfo == null) {
					switch (map_id) {
					case 15871:
					case 15881:
					case 15891:
					case 10500:
					case 10502:
					case 12852:
					case 12853:
					case 12854:
					case 12855:
					case 12856:
					case 12857:
					case 12858:
					case 12859:
					case 12860:
					case 12861:
					case 12862:
						break;
					default:
						// 로컬 인터서버일때 읽는다...
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(owner, next_x, next_y, map_id, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK);
						break;
					}
				}
			}
				break;
			}

			return false;
		}

		@Override
		public boolean is_teleport(L1PcInstance owner, int next_x, int next_y, int map_id) {
			return false;
		}

	}
}
