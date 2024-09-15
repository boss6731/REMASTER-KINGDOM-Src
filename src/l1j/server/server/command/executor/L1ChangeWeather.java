package l1j.server.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1ChangeWeather implements l1j.server.server.command.executor.L1CommandExecutor {

	private L1ChangeWeather() {	}

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1ChangeWeather();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			
			int weather = Integer.parseInt(tok.nextToken());
			L1World.getInstance(). setWeather(weather);
			L1World.getInstance(). broadcastPacketToAll(String.valueOf(new S_Weather(weather)));
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入 0~3（雪）, 16~19（雨）。")));
		}
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {

	}

}
