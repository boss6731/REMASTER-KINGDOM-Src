package l1j.server.server.server.command.executor;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.Account;
import l1j.server.server.model.L1World;
import l1j.server.server.server.datatables.IpTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class L1RangeKick implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1RangeKick.class.getName());

	private L1RangeKick() {}

	public static L1CommandExecutor getInstance() {
		return new L1RangeKick();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String				sname	= st.nextToken();
			if(sname == null || sname.equalsIgnoreCase(""))
				throw new Exception("");

			Integer reason = S_LoginResult.banServerCodes.get(Integer.parseInt(st.nextToken()));
			if(reason == null)
				throw new Exception("");

			L1PcInstance target = L1World.getInstance().getPlayer(sname);
//			if (target == null) {
//				target = CharacterTable.getInstance().restoreCharacter(arg);
//			}

			if (target != null) {
				IpTable ip = IpTable.getInstance();

				Account.ban(target.getAccountName(), reason); // 封禁帳號
				ip.rangeBanIp(target.getNetConnection().getHostname());  // 封禁IP範圍
				pc.sendPackets(String.valueOf(new S_SystemMessage(target.getName() + "[" + pc.getNetConnection() + "] 已被廣域驅逐")));
				L1World.getInstance().removeObject(target);
				l1j.server.server.command.executor.L1PowerKick.duplicateKick(target.getNetConnection().getIp(), target, reason);
			} else {
				final MJObjectWrapper<String> wrapper = new MJObjectWrapper<String>();
				wrapper.value = "";
				Selector.exec("select account_name from characters where char_name=?", new SelectorHandler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setString(1, sname);
					}

					@Override
					public void result(ResultSet rs) throws Exception {
						while(rs.next()){
							wrapper.value = rs.getString("account_name");
						}
					}

				});
				if(l1j.server.MJTemplate.MJString.isNullOrEmpty(wrapper.value)){
					pc.sendPackets(String.valueOf(new S_SystemMessage(String.format("%s 是不存在的角色名。", sname))));
				}else{
					IpTable ip = IpTable.getInstance();
					Account.ban(wrapper.value, reason);
					Integer[] octet = Account.loadAccountAddress(wrapper.value);
					StringBuilder sb = new StringBuilder(256);
					sb.append(sname).append(" 已被廣域驅逐。(").append(wrapper.value);
					if(octet != null){
						sb.append(", ").append(octet[0]).append(".").append(octet[1]).append(".").append(octet[2]).append(".").append("*");
						ip.rangeBanIp(octet);
					}
					sb.append(")");
					pc.sendPackets(sb.toString());
				}
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名] [停權原因編號]請輸入。")));
		}
	}
}
