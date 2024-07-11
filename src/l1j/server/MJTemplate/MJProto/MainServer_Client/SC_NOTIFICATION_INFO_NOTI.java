package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import l1j.server.Config;
import l1j.server.DeathMatch.DeathMatchSystem;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.NotificationInfomation.EventNpcData;
import l1j.server.MJTemplate.MJProto.MainServer_Client.NotificationInfomation.EventNpcInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client.NotificationInfomation.TeleportData;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Controller.EventThread;
import l1j.server.server.datatables.AinhasadBonusMonsterTable;
import l1j.server.server.datatables.EventTimeTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.EventTimeTemp;

public class SC_NOTIFICATION_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_NOTIFICATION_INFO_NOTI newInstance(L1PcInstance pc, int npcid, boolean check) {
		SC_NOTIFICATION_INFO_NOTI noti = newInstance();
		noti.set_currentpagecount(1);
		noti.set_maxpagecount(1);

		SC_NOTIFICATION_CHANGE_NOTI change_noti = SC_NOTIFICATION_CHANGE_NOTI.newInstance();
		Iterator<L1NpcInstance> iter = EventTimeTable.getInstance().get_npc_iter();
		L1NpcInstance npc = null;

		while (iter.hasNext()) {
			npc = iter.next();
			if (npc == null) {
				continue;
			}
			
			if (!npc.is_boss_alarm())
				continue;

			if (check) {
				if (npc.getNpcId() != npcid) {
					continue;
				}
			}
//			System.out.println(npc.getNpcId()+"+"+npc.get_boss_hour()+"+"+npc.get_boss_minute());
			//TODO 여왕 개미 은신처 출현 이펙트
			if (npc.getNpcId() == 8503163) {
				if (EventThread.getInstance().get_boss_spawn_time(npc.get_boss_hour(), npc.get_boss_minute())) {
					SC_NOTIFICATION_CHANGE_NOTI.sendQueenAnt(pc);
					continue;
				}
			}

			if (npc.getNpcId() == 120717){
				if (EventThread.getInstance().get_boss_spawn_time(npc.get_boss_hour(), npc.get_boss_minute())) {
					SC_NOTIFICATION_CHANGE_NOTI.AnimationAlam(pc, npc);
					continue;
				}
			} 
			
			
			/*if (npc.getNpcId() == 120717 && (System.currentTimeMillis() >= npc.get_boss_time() && System.currentTimeMillis() < npc.get_end_boss_time())) {
				SC_NOTIFICATION_CHANGE_NOTI.AnimationAlam(pc, npc);
				continue;
			}*/

			String main_name = "orc", monster_name = npc.getName();

			if (npc.getNpcId() == 81111) {
				monster_name = "紅色騎士團的進擊";
			}

			int bosstype = npc.get_boss_type();
			long endtime = npc.get_end_boss_time() / 1000; // 알람창에서 사라질 시간 분단위 1이면 1분
			long start_time = npc.get_boss_time() / 1000; // 보스 뜨는시간
			long current_time = System.currentTimeMillis();
			// TODO 강제로 이름을 변경한다
			String TeamDeathMatchName = Config.DeathMatch.TEAM_DEATH_MATCH_NAME;
			if (bosstype == 100 || bosstype == 101) {
				monster_name = "統治之塔";
			} else if (bosstype == 102) {
				monster_name = "無限大戰";
			} else if (bosstype == 5) {
				monster_name = "激戰的圓形競技場";
			} else if (bosstype == 50) {
				monster_name = "被遺忘的島嶼";
			//                Timestamp et = new Timestamp(endtime);
			//                System.out.println(et);
			} else if (bosstype == 51) {
				monster_name = TeamDeathMatchName + " 死鬥 1次";
			} else if (bosstype == 52) {
				monster_name = TeamDeathMatchName + " 死鬥 2次";
			} else if (bosstype == 53) {
				monster_name = TeamDeathMatchName + " 死鬥 3次";
			} else if (bosstype == 54) {
				monster_name = TeamDeathMatchName + " 死鬥 4次";
			}
			}
/*			if (start_time - (current_time / 1000) > 3 * 60 * 60) {
				continue;
			}*/
			
			
//			System.out.println("이벤트 시작시간"+start_time);
			
			
			
			// System.out.println("monster_name : " + monster_name + " / start_time : " +
			// start_time + " / endtime : "+ endtime);

			NotificationInfomation info = NotificationInfomation.newInstance();
			info.set_notification_id(bosstype);
			info.set_hyperlink(main_name.getBytes());
			info.set_displaydesc(monster_name.getBytes());
			if (bosstype == 17) { // 열기구
				long currentSeconds = System.currentTimeMillis() / 1000L;
				info.set_startdate(currentSeconds - 1);
				info.set_enddate((currentSeconds + 86400 + 60));
			} else {
				info.set_startdate(start_time);
				info.set_enddate(endtime);
			}
			EventTimeTemp eventTimeTemp = npc.getEventtimeTemp();
			if (eventTimeTemp != null) {
				info.set_rest_gauge_icon_display(eventTimeTemp.isAinEffect());
				info.set_new(eventTimeTemp.isNewEffect());
				Integer effect_value = AinhasadBonusMonsterTable.getInstance().getAlarmAinhasadBonus(npc.getNpcId());
				if (effect_value != null) {
					info.set_rest_gauge_bonus_display(effect_value);
				}
			}
			info.set_objectid(pc.getId());

