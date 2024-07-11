package l1j.server.MJKDASystem;

/**********************************
 * 
 * MJ Kill Death Assist LoadManager.
 * made by mjsoft, 2017.
 *  
 **********************************/
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayDeque;
import java.util.Properties;

import Chart.MJKDAChartScheduler;
import l1j.server.server.model.Instance.L1PcInstance;
import model.L1World;
import serverpackets.S_SystemMessage;
import utils.MJCommons;
import utils.SQLUtil;

public class MJKDALoadManager {
	private static MJKDALoadManager _instance;

	public static MJKDALoadManager getInstance() {
		if (_instance == null)
			_instance = new MJKDALoadManager();
		return _instance;
	}

	public static <L1PcInstance> void commands(L1PcInstance gm, String param) {
		try {
			ArrayDeque<Integer> argsQ = MJCommons.parseToIntQ(param);
			if (argsQ == null || argsQ.isEmpty())
				throw new Exception("");

			switch (argsQ.poll()) {
				case 1:
					chartCommands(gm, argsQ);
					break;
				case 2:
					_instance.reload();
					gm.sendPackets(new S_SystemMessage("[MJKDA 系統配置重新加載完成。]"));
					break;
				case 3:
					String[] arrs = param.split(" ");
					initCommands(gm, arrs[arrs.length - 1]);
					break;
				case 4:
					MJKDALoader.getInstance().store();
					gm.sendPackets(new S_SystemMessage("[MJKDA 系統儲存完成。 ]"));
					break;
				default:
					throw new Exception("");
			}

		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage("[1. 開啟/關閉擊殺排行榜][2. 重新加載][3. 用戶重置][4. 強制保存]"));
		}
	}

	private static void initCommands(L1PcInstance gm, String s) {
		try {
			if (s == null || s.equalsIgnoreCase("")) {
				throw new Exception("");
			}

			L1PcInstance pc = L1World.getInstance().findpc(s);
			if (pc == null) {
				gm.sendPackets(new S_SystemMessage(String.format("找不到 %s。", s)));
			} else {
				pc.getKDA().onInit(pc);
				gm.sendPackets(new S_SystemMessage(String.format("已重置 %s 的擊殺死亡數據。", s)));
			}
		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage("使用方法 .擊殺死亡 3 [角色名稱]"));
		}
	}

	private static void chartCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ) {
		try {
			if (argsQ.isEmpty()) {
				throw new Exception("");
			}

			switch (argsQ.poll()) {
				case 0:
					KDA_CHART_RUN = false;
					MJKDAChartScheduler.release();
					gm.sendPackets(new S_SystemMessage("擊殺死亡排行榜系統已關閉。"));
					break;
				case 1:
					if (MJKDAChartScheduler.isLoaded()) {
						gm.sendPackets(new S_SystemMessage("系統已經在運行中。"));
					} else {
						KDA_CHART_RUN = true;
						MJKDAChartScheduler.getInstance().run();
						gm.sendPackets(new S_SystemMessage("擊殺死亡排行榜系統已啟動。"));
					}
					break;
				default:
					throw new Exception("");
			}

		} catch (Exception e) {
			gm.sendPackets(new S_SystemMessage("使用方法 .擊殺死亡 1 0(關閉) 或 1(開啟)"));
		}
	}

	public void reload() {
		loadConfig();
	}

	public void release() {
		MJKDALoader.release();
		MJKDAChartScheduler.release();
	}

	private MJKDALoadManager() {
	}

	public void load() {
		loadConfig();
		loadTotalPvp();
		MJKDALoader.getInstance();
		if (KDA_CHART_RUN)
			MJKDAChartScheduler.getInstance().run();
	}

	public static int KDA_TOTAL_PVP;
	public static int KDA_CHART_DELAY_SEC;
	public static boolean KDA_CHART_RUN;
	public static long KDA_DEATH_DELAY_MS;
	public static long KDA_KILL_DUPL_DELAY_MS;

	private void loadTotalPvp() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT sum(killing) as totalpvp from tb_kda");
			rs = pstm.executeQuery();
			if (rs.next())
				KDA_TOTAL_PVP = rs.getInt("totalpvp");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void loadConfig() {
		String column = null;
		try {
			Properties settings = new Properties();
			InputStream is = new FileInputStream(new File("./config/mjkda.properties"));
			settings.load(is);
			is.close();

			column = "ChartUpdateDelaySecond";
			KDA_CHART_DELAY_SEC = Integer.parseInt(settings.getProperty(column, "60"));

			column = "IsChartUpdate";
			KDA_CHART_RUN = Boolean.parseBoolean(settings.getProperty(column, "false"));

			column = "DeathDelaySecond";
			KDA_DEATH_DELAY_MS = Integer.parseInt(settings.getProperty(column, "60")) * 1000;

			column = "KillDuplicationDelaySecond";
			KDA_KILL_DUPL_DELAY_MS = Integer.parseInt(settings.getProperty(column, "90")) * 1000;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
