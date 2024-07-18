package l1j.server.server.serverpackets;

import java.sql.PreparedStatement;

import MJShiftObject.MJEShiftObjectType;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.MJKDASystem.MJKDALoader;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.ObServer.MJCopyMapObservable;
import l1j.server.server.BadNamesList;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.UserCommands;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ChangeCharName extends ServerBasePacket {
	public static S_ChangeCharName getChangedSuccess() {
		return getChangedResult(true);
	}

	public static S_ChangeCharName getChangedFailure() {
		return getChangedResult(false);
	}

	public static S_ChangeCharName getChangedResult(boolean isSuccess) {
		S_ChangeCharName s = new S_ChangeCharName();
		s.writeC(Opcodes.S_VOICE_CHAT);
		s.writeC(0x1e);
		s.writeH(isSuccess ? 0x02 : 0x06);
		s.writeH(0x00);
		return s;
	}

	public static S_ChangeCharName getChangedStart() {
		S_ChangeCharName s = new S_ChangeCharName(); // 創建新的 S_ChangeCharName 對象
		s.writeC(Opcodes.S_VOICE_CHAT); // 寫入操作碼，表示語音聊天
		s.writeC(0x1D); // 寫入數值 0x1D
		// s.writeC(0x1F); // 血盟名稱變更（註釋掉的代碼）
		// s.writeC(0x03); // 世界逃脫（註釋掉的代碼）
		s.writeD(0x00); // 寫入整數 0x00
		return s; // 返回配置好的 S_ChangeCharName 對象
	}

	public static ServerBasePacket doChangeCharName(GameClient client, String sourceName, String destinationName, boolean world_in) {
		try {
			L1PcInstance pc = client.getActiveChar();

			if (pc == null) {
				pc = L1PcInstance.load(sourceName);
			}

			byte[] buff = destinationName.getBytes("UTF-8");
			if (world_in) {
				if (buff.length <= 0)
					return getChangedFailure();
				if (CharacterTable.getInstance().isContainNameList(destinationName) || MJBotNameLoader.isAlreadyName(destinationName))
					return getChangedFailure();

				if (BadNamesList.getInstance().isBadName(destinationName))
					return getChangedFailure();

				for (int i = 0; i < destinationName.length(); i++) {
					if (destinationName.charAt(i) == 'ㅗ' || destinationName.charAt(i) == 'ㅓ' || destinationName.charAt(i) == 'ㅏ' || destinationName.charAt(i) == 'ㅣ' || // 逐字符比較
							destinationName.charAt(i) == 'ㅠ' || destinationName.charAt(i) == 'ㅜ' || destinationName.charAt(i) == 'ㅡ' || destinationName.charAt(i) == 'ㅒ' || // 逐字符比較
							destinationName.charAt(i) == 'ㅖ' || destinationName.charAt(i) == 'ㅢ' || destinationName.charAt(i) == 'ㅟ' || destinationName.charAt(i) == 'ㅝ' || // 逐字符比較
							destinationName.charAt(i) == 'ㅞ' || destinationName.charAt(i) == 'ㅙ' || destinationName.charAt(i) == 'ㅚ' || destinationName.charAt(i) == 'ㅘ' || // 逐字符比較
							destinationName.charAt(i) == '씹' || destinationName.charAt(i) == '좃' || destinationName.charAt(i) == '좆' || destinationName.charAt(i) == 'ㅤ') { // 逐字符比較
						return getChangedFailure(); // 如果匹配到這些字符，則返回失敗
					}
				}

				for (int i = 0; i < destinationName.length(); i++) {
					if (!Character.isLetterOrDigit(destinationName.charAt(i)))
						return getChangedFailure();
				}

				if (!UserCommands.isAlphaNumeric(destinationName))
					return getChangedFailure();

//				if (!pc.is_shift_transfer()) {
//					if (!pc.getInventory().checkItem(408991, 1)) {
//						pc.sendPackets(new S_Disconnect());
//						pc.getNetConnection().close();
//						return null;
//					}
//				}
			}

			Updator.exec("UPDATE characters SET char_name=? WHERE char_name=?", new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, destinationName);
					pstm.setString(2, sourceName);
				}
			});

			try {
				// 更新數據庫中的角色名稱
				Updator.exec("UPDATE tb_kda SET name=? WHERE name=?", new Handler() {
					@override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setString(1, destinationName);
						pstm.setString(2, sourceName);
					}
				});

				// 更改角色名稱
				MJKDALoader.getInstance().getChangeName(pc.getId(), destinationName);

				// 保存角色數據
				pc.save(); // 저장

				// 打印角色名稱變更信息
				System.out.println(String.format("[%s][%s] 將角色名稱從 %s 更改為 %s.\r\n", MJNSHandler.getLocalTime(), client.getIp(), sourceName, destinationName));

				// 判斷是否轉移
				if (pc.is_shift_transfer()) {
					pc.set_shift_type(MJEShiftObjectType.NONE); // 設置轉移類型為 NONE
				} else {
					pc.getInventory().consumeItem(408991, 1); // 消耗一個 ID 為 408991 的物品
				}

				// 刪除好友
				UserCommands.buddys(pc);
				// 刪除信件
				UserCommands.편지삭제(pc);

				// 重置位置
				MJCopyMapObservable.getInstance().resetPosition(pc);

				// 返回角色
				MJRaidSpace.getInstance().getBackPc(pc);

				// 重啟角色選擇過程
				C_NewCharSelect.restartProcess(pc);

				return getChangedSuccess(); // 返回變更成功

			} catch (Exception e) {
				e.printStackTrace(); // 捕捉並打印異常
			}


			return getChangedFailure(); // 返回變更失敗
		}
	public S_ChangeCharName() {
	}

	@Override
	public byte[] getContent() {
		return getBytes();

	}


}
