package l1j.server.server.command.executor;

import java.sql.PreparedStatement;
import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.UserCommands;
import l1j.server.server.clientpackets.C_CommonClick;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;


public class L1Status implements L1CommandExecutor {

	private L1Status() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Status();
	}

    @override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            // 使用 StringTokenizer 分割參數
            StringTokenizer st = new StringTokenizer(arg);

            // 解析角色名稱
            String char_name = st.nextToken();
            // 解析參數名稱
            String param = st.nextToken();
            // 解析參數值
            int value = Integer.parseInt(st.nextToken());

            // 獲取目標玩家對象
            L1PcInstance target = null;
            target = L1World.getInstance().getPlayer(char_name);

            // 如果目標玩家未在線，發送消息並返回
            if (target == null) {
                pc.sendPackets(new S_ServerMessage(73, char_name)); // 1%0은 게임을 하고 있지 않습니다。
                return;
            }

            // 根據指令調整玩家屬性，不涉及數據庫操作
            if (param.equalsIgnoreCase("防禦")) { // 防禦"방어"
                target.getAC().addAc((byte) (value - target.getAC().getAc()));
            } else if (param.equalsIgnoreCase("魔法防禦")) { // 魔法防禦"마방"
                target.getResistance().addMr((short) (value - target.getResistance().getMr()));
            } else if (param.equalsIgnoreCase("命中率")) { // 命中率"공성"
                target.addHitup((short) (value - target.getHitup()));
            } else if (param.equalsIgnoreCase("傷害")) { // 傷害"대미지"
                target.addDmgup((short) (value - target.getDmgup()));
            }

            // 可以在這裡添加其他屬性調整邏輯

        } catch (Exception e) {
            // 捕獲所有異常，發送錯誤消息
            pc.sendPackets(new S_SystemMessage(cmdName + " 命令執行失敗，參數格式錯誤或其他內部錯誤。"));
        }
    }
				// -- use DB --

}  else {

	if (param.equalsIgnoreCase("血量")) { // 血量"피"
		target.addBaseMaxHp((short) (value - target.getBaseMaxHp()));
		target.setCurrentHp(target.getMaxHp());

	} else if (param.equalsIgnoreCase("魔力")) { // 魔力"엠피"
		target.addBaseMaxMp((short) (value - target.getBaseMaxMp()));
		target.setCurrentMp(target.getMaxMp());

	} else if (param.equalsIgnoreCase("性向")) { // 性向"성향"
		target.setLawful(value);
		S_Lawful s_lawful = new S_Lawful(target.getId(), target.getLawful());

		target.sendPackets(s_lawful);
		target.broadcastPacket(s_lawful);

	} else if (param.equalsIgnoreCase("友好度")) { // 友好度"우호도"
		target.setKarma(value);

	} else if (param.equalsIgnoreCase("GM 設定")) { // GM 設定"지엠"
		int targetid = target.getId();
		String targetname = target.getName();
		String change_name = st.nextToken();

		if (change_name != null) {
			if (value == Config.ServerAdSetting.GMCODE) {
				byte[] buff = change_name.getBytes("MS949");
				if (buff.length <= 0) {
					pc.sendPackets("請輸入其他 ID");
					return;
				}

				if (CharacterTable.getInstance().isContainNameList(change_name) || MJBotNameLoader.isAlreadyName(change_name)) {
					// 如果角色表或 MJ機器人名稱加載器中已包含該名稱，發送消息並返回
		pc.sendPackets("請輸入其他 ID");
		return;
				}

				for (int i = 0; i < change_name.length(); i++) {
					if (change_name.charAt(i) == 'ㄱ' || change_name.charAt(i) == 'ㄲ' || change_name.charAt(i) == 'ㄴ' || change_name.charAt(i) == 'ㄷ'
		|| // 逐字符比較
		change_name.charAt(i) == 'ㄸ' || change_name.charAt(i) == 'ㄹ' || change_name.charAt(i) == 'ㅁ'
		|| change_name.charAt(i) == 'ㅂ' || // 逐字符比較
		change_name.charAt(i) == 'ㅃ' || change_name.charAt(i) == 'ㅅ' || change_name.charAt(i) == 'ㅆ'
		|| change_name.charAt(i) == 'ㅇ' || // 逐字符比較
		change_name.charAt(i) == 'ㅈ' || change_name.charAt(i) == 'ㅉ' || change_name.charAt(i) == 'ㅊ'
		|| change_name.charAt(i) == 'ㅋ' || // 逐字符比較
		change_name.charAt(i) == 'ㅌ' || change_name.charAt(i) == 'ㅍ' || change_name.charAt(i) == 'ㅎ'
		|| change_name.charAt(i) == 'ㅛ' || // 逐字符比較
		change_name.charAt(i) == 'ㅕ' || change_name.charAt(i) == 'ㅑ' || change_name.charAt(i) == 'ㅐ'
		|| change_name.charAt(i) == 'ㅔ') { // 逐字符比較
						pc.sendPackets("請輸入其他 ID3");
						return;
					}
				}

				if (!UserCommands.isAlphaNumeric(change_name)) {
					pc.sendPackets("請輸入其他 ID");
					return;
				}
								
								Updator.exec("UPDATE characters SET char_name=? WHERE char_name=?", new Handler(){
									@Override
									public void handle(PreparedStatement pstm) throws Exception {
										pstm.setString(1, change_name);
										pstm.setString(2, targetname);
									}
								});
								
								Updator.exec("UPDATE tb_kda SET name=? WHERE objid=?", new Handler(){
									@Override
									public void handle(PreparedStatement pstm) throws Exception {
										pstm.setString(1, change_name);
										pstm.setInt(2, targetid);
									}
								});
							}

							// 設置目標玩家的訪問級別
							target.setAccessLevel((short) value);
					// 發送系統消息通知玩家他們已被賦予副管理員權限
							target.sendPackets(new S_SystemMessage("您已被 Metis 賦予副管理員權限。"));

					// 獲取目標玩家的連接對象
							GameClient clnt = target.getNetConnection();
					// 重啟角色選擇過程
							C_NewCharSelect.restartProcess(target);
					// 獲取目標玩家的賬戶
							Account acc = clnt.getAccount();
					// 發送角色數量信息包
							clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
					// 如果賬戶中有角色，發送角色數據包
							if (acc.countCharacters() > 0) {
							C_CommonClick.sendCharPacks(clnt);
							}
					// 發送角色名稱更改成功的消息
							pc.sendPackets(S_ChangeCharName.getChangedSuccess());
							} else {
					// 發送系統消息通知玩家 GM 編號不匹配
							target.sendPackets(new S_SystemMessage("GM 編號不匹配。"));
							}
					
					/*if(value == Config.GMCODE){
						target.setAccessLevel((short) value);
						target.getAccount().setAccessLevel(1);
						target.sendPackets(new S_SystemMessage("당신은 메티스께서 운영자권한을 부여 하였습니다."));
						GameClient clnt = target.getNetConnection();
						C_NewCharSelect.restartProcess(target);
						Account acc		= clnt.getAccount();
						clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
						if(acc.countCharacters() > 0)
							C_CommonClick.sendCharPacks(clnt);
					}else {
						target.sendPackets(new S_SystemMessage("GM번호가 일치하지 않습니다."));
					}*/
			} else if (param.equalsIgnoreCase("力量")) {
				target.getAbility().setStr((byte)value); // 設置力量
			} else if (param.equalsIgnoreCase("體質")) {
				target.getAbility().setCon((byte)value); // 設置體質
			} else if (param.equalsIgnoreCase("敏捷")) {
				target.getAbility().setDex((byte)value); // 設置敏捷
			} else if (param.equalsIgnoreCase("智力")) {
				target.getAbility().setInt((byte)value); // 設置智力
			} else if (param.equalsIgnoreCase("精神")) {
				target.getAbility().setWis((byte)value); // 設置精神
			} else if (param.equalsIgnoreCase("魅力")) {
				target.getAbility().setCha((byte)value); // 設置魅力
			} else {
				pc.sendPackets(new S_SystemMessage("狀態 " + param + " (是)未知的。"));
				return;
			}
			try {
				if (!param.equalsIgnoreCase("GM（遊戲管理員）"))
					target.save(); // 將角色資訊寫入數據庫
			}
			target.sendPackets(new S_OwnCharStatus(target)); // 發送角色狀態信息包
				target.RenewStat(); // 更新角色屬性
				pc.sendPackets(new S_SystemMessage(target.getName() + "的 " + param + " 已更改為 " + value + "。"));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " [角色名稱] [屬性] [更改值] 輸入。"));
			pc.sendPackets(new S_SystemMessage("血量 魔力 性向 友好度 GM 防禦 魔法防禦 命中率 傷害 力量 體質 敏捷 智力 精神 魅力"));
	}
}
