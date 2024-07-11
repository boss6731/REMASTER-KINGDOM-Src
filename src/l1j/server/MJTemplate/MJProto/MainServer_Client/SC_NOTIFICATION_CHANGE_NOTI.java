package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eNotiAnimationType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.NotificationInfomation.TeleportData;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Controller.EventThread;
import l1j.server.server.datatables.AinhasadBonusMonsterTable;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_NOTIFICATION_CHANGE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	public static SC_NOTIFICATION_CHANGE_NOTI send(L1PcInstance pc, NotificationInfomation info) {
		SC_NOTIFICATION_CHANGE_NOTI noti = newInstance();
		noti.add_delete_notification_id(info.get_notification_id());
		noti.add_delete_objectid(info.get_objectid());
		noti.add_change(info);
		pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_CHANGE_NOTI, true);
		return noti;
	}

	public static SC_NOTIFICATION_CHANGE_NOTI reload(L1PcInstance pc) {
		SC_NOTIFICATION_CHANGE_NOTI noti = newInstance();
		for (int i = 0; i < 200; ++i) {
			noti.add_delete_notification_id(i);
			noti.add_delete_objectid(pc.getId());
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_CHANGE_NOTI, true);
		return noti;
	}

	public static ProtoOutputStream change_stream(L1PcInstance pc, NotificationInfomation info) {
		SC_NOTIFICATION_CHANGE_NOTI noti = send(pc, info);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_NOTIFICATION_CHANGE_NOTI);
		noti.dispose();
		return stream;
	}

	public static void AnimationAlam(L1PcInstance pc, L1NpcInstance npc) {
		if (npc == null) {
			return;
		}
		SC_NOTIFICATION_CHANGE_NOTI noti = newInstance();
		NotificationInfomation info = NotificationInfomation.newInstance();
		noti.add_delete_notification_id(info.get_notification_id());
		String monster_name = npc.getName();
		monster_name = "$33543";
		info.add_worlds(EventThread.getInstance().num1);
		info.set_new(true);
		info.set_notification_id(npc.get_boss_type());
		info.set_hyperlink(null);
		info.set_displaydesc(monster_name.getBytes());
		info.set_animation_type(eNotiAnimationType.OMAN_MORPH);
		noti.add_change(info);
		noti.add_delete_objectid(pc.getId());
		pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_CHANGE_NOTI, true);
		noti.dispose();
	}

	public static void sendQueenAnt(L1PcInstance pc) {
		pc.sendPackets(_queenAntCache, false);
	}

	private static ProtoOutputStream _queenAntCache;

	protected static void reLoadedQueenAntCache() {
		SC_NOTIFICATION_CHANGE_NOTI noti = SC_NOTIFICATION_CHANGE_NOTI.newInstance();
		noti.add_delete_notification_id(23);
		NotificationInfomation info = NotificationInfomation.newInstance();
		info.set_notification_id(23);
		info.set_hyperlink(null);
		info.set_displaydesc("$32010".getBytes());

		// NEW Ein 附加效果
		Integer effect_value = AinhasadBonusMonsterTable.getInstance().getAlarmAinhasadBonus(8503163);
		info.set_new(true);
		info.set_rest_gauge_icon_display(true);
		info.set_rest_gauge_bonus_display(effect_value);

		TeleportData td = TeleportData.newInstance();
		byte[] string = new byte[] { 52, 54, 53, 52, };
		td.set_stringk(string);
		td.set_adenacount(1000);
		info.set_teleport(td);
		noti.add_change(info);
		_queenAntCache = noti.writeTo(MJEProtoMessages.SC_NOTIFICATION_CHANGE_NOTI);

		noti.dispose();
		noti = null;
	}

	public static SC_NOTIFICATION_CHANGE_NOTI newInstance() {
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					reLoadedQueenAntCache();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1000);

		return new SC_NOTIFICATION_CHANGE_NOTI();
	}

	private java.util.LinkedList<Integer> _delete_notification_id;
	private java.util.LinkedList<Integer> _delete_objectid;
	private java.util.LinkedList<NotificationInfomation> _change;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_NOTIFICATION_CHANGE_NOTI() {
	}

	public java.util.LinkedList<Integer> get_delete_notification_id() {
		return _delete_notification_id;
	}

	public void add_delete_notification_id(int val) {
		if (!has_delete_notification_id()) {
			_delete_notification_id = new java.util.LinkedList<Integer>();
			_bit |= 0x1;
		}
		_delete_notification_id.add(val);
	}

	public boolean has_delete_notification_id() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<Integer> get_delete_objectid() {
		return _delete_objectid;
	}

	public void add_delete_objectid(int val) {
		if (!has_delete_objectid()) {
			_delete_objectid = new java.util.LinkedList<Integer>();
			_bit |= 0x2;
		}
		_delete_objectid.add(val);
	}

	public boolean has_delete_objectid() {
		return (_bit & 0x2) == 0x2;
	}

	public java.util.LinkedList<NotificationInfomation> get_change() {
		return _change;
	}

	public void add_change(NotificationInfomation val) {
		if (!has_change()) {
			_change = new java.util.LinkedList<NotificationInfomation>();
			_bit |= 0x4;
		}
		_change.add(val);
	}

	public boolean has_change() {
		return (_bit & 0x4) == 0x4;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_delete_notification_id()) {
			for (int val : _delete_notification_id)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, val);
		}
		if (has_delete_objectid()) {
			for (int val : _delete_objectid)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, val);
		}
		if (has_change()) {
			for (NotificationInfomation val : _change)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_delete_notification_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_delete_objectid()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_change()) {
			for (NotificationInfomation val : _change) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_delete_notification_id()) {
			for (int val : _delete_notification_id) {
				output.wirteInt32(1, val);
			}
		}
		if (has_delete_objectid()) {
			for (int val : _delete_objectid) {
				output.wirteInt32(2, val);
			}
		}
		if (has_change()) {
			for (NotificationInfomation val : _change) {
				output.writeMessage(3, val);
			}
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x00000008: {
					add_delete_notification_id(input.readInt32());
					break;
				}
				case 0x00000010: {
					add_delete_objectid(input.readInt32());
					break;
				}
				case 0x0000001A: {
					add_change((NotificationInfomation) input.readMessage(NotificationInfomation.newInstance()));
					break;
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_NOTIFICATION_CHANGE_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_delete_notification_id()) {
			_delete_notification_id.clear();
			_delete_notification_id = null;
		}
		if (has_delete_objectid()) {
			_delete_objectid.clear();
			_delete_objectid = null;
		}
		if (has_change()) {
			for (NotificationInfomation val : _change)
				val.dispose();
			_change.clear();
			_change = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
