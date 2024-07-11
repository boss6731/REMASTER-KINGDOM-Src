package l1j.server.lotto;

import l1j.server.Config;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_MSG_ANNOUNCE;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;

import java.util.*;
import java.util.Map.Entry;

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
		// System.out.println("樂透系統實施了嗎？");
		if (Config.ServerAdSetting.LOTTO_USE) {
			checkround();
			GeneralThreadPool.getInstance().schedule(new timer(), 1000);
		}
	}

	class timer implements Runnable {
		public void run() {
			try {
				// 獲取當前時間
				Date now = new Date();
				// Calendar now = Calendar.getInstance();

				// 獲取當前小時和分鐘
				int 小時 = now.getHours();
				int 分鐘 = now.getMinutes();
				int timecal_live = 小時 * 60 + 分鐘;

				/*
				 * int 시간 = now1.HOUR;
				 * int 분 = now1.MINUTE;
				 */

				// 獲取設置的小時和分鐘
				int starthour = Config.ServerAdSetting.LOTTO_HOUR;
				int startminute = Config.ServerAdSetting.LOTTO_MINUTE;
				int timecal_setting = starthour * 60 + startminute;

				// System.out.println("樂透線程返回?" + 小時 + ":" + 分鐘);

				switch (stage) {
					case WAIT:
						if (sub_step == 0) {
						// 如果當前時間與設定時間的差在5至35分鐘之間
							if (timecal_setting - timecal_live <= 35 && timecal_setting - timecal_live >= 5) {
								// 更新階段和子步驟
								stage = 2;
								sub_step = 0;
								GeneralThreadPool.getInstance().schedule(this, 60 * 1000); // 等待1分鐘
								return;
							} else {
								GeneralThreadPool.getInstance().schedule(this, 30 * 60 * 1000); // 等待30分鐘
								return;
							}
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
						return;
					case READY:
						if (sub_step == 0) {
							if (timecal_setting - timecal_live == 2 && !lottoready) {
								checkround();
								GREEN_MSG("即將 " + get_round() + "本期樂透抽獎開始了。");
								lottoready = true;
								sub_step = 1;
							}
							GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待1分鐘
							return;
						}
						if (sub_step == 1) {
							if (timecal_setting - timecal_live == 1 && !lottonotice) {
								roundprize();
								GREEN_MSG(get_round() + "本期樂透的累積中獎金額是 " + totalprize / 2 + "億");
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
							GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待 1 分鐘。
							return;
						}
						if (sub_step == 1) {
							// System.out.println("查看"+lotto[0]+"+"+lotto[1]);

							GREEN_MSG(round + "本期樂透中獎號碼是 " + lotto[0] + ", " + lotto[1] + "本期樂透中獎號碼是。");
							sub_step = 2;
							GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待 1 分鐘。
							return;
						}
						if (sub_step == 2) {
							int prizenumber = lottoMatching();
							if (prizenumber == 0) {
								// System.out.println("會來嗎？>?");
								GREEN_MSG("由於中獎者為0人，本次獎金將累積至下一期。");
								lotto_system_loader.getInstance().updateLotto(round, false);
								stage = 4;
								sub_step = 0;
								GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待 1 分鐘。
								return;
							} else {
								GREEN_MSG("中獎者總共是 " + prizenumber + "由於中獎者為0名，本獎金將累積至下一期。");
								lotto_system_loader.getInstance().updateLotto(round, true);
								sub_step = 3;
								GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待 1 分鐘。
								return;
							}

						}
						if (sub_step == 3) {
							giveprize();
							stage = 4;
						}
						GeneralThreadPool.getInstance().schedule(this, 30 * 1000); // 等待 1 分鐘。
						return;
					case FIN:
						stage = 1;
						sub_step = 0;
						lottoclear();
						GeneralThreadPool.getInstance().schedule(this, 60 * 1000); // 等待 1 分鐘。
						return;
				}

				/*
				 * if (시간 == starthour) {
				 * if (분 >= startminute && !lottostart) {
				 * lottoround();
				 * lottostart = true;
				 * } else if (분 == startminute - 4 || 분 == startminute - 3 && !lottoready) {
				 * GREEN_MSG("곧 "+get_round()+"一年一度的樂透抽獎開始了。");
				 * lottoready = true;
				 * } else if (분 == startminute - 2 || 분 == startminute - 1 && !lottonotice) {
				 * roundprize();
				 * GREEN_MSG(get_round()+"彩券累計中獎金額為 "+ totalprize / 2 +"억 입니다.");
				 * lottonotice = true;
				 * }
				 * }
				 * GeneralThreadPool.getInstance().schedule(this, 60 * 1000);
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		// System.out.println(lotto[0]+"+"+lotto[1]);
		lotto_system_loader.getInstance().updateLotto(thisround, lotto[0], lotto[1]);
	}

	/*
	 * roundprize();
	 * 
	 * GREEN_MSG(thisround+"회차 로또 당첨 번호는 "+lottonum+"입니다.");
	 * int prizenumber = lottoMatching();
	 * if (prizenumber == 0) {
	 * GREEN_MSG("당첨자 0 명으로 이번 상금은 이월됩니다.");
	 * lotto_system_loader.getInstance().updateLotto(thisround, false);
	 * } else {
	 * GREEN_MSG("당첨자는 총 "+prizenumber+"명 입니다.");
	 * lotto_system_loader.getInstance().updateLotto(thisround, true);
	 * giveprize();
	 * }
	 * 
	 * lotto_system_loader.getInstance().addLotto(thisround+1);
	 * 
	 * 
	 * }
	 */
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
			// System.out.println(info.size());
			for (int i = info.size(); i > 0; i--) {
				// System.out.println(info.get(i).get_round());
				if (info.get(i).get_number1() == 0) {
					round = info.get(i).get_round();
					// System.out.println("本期： "+round);
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
		// System.out.println("確認期數： "+round);
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
		L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[公告]： " + msg + ""), true);
	}

	private void addplayer() {
		lotto_system_loader.getInstance().loadlotto();
		lotto_system_info info = lotto_system_loader.getInstance().getlotto(round);

		lotto_system_loader.getInstance().addLottoPc(round, info.get_pc_count() + 1);
	}

	public boolean lottopossible(L1PcInstance pc) {
		lotto_character_loader.getInstance().loadroundcharacter(round);
		lotto_character_info Info = lotto_character_loader.getInstance().getlottonumber(pc.getId());
		if (Info != null) {
			pc.sendPackets(round + "您已參加本期。");
			return false;
		}
		if (lottoready) {
			pc.sendPackets("現在無法參加樂透。");
			return false;
		}

		return true;
	}

	public void addcharlotto(L1PcInstance pc, int number1, int number2) {
		// 如果您已經參加過本次會議，則無法參加。
		if (lottopossible(pc)) {
			addplayer();
			checkround();
			pc.sendPackets("選擇的號碼： " + number1 + ", " + number2 + " 選擇的號碼是：");
			lotto_character_loader.getInstance().updateLotto(pc, round, number1, number2);
		}
	}

	public void checklotto(L1PcInstance pc) {
		int number1, number2;
		lotto_character_loader.getInstance().loadroundcharacter(round);
		lotto_character_info Info = lotto_character_loader.getInstance().getlottonumber(pc.getId());
		if (Info != null) {
			number1 = Info.get_number1();
			number2 = Info.get_number2();
			pc.sendPackets("您的樂透號碼是 " + number1 + ", " + number2 + " 您的樂透號碼是。");
		} else {
			pc.sendPackets("您未參加本期樂透。");
		}
	}

}