			TeleportData tel = TeleportData.newInstance();
			byte[] string = new byte[] { 52, 54, 53, 52, };
			tel.set_stringk(string);
			tel.set_adenacount(npc.get_boss_tel_count());
			if (npc.is_boss_tel()) {
				info.set_teleport(tel);
			}

			if (npc.getEventTitle() != null) {
				EventNpcData npcdata = EventNpcData.newInstance();
				info.set_displaydesc(npc.getEventTitle().getBytes());
				String[] subtitle = (String[]) MJArrangeParser.parsing(npc.getEventSubTitle(), ",", MJArrangeParseeFactory.createStringArrange()).result();
				String[] subnpc = (String[]) MJArrangeParser.parsing(npc.getEventSubNpc(), ",", MJArrangeParseeFactory.createStringArrange()).result();
				for (int i = 0; i < subtitle.length; i++) {
					EventNpcInfo npcinfo = EventNpcInfo.newInstance();
					int subnpcid = Integer.parseInt(subnpc[i]);
//					System.out.println(subnpcid);
					L1Object shop = L1World.getInstance().isNpcShop(subnpcid);
					npcinfo.set_npc_id(shop.getId());
					npcinfo.set_displaydesc(subtitle[i].getBytes());
					npcdata.add_eventinfo(npcinfo);

					// info.set_new(true);
				}

				info.set_eventnpc(npcdata);
			}
			noti.add_notificationinfo(info);
			change_noti.add_delete_notification_id(bosstype);
			change_noti.add_delete_objectid(info.get_objectid());
		}if(change_noti.isInitialized())

	{
		pc.sendPackets(change_noti, MJEProtoMessages.SC_NOTIFICATION_CHANGE_NOTI);
	}return noti;
	}

	public static SC_NOTIFICATION_INFO_NOTI newInstance() {
		return new SC_NOTIFICATION_INFO_NOTI();
	}

	public static ProtoOutputStream make_stream(L1PcInstance pc, int npcid, boolean check) {
		SC_NOTIFICATION_INFO_NOTI noti = newInstance(pc, npcid, check);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_NOTIFICATION_INFO_NOTI);
		noti.dispose();
		return stream;
	}

	public static SC_NOTIFICATION_INFO_NOTI newEventTick() {
		SC_NOTIFICATION_INFO_NOTI noti = SC_NOTIFICATION_INFO_NOTI.newInstance();
		noti.set_maxpagecount(1);
		noti.set_currentpagecount(1);
		return noti;
	}

	private static void newEventTick(final Collection<L1PcInstance> col) {
		SC_NOTIFICATION_INFO_NOTI noti = newEventTick();
		for (L1PcInstance pc : col) {
			if (pc == null || pc.getNetConnection() == null) {
				continue;
			}

			pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_INFO_NOTI, false);
		}
		noti.dispose();
	}

	public static void onEventTick(final L1PcInstance pc, final long delayMillis) {
		if (delayMillis <= 0) {
			pc.sendPackets(newEventTick(), MJEProtoMessages.SC_NOTIFICATION_INFO_NOTI);
		}

		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				pc.sendPackets(newEventTick(), MJEProtoMessages.SC_NOTIFICATION_INFO_NOTI);
			}
		}, delayMillis);
	}

	public static void onEventTicks(final Collection<L1PcInstance> col, final long delayMillis) {
		if (delayMillis <= 0) {
			newEventTick(col);
		}
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				newEventTick(col);
			}
		}, delayMillis);
	}

	private int _maxpagecount;
	private int _currentpagecount;
	private java.util.LinkedList<NotificationInfomation> _notificationinfo;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_NOTIFICATION_INFO_NOTI() {
	}

	public int get_maxpagecount() {
		return _maxpagecount;
	}

	public void set_maxpagecount(int val) {
		_bit |= 0x1;
		_maxpagecount = val;
	}

	public boolean has_maxpagecount() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_currentpagecount() {
		return _currentpagecount;
	}

	public void set_currentpagecount(int val) {
		_bit |= 0x2;
		_currentpagecount = val;
	}

	public boolean has_currentpagecount() {
		return (_bit & 0x2) == 0x2;
	}

	public java.util.LinkedList<NotificationInfomation> get_notificationinfo() {
		return _notificationinfo;
	}

	public void add_notificationinfo(NotificationInfomation val) {
		if (!has_notificationinfo()) {
			_notificationinfo = new java.util.LinkedList<NotificationInfomation>();
			_bit |= 0x4;
		}
		_notificationinfo.add(val);
	}

	public boolean has_notificationinfo() {
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
		if (has_maxpagecount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _maxpagecount);
		if (has_currentpagecount())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _currentpagecount);
		if (has_notificationinfo()) {
			for (NotificationInfomation val : _notificationinfo)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_maxpagecount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_currentpagecount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_notificationinfo()) {
			for (NotificationInfomation val : _notificationinfo) {
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
		if (has_maxpagecount()) {
			output.wirteInt32(1, _maxpagecount);
		}
		if (has_currentpagecount()) {
			output.wirteInt32(2, _currentpagecount);
		}
		if (has_notificationinfo()) {
			for (NotificationInfomation val : _notificationinfo) {
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
					set_maxpagecount(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_currentpagecount(input.readInt32());
					break;
				}
				case 0x0000001A: {
					add_notificationinfo(
							(NotificationInfomation) input.readMessage(NotificationInfomation.newInstance()));
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
		return new SC_NOTIFICATION_INFO_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_notificationinfo()) {
			for (NotificationInfomation val : _notificationinfo)
				val.dispose();
			_notificationinfo.clear();
			_notificationinfo = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
