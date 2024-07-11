package l1j.server.MJTemplate.MJProto.MainServer_Client_Potential;

import l1j.server.Config;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.function.L1MagicDoll;
import l1j.server.server.monitor.LoggerInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_ENCHANT_POTENTIAL_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_ENCHANT_POTENTIAL_REQ newInstance() {
		return new CS_ENCHANT_POTENTIAL_REQ();
	}

	private int _target_id;
	private boolean _is_event;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_ENCHANT_POTENTIAL_REQ() {
	}

	public int get_target_id() {
		return _target_id;
	}

	public void set_target_id(int val) {
		_bit |= 0x1;
		_target_id = val;
	}
	
	public boolean get_is_event(){
		return _is_event;
	}
	public void set_is_event(boolean val){
		_bit |= 0x2;
		_is_event = val;
	}
	public boolean has_is_event(){
		return (_bit & 0x2) == 0x2;
	}
	public boolean has_target_id() {
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
		if (has_target_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _target_id);
		}
		if (has_is_event()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_event);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_target_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_target_id()) {
			output.wirteInt32(1, _target_id);
		}
		if (has_is_event()){
			output.writeBool(2, _is_event);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_target_id(input.readInt32());
				break;
			}
			case 0x00000010:{
				set_is_event(input.readBool());
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
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			L1ItemInstance doll_item = pc.getInventory().getItem(this.get_target_id());
			L1MagicDoll magicDollItem = L1MagicDoll.get(doll_item.getItemId());
			if (magicDollItem != null) {
				int grade = magicDollItem.getInfo().getGrade() - 1; // -1처리
				int bonus_type = doll_item.get_Doll_Bonus_Level();
				int bonus_value = doll_item.get_Doll_Bonus_Value();
				int current_value = doll_item.get_Doll_Bonus_Value();
				
				int potion_count = Config.MagicDollInfo.POTION_COUNT;
				if (bonus_type == 1)
					potion_count = Config.MagicDollInfo.POTION_COUNT1;
				else if (bonus_type == 2)
					potion_count = Config.MagicDollInfo.POTION_COUNT2;
				else if (bonus_type == 3)
					potion_count = Config.MagicDollInfo.POTION_COUNT3;
				else if (bonus_type == 4)
					potion_count = Config.MagicDollInfo.POTION_COUNT4;

				int random_per = MJRnd.next(100);
				int random_up = MJRnd.next(100);
				boolean success = false;
				// TODO 단계 확률
				if (bonus_type < grade) {
					if (bonus_type == 0) {
						if (bonus_type == 0) { // 일반에서 고급으로 성공할 확률
							if (doll_item.getBless() == 0) {
								if (random_per < Config.MagicDollInfo.BONUS_TYPE_BLESS1) { // 1단계는 일반이다 2단계 즉 고급으로 바뀌는 확률
									if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX)
										bonus_type += 1;
									else if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN1 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX1) // 15%확률
										bonus_type += 2; // 확률이 좋다면 일반->2단계 위 희귀까지
									success = true;
								}
							} else {
								if (random_per < Config.MagicDollInfo.BONUS_TYPE1) { // 1단계는 일반이다 2단계 즉 고급으로 바뀌는 확률
									if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN2 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX2)
										bonus_type += 1;
									else if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN3 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX3)
										bonus_type += 2;
									success = true;
								}
							}
						}
					} else if (bonus_type == 1) { // 고급에서 희귀로 성공할 확률
						if (doll_item.getBless() == 0) {
							if (random_per < Config.MagicDollInfo.BONUS_TYPE_BLESS2) { // 2단계는 고급->희귀 확률
								if (grade > 2) {
									if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN4 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX4)
										bonus_type += 1;
									else if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN5 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX5)
										bonus_type += 2;
								} else
									bonus_type++;
								success = true;
							}
						} else {
							if (random_per < Config.MagicDollInfo.BONUS_TYPE2) { // 2단계는 고급->희귀 확률
								if (grade > 2) {
									if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN6 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX6)
										bonus_type += 1;
									else if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN7 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX7)
										bonus_type += 2;
								} else
									bonus_type++;
								success = true;
							}
						}
					} else if (bonus_type == 2) { // 희귀에서 영웅으로 성공할 확률
						if (doll_item.getBless() == 0) {
							if (random_per < Config.MagicDollInfo.BONUS_TYPE_BLESS3) { // 3단계 희귀->영웅 확률
								if (grade > 3) {
									if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN8 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX8)
										bonus_type += 1;
									else if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN9 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX9)
										bonus_type += 2;
								} else
									bonus_type++;
								success = true;
							}
						} else {
							if (random_per < Config.MagicDollInfo.BONUS_TYPE3) { // 3단계 희귀->영웅 확률
								if (grade > 3) {
									if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN10 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX10)
										bonus_type += 1;
									else if (random_up >= Config.MagicDollInfo.RANDOM_UP_MIN11 && random_up <= Config.MagicDollInfo.RANDOM_UP_MAX11) // 희귀->전설
										bonus_type += 2;
								} else
									bonus_type++;
								success = true;
							}
						}
					} else if (bonus_type == 3) { // 영웅에서 전설로 성공할 확률
						if (doll_item.getBless() == 0) {
							if (random_per < Config.MagicDollInfo.BONUS_TYPE_BLESS4) { // 4단계 영웅->전설 확률
								bonus_type++;
								success = true;
							}
						} else {
							if (random_per < Config.MagicDollInfo.BONUS_TYPE4) { // 4단계 영웅->전설 확률
								bonus_type++;
								success = true;
							}
						}
					}
				}
				if (pc.isGm()) {
// 打印出 (GM: 인형: 잠재력) : n단계 / 강화:성공 또는 실패 / 최종확률: p
					System.out.println(String.format("(GM:人形:潜在力) : %d阶段 / 强化:%s / 最终概率:%s)", bonus_type + 1, success == true ? "成功" : "失败", random_per));
				}

