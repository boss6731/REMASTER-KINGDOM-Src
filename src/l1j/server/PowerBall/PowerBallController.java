package l1j.server.PowerBall;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.templates.WareHouseLeaveType;

public class PowerBallController implements Runnable {
	private static PowerBallController _instance;

	public static final int EXECUTE_STATUS_NONE 	= 0;
	public static final int EXECUTE_STATUS_PREPARE 	= 1;
	public static final int EXECUTE_STATUS_READY 	= 2;
	public static final int EXECUTE_STATUS_FINALIZE = 3;
	public static final int EXECUTE_STATUS_RELOAD 	= 4;
	public static final int EXECUTE_STATUS_STOP 	= 5;

	public int _executeStatus = EXECUTE_STATUS_NONE;

	private static final AtomicInteger powerBallId = new AtomicInteger(0);

	public int _num = 0;
	public int _next = 0;
	public boolean Start = false;
	public int _State = 2;
	public int _RemainTime;
	public int _MentTime;
	public int _MentNum;

	L1NpcInstance _npc;

	public int[] _ticketCount = new int[8];
	public int[] _ticketId = new int[8];

	List<L1ShopItem> _purchasingList = new ArrayList<L1ShopItem>();

	private final HashMap<Integer, PowerBallTicket> _power = new HashMap<Integer, PowerBallTicket>(32);

	public HashMap<Integer, PowerBallTicket> getAllTemplates() {
		return _power;
	}

	private L1Shop _shop;
	public L1Shop get_shop() {
		return _shop;
	}

	public int[] ticket = { 0, 0, 0, 0, 0, 0, 0, 0 };
	// 勝率初始化
	public double[] _winRate = { 0, 0, 0, 0, 0, 0, 0, 0 };
	// 倍率初始化
	public double _ration[] = { 0, 0, 0, 0, 0, 0, 0, 0 };

	/*public static PowerBallController getInstance() {
		if (_instance == null) {
			_instance = new PowerBallController();
			PowerBallInfoParse.getInstance().PowerBallInfo();
		}
		return _instance;
	}*/

