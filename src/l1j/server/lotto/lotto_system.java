package l1j.server.lotto;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_MSG_ANNOUNCE;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class lotto_system {
	private static lotto_system _instance;

	public static lotto_system getInstance() {
		if (_instance == null) {
			_instance = new lotto_system();
		}
		return _instance;
	}

	public static void reload() {
		if (_instance != null) {
			_instance = new lotto_system();
		}
	}

	private int[] lotto = new int[2];
	private int totalprize = 0;
	private boolean lottoready = false;
	private boolean lottonotice = false;
	private boolean lottostart = false;
	private int stage = 1;
	private int sub_step = 0;
	private static final int WAIT = 1;
	private static final int READY = 2;
	private static final int START = 3;
	private static final int FIN = 4;
	private int round;


	public lotto_system() {
//        System.out.println("樂透系統已啟動？");
		if (Config.ServerAdSetting.LOTTO_USE) {
			checkround();
			GeneralThreadPool.getInstance().schedule(new timer(), 1000);
		}
	}

	private void checkround() {

	}

	class timer implements Runnable {
		public void run() {
			try {
				Date now = new Date();
//                Calendar now = Calendar.getInstance();
				int hour = now.getHours();
				int minute = now.getMinutes();
				int timecal_live = hour * 60 + minute;
				/*                int hour = now1.HOUR;
				int minute  = now1.MINUTE;
				*/
				int starthour = Config.ServerAdSetting.LOTTO_HOUR;
				int startminute = Config.ServerAdSetting.LOTTO_MINUTE;
				int timecal_setting = starthour * 60 + startminute;

//                System.out.println("樂透線程運行中？"+hour+":"+minute);

				switch (stage) {
					case WAIT:
						if (sub_step == 0) {
							if (timecal_setting - timecal_live <= 35 && timecal_setting - timecal_live >= 5) {
								stage = 2;
								sub_step = 0;
								GeneralThreadPool.getInstance().schedule(this, 60 * 1000); // 等待1分鐘
								return;
							} else {
//                            GeneralThreadPool.getInstance().schedule(this, 60 * 1000); // 等待30分鐘
								GeneralThreadPool.getInstance().schedule(this, 30 * 60 * 1000); // 等待30分鐘
								return;
							}
						}
						return;
					case READY:
						if (sub_step == 0) {
							if (timecal_setting - timecal_live == 2 && !lottoready) {
								checkround();
								GREEN_MSG("即將開始第 " + get_round() + " 次樂透抽獎。");
								lottoready = true;
								sub_step = 1;
							}
							GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘
							return;
						}
						if (sub_step == 1) {
							if (timecal_setting - timecal_live == 1 && !lottonotice) {
								roundprize();
								GREEN_MSG("第 " + get_round() + " 次樂透的累計獎金為 " + totalprize / 2 + " 億。");
								lottonotice = true;
								stage = 3;
								sub_step = 0;
							}
							GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘
							return;
						}
						GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘
						return;
					case START:
						if (sub_step == 0) {
							lottoround();
							sub_step = 1;
							GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘。
							return;
						}
						if (sub_step == 1) {
//                        System.out.println("確認"+lotto[0]+"+"+lotto[1]);

							GREEN_MSG(round + " 次樂透中獎號碼為 " + lotto[0] + ", " + lotto[1] + "。");
							sub_step = 2;
							GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘。
							return;
						}
						if (sub_step == 2) {
							int prizenumber = lottoMatching();
							if (prizenumber == 0) {
//                            System.out.println("什麼>?");
								GREEN_MSG("無中獎者，本次獎金將累積到下次。");
								lotto_system_loader.getInstance().updateLotto(round, false);
								stage = 4;
								sub_step = 0;
								GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘。
								return;
							} else {
								GREEN_MSG("總共有 " + prizenumber + " 名中獎者。");
								lotto_system_loader.getInstance().updateLotto(round, true);
								sub_step = 3;
								GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘。
								return;
							}
							if (sub_step == 3) {
								giveprize();
								stage = 4;
							}
							GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘。
							return;
							case FIN:
								stage = 1;
								sub_step = 0;
								lottoclear();
								GeneralThreadPool.getInstance().schedule(this, 60 * 1000); // 等待1分鐘。
								return;
						}

						/*                if (hour == starthour) {
						if (minute >= startminute && !lottostart) {
						lottoround();
						lottostart = true;
						} else if (minute == startminute - 4 || minute == startminute - 3 && !lottoready) {
						GREEN_MSG("即將開始第 " + get_round() + " 次樂透抽獎。");
						lottoready = true;
						} else if (minute == startminute - 2 || minute == startminute - 1 && !lottonotice) {
						roundprize();
						GREEN_MSG("第 " + get_round() + " 次樂透的累計獎金為 " + totalprize / 2 + " 億。");
						lottonotice = true;
						}
						}
						GeneralThreadPool.getInstance().schedule(this, 60 * 1000);*/

				} catch(Exception Throwable e;
				){
					e.printStackTrace();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}


		public int get_round() {
			return round;
		}

		public void set_round(int i) {
			round = i;
		}


		private void lottoround() {
			int thisround = round;
			lotto[0] = (int) (Math.random() * 15 + 1);
			lotto[1] = (int) (Math.random() * 15 + 1);
			while (true) {
				if (lotto[1] == lotto[0]) {
					lotto[1] = (int) (Math.random() * 15 + 1);
				} else {
					break;
				}
			}
			Arrays.sort(lotto);
//		System.out.println(lotto[0]+"+"+lotto[1]);
			lotto_system_loader.getInstance().updateLotto(thisround, lotto[0], lotto[1]);
		}

		/*        roundprize();

        GREEN_MSG(thisround + " 次樂透中獎號碼為 " + lottonum + "。");
        int prizenumber = lottoMatching();
        if (prizenumber == 0) {
        GREEN_MSG("無中獎者，本次獎金將累積到下次。");
        lotto_system_loader.getInstance().updateLotto(thisround, false);
        } else {
        GREEN_MSG("總共有 " + prizenumber + " 名中獎者。");
        lotto_system_loader.getInstance().updateLotto(thisround, true);
        giveprize();
        }

        lotto_system_loader.getInstance().addLotto(thisround + 1);

        }*/
		private ArrayList<lotto_character_info> getprizecharacter = new ArrayList<lotto_character_info>();

		private int lottoMatching() {
			int num = 0;
			int thisround = round;
			lotto_character_loader.getInstance().getlottocharacter(thisround);
			HashMap<Integer, lotto_character_info> characternum = lotto_character_loader.getInstance().getlottoinfo();
			;
			Iterator<Entry<Integer, lotto_character_info>> temp = characternum.entrySet().iterator();

			while (temp.hasNext()) {
				Entry<Integer, lotto_character_info> entrySet = (Entry<Integer, lotto_character_info>) temp.next();
				if (thisround != entrySet.getValue().get_round()) {
					continue;
				}
				if (lotto[0] == entrySet.getValue().get_number1()) {
					if (lotto[1] == entrySet.getValue().get_number2()) {
						num++;
						getprizecharacter.add(entrySet.getValue());
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
			characternum.clear();

			return num;
		}

		private void giveprize() {
			for (int i = 0; i < getprizecharacter.size(); i++) {
				L1PcInstance pc = L1World.getInstance().getPlayer(getprizecharacter.get(i).get_char_name());
				if (pc == null) {
					continue;
				}
				pc.getInventory().storeItem(400254, totalprize / 2 / getprizecharacter.size());
			}
		}

		private void checkround() {
			lotto_system_loader.getInstance().loadlotto();
			HashMap<Integer, lotto_system_info> info = lotto_system_loader.getInstance().getlottoall();
			if (info.size() == 0) {
				round = 1;
				lotto_system_loader.getInstance().addLotto(round);
			} else {
//            System.out.println(info.size());
				for (int i = info.size(); i > 0; i--) {
//                System.out.println(info.get(i).get_round());
					if (info.get(i).get_number1() == 0) {
						round = info.get(i).get_round();
//                    System.out.println("本次回合: " + round);
					} else {
						continue;
					}
				}
				if (round == 0) {
					lotto_system_loader.getInstance().addLotto(info.size() + 1);
					round = info.size() + 1;
				}
			}
		}

		private void lottoclear() {
			totalprize = 0;
			if (!getprizecharacter.isEmpty()) {
				getprizecharacter.clear();
			}
			round++;
//        System.out.println("回合確認: " + round);
			lotto_system_loader.getInstance().addLotto(round);
			lottoready = false;
			lottonotice = false;
			lottostart = false;
			lotto_character_loader.getInstance().dellotto();
		}

		private void roundprize() {
			lotto_system_loader.getInstance().loadlotto();
			HashMap<Integer, lotto_system_info> info = lotto_system_loader.getInstance().getlottoall();

			for (int i = info.size(); i > 0; i--) {
				if (info.get(i).is_prize()) {
					break;
				} else {
					totalprize += info.get(i).get_pc_count();
				}
			}


		}

		private void GREEN_MSG(String msg) {
			L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[公告] : " + msg + ""), true);
		}

		private void addplayer() {
			lotto_system_loader.getInstance().loadlotto();
			lotto_system_info info = lotto_system_loader.getInstance().getlotto(round);

			lotto_system_loader.getInstance().addLottoPc(round, info.get_pc_count() + 1);
		}

		public boolean lottopossible(L1PcInstance pc) {
			lotto_character_loader.getInstance().loadroundcharacter(round);
			lotto_character_info info = lotto_character_loader.getInstance().getlottonumber(pc.getId());
			if (info != null) {
				pc.sendPackets(round + " 次回合你已經參加過了。");
				return false;
			}
			if (lottoready) {
				pc.sendPackets("現在無法參加樂透。");
				return false;
			}

			return true;
		}

		public void addcharlotto(L1PcInstance pc, int number1, int number2) {
			//已經參加本次回合則無法再次參加
			if (lottopossible(pc)) {
				addplayer();
				checkround();
				pc.sendPackets("選中的號碼: " + number1 + ", " + number2 + "。");
				lotto_character_loader.getInstance().updateLotto(pc, round, number1, number2);
			}
		}

		public void checklotto(L1PcInstance pc) {
			int number1, number2;
			lotto_character_loader.getInstance().loadroundcharacter(round);
			lotto_character_info info = lotto_character_loader.getInstance().getlottonumber(pc.getId());
			if (info != null) {
				number1 = info.get_number1();
				number2 = info.get_number2();
				pc.sendPackets("你的樂透號碼是 " + number1 + ", " + number2 + "。");
			} else {
				pc.sendPackets("你未參加本次回合的樂透。");
			}
		}
	}
}