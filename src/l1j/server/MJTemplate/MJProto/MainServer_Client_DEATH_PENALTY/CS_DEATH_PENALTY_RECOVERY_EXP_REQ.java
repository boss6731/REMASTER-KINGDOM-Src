package l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY;

import java.text.DecimalFormat;

import l1j.server.MJDeathPenalty.MJDeathPenaltyProvider;
import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyExpModel;
import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyexpDatabaseLoader;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.RenewalExpTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.L1ItemId;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_DEATH_PENALTY_RECOVERY_EXP_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_DEATH_PENALTY_RECOVERY_EXP_REQ newInstance() {
		return new CS_DEATH_PENALTY_RECOVERY_EXP_REQ();
	}

	private int _index;
	private boolean _use_recovery_item;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_DEATH_PENALTY_RECOVERY_EXP_REQ() {
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

	public boolean get_use_recovery_item() {
		return _use_recovery_item;
	}

	public void set_use_recovery_item(boolean val) {
		_bit |= 0x2;
		_use_recovery_item = val;
	}

	public boolean has_use_recovery_item() {
		return (_bit & 0x2) == 0x2;
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
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _index);
		}
		if (has_use_recovery_item()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _use_recovery_item);
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
		if (!has_use_recovery_item()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_index()) {
			output.writeUInt32(1, _index);
		}
		if (has_use_recovery_item()) {
			output.writeBool(2, _use_recovery_item);
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
					set_index(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_use_recovery_item(input.readBool());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.
			MJDeathPenaltyExpModel model = pc.get_deathpenalty_exp().get(get_index());
			if (model == null) {
				return this;
			}
			if (get_use_recovery_item()) {
				if (!(pc.getInventory().checkItem(3000049, 1) || pc.getInventory().checkItem(4100694, 1))) {
					pc.sendPackets("沒有救濟證書。.");
					return this;
				} else {
					if (pc.getInventory().checkItem(4100694, 1)) {
						pc.getInventory().consumeItem(4100694, 1);
					} else if (pc.getInventory().checkItem(3000049, 1)) {
						pc.getInventory().consumeItem(3000049, 1);
					}
				}
			} else {
				DecimalFormat formatter = new DecimalFormat("###,###");
				int cost = model.getRecovery_cost();
				int needcost = cost - pc.getInventory().checkItemCount(L1ItemId.ADENA);
				if (!pc.getInventory().checkItem(L1ItemId.ADENA, cost)) {
					pc.sendPackets("恢復經驗值所需的 金幣 " + formatter.format(needcost) + "恢復經驗值所需的 金幣 不足。"); // 每輸入 3 位數字
					return this;
				} else {
					pc.getInventory().consumeItem(L1ItemId.ADENA, cost);
				}
			}

			double ration = model.getExp_ratio();
			if (model.getDeathLevel() == pc.getLevel()) {

			}
			long exp = 0;
			double needExp = ExpTable.getNeedExpNextLevel(model.getDeathLevel())
					/ ExpTable.getPenaltyRate(model.getDeathLevel());
			double needExp2 = ExpTable.getNeedExpNextLevel(pc.getLevel()) / ExpTable.getPenaltyRate(pc.getLevel());
			double needExp3 = ExpTable.getNeedExpNextLevel(pc.getLevel());
			// exp = (long) (needExp * ration);
			double levelDif = 1;
			int recoverRation = 70;
			if (pc.getLevel() > 50) {
				recoverRation += (pc.getLevel() - 50) / 2;
			}
			if (recoverRation >= 90) {
				recoverRation = 90;
			}
			ration *= (double) recoverRation / 100;
			if (model.getDeathLevel() < pc.getLevel()) {
				levelDif = needExp / needExp2;
				ration *= levelDif;
			}
			double penality = RenewalExpTable.getPenaltyRate(pc.getLevel());
			exp = (long) (needExp3 * ration / 10000 / 100) * (long) penality;

			// System.out.println(model.getExp_ratio()+"+"+recoverRation/100+"+"+ration/10000);

			pc.delete_deathpenalty_exp(model.getId());
			pc.add_exp(exp);
			// pc.resExpToTemple();
			MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(pc);

			// MJDeathPenaltyProvider.provider().recovery_exp(pc, this);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_DEATH_PENALTY_RECOVERY_EXP_REQ();
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
