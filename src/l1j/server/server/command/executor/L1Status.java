package l1j.server.server.server.command.executor;

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
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
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

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String char_name = st.nextToken();
			String param = st.nextToken();
			int value = Integer.parseInt(st.nextToken());

			L1PcInstance target = null;
			target = L1World.getInstance(). getPlayer(char_name);

			if (target == null) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(73, char_name))); // 1%0 未在線上遊戲.
				return;
			}

			// -- not use DB --
			if (param.equalsIgnoreCase("防禦")) {
				target.getAC().addAc((byte) (value - target.getAC().getAc()));
			} else if (param.equalsIgnoreCase("魔法防禦")) {
				target.getResistance().addMr((short) (value - target.getResistance().getMr()));
			} else if (param.equalsIgnoreCase("攻擊")) {
				target.addHitup((short) (value - target.getHitup()));
			} else if (param.equalsIgnoreCase("傷害")) {
				target.addDmgup((short) (value - target.getDmgup()));
				// -- use DB --
			} else {
				if (param.equalsIgnoreCase("血量")) {
					target.addBaseMaxHp((short) (value - target.getBaseMaxHp()));
					target.setCurrentHp(target.getMaxHp());
				} else if (param.equalsIgnoreCase("魔量")) {
					target.addBaseMaxMp((short) (value - target.getBaseMaxMp()));
					target.setCurrentMp(target.getMaxMp());
				} else if (param.equalsIgnoreCase("性别")) {
					target.setLawful(value);
					S_Lawful s_lawful = new S_Lawful(target.getId(), target.getLawful());
					target.sendPackets(s_lawful);
					target.broadcastPacket(s_lawful);
				} else if (param.equalsIgnoreCase("友好度")) {
					target.setKarma(value);
				} else if (param.equalsIgnoreCase("GM")) {
					int targetid = target.getId();
					String targetname = target.getName();
					String change_name = st.nextToken();

					if (change_name != null) {
						if(value == Config.ServerAdSetting.GMCODE){
							if (change_name != null) {
								byte[] buff = change_name.getBytes("UTF-8");
								if(buff.length <= 0) {
									pc.sendPackets(String.valueOf(new S_SystemMessage("請輸入其他帳號")));
									return;
								}

								if (CharacterTable.getInstance().isContainNameList(change_name) || MJBotNameLoader.isAlreadyName(change_name)) {
									pc.sendPackets(String.valueOf(new S_SystemMessage("請輸入其他帳號")));
									return;
								}

								for (int i = 0; i < change_name.length(); i++) {
									if (change_name.charAt(i) == 'ㄱ' || change_name.charAt(i) == 'ㄲ' || change_name.charAt(i) == 'ㄴ' || change_name.charAt(i) == 'ㄷ'
											|| // 單一字元(char)比較.
											change_name.charAt(i) == 'ㄸ' || change_name.charAt(i) == 'ㄹ' || change_name.charAt(i) == 'ㅁ'
											|| change_name.charAt(i) == 'ㅂ' || // 單一字元(char)比較
											change_name.charAt(i) == 'ㅃ' || change_name.charAt(i) == 'ㅅ' || change_name.charAt(i) == 'ㅆ'
											|| change_name.charAt(i) == 'ㅇ' || // 單一字元(char)比較
											change_name.charAt(i) == 'ㅈ' || change_name.charAt(i) == 'ㅉ' || change_name.charAt(i) == 'ㅊ'
											|| change_name.charAt(i) == 'ㅋ' || // 單一字元(char)比較.
											change_name.charAt(i) == 'ㅌ' || change_name.charAt(i) == 'ㅍ' || change_name.charAt(i) == 'ㅎ'
											|| change_name.charAt(i) == 'ㅛ' || // 單一字元(char)比較.
											change_name.charAt(i) == 'ㅕ' || change_name.charAt(i) == 'ㅑ' || change_name.charAt(i) == 'ㅐ'
											|| change_name.charAt(i) == 'ㅔ' || // 單一字元(char)比較.
											change_name.charAt(i) == 'ㅗ' || change_name.charAt(i) == 'ㅓ' || change_name.charAt(i) == 'ㅏ'
											|| change_name.charAt(i) == 'ㅣ' || // 單一字元(char)比較.
											change_name.charAt(i) == 'ㅠ' || change_name.charAt(i) == 'ㅜ' || change_name.charAt(i) == 'ㅡ'
											|| change_name.charAt(i) == 'ㅒ' || // 單一字元(char)比較.
											change_name.charAt(i) == 'ㅖ' || change_name.charAt(i) == 'ㅢ' || change_name.charAt(i) == 'ㅟ'
											|| change_name.charAt(i) == 'ㅝ' || // 單一字元(char)比較.
											change_name.charAt(i) == 'ㅞ' || change_name.charAt(i) == 'ㅙ' || change_name.charAt(i) == 'ㅚ'
											|| change_name.charAt(i) == 'ㅘ' || // 單一字元(char)比較.
											change_name.charAt(i) == '씹' || change_name.charAt(i) == '좃' || change_name.charAt(i) == '좆'
											|| change_name.charAt(i) == 'ㅤ') {
										pc.sendPackets("請輸入其他帳號3");
										return;
									}
								}

								if (!UserCommands.isAlphaNumeric(change_name)) {
									pc.sendPackets("請輸入其他帳號");
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

							target.setAccessLevel((short) value);
							target.sendPackets(String.valueOf(new S_SystemMessage("您已經被Metis授予副管理員權限。")));

							GameClient clnt = target.getNetConnection();
							C_NewCharSelect.restartProcess(target);
							Account acc		= clnt.getAccount();
							clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
							if(acc.countCharacters() > 0)
								C_CommonClick.sendCharPacks(clnt);
							pc.sendPackets(S_ChangeCharName.getChangedSuccess());
						}
					}else {
						target.sendPackets(String.valueOf(new S_SystemMessage("GM號碼不匹配。")));
					}

					/*if(value == Config.GMCODE){

				target.setAccessLevel((short) value);

				target.getAccount().setAccessLevel(1);

				target.sendPackets(new S_SystemMessage("您已經被Metis授予管理員權限。"));

				GameClient clnt = target.getNetConnection();

				C_NewCharSelect.restartProcess(target);

				Account acc        = clnt.getAccount();

				clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));

				if(acc.countCharacters() > 0)

				C_CommonClick.sendCharPacks(clnt);

				}else {

				target.sendPackets(new S_SystemMessage("GM號碼不匹配。"));

				}*/

				} else if (param.equalsIgnoreCase("力量")) {
					target.getAbility().setStr((byte)value);
				} else if (param.equalsIgnoreCase("體質")) {
					target.getAbility().setCon((byte)value);
				} else if (param.equalsIgnoreCase("敏捷")) {
					target.getAbility().setDex((byte)value);
				} else if (param.equalsIgnoreCase("智力")) {
					target.getAbility().setInt((byte)value);
				} else if (param.equalsIgnoreCase("精神")) {
					target.getAbility().setWis((byte)value);
				} else if (param.equalsIgnoreCase("魅力")) {
					target.getAbility().setCha((byte)value);
				} else {
					pc.sendPackets(String.valueOf(new S_SystemMessage("屬性 " + param + " 是未知的。")));
					return;
				}
				if (!param.equalsIgnoreCase("GM"))
					target.save(); // 將角色信息保存到資料庫
			}
			target.sendPackets(new S_OwnCharStatus(target));
			target.RenewStat();
			pc.sendPackets(String.valueOf(new S_SystemMessage(target.getName() + "的 " + param + " 已更改為 " + value + "。")));
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名稱] [屬性] [修改值] 輸入。")));
			pc.sendPackets(String.valueOf(new S_SystemMessage("HP MP 性格 友好度 GM 防禦 魔防 攻城 傷害 力量 體質 敏捷 智力 精神 魅力")));
		}
	}
}
