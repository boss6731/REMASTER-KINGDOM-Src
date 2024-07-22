package MJShiftObject.Object.Converter;

import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.MJShiftObjectHelper;
import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Object.MJShiftObject;
import MJShiftObject.Object.MJShiftObjectOneTimeToken;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJShiftObjectSender {
	private String m_server_identity;
	private ConcurrentHashMap<Integer, MJShiftObject> m_objects;

	public MJShiftObjectSender(String server_identity) {
		this.m_server_identity = server_identity;
		this.m_objects = new ConcurrentHashMap<>();
		this.m_objects_from_accounts = new ConcurrentHashMap<>();
		this.m_sender = MJShiftObjectDBConverterFactory.create_sender(this.m_server_identity);
	}

	private ConcurrentHashMap<String, MJShiftObject> m_objects_from_accounts;
	private IMJShiftObjectDBConverter m_sender;

	public MJShiftObject get_object(int object_id) {
		return this.m_objects.get(Integer.valueOf(object_id));
	}

	public MJShiftObject get_object_from_accounts(String account) {
		return this.m_objects_from_accounts.get(account);
	}

	private MJShiftObject create_object(L1PcInstance pc, MJEShiftObjectType shift_type, String parameters) {
		int object_id = pc.getId();
		MJShiftObject sobject = get_object(object_id);
		if (sobject == null) {
			String string_identity = String.format("%s%X", new Object[]{MJShiftObjectManager.getInstance().get_home_server_identity(), Integer.valueOf(object_id)});


			sobject = MJShiftObject.newInstance().set_source_id(object_id).set_destination_id(0).set_shift_type(shift_type).set_source_character_name(pc.getName()).set_source_account_name(pc.getAccountName()).set_destination_character_name(string_identity).set_destination_account_name(string_identity).set_convert_parameters(parameters);
			this.m_objects.put(Integer.valueOf(object_id), sobject);
			this.m_objects_from_accounts.put(sobject.get_source_account_name(), sobject);
		}
		sobject.set_shift_type(shift_type);
		return sobject;
	}

	public int get_object_count() {
		return this.m_objects.size();
	}

	public void do_getback(GameClient clnt, int source_object_id, MJShiftObjectOneTimeToken token) {
		this.m_sender.delete(token.shift_object);
		this.m_objects.remove(Integer.valueOf(source_object_id));
		this.m_objects_from_accounts.remove(token.shift_object.get_source_account_name());
	}

	public void do_getback() {
		MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), false, false);
		this.m_objects.clear();
		this.m_objects_from_accounts.clear();
	}

	private void leave_clan(L1PcInstance pc) throws Exception {
		L1Clan clan = pc.getClan();
		if (clan == null) {
			return;
		}
		String player_name = pc.getName();
		String clan_name = pc.getClanname();
		L1PcInstance[] clanMember = clan.getOnlineClanMember();

		for (int i = 0; i < clanMember.length; i++) {
			clanMember[i].sendPackets((ServerBasePacket) new S_ServerMessage(178, player_name, clan_name));
		}
		if (pc.isClanBuff()) {
			pc.killSkillEffectTimer(7789);
			pc.sendPackets((ServerBasePacket) new S_PacketBox(180, 450, false));
			pc.setClanBuff(false);
		}
		pc.ClearPlayerClanData(clan);
		clan.removeClanMember(player_name);
	}

	public void do_send(L1PcInstance pc, MJEShiftObjectType shift_type, MJShiftObjectReceiver receiver, String parameters, int kind) throws Exception {
		try {
			if (shift_type.equals(MJEShiftObjectType.TRANSFER))
				leave_clan(pc);
			final int object_id = pc.getId();
			boolean is_reconnected = true;
			MJShiftObject sobject = get_object(pc.getId());
			if (sobject == null) {
				sobject = create_object(pc, shift_type, parameters);
				int result = this.m_sender.work(sobject);
				if (result != 1) {
					System.out.println(String.format("%s(%d)(%s) 的資料庫轉換失敗.(%d) send", new Object[]{pc.getName(), Integer.valueOf(object_id), shift_type.to_name(), Integer.valueOf(result)}));
					return;
				}
				is_reconnected = false;
			}
			SC_CONNECT_HIBREEDSERVER_NOTI_PACKET pck = SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.newInstance();
			pck.set_destIP(receiver.get_server_address_bigendian());
			pck.set_destPort(receiver.get_server_port());
			pck.set_domainname(receiver.get_server_address());
			pck.set_interkind(kind);
			pck.set_onetimetoken((new MJShiftObjectOneTimeToken(receiver.get_server_identity(), false, sobject, this.m_server_identity, is_reconnected)).to_onetime_token().getBytes());
			pck.set_reservednumber(object_id);
			pc.sendPackets((MJIProtoMessage) pck, MJEProtoMessages.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET, true);
			final MJShiftObject sobj = sobject;
			if (shift_type.equals(MJEShiftObjectType.TRANSFER) && !pc.isGm()) {
				GeneralThreadPool.getInstance().execute(new Runnable() {
					public void run() {
						MJShiftObjectHelper.shuffle_character_name(object_id, sobj.get_source_character_name(), sobj.get_source_account_name());
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


