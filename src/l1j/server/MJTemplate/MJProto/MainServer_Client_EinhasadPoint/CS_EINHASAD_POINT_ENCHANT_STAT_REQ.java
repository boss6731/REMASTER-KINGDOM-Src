package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.Config;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.monitor.LoggerInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_EINHASAD_POINT_ENCHANT_STAT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_EINHASAD_POINT_ENCHANT_STAT_REQ newInstance() {
		return new CS_EINHASAD_POINT_ENCHANT_STAT_REQ();
	}

	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_EINHASAD_POINT_ENCHANT_STAT_REQ() {
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
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			if (pc.getAccount().getBlessOfAinBonusPoint() < 5000) {
				System.out.println(String.format("艾哈薩德屬性錯誤：懷疑使用中繼器 角色名(%s)", pc.getName()));
				return this;
			}

			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
			if (Info != null) {
				int percent = MJRnd
						.next((Info.get_cur_enchant_level() + 1) * Config.ServerRates.AinBonusPoint_Kard_Probability);
				boolean success = MJRnd.isWinning(10000, percent);
				int level = (Info.get_total_stat() / 10) + 1;
				int ainpoint = 5000 * level;
				int point = 0;
				int bonus = 0;
				// success = true;//무조건 성공할때
				if (pc.isGm()) {
					success = true;
				} else {
					if (Info.get_cur_enchant_level() > 8) {
						success = true;
					}
				}

				if (success) {
					if (!pc.isGm()) {
						if (MJRnd.isWinning(100, Config.ServerRates.AinBonusPoint_Kard_Lucky)) {// 그랑,아인 확률
							point += MJRnd.next(2) + 1;
							bonus += 1;
						}
					}
					if (pc.isGm()) {
						point += 150;
					} else {
						point += 1;
					}

					Info.add_total_stat(point);
					Info.set_cur_enchant_level(0);
				} else {
					Info.add_cur_enchant_level(1);
				}

				String log = "[角色名稱]：" + pc.getName() + " [強化等級]：" + Info.get_cur_enchant_level() + " [獎勵點數]：" + point
						+ " [總點數]:" + Info.get_total_stat() + " [結果]:" + (success ? "成功" : "失敗");
				LoggerInstance.getInstance().addBlessOfAinPointCard(log);
				pc.getAccount().minusBlessOfAinBonusPoint(ainpoint);
				SC_EINHASAD_POINT_ENCHANT_STAT_ACK.send_point_enchant_stat(pc, point, bonus,
						Info.get_cur_enchant_level(), Info.get_total_stat());
				SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, pc.getAccount().getBlessOfAinBonusPoint());
				AinhasadSpecialStatLoader.getInstance().updateSpecialStat(pc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_EINHASAD_POINT_ENCHANT_STAT_REQ();
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
