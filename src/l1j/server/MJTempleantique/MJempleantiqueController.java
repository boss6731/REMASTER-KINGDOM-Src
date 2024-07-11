package l1j.server.MJTempleantique;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;

public class MJempleantiqueController implements Runnable {
	private static MJempleantiqueController _instance;

	public static MJempleantiqueController getInstance() {
		if (_instance == null) {
			_instance = new MJempleantiqueController();
		}
		return _instance;
	}
	
	public static TempleAntique templeantique; 
	public static class TempleAntique{
		public boolean use;
		public boolean clanmode;
		public int gametime;
		public TempleAntique() {
			use = false;
			clanmode = true;
			gametime = 3600;
		}
	}
	
	public static void load_config() {
		try {
			templeantique = MJJsonUtil.fromFile("./config/TempleAntique.json", TempleAntique.class);
		}catch(Exception e) {
			e.printStackTrace();
			throw new Error("無法載入 ./config/TempleAntique.json 檔案。");
		}
	}
	
	private int gametimenoti = 0;
	private long starttime = 0;
	public boolean isopen = false;

	public MJempleantiqueController() {
	}
	
	public void Start(long Starttime){
		starttime = Starttime;
		isopen = true;
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run() {
		try {
			while(isopen) { 
				checkTime();
				Thread.sleep(1000);
			}
			checkTime();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void checkTime() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(1209).values()) {
			if (ob instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) ob;
				gametimenoti = (int)(((starttime + MJempleantiqueController.templeantique.gametime * 1000) - System.currentTimeMillis()) / 1000);
				if(gametimenoti >= 0) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.MAP_TIMER, gametimenoti));
				} else {
					isopen = false;
					ClanTable.getInstance().TempleantiqueclanId = 0;
					pc.start_teleport(33436 + MJRnd.next(12), 32795 + MJRnd.next(12), 4, 5, 169, true);
				}
			}
		}
	}
}