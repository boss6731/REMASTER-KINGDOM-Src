package l1j.server.MJNetSafeSystem.Distribution;

import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.utils.DelayClose;

public abstract class Distributor {
	protected void toInvalidOp(GameClient clnt, int op, int length, String state, boolean isClose) throws Exception{
		if(Opcodes.C_ANSWER == op) {
			return;
		}
		if(isClose){
			System.out.println(String.format("無效操作碼(1). [%s] 操作碼 : %d, 長度 : %d -> 踢出.(%s)", clnt.getIp(), op, length, state));
			clnt.kick();
		}else {
			System.out.println(String.format("無效操作碼(2). [%s] 操作碼 : %d, 長度 : %d -> (%s)", clnt.getIp(), op, length, state));
		}
	}
}

	protected boolean isBanned(GameClient clnt) {
		String ip = clnt.getIp();
		if (IpTable.getInstance().isBannedIp(ip)) {
			System.out.println("\n┌──────────────────────────────────────┐");
			System.out.println(String.format("\t|  被封鎖的 IP 試圖連接！IP=%s", ip));
			System.out.println("└──────────────────────────────────────┘\n");
					clnt.sendPacket(new S_LoginResult(S_LoginResult.REASON_ACCOUNT_ALREADY_EXISTS));
			GeneralThreadPool.getInstance().schedule(new DelayClose(clnt), 5000);
			return true;
		}
		return false;
	}

	public abstract ClientBasePacket handle(GameClient clnt, byte[] data, int op) throws Exception;
}
