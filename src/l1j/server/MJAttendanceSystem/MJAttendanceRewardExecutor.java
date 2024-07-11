package l1j.server.MJAttendanceSystem;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.AttendanceBonusInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client.AttendanceGroupType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.AttendanceUserFlag;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_GROUP_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_BONUS_INFO_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_DATA_UPDATE_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_REWARD_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_REWARD_ITEM_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_REWARD_ITEM_NOTI.ATTENDANCE_REWARD_ITEM;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_TAB_OPEN_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.MJTemplate.MJProto.MainServer_Client.UserAttendanceDataExtend;
import l1j.server.MJTemplate.MJProto.MainServer_Client.UserAttendanceDataGroup;
import l1j.server.MJTemplate.MJProto.MainServer_Client.UserAttendanceState;
import l1j.server.server.Account;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;

public class MJAttendanceRewardExecutor implements Runnable {

	public static MJAttendanceRewardExecutor newInstance() {
		return new MJAttendanceRewardExecutor();
	}

	private L1PcInstance m_pc;
	private int m_attendance_index;
	private int m_season_num;
	private boolean m_is_probability_reward;
	private AttendanceGroupType m_group_type;
	private AttendanceBonusInfo m_bonus_info;

	private MJAttendanceRewardExecutor() {
	}

	public MJAttendanceRewardExecutor set_season_num(int season_num) {
		m_season_num = season_num;
		return this;
	}

	public int get_season_num() {
		return m_season_num;
	}

	public MJAttendanceRewardExecutor set_pc(L1PcInstance pc) {
		m_pc = pc;
		return this;
	}

	public MJAttendanceRewardExecutor set_attendance_index(int attendance_index) {
		m_attendance_index = attendance_index;
		return this;
	}

	public MJAttendanceRewardExecutor set_is_probability_reward(boolean is_probability_reward) {
		m_is_probability_reward = is_probability_reward;
		return this;
	}

	public MJAttendanceRewardExecutor set_group_type(AttendanceGroupType group_type) {
		m_group_type = group_type;
		return this;
	}

	public MJAttendanceRewardExecutor set_bonus_info(AttendanceBonusInfo bonus_info) {
		m_bonus_info = bonus_info;
		return this;
	}

	public L1PcInstance get_pc() {
		return m_pc;
	}

	public int get_attendance_index() {
		return m_attendance_index;
	}

	public boolean get_is_probability_reward() {
		return m_is_probability_reward;
	}

	public AttendanceGroupType get_group_type() {
		return m_group_type;
	}

	public AttendanceBonusInfo get_bonus_info() {
		return m_bonus_info;
	}

	public void do_execute() {
		// System.out.println("檢查3個傳入封包");

		SC_ATTENDANCE_DATA_UPDATE_EXTEND.send(m_pc, get_attendance_index(), m_group_type, UserAttendanceState.CLEAR);
		if (get_is_probability_reward()) {
			GeneralThreadPool.getInstance().execute(this);
		} else {
			do_bonus_store(m_bonus_info.get_is_broadcast(), false);
		}
	}

	private void do_bonus_store(boolean is_broadcast, boolean is_probability_rewards) {
		try {
			SC_ATTENDANCE_USER_DATA_EXTEND user_data = m_pc.getAttendanceData();
			Account account = m_pc.getAccount();
			user_data.get_groups().get(m_group_type.toInt()).get_groupData().get(m_attendance_index - 1)
					.set_state(UserAttendanceState.CLEAR);
			SC_ATTENDANCE_USER_DATA_EXTEND.update(m_pc.getAccountName(), user_data);

			L1ItemInstance item = m_pc.getInventory().storeItem(m_bonus_info.get_itemId(),
					m_bonus_info.get_itemCount());
			m_pc.getAccount().addBlessOfAin((m_bonus_info.getAinHasadCharge() * 10000), m_pc, "出席");
			SC_ATTENDANCE_REWARD_ACK.send(m_pc, get_attendance_index(), m_group_type, item.getItem().getItemDescId(),
					m_bonus_info.get_itemCount(), is_broadcast);

			if (!m_pc.isGm() && is_broadcast) {
				L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] {
						new S_SystemMessage(String.format("有人已在出席檢查獎勵中於 %d 號獲得了 %s 道具。", m_attendance_index,
								m_bonus_info.get_itemName())),
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
								String.format("有人已在出席檢查獎勵中於 %d 號獲得了 %s 道具。", m_attendance_index,
										m_bonus_info.get_itemName()))
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		L1Item template = ItemTable.getInstance().getTemplate(m_bonus_info.get_itemId());
		MJAttendanceRewardsHistory.newInstance()
				.set_attendance_id(m_attendance_index)
				.set_object_id(m_pc.getId())
				.set_character_name(m_pc.getName())
				.set_group_id(get_group_type().toInt())
				.set_is_probability_rewards(is_probability_rewards)
				.set_reward_item_name(template.getName())
				.set_reward_desc_id(m_bonus_info.get_desc_id())
				.set_reward_item_count(m_bonus_info.get_itemCount())
				.set_season_num(m_season_num)
				.do_update();

		SC_ATTENDANCE_USER_DATA_EXTEND userData = m_pc.getAttendanceData();
		int clearcount = 0;
		for (UserAttendanceDataGroup dGroup : userData.get_groups()) {
			if (m_group_type != AttendanceGroupType.PC_CAFE) {
				if (m_group_type == dGroup.get_groupType()) {
					for (UserAttendanceDataExtend gData : dGroup.get_groupData()) {
						if (gData.get_state().equals(UserAttendanceState.CLEAR))
							++clearcount;
					}

					if (clearcount >= dGroup.get_groupData().size()) {
						MJAttendanceRewardsHistory.DeleteHistory(m_pc, dGroup.get_groupType().toInt());
						userData.get_groups().set(dGroup.get_groupType().toInt(),
								UserAttendanceDataGroup.newInstance(m_pc, dGroup.get_groupType()));
						userData.add_active_group(dGroup.get_groupType());
						m_pc.setAttendanceData(userData);
						userData.set_userFlag(AttendanceUserFlag.USER_FLAG_NORMAL);
						if (!m_pc.is_shift_battle()) {
							SC_ATTENDANCE_USER_DATA_EXTEND.store(m_pc);
							SC_ATTENDANCE_BONUS_INFO_EXTEND.send(m_pc);
							SC_ATTENDANCE_BONUS_GROUP_INFO.send(m_pc);
							SC_ATTENDANCE_USER_DATA_EXTEND.send(m_pc);
							SC_ATTENDANCE_BONUS_GROUP_INFO.openinfo(m_pc);
							MJAttendanceRewardsHistory.send_history(m_pc);
							if (account.getAttendance_Premium()) {
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(m_pc,
										AttendanceGroupType.fromInt(2), 0), true);
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(m_pc,
										AttendanceGroupType.fromInt(2), 0), true);
							}
							if (account.getAttendance_Special()) {
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(m_pc,
										AttendanceGroupType.fromInt(3), 0), true);
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(m_pc,
										AttendanceGroupType.fromInt(3), 0), true);
							}
							if (account.getAttendance_Brave_Warrior()) {
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(m_pc,
										AttendanceGroupType.fromInt(4), 1), true);
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(m_pc,
										AttendanceGroupType.fromInt(4), 1), true);
							}
							if (account.getAttendance_Aden_World()) {
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(m_pc,
										AttendanceGroupType.fromInt(5), 1), true);
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(m_pc,
										AttendanceGroupType.fromInt(5), 1), true);
							}
							if (account.getAttendance_Bravery_Medal()) {
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_server(m_pc,
										AttendanceGroupType.fromInt(6), 1), true);
								m_pc.sendPackets(SC_ATTENDANCE_TAB_OPEN_ACK.make_stream_db(m_pc,
										AttendanceGroupType.fromInt(6), 1), true);
							}
						}
					}
				}
			}
		}
	}catch(

	IOException e)
	{
		e.printStackTrace();
	}
	}

	// public void do_clear(L1PcInstance pc) {
	// Updator.exec("delete from attendance_rewards_history where object_id=?", new
	// Handler() {
	// @Override
	// public void handle(PreparedStatement pstm) throws Exception {
	// pstm.setInt(1, pc.getId());
	// }
	// });
	// Updator.exec("delete from attendance_userinfo where account=?", new Handler()
	// {
	// @Override
	// public void handle(PreparedStatement pstm) throws Exception {
	// pstm.setString(1, pc.getAccountName());
	// }
	// });
	// }

	public void isInit() {
		// System.out.println("檢查傳入的封包");
		if (m_pc.getAttendanceData().get_groups() == null) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(0) == null && m_group_type == AttendanceGroupType.NORMAL) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(1) == null && m_group_type == AttendanceGroupType.PC_CAFE) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(2) == null && m_group_type == AttendanceGroupType.PREMIUM) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(3) == null && m_group_type == AttendanceGroupType.SPECIAL) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(4) == null && m_group_type == AttendanceGroupType.BRAVE_WARRIOR) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(5) == null && m_group_type == AttendanceGroupType.ADEN_WORLD) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(6) == null && m_group_type == AttendanceGroupType.BRAVERY_MEDAL) {
			return;
		}

		if (m_pc.getAttendanceData().get_groups().get(0).get_currentAttendanceIndex() + 1 < AttendanceGroupType.NORMAL
				.getBonusSize() && m_group_type == AttendanceGroupType.NORMAL) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(1).get_currentAttendanceIndex() + 1 < AttendanceGroupType.PC_CAFE
				.getBonusSize() && m_group_type == AttendanceGroupType.PC_CAFE) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(2).get_currentAttendanceIndex() + 1 < AttendanceGroupType.PREMIUM
				.getBonusSize() && m_group_type == AttendanceGroupType.PREMIUM) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(3).get_currentAttendanceIndex() + 1 < AttendanceGroupType.SPECIAL
				.getBonusSize() && m_group_type == AttendanceGroupType.SPECIAL) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(4).get_currentAttendanceIndex()
				+ 1 < AttendanceGroupType.BRAVE_WARRIOR.getBonusSize()
				&& m_group_type == AttendanceGroupType.BRAVE_WARRIOR) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(5).get_currentAttendanceIndex()
				+ 1 < AttendanceGroupType.ADEN_WORLD.getBonusSize() && m_group_type == AttendanceGroupType.ADEN_WORLD) {
			return;
		}
		if (m_pc.getAttendanceData().get_groups().get(6).get_currentAttendanceIndex()
				+ 1 < AttendanceGroupType.BRAVERY_MEDAL.getBonusSize()
				&& m_group_type == AttendanceGroupType.BRAVERY_MEDAL) {
			return;
		}
		if (m_group_type.toInt() >= 7) {
			return;
		}
		if (!isclearAll()) {
			return;
		}
		// System.out.println("重置考勤");
		Account account = m_pc.getAccount();
		MJAttendanceRewardsHistory.DeleteHistory(m_pc, m_group_type.toInt());
		SC_ATTENDANCE_USER_DATA_EXTEND userData = m_pc.getAttendanceData();
		if (m_group_type == AttendanceGroupType.NORMAL) {
			userData.get_groups().set(0, UserAttendanceDataGroup.InitData(m_pc, m_group_type));
		}
		if (m_group_type == AttendanceGroupType.PC_CAFE) {
			userData.get_groups().set(1, UserAttendanceDataGroup.InitData(m_pc, m_group_type));
		}
		if (m_group_type == AttendanceGroupType.PREMIUM) {
			account.setAttendance_Premium(false);
			userData.get_groups().set(2, UserAttendanceDataGroup.InitData(m_pc, m_group_type));
		}
		if (m_group_type == AttendanceGroupType.SPECIAL) {
			account.setAttendance_Special(false);
			userData.get_groups().set(3, UserAttendanceDataGroup.InitData(m_pc, m_group_type));
		}
		if (m_group_type == AttendanceGroupType.BRAVE_WARRIOR) {
			account.setAttendance_Brave_Warrior(false);
			userData.get_groups().set(4, UserAttendanceDataGroup.InitData(m_pc, m_group_type));
		}
		if (m_group_type == AttendanceGroupType.ADEN_WORLD) {
			account.setAttendance_Aden_World(false);
			userData.get_groups().set(5, UserAttendanceDataGroup.InitData(m_pc, m_group_type));
		}
		if (m_group_type == AttendanceGroupType.BRAVERY_MEDAL) {
			account.setAttendance_Bravery_Medal(false);
			userData.get_groups().set(6, UserAttendanceDataGroup.InitData(m_pc, m_group_type));
		}

		userData.add_active_group(m_group_type);
		m_pc.setAttendanceData(userData);
		userData.set_userFlag(AttendanceUserFlag.USER_FLAG_NORMAL);
		SC_ATTENDANCE_USER_DATA_EXTEND.store(m_pc);
		if (!m_pc.is_shift_battle()) {
			SC_ATTENDANCE_USER_DATA_EXTEND.send(m_pc);
			SC_ATTENDANCE_BONUS_GROUP_INFO.openinfo(m_pc);
		}
	}

	public boolean isclearAll() {
		SC_ATTENDANCE_USER_DATA_EXTEND user_data = m_pc.getAttendanceData();
		if (m_group_type == AttendanceGroupType.NORMAL) {
			for (int i = 0; i < AttendanceGroupType.NORMAL.getBonusSize(); i++) {
				if (user_data.get_groups().get(AttendanceGroupType.NORMAL.toInt()).get_groupData().get(i)
						.get_state() != UserAttendanceState.CLEAR) {
					return false;
				}
			}
		} else if (m_group_type == AttendanceGroupType.PC_CAFE) {
			for (int i = 0; i < AttendanceGroupType.PC_CAFE.getBonusSize(); i++) {
				if (user_data.get_groups().get(AttendanceGroupType.PC_CAFE.toInt()).get_groupData().get(i)
						.get_state() != UserAttendanceState.CLEAR) {
					return false;
				}
			}
		} else if (m_group_type == AttendanceGroupType.PREMIUM) {
			for (int i = 0; i < AttendanceGroupType.PREMIUM.getBonusSize(); i++) {
				if (user_data.get_groups().get(AttendanceGroupType.PREMIUM.toInt()).get_groupData().get(i)
						.get_state() != UserAttendanceState.CLEAR) {
					return false;
				}
			}
		} else if (m_group_type == AttendanceGroupType.SPECIAL) {
			for (int i = 0; i < AttendanceGroupType.SPECIAL.getBonusSize(); i++) {
				if (user_data.get_groups().get(AttendanceGroupType.SPECIAL.toInt()).get_groupData().get(i)
						.get_state() != UserAttendanceState.CLEAR) {
					return false;
				}
			}
		} else if (m_group_type == AttendanceGroupType.BRAVE_WARRIOR) {
			for (int i = 0; i < AttendanceGroupType.BRAVE_WARRIOR.getBonusSize(); i++) {
				if (user_data.get_groups().get(AttendanceGroupType.BRAVE_WARRIOR.toInt()).get_groupData().get(i)
						.get_state() != UserAttendanceState.CLEAR) {
					return false;
				}
			}
		} else if (m_group_type == AttendanceGroupType.ADEN_WORLD) {
			for (int i = 0; i < AttendanceGroupType.ADEN_WORLD.getBonusSize(); i++) {
				if (user_data.get_groups().get(AttendanceGroupType.ADEN_WORLD.toInt()).get_groupData().get(i)
						.get_state() != UserAttendanceState.CLEAR) {
					return false;
				}
			}
		} else if (m_group_type == AttendanceGroupType.BRAVERY_MEDAL) {
			for (int i = 0; i < AttendanceGroupType.BRAVERY_MEDAL.getBonusSize(); i++) {
				if (user_data.get_groups().get(AttendanceGroupType.BRAVERY_MEDAL.toInt()).get_groupData().get(i)
						.get_state() != UserAttendanceState.CLEAR) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void run() {
		try {
			SC_ATTENDANCE_REWARD_ACK ack = SC_ATTENDANCE_REWARD_ACK.newInstance();
			ack.set_attendance_id(get_attendance_index());
			ack.set_group_id(get_group_type().toInt());
			ack.set_status(3);
			ack.set_item_count(m_bonus_info.get_itemCount());
			ack.set_item_name_id(m_bonus_info.get_desc_id());
			ack.set_broadcast_item(m_bonus_info.get_is_broadcast());
			ack.set_season_num(m_season_num);
			m_pc.sendPackets(ack, MJEProtoMessages.SC_ATTENDANCE_REWARD_ACK, true);
			Thread.sleep(5000L);
	
			SC_ATTENDANCE_REWARD_ITEM_NOTI item_noti = SC_ATTENDANCE_REWARD_ITEM_NOTI.newInstance();
			ATTENDANCE_REWARD_ITEM reward_item = ATTENDANCE_REWARD_ITEM.newInstance();
			reward_item.set_attendance_id(get_attendance_index());
			reward_item.set_group_id(get_group_type().toInt());
			reward_item.set_item_count(m_bonus_info.get_itemCount());
			reward_item.set_item_name_id(m_bonus_info.get_desc_id());
			item_noti.add_reward_item_info(reward_item);
			m_pc.sendPackets(item_noti, MJEProtoMessages.SC_ATTENDANCE_REWARD_ITEM_NOTI, true);
	
			do_bonus_store(m_bonus_info.get_is_broadcast(), true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(String.format("[出席檢查獎勵異常訊息] - 角色：%s，出席檢查ID：%d，組別：%s", m_pc.getName(), get_attendance_index(), get_group_type()));
		}
	}