	public void run() {
		try {
			if (Config.Web.powerBall) {
				if (!isParseTime()) {
					PowerBallInfo info = getinfo();
					switch (_executeStatus) {
						case EXECUTE_STATUS_NONE: {
							initGame();
							_executeStatus = EXECUTE_STATUS_PREPARE;
							GeneralThreadPool.getInstance().schedule(this, 1000L);
						}
						break;
						case EXECUTE_STATUS_PREPARE: {
							startSellTicket();
							_MentTime = 10;
							_executeStatus = EXECUTE_STATUS_READY;
							GeneralThreadPool.getInstance().schedule(this, 1000L);
						}
						break;
						case EXECUTE_STATUS_READY: {
							if (_MentTime < 0) {
								_MentTime = 30;
								if (_MentNum == 0)
									broadcastNpc("應用中心 [CTRL + Z] 通過 '普通球' 菜單可以查看結果和實時 '視圖'。");
								else if (_MentNum == 1)
									broadcastNpc("說明(1) 如果5個普通球的總數是單數，則為單！如果是雙數，則為雙！");
								else if (_MentNum == 2)
									broadcastNpc("說明(2) 如果結果總數大於等於 74 則為大！小於等於 73 則為小！");
								else if (_MentNum == 3)
									broadcastNpc("說明(3) 如果結果值是 67，則為單(小)，如果是 74，則為雙(大)！");
								else if (_MentNum == 4)
									broadcastNpc("只要不貪心，任何人都可以輕易賺錢。");
								else if (_MentNum == 5)
									broadcastNpc("為了防止大量購買，每次最多只能購買 '99' 個。");
								else if (_MentNum == 6)
									broadcastNpc("每場遊戲沒有最大限制。請參考。");
								else if (_MentNum == 7)
									broadcastNpc("應用中心 [CTRL + Z] 通過 '普通球' 菜單可以查看結果和實時 '視圖'。");
								else if (_MentNum == 8)
									broadcastNpc("應用中心 [CTRL + Z] 通過 '普通球' 菜單可以查看結果和實時 '視圖'。");
								_MentNum++;
							}

							if (_MentNum >= 8)
								_MentNum = 0;

							_MentTime--;
							GeneralThreadPool.getInstance().schedule(this, 1000L);
						}
						break;
						case EXECUTE_STATUS_FINALIZE: {
							long remainTime = checkTicketSellTime(1);
							if (remainTime > 0) {
								GeneralThreadPool.getInstance().schedule(this, 1000L);
							} else {
								if (info.getoddEven().equalsIgnoreCase("單")) {
									EndGame(0);
								} else if (info.getoddEven().equalsIgnoreCase("雙")) {
									EndGame(1);
								} else {
									System.out.println("單雙錯誤");
									System.out.println(info.getoddEven());
								}

								if (info.getUnderOver().equalsIgnoreCase("小")) {
									EndGame(2);
								} else if (info.getUnderOver().equalsIgnoreCase("大")) {
									EndGame(3);
								} else {
									System.out.println("大小錯誤");
									System.out.println(info.getUnderOver());
								}

								if (info.getoddEven().equalsIgnoreCase("單") && info.getUnderOver().equalsIgnoreCase("小")) {
									EndGame(4);
								} else if (info.getoddEven().equalsIgnoreCase("單")
										&& info.getUnderOver().equalsIgnoreCase("大")) {
									EndGame(5);
								} else if (info.getoddEven().equalsIgnoreCase("雙")
										&& info.getUnderOver().equalsIgnoreCase("小")) {
									EndGame(6);
								} else if (info.getoddEven().equalsIgnoreCase("雙")
										&& info.getUnderOver().equalsIgnoreCase("大")) {
									EndGame(7);
								} else {
									System.out.println("打包錯誤");
									System.out.println(info.getoddEven());
									System.out.println(info.getUnderOver());
								}

								broadcastNpc((getinfo().getTodatCount() + 1) + " 次組合號碼為 " + getinfo().getPlusNum() + " ["
										+ info.getoddEven() + "] [" + info.getUnderOver() + "]。");
								_executeStatus = EXECUTE_STATUS_RELOAD;
								GeneralThreadPool.getInstance().schedule(this, 1000L); // 1秒！
							}
						}
						break;
						case EXECUTE_STATUS_RELOAD: {
							long remainTime = checkTicketSellTime(2);
							if (remainTime > 0) {
								GeneralThreadPool.getInstance().schedule(this, 1000L);
							} else {
								_executeStatus = EXECUTE_STATUS_NONE;
								GeneralThreadPool.getInstance().schedule(this, 1000L); // 1秒！
							}
						}
						break;
						case EXECUTE_STATUS_STOP: {
							if (_MentTime < 0) {
								_MentTime = 30;
								if (_MentNum == 0)
									broadcastNpc("目前沒有進行中的 Powerball 遊戲。");
								else if (_MentNum == 1)
									broadcastNpc("目前沒有進行中的 Powerball 遊戲。");
								_MentNum++;
							}

							if (_MentNum >= 1)
								_MentNum = 0;

							_MentTime--;
							GeneralThreadPool.getInstance().schedule(this, 1000L);
							break;
						}
						default:
							System.out.println(String.format("invalid power ball status... %d", _executeStatus));
							break;
					}
				} else {
					//System.out.println(String.format("%d 期次強力球信息更新完成。", getinfo().getNum()));
					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
			} else {
				GeneralThreadPool.getInstance().schedule(this, 1000L);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public long checkTicketSellTime(int status) {
		if (status == 1) {
			if (_RemainTime == 10) {
				broadcastNpc("門票銷售即將結束。");
			} else if (_RemainTime == 15) {
				broadcastNpc("即將公布普通球抽獎結果。");
			} else if (_RemainTime <= 3) {
				broadcastNpc("距離結果公布還有 " + _RemainTime + " 秒！");
			}
			_RemainTime--;
			return _RemainTime;
		} else if (status == 2) {
			if (_RemainTime < 10) {
				broadcastNpc("距離門票銷售開始還有 " + _RemainTime + " 秒！");
			}
			_RemainTime--;
			return _RemainTime;
		}
		return 0;
	}

	public void initGame() {
		try {
			initNpc();
			initTicketCount();
			initShopNpc();
			setState(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void EndGame(int i) {
		synchronized (this) {
			if (i == 0) {
				SetLosePowerBallTicketPrice(ticket[1], _ration[1]);
				SetWinPowerBallTicketPrice(ticket[i], _ration[i], false);
			} else if (i == 1) {
				SetLosePowerBallTicketPrice(ticket[0], _ration[0]);
				SetWinPowerBallTicketPrice(ticket[i], _ration[i], false);
			} else if (i == 2) {
				SetLosePowerBallTicketPrice(ticket[3], _ration[3]);
				SetWinPowerBallTicketPrice(ticket[i], _ration[i], false);
			} else if (i == 3) {
				SetLosePowerBallTicketPrice(ticket[2], _ration[2]);
				SetWinPowerBallTicketPrice(ticket[i], _ration[i], false);
			} else if (i == 4) {
				SetLosePowerBallTicketPrice(ticket[5], _ration[5]);
				SetLosePowerBallTicketPrice(ticket[6], _ration[6]);
				SetLosePowerBallTicketPrice(ticket[7], _ration[7]);
				SetWinPowerBallTicketPrice(ticket[i], _ration[i], true);
			} else if (i == 5) {
				SetLosePowerBallTicketPrice(ticket[4], _ration[4]);
				SetLosePowerBallTicketPrice(ticket[6], _ration[6]);
				SetLosePowerBallTicketPrice(ticket[7], _ration[7]);
				SetWinPowerBallTicketPrice(ticket[i], _ration[i], true);
			} else if (i == 6) {
				SetLosePowerBallTicketPrice(ticket[4], _ration[4]);
				SetLosePowerBallTicketPrice(ticket[5], _ration[5]);
				SetLosePowerBallTicketPrice(ticket[7], _ration[7]);
				SetWinPowerBallTicketPrice(ticket[i], _ration[i], true);
			} else if (i == 7) {
				SetLosePowerBallTicketPrice(ticket[4], _ration[4]);
				SetLosePowerBallTicketPrice(ticket[5], _ration[5]);
				SetLosePowerBallTicketPrice(ticket[6], _ration[6]);
				SetWinPowerBallTicketPrice(ticket[i], _ration[i], true);
			}
			_RemainTime = 15 * 1;
		}
	}

	public void initNpc() {
		L1NpcInstance n = null;
		for (Object obj : L1World.getInstance().getVisibleObjects(4).values()) {
			if (obj instanceof L1NpcInstance) {
				n = (L1NpcInstance) obj;
				if (n.getNpcTemplate().get_npcId() == 370041) {
					_npc = n;
				}
			}
		}
	}

	public void initShopNpc() {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		_shop = new L1Shop(370041, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(370041, _shop);
	}

	public void broadcastNpc(String msg) {
		if (_npc != null) {
			_npc.broadcastPacket(new S_NpcChatPacket(_npc, msg, 2));
		}
	}

	public void startSellTicket() {
		LoadNpcShopList();
		broadcastNpc("普通球票銷售已開始。");
		setState(0);
	}

	public void LoadNpcShopList() {
		try {
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();

			int num = getinfo().getTodatCount() + 1;
			for (int i = 0; i < 8; i++) {
				ticket[i] = 10000000 + powerBallId.incrementAndGet();
				if (i == 0)
					SavePowerBall(ticket[i], "普通球組合 [單] #" + num + "次");
				else if (i == 1)
					SavePowerBall(ticket[i], "普通球組合 [雙] #" + num + "次");
				else if (i == 2)
					SavePowerBall(ticket[i], "普通球組合 [下] #" + num + "次");
				else if (i == 3)
					SavePowerBall(ticket[i], "普通球組合 [上] #" + num + "次");
				else if (i == 4)
					SavePowerBall(ticket[i], "普通球組合 [單/下] #" + num + "次");
				else if (i == 5)
					SavePowerBall(ticket[i], "普通球組合 [單/上] #" + num + "次");
				else if (i == 6)
					SavePowerBall(ticket[i], "普通球組合 [雙/下] #" + num + "次");
				else if (i == 7)
					SavePowerBall(ticket[i], "普通球組合 [雙/上] #" + num + "次");
				L1ShopItem item = new L1ShopItem(ticket[i], 10000, 1); // 銷售清單
				sellingList.add(item);
				this._ticketId[i] = ticket[i];
			}

			_shop = new L1Shop(370041, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(370041, _shop);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int GetIssuedTicket() {
		return _power.size();
	}

	private void SavePowerBall(int i, String j) {
		PowerBallTicket etcItem = new PowerBallTicket();
		L1Item item = ItemTable.getInstance().getTemplate(4100283);
		etcItem.setItemId(i);
		etcItem.setItemDescId(1);
		etcItem.setGfxId(6438);//143
		etcItem.setGroundGfxId(151);
		etcItem.setName(j);
		etcItem.setNameId(j);
		etcItem.setUseType(item.getUseType());
		etcItem.setType2(0);
		etcItem.setType(12);
		etcItem.setType1(12);
		etcItem.setMaterial(5);

		etcItem.setUseRoyal(item.isUseRoyal());
		etcItem.setUseKnight(item.isUseKnight());
		etcItem.setUseMage(item.isUseMage());
		etcItem.setUseElf(item.isUseElf());
		etcItem.setUseDarkelf(item.isUseDarkelf());
		etcItem.setUseDragonKnight(item.isUseDragonKnight());
		etcItem.setUseBlackwizard(item.isUseBlackwizard());
		etcItem.setUse전사(item.isUse전사());
		etcItem.setUseFencer(item.isUseFencer());
		etcItem.setUseLancer(item.isUseLancer());
		etcItem.setWeight(item.getWeight());
		etcItem.set_stackable(item.isStackable());
//		etcItem.setMaxChargeCount(item.getMaxChargeCount());
		etcItem.setHitModifier(item.getHitModifier());
		etcItem.setDmgModifier(item.getDmgModifier());
		etcItem.setMinLevel(item.getMinLevel());
		etcItem.setMaxLevel(item.getMaxLevel());
		etcItem.setBless(1);
		etcItem.setTradable(false);
		etcItem.setWareHouseLimitType(WareHouseLeaveType.NO_WAREHOUSE);
		etcItem.setWareHouseLimitLevel(item.getWareHouseLimitLevel());
		etcItem.setCantDelete(true);
		etcItem.set_stackable(true);
		etcItem.set_price(1000);
		etcItem.setFoodVolume(item.getFoodVolume());
		etcItem.setToBeSavedAtOnce(item.isToBeSavedAtOnce());
		etcItem.setEndedTimeMessage(item.isEndedTimeMessage());
		etcItem.setUseEffetId(item.getUseEffectId());





		AddTicket(etcItem);
	}

	public void AddTicket(PowerBallTicket power) {
		_power.put(new Integer(power.getItemId()), power);
		ItemTable.getInstance().getAllTemplates().put(power.getItemId(), power);
	}

	public int getTotalTicketCount() {
		int total = 0;
		for (int row = 0; row < 8; row++) {
			total += _ticketCount[row];
		}
		return total;
	}

	public void initTicketCount() {
		for (int row = 0; row < 8; row++) {
			_ticketCount[row] = 0;
		}
	}

	private boolean isParseTime() {
		Calendar calender = Calendar.getInstance();
		int minute, second;
		minute = calender.get(Calendar.MINUTE);
		second = calender.get(Calendar.SECOND);
		if (Config.Web.powerBallTime.length() > 0) {
			StringTokenizer stt = new StringTokenizer(Config.Web.powerBallTime, ",");
			while (stt.hasMoreTokens()) {
				String event_time = stt.nextToken();
				String event_m = event_time.substring(0, event_time.indexOf(":"));
				String event_s = event_time.substring(event_m.length() + 1, event_time.length());
				// 銷售時間.
				if (minute == Integer.valueOf(event_m) && second == Integer.valueOf(event_s)
						|| minute == 10 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s)
						|| minute == 20 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s)
						|| minute == 30 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s)
						|| minute == 40 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s)
						|| minute == 50 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s)) {
					//PowerBallInfoParse.getInstance().PowerBallInfo();
					return true;
				} else if (minute == Integer.valueOf(event_m) && second == Integer.valueOf(event_s) - Config.Web.powerBallTicketEndTime
						|| minute == 10 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s) - Config.Web.powerBallTicketEndTime
						|| minute == 20 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s) - Config.Web.powerBallTicketEndTime
						|| minute == 30 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s) - Config.Web.powerBallTicketEndTime
						|| minute == 40 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s) - Config.Web.powerBallTicketEndTime
						|| minute == 50 + Integer.valueOf(event_m) && second == Integer.valueOf(event_s) - Config.Web.powerBallTicketEndTime) {
					setState(1);
					broadcastNpc("距離結果公布只剩30秒！門票銷售即將結束！");
					_executeStatus = EXECUTE_STATUS_STOP;
					return false;
				}
			}
		}
		return false;
	}

	public void StopGame() {
		for (int i = 0; i < 8; i++) {
			L1ShopItem newItem = new L1ShopItem(ticket[i], (int) ((10000)), 1);
			_purchasingList.add(newItem);
			initShopNpc();
		}
	}

	public void SetWinPowerBallTicketPrice(int id, double rate, boolean jackpot) {
		if (jackpot)
			rate = 3.5;
		else
			rate = 1.93;
		L1ShopItem newItem = new L1ShopItem(id, (int) ((10000 * rate)), 1);
		_purchasingList.add(newItem);
		initShopNpc();
	}

	public void SetLosePowerBallTicketPrice(int id, double rate) {
		L1ShopItem newItem = new L1ShopItem(id, 0, 1);
		_purchasingList.add(newItem);
		initShopNpc();
	}

	private static PowerBallInfo _info;

	public void setinfo(PowerBallInfo info){
		_info = info;
	}

	public static l1j.server.PowerBall.PowerBallInfo getinfo(){
		return _info;
	}

	public int getState() {
		return this._State;
	}

	public void setState(int state) {
		this._State = state;
	}

}
