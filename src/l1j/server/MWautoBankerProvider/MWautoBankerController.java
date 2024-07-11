package l1j.server.MWautoBankerProvider;

import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.server.GeneralThreadPool;

public class MWautoBankerController implements Runnable {
	public void CheckStart(){
		load_config();
		GeneralThreadPool.getInstance().execute(this);
	}

	private static MWautoBankerController _instance;
	public static MWautoBankerController getInstance() {
		if(_instance == null)
			_instance = new MWautoBankerController();
		return _instance;
	}
	
	public MWautoInfo MWautoInfo; 
	public class MWautoInfo{
		public boolean NcoinService;
		public int EctItemService;
		public double ServicePer;
		public int ServiceItem1;
		public int ServiceItem2;
		public int ServiceItem3;
		public int ServiceItem1count;
		public int ServiceItem2count;
		public int ServiceItem3count;
		public MWautoInfo() {
			NcoinService = false;
			EctItemService = 0;
			ServicePer = 1;
			ServiceItem1 = 3721;
			ServiceItem2 = 5918;
			ServiceItem3 = 6759;
			ServiceItem1count = 10;
			ServiceItem2count = 10;
			ServiceItem3count = 10;
		}
	}
	
	public void load_config() {
		try {
			MWautoInfo = MJJsonUtil.fromFile("./config/MWautoInfo.json", MWautoInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("無法載入./config/MWautoInfo.json文件.");
		}
	}

	@Override
	public void run() {
		// TODO 자동 생성된 메소드 스텁
		try {
			MWautoBankerDataTable banker = new MWautoBankerDataTable();
			banker.LoadDepositorInfo();
			Thread.sleep(1000);
			if(banker.isDepositorinfo()) {
				for (MWautoBankerInfo bankerinfo : banker.get_members()) {
					if(bankerinfo.user !=null && bankerinfo.DepositStat && !bankerinfo.PaidNcoinStat) {
						switch(MWautoInfo.EctItemService){
						case 1:
							bankerinfo.user.getInventory().storeItem(MWautoInfo.ServiceItem1, bankerinfo.inmountcount >= 10000 ? (int)(MWautoInfo.ServiceItem1count * bankerinfo.inmountcount / 10000) : 1);
							break;
						case 2:
							bankerinfo.user.getInventory().storeItem(MWautoInfo.ServiceItem1, bankerinfo.inmountcount >= 10000 ? (int)(MWautoInfo.ServiceItem1count * bankerinfo.inmountcount / 10000) : 1);
							bankerinfo.user.getInventory().storeItem(MWautoInfo.ServiceItem2, bankerinfo.inmountcount >= 10000 ? (int)(MWautoInfo.ServiceItem1count * bankerinfo.inmountcount / 10000) : 1);
							break;
						case 3:
							bankerinfo.user.getInventory().storeItem(MWautoInfo.ServiceItem1, bankerinfo.inmountcount >= 10000 ? (int)(MWautoInfo.ServiceItem1count * bankerinfo.inmountcount / 10000) : 1);
							bankerinfo.user.getInventory().storeItem(MWautoInfo.ServiceItem2, bankerinfo.inmountcount >= 10000 ? (int)(MWautoInfo.ServiceItem1count * bankerinfo.inmountcount / 10000) : 1);
							bankerinfo.user.getInventory().storeItem(MWautoInfo.ServiceItem3, bankerinfo.inmountcount >= 10000 ? (int)(MWautoInfo.ServiceItem1count * bankerinfo.inmountcount / 10000) : 1);
							break;
						default:
							break;
						}
						bankerinfo.user.getNetConnection().getAccount().Ncoin_point += givecount(bankerinfo);
						bankerinfo.user.getNetConnection().getAccount().updateNcoin();
						banker.PaidDepositorNcoin(bankerinfo.user);
						bankerinfo.user.sendPackets("\f2充值已完成。");
						//banker.DepositorInfoLog(bankerinfo.user, bankerinfo.Depositor, bankerinfo.inmountcount, bankerinfo.Depositdate);
					}
				}
			}
			banker.Depositorinfoclear();
			banker = null;
		} catch (Exception e) {
		}
		GeneralThreadPool.getInstance().schedule(this, 60 * 1000);
	}
	
	private int givecount(MWautoBankerInfo bankerinfo) {
		return MWautoInfo.NcoinService ? bankerinfo.inmountcount + (int)(bankerinfo.inmountcount * (MWautoInfo.ServicePer / 100)): bankerinfo.inmountcount;
	}
}