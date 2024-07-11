package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1Skills;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SPELL_BUFF_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	private static final int[][] ANGER_STAT_MAPPER = new int[][] { { 0, 0 }, { 6432, 3932 }, { 6433, 3933 },
			{ 6434, 3934 }, { 6435, 3935 }, { 6436, 3936 }, { 6437, 3937 }, };

	public static void sendFatigueOff(L1PcInstance pc, int penaltyLevel) {
		if (penaltyLevel <= 0 || penaltyLevel >= ANGER_STAT_MAPPER.length)
			return;

		int iconId = ANGER_STAT_MAPPER[penaltyLevel][0];
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.END);
		noti.set_spell_id(iconId);
		noti.set_off_icon_id(0);
		noti.set_on_icon_id(0);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}

	public static void sendFatigueOn(L1PcInstance pc, int penaltyLevel, int time) {
		if (penaltyLevel <= 0 || penaltyLevel >= ANGER_STAT_MAPPER.length)
			return;

		int iconId = ANGER_STAT_MAPPER[penaltyLevel][0];
		int tooltipId = ANGER_STAT_MAPPER[penaltyLevel][1];
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.RESTAT);
		noti.set_spell_id(iconId);
		noti.set_duration(time * 60);
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
		noti.set_on_icon_id(iconId);
		noti.set_off_icon_id(iconId);
		noti.set_icon_priority(1);
		noti.set_tooltip_str_id(tooltipId);
		noti.set_new_str_id(3215);
		noti.set_end_str_id(2238);
		noti.set_is_good(true);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}

	public static void sendDatabaseIcon(L1PcInstance pc, L1Skills skill, int time, boolean onoff) {
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();

		eNotiType onoffType = eNotiType.NEW;
		if (pc.hasSkillEffect(skill.getSkillId()))
			onoffType = eNotiType.RESTAT;
		if (!onoff)
			onoffType = eNotiType.END;

		noti.set_noti_type(onoffType);
		noti.set_spell_id(skill.getSkillId() < 400 ? (skill.getSkillId() - 1) : skill.getSkillId());

		if (onoffType != eNotiType.END) {
			noti.set_duration(time);
			noti.set_duration_show_type(skill.getDurationShowType());

			int start_icon = skill.getInvenIconStart();
			int tooltip_str = skill.getInvenIconStringNo();
			int new_str_id = skill.getInvenIconStartStrId();
			if (skill.getSkillId() == L1SkillId.REDUCTION_ARMOR) {

				if (pc.isPassive(MJPassiveID.REDUCTION_ARMOR_VETERAN.toInt())) {
					start_icon = 9831; // 9831 //1889
				}
			}
			noti.set_on_icon_id(start_icon);
			noti.set_off_icon_id(skill.getInvenIconEnd());
			noti.set_icon_priority(skill.getInvenIconSort());
			noti.set_tooltip_str_id(tooltip_str);
			// noti.set_tooltip_str_id(skill.getInvenIconStringNo());
			noti.set_new_str_id(new_str_id);
			noti.set_end_str_id(skill.getInvenIconEndStrId());
			noti.set_overlap_buff_icon(skill.getInvenIconOverLapBuffIcon());
			noti.set_is_good(skill.isBuff());
			noti.set_buff_group_id(skill.getInvenIconBuffGroupId());
			noti.set_buff_group_priority(skill.getInvenIconBuffGroupPriority());
			noti.set_buff_icon_priority(skill.getInvenIconBuffIconPriority());
			noti.set_main_tooltip_str_id(skill.getInvenIconMainTooltipStrId());
		} else {
			noti.set_off_icon_id(skill.getInvenIconEnd());
			noti.set_end_str_id(skill.getInvenIconEndStrId());
		}

		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
	}

	public static void sendBerserk(L1PcInstance pc, boolean onoff) {
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		int icon, msg;
		if (onoff) {
			icon = 10717;
			msg = 8614;
		} else {
			icon = 10654;
			msg = 8613;
		}
		noti.set_noti_type(eNotiType.NEW);
		noti.set_spell_id(91);
		noti.set_on_icon_id(icon);
		noti.set_tooltip_str_id(msg);
		noti.set_is_good(true);
		noti.set_boost_type(onoff ? eBoostType.HP_UI_CHANGE : eBoostType.BOOST_NONE);

		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);

	}

	public static SC_SPELL_BUFF_NOTI newInstance() {

		return new SC_SPELL_BUFF_NOTI();
	}

	private eNotiType _noti_type;
	private int _spell_id;
	private int _duration;
	private eDurationShowType _duration_show_type;
	private int _on_icon_id;
	private int _off_icon_id;
	private int _icon_priority;
	private int _tooltip_str_id;
	private int _new_str_id;
	private int _end_str_id;
	private boolean _is_good;
	private int _overlap_buff_icon;
	private int _main_tooltip_str_id;
	private int _buff_icon_priority;
	private int _buff_group_id;// 1월3일자 추가
	private int _buff_group_priority;
	private int _expire_duration;
	private SC_SPELL_BUFF_NOTI.eBoostType _boost_type; // 22년 7월 26일 추가
	private boolean _is_passive_spell;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_SPELL_BUFF_NOTI() {
	}

	public eNotiType get_noti_type() {
		return _noti_type;
	}

	public void set_noti_type(eNotiType val) {
		_bit |= 0x1;
		_noti_type = val;
	}

	public boolean has_noti_type() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_spell_id() {
		return _spell_id;
	}

	public void set_spell_id(int val) {
		_bit |= 0x2;
		_spell_id = val;
	}

	public boolean has_spell_id() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_duration() {
		return _duration;
	}

	public void set_duration(int val) {
		_bit |= 0x4;
		_duration = val;
	}

	public boolean has_duration() {
		return (_bit & 0x4) == 0x4;
	}

	public eDurationShowType get_duration_show_type() {
		return _duration_show_type;
	}

	public void set_duration_show_type(eDurationShowType val) {
		_bit |= 0x8;
		_duration_show_type = val;
	}

	public boolean has_duration_show_type() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_on_icon_id() {
		return _on_icon_id;
	}

	public void set_on_icon_id(int val) {
		_bit |= 0x10;
		_on_icon_id = val;
	}

	public boolean has_on_icon_id() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_off_icon_id() {
		return _off_icon_id;
	}

	public void set_off_icon_id(int val) {
		_bit |= 0x20;
		_off_icon_id = val;
	}

	public boolean has_off_icon_id() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_icon_priority() {
		return _icon_priority;
	}

	public void set_icon_priority(int val) {
		_bit |= 0x40;
		_icon_priority = val;
	}

	public boolean has_icon_priority() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_tooltip_str_id() {
		return _tooltip_str_id;
	}

	public void set_tooltip_str_id(int val) {
		_bit |= 0x80;
		_tooltip_str_id = val;
	}

	public boolean has_tooltip_str_id() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_new_str_id() {
		return _new_str_id;
	}

	public void set_new_str_id(int val) {
		_bit |= 0x100;
		_new_str_id = val;
	}

	public boolean has_new_str_id() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_end_str_id() {
		return _end_str_id;
	}

	public void set_end_str_id(int val) {
		_bit |= 0x200;
		_end_str_id = val;
	}

	public boolean has_end_str_id() {
		return (_bit & 0x200) == 0x200;
	}

	public boolean get_is_good() {
		return _is_good;
	}

	public void set_is_good(boolean val) {
		_bit |= 0x400;
		_is_good = val;
	}

	public boolean has_is_good() {
		return (_bit & 0x400) == 0x400;
	}

	public int get_overlap_buff_icon() {
		return _overlap_buff_icon;
	}

	public void set_overlap_buff_icon(int val) {
		_bit |= 0x800;
		_overlap_buff_icon = val;
	}

	public boolean has_overlap_buff_icon() {
		return (_bit & 0x800) == 0x800;
	}

	public int get_main_tooltip_str_id() {
		return _main_tooltip_str_id;
	}

	public void set_main_tooltip_str_id(int val) {
		_bit |= 0x1000;
		_main_tooltip_str_id = val;
	}

	public boolean has_main_tooltip_str_id() {
		return (_bit & 0x1000) == 0x1000;
	}

	public int get_buff_icon_priority() {
		return _buff_icon_priority;
	}

	public void set_buff_icon_priority(int val) {
		_bit |= 0x2000;
		_buff_icon_priority = val;
	}

	public boolean has_buff_icon_priority() {
		return (_bit & 0x2000) == 0x2000;
	}

	public int get_buff_group_id() {
		return _buff_group_id;
	}

	public void set_buff_group_id(int val) {
		_bit |= 0x4000;
		_buff_group_id = val;
	}

	public boolean has_buff_group_id() {
		return (_bit & 0x4000) == 0x4000;
	}

	public int get_buff_group_priority() {
		return _buff_group_priority;
	}

	public void set_buff_group_priority(int val) {
		_bit |= 0x8000;
		_buff_group_priority = val;
	}

	public boolean has_buff_group_priority() {
		return (_bit & 0x8000) == 0x8000;
	}

	public void set_expire_duration(int val) {
		_bit |= 0x10000;
		_expire_duration = val;
	}

	public boolean has_expire_duration() {
		return (_bit & 0x10000) == 0x10000;
	}

	public SC_SPELL_BUFF_NOTI.eBoostType get_boost_type() {
		return _boost_type;
	}

	public void set_boost_type(SC_SPELL_BUFF_NOTI.eBoostType val) {
		_bit |= 0x20000;
		_boost_type = val;
	}

	public boolean has_boost_type() {
		return (_bit & 0x20000) == 0x20000;
	}

	public boolean get_is_passive_spell() {
		return _is_passive_spell;
	}

	public void set_is_passive_spell(boolean val) {
		_bit |= 0x40000;
		_is_passive_spell = val;
	}

	public boolean has_is_passive_spell() {
		return (_bit & 0x40000) == 0x40000;
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
		if (has_noti_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _noti_type.toInt());
		if (has_spell_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _spell_id);
		if (has_duration())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _duration);
		if (has_duration_show_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _duration_show_type.toInt());
		if (has_on_icon_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _on_icon_id);
		if (has_off_icon_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _off_icon_id);
		if (has_icon_priority())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _icon_priority);
		if (has_tooltip_str_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _tooltip_str_id);
		if (has_new_str_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(9, _new_str_id);
		if (has_end_str_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(10, _end_str_id);
		if (has_is_good())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(11, _is_good);
		if (has_overlap_buff_icon())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(12, _overlap_buff_icon);
		if (has_main_tooltip_str_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(13, _main_tooltip_str_id);
		if (has_buff_icon_priority())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(14, _buff_icon_priority);
		if (has_buff_group_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(15, _buff_group_id);
		if (has_buff_group_priority())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(16, _buff_group_priority);
		if (has_expire_duration()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(17, _expire_duration);
		}
		if (has_boost_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(18, _boost_type.toInt());
		}
		if (has_is_passive_spell()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(19, _is_passive_spell);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_noti_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_spell_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_noti_type()) {
			output.writeEnum(1, _noti_type.toInt());
		}
		if (has_spell_id()) {
			output.writeUInt32(2, _spell_id);
		}
		if (has_duration()) {
			output.writeUInt32(3, _duration);
		}
		if (has_duration_show_type()) {
			output.writeEnum(4, _duration_show_type.toInt());
		}
		if (has_on_icon_id()) {
			output.writeUInt32(5, _on_icon_id);
		}
		if (has_off_icon_id()) {
			output.writeUInt32(6, _off_icon_id);
		}
		if (has_icon_priority()) {
			output.writeUInt32(7, _icon_priority);
		}
		if (has_tooltip_str_id()) {
			output.writeUInt32(8, _tooltip_str_id);
		}
		if (has_new_str_id()) {
			output.writeUInt32(9, _new_str_id);
		}
		if (has_end_str_id()) {
			output.writeUInt32(10, _end_str_id);
		}
		if (has_is_good()) {
			output.writeBool(11, _is_good);
		}
		if (has_overlap_buff_icon()) {
			output.writeUInt32(12, _overlap_buff_icon);
		}
		if (has_main_tooltip_str_id()) {
			output.writeUInt32(13, _main_tooltip_str_id);
		}
		if (has_buff_icon_priority()) {
			output.writeUInt32(14, _buff_icon_priority);
		}
		if (has_buff_group_id()) {
			output.writeUInt32(15, _buff_group_id);
		}
		if (has_buff_group_priority()) {
			output.writeUInt32(16, _buff_group_priority);
		}
		if (has_expire_duration()) {
			output.writeUInt32(17, _expire_duration);
		}
		if (has_boost_type()) {
			output.writeEnum(18, _boost_type.toInt());
		}
		if (has_is_passive_spell()) {
			output.writeBool(19, _is_passive_spell);
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

				case 0x00000008: {
					set_noti_type(eNotiType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_spell_id(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_duration(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_duration_show_type(eDurationShowType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000028: {
					set_on_icon_id(input.readUInt32());
					break;
				}
				case 0x00000030: {
					set_off_icon_id(input.readUInt32());
					break;
				}
				case 0x00000038: {
					set_icon_priority(input.readUInt32());
					break;
				}
				case 0x00000040: {
					set_tooltip_str_id(input.readUInt32());
					break;
				}
				case 0x00000048: {
					set_new_str_id(input.readUInt32());
					break;
				}
				case 0x00000050: {
					set_end_str_id(input.readUInt32());
					break;
				}
				case 0x00000058: {
					set_is_good(input.readBool());
					break;
				}
				case 0x00000060: {
					set_overlap_buff_icon(input.readUInt32());
					break;
				}
				case 0x00000068: {
					set_main_tooltip_str_id(input.readUInt32());
					break;
				}
				case 0x00000070: {
					set_buff_icon_priority(input.readUInt32());
					break;
				}
				case 0x00000078: {
					set_buff_group_id(input.readUInt32());
					break;
				}
				case 0x00000080: {
					set_buff_group_priority(input.readUInt32());
					break;
				}
				case 0x00000088: {
					set_expire_duration(input.readUInt32());
					break;
				}
				case 0x00000090: {
					set_boost_type(SC_SPELL_BUFF_NOTI.eBoostType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000098: {
					set_is_passive_spell(input.readBool());
					break;
				}
				default: {
					return this;
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

			L1PcInstance pc = clnt.getActiveChar();
			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)) { // 변신 지배 반지 lv100
				_on_icon_id = 8228;
				_tooltip_str_id = 9089;
				_new_str_id = 9089;
			}
			// System.out.println(_on_icon_id);
			// System.out.println(_spell_id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_SPELL_BUFF_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public enum eNotiType {
		NEW(1), RESTAT(2), END(3), TIMEOUT(4);

		private int value;

		eNotiType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eNotiType v) {
			return value == v.value;
		}

		public static eNotiType fromInt(int i) {
			switch (i) {
				case 1:
					return NEW;
				case 2:
					return RESTAT;
				case 3:
					return END;
				case 4:
					return TIMEOUT;
				default:
					return null;
			}
		}
	}

	public enum eDurationShowType {
		TYPE_EFF_NONE(0), TYPE_EFF_PERCENT(1), TYPE_EFF_MINUTE(2), TYPE_EFF_PERCENT_ORC_SERVER(3),
		TYPE_EFF_EINHASAD_COOLTIME_MINUTE(4), TYPE_EFF_LEGACY_TIME(5), TYPE_EFF_VARIABLE_VALUE(6),
		TYPE_EFF_DAY_HOUR_MIN(7), TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC(8), TYPE_EFF_NSERVICE_TOPPING(9), TYPE_EFF_UNLIMIT(10),
		TYPE_EFF_CUSTOM(11), TYPE_EFF_COUNT(12), TYPE_EFF_RATE(13), TYPE_EFF_EINHASAD_FAVOR(14), TYPE_EFF_MILLISEC(15);

		private int value;

		eDurationShowType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eDurationShowType v) {
			return value == v.value;
		}

		public static eDurationShowType fromInt(int i) {
			switch (i) {
				case 0:
					return TYPE_EFF_NONE;
				case 1:
					return TYPE_EFF_PERCENT;
				case 2:
					return TYPE_EFF_MINUTE;
				case 3:
					return TYPE_EFF_PERCENT_ORC_SERVER;
				case 4:
					return TYPE_EFF_EINHASAD_COOLTIME_MINUTE;
				case 5:
					return TYPE_EFF_LEGACY_TIME;
				case 6:
					return TYPE_EFF_VARIABLE_VALUE;
				case 7:
					return TYPE_EFF_DAY_HOUR_MIN;
				case 8:
					return TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC;
				case 9:
					return TYPE_EFF_NSERVICE_TOPPING;
				case 10:
					return TYPE_EFF_UNLIMIT;
				case 11:
					return TYPE_EFF_CUSTOM;
				case 12:
					return TYPE_EFF_COUNT;
				case 13:
					return TYPE_EFF_RATE;
				case 14:
					return TYPE_EFF_EINHASAD_FAVOR;
				case 15:
					return TYPE_EFF_MILLISEC;
				default:
					return null;
			}
		}

		public static eDurationShowType fromString(String i) {
			switch (i) {
				case "NONE(0)":
					return TYPE_EFF_NONE;
				case "PERCENT(1)":
					return TYPE_EFF_PERCENT;
				case "MINUTE(2)":
					return TYPE_EFF_MINUTE;
				case "PERCENT_ORC_SERVER(3)":
					return TYPE_EFF_PERCENT_ORC_SERVER;
				case "EINHASAD_COOLTIME_MINUTE(4)":
					return TYPE_EFF_EINHASAD_COOLTIME_MINUTE;
				case "LEGACY_TIME(5)":
					return TYPE_EFF_LEGACY_TIME;
				case "VARIABLE_VALUE(6)":
					return TYPE_EFF_VARIABLE_VALUE;
				case "DAY_HOUR_MIN(7)":
					return TYPE_EFF_DAY_HOUR_MIN;
				case "AUTO_DAY_HOUR_MIN_SEC(8)":
					return TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC;
				case "NSERVICE_TOPPING(9)":
					return TYPE_EFF_NSERVICE_TOPPING;
				case "UNLIMIT(10)":
					return TYPE_EFF_UNLIMIT;
				case "CUSTOM(11)":
					return TYPE_EFF_CUSTOM;
				case "COUNT(12)":
					return TYPE_EFF_COUNT;
				case "RATE(13)":
					return TYPE_EFF_RATE;
				case "EINHASAD_FAVOR(14)":
					return TYPE_EFF_EINHASAD_FAVOR;
				case "MILLISEC(14)":
					return TYPE_EFF_MILLISEC;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eDurationShowType，%d", i));
			}
		}
	}

	public enum eBoostType {
		BOOST_NONE(0), HP_UI_CHANGE(1),;

		private int value;

		eBoostType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eBoostType v) {
			return value == v.value;
		}

		public static eBoostType fromInt(int i) {
			switch (i) {
				case 0:
					return BOOST_NONE;
				case 1:
					return HP_UI_CHANGE;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eBoostType，%d", i));
			}
		}
	}
}
