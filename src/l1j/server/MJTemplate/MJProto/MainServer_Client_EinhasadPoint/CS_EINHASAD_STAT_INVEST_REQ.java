package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.MJTemplate.MJRnd;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_EINHASAD_STAT_INVEST_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_EINHASAD_STAT_INVEST_REQ newInstance() {
		return new CS_EINHASAD_STAT_INVEST_REQ();
	}

	private int _index;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_EINHASAD_STAT_INVEST_REQ() {
	}

	public int get_index() {
		return _index;
	}

	public void set_index(int val) {
		_bit |= 0x1;
		_index = val;
	}

	public boolean has_index() {
		return (_bit & 0x1) == 0x1;
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
		if (has_index()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _index);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_index()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_index()) {
			output.wirteInt32(1, _index);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				case 0x00000008: {
					set_index(input.readInt32());
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
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
			if (Info != null) {
				int current_stat = Info.get_bless() + Info.get_lucky() + Info.get_vital() + Info.get_invoke()
						+ Info.get_restore() + Info.get_potion() + 1;
				if (current_stat > Info.get_total_stat()) {
					System.out.println(String.format("艾哈薩德屬性超過限制：懷疑使用中繼器 角色名(%s)", pc.getName()));
					return this;
				}

				if (Info.get_bless() < 0 || Info.get_lucky() < 0 || Info.get_vital() < 0
						|| Info.get_invoke() < 0 || Info.get_restore() < 0 || Info.get_potion() < 0) {
					System.out.println(String.format("艾哈薩德屬性超過限制：懷疑使用中繼器 角色名(%s)", pc.getName()));
					return this;
				}

				if (get_index() >= 3 && Info.get_total_stat() < 75) {
					System.out.println(String.format("艾哈薩德屬性超過限制：懷疑使用中繼器 角色名(%s)", pc.getName()));
					return this;
				}

				switch (get_index()) {
					case 0:
						if (Info.get_bless() > 25)
							return this;
						Info.add_bless(1);
						break;
					case 1: {
						if (Info.get_lucky() > 25)
							return this;
						Info.add_lucky(1);
						break;
					}
					case 2: {
						if (Info.get_vital() > 25)
							return this;
						Info.add_vital(1);
						break;
					}
					case 3: {
						if (Info.get_invoke() > 35)
							return this;

						int value = MJRnd.next(10, 20);
						Info.add_invoke(1);
						switch (Info.get_invoke()) {
							case 1:
							case 2:
							case 3:
								Info.add_invoke_val_1(value);
								break;
							case 4:
							case 5:
								Info.add_invoke_val_2(value);
								break;
							case 6:
							case 7:
							case 8:
								Info.add_invoke_val_1(value);
								break;
							case 9:
							case 10:
								Info.add_invoke_val_2(value);
								break;
							case 11:
							case 12:
							case 13:
								Info.add_invoke_val_1(value);
								break;
							case 14:
							case 15:
								Info.add_invoke_val_2(value);
								break;
							case 16:
							case 17:
							case 18:
								Info.add_invoke_val_1(value);
								break;
							case 19:
							case 20:
								Info.add_invoke_val_2(value);
								break;
							case 21:
							case 22:
							case 23:
								Info.add_invoke_val_1(value);
								break;
							case 24:
							case 25:
								Info.add_invoke_val_2(value);
								break;
							case 26:
							case 27:
							case 28:
								Info.add_invoke_val_1(value);
								break;
							case 29:
							case 30:
								Info.add_invoke_val_2(value);
								break;
							case 31:
							case 32:
							case 33:
								Info.add_invoke_val_1(value);
								break;
							case 34:
							case 35:
								Info.add_invoke_val_2(value);
								break;
							default: {
								System.out.println(String.format("通過不正常的方法獲取屬性值。(角色名：%s)", pc.getName()));
								return this;
							}
						}
						break;
					}
					case 4: {
						if (Info.get_restore() > 35)
							return this;
						Info.add_restore(1);
						switch (Info.get_restore()) {
							case 1:
							case 2:
								Info.add_restore_val_1(1);
								break;
							case 3:
							case 4:
							case 5:
								Info.add_restore_val_2(1);
								break;
							case 6:
							case 7:
								Info.add_restore_val_1(1);
								break;
							case 8:
							case 9:
							case 10:
								Info.add_restore_val_2(1);
								break;
							case 11:
							case 12:
								Info.add_restore_val_1(1);
								break;
							case 13:
							case 14:
							case 15:
								Info.add_restore_val_2(1);
								break;
							case 16:
							case 17:
								Info.add_restore_val_1(1);
								break;
							case 18:
							case 19:
							case 20:
								Info.add_restore_val_2(1);
								break;
							case 21:
							case 22:
								Info.add_restore_val_1(1);
								break;
							case 23:
							case 24:
							case 25:
								Info.add_restore_val_2(1);
								break;
							case 26:
							case 27:
								Info.add_restore_val_1(1);
								break;
							case 28:
							case 29:
							case 30:
								Info.add_restore_val_2(1);
								break;
							case 31:
							case 32:
								Info.add_restore_val_1(1);
								break;
							case 33:
							case 34:
							case 35:
								Info.add_restore_val_2(1);
								break;
							default:
								System.out.println(String.format("通過不正常的方法獲取屬性值。(角色名：%s)", pc.getName()));
								return this;
						}
						pc.startAinhasadTimer(Info);
						break;
					}
					case 5: {
						if (Info.get_potion() > 35)
							return this;
						int value_1 = MJRnd.next(10, 30);
						int value_1_1 = MJRnd.next(10, 40);
						int value_2 = MJRnd.next(4, 8);
						Info.add_potion(1);
						switch (Info.get_potion()) {
							case 1:
							case 2:
								Info.add_potion_val_1(value_1);
								break;
							case 3:
								Info.add_potion_val_1(value_1_1);
								break;
							case 4:
							case 5:
								Info.add_potion_val_2(value_2);
								break;
							case 6:
							case 7:
								Info.add_potion_val_1(value_1);
								break;
							case 8:
								Info.add_potion_val_1(value_1 - 1);
								break;
							case 9:
							case 10:
								Info.add_potion_val_2(value_2);
								break;
							case 11:
							case 12:
								Info.add_potion_val_1(value_1);
								break;
							case 13:
								Info.add_potion_val_1(value_1 - 1);
								break;
							case 14:
							case 15:
								Info.add_potion_val_2(value_2);
								break;
							case 16:
							case 17:
								Info.add_potion_val_1(value_1);
								break;
							case 18:
								Info.add_potion_val_1(value_1 - 1);
								break;
							case 19:
							case 20:
								Info.add_potion_val_2(value_2);
								break;
							case 21:
							case 22:
								Info.add_potion_val_1(value_1);
								break;
							case 23:
								Info.add_potion_val_1(value_1 - 1);
								break;
							case 24:
							case 25:
								Info.add_potion_val_2(value_2);
								break;
							case 26:
							case 27:
								Info.add_potion_val_1(value_1);
								break;
							case 28:
								Info.add_potion_val_1(value_1 - 1);
								break;
							case 29:
							case 30:
								Info.add_potion_val_2(value_2);
								break;
							case 31:
							case 32:
								Info.add_potion_val_1(value_1);
								break;
							case 33:
								Info.add_potion_val_1(value_1 - 1);
								break;
							case 34:
							case 35:
								Info.add_potion_val_2(value_2);
								break;
							default:
								System.out.println(String.format("通過不正常的方法獲取屬性值。(角色名：%s)", pc.getName()));
								return this;
						}
						break;
					}
					case 6:
						break;
					case 7:
						break;
					case 8:
						break;
					case 9:
						break;
				}
			}

			AinhasadSpecialStatLoader.getInstance().updateSpecialStat(pc);
			SC_EINHASAD_STAT_INVEST_ACK.send_point_stat_invest(pc, get_index(), Info);
			SC_EINHASAD_POINT_STAT_INFO_NOTI.send_point(pc, Info);
			SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_EINHASAD_STAT_INVEST_REQ();
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