// 添加日志记录
				LoggerInstance.getInstance().addDollPotencial(String.format("[角色名]:%s [阶段]:%d [强化]:%s [最终概率]:%d", pc.getName(), bonus_type + 1, success == true ? "成功" : "失败", random_per));

				// TODO 옵션들 효과 개별 확률
				/*int bonus_random_per = MJRnd.next(100);
				if (bonus_type == 0) {
					if (bonus_random_per >= 6 && bonus_random_per <= 50)
						bonus_value = MJRnd.next(1, 10);
					else if (bonus_random_per >= 51 && bonus_random_per <= 100)
						bonus_value = MJRnd.next(1, 19);
					else if (bonus_random_per <= 5)// 100% 중에 2%라면
						bonus_type = 5;
				} else if (bonus_type == 1) {
					if (bonus_random_per >= 6 && bonus_random_per <= 50)
						bonus_value = MJRnd.next(20, 30);
					else if (bonus_random_per >= 51 && bonus_random_per <= 100)
						bonus_value = MJRnd.next(20, 52);
					else if (bonus_random_per <= 5)// 100% 중에 2%라면
						bonus_type = 5;
				} else if (bonus_type == 2) {
					if (bonus_random_per >= 6 && bonus_random_per <= 50)
						bonus_value = MJRnd.next(53, 70);
					else if (bonus_random_per >= 51 && bonus_random_per <= 100)
						bonus_value = MJRnd.next(53, 92);
					else if (bonus_random_per <= 5)// 100% 중에 2%라면
						bonus_type = 5;
				} else if (bonus_type == 3) {
					if (bonus_random_per >= 6 && bonus_random_per <= 50)
						bonus_value = MJRnd.next(93, 110);
					else if (bonus_random_per >= 51 && bonus_random_per <= 100)
						bonus_value = MJRnd.next(93, 130);
					else if (bonus_random_per <= 5)// 100% 중에 2%라면
						bonus_type = 5;
				} else if (bonus_type == 4) { // 단계 4
					if (bonus_value >= 131 && bonus_value <= 135)
						bonus_value = MJRnd.next(131, 135);
					if (bonus_value == 134 || bonus_value == 135)
						bonus_value = MJRnd.next(131, 133);
					if (bonus_random_per < 2)
						bonus_type = 5;
				}*/

				// TODO 단체 효과 확률
				int bonus_random_per = MJRnd.next(100);
				if (bonus_type == 0) {
					bonus_value = MJRnd.next(1, 19);
					if (bonus_random_per < Config.MagicDollInfo.BONUS_RANDOM_PER1) // 슬롯 확률
						bonus_type = 5;
				} else if (bonus_type == 1) {
					bonus_value = MJRnd.next(20, 52);
					if (bonus_random_per < Config.MagicDollInfo.BONUS_RANDOM_PER2) // 슬롯 확률
						bonus_type = 5;
				} else if (bonus_type == 2) {
					bonus_value = MJRnd.next(53, 93);
					if (bonus_random_per < Config.MagicDollInfo.BONUS_RANDOM_PER3) // 슬롯 확률
						bonus_type = 5;
				} else if (bonus_type == 3) {
/*					int rand = MJRnd.next(1, 45);
					if (rand <=38) {
						bonus_value = 93 + rand; //94 ~ 131
					} else {
						bonus_value = 109+ rand; // 148~ 154
					}
*/					int rand1 = MJRnd.next(1,2);
					if (rand1 == 1) {
						bonus_value = MJRnd.next(94, 131);	
					} else {
						bonus_value = MJRnd.next(148, 154);
					}
					if (bonus_random_per < Config.MagicDollInfo.BONUS_RANDOM_PER4) // 슬롯 확률
						bonus_type = 5;
				} else if (bonus_type == 4) {
					int ran2 = MJRnd.next(1,2);
					if (ran2 == 1) {
						bonus_value = MJRnd.next(132, 146);	
					} else {
						bonus_value = MJRnd.next(155, 163);
					}
/*					
					int rand = MJRnd.next(1, 28);
					if (rand >= 15) {
						bonus_value = 131 + rand; //132~146
					} else {
						bonus_value = 139 + rand; // 155~167
					}
*///					bonus_value = MJRnd.next(132, 143);
					if (bonus_random_per < Config.MagicDollInfo.BONUS_RANDOM_PER5) // 슬롯 확률
						bonus_type = 5;
				}
				
				if (bonus_value == 134)
					bonus_value--;
				else if (bonus_value == 135)
					bonus_value++;

				if (pc.getInventory().consumeItem(Config.MagicDollInfo.POTION_ITEMID, potion_count)) {
					// 發送潛在力強化結果通知
					SC_ENCHANT_POTENTIAL_RESULT_NOTI.potenccial_send(pc, this.get_target_id(), bonus_type + 1, bonus_type + 1, bonus_value, current_value == 0 ? 3435 : current_value + 32065);
				} else {
					// 打印錯誤信息到控制台
					System.out.println(String.format("魔法人形鍊金術：%s 非正常升級嘗試。 (懷疑無潛在力強化藥水進行升級)", pc.getName()));
					return this;
				}
			} else {
				// 打印錯誤信息到控制台
				System.out.println(String.format("魔法人形鍊金術：%s 非正常升級嘗試。 (懷疑無魔法人形進行升級)", pc.getName()));
				return this;
			}
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_ENCHANT_POTENTIAL_REQ();
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
