package l1j.server.server.model.item.function;

import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.DESPERADO;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;

import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.DomTower.MJDomTowerNpcActionInfo;
import l1j.server.Config;
import l1j.server.MJActionListener.ActionListener;
import l1j.server.MJActionListener.ActionListenerLoader;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.Lineage2D.MJRectangle;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;


public class TelBook {
	
	public static void clickItem(L1PcInstance pc, int itemId, int BookTel, L1ItemInstance l1iteminstance) {
		if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
			return;
		}
		if (!pc.getMap().isEscapable()) {
			pc.sendPackets(647);
			return;
		}
		if ((pc.hasSkillEffect(SHOCK_STUN)) 
				|| pc.hasSkillEffect(L1SkillId.EMPIRE) || pc.hasSkillEffect(L1SkillId.PANTHERA)
				|| (pc.hasSkillEffect(ICE_LANCE)) || (pc.hasSkillEffect(BONE_BREAK))
				|| (pc.hasSkillEffect(DESPERADO)) || (pc.hasSkillEffect(EARTH_BIND))
				|| (pc.hasSkillEffect(L1SkillId.ETERNITI)) || (pc.hasSkillEffect(L1SkillId.FORCE_STUN))
				|| (pc.hasSkillEffect(L1SkillId.TEMPEST)) || (pc.hasSkillEffect(L1SkillId.PHANTOM))
				|| pc.hasSkillEffect(L1SkillId.CRUEL)
				|| (pc.hasSkillEffect(L1SkillId.DISINTEGRATE))) {
			return;
		}
		pc.set_MassTel(true);
		if (itemId == 560025) {
			try {
				final int[][] villageMemoryBook = {
						{ 34060, 32281, 4 }, // 奧倫
						{ 33079, 33390, 4 }, // 銀騎士
						{ 32750, 32439, 4 }, // 亞丁森林
						{ 32612, 33188, 4 }, // 風木
						{ 33720, 32492, 4 }, // 威爾登
						{ 32872, 32912, 304 }, // 沉默洞穴
						{ 32612, 32781, 4 }, // 古魯丁
						{ 33067, 32803, 4 }, // 肯特
						{ 33933, 33358, 4 }, // 亞丁
						{ 33601, 33232, 4 }, // 海音
						{ 32574, 32942, 0 }, // 說話之島
						{ 33430, 32815, 4 }, // 奇岩
				};
				int[] a = villageMemoryBook[BookTel];
				if (a != null) {
					pc.start_teleport(a[0], a[1], a[2], pc.getHeading(), 18339, true, true);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if (itemId == 560027) {
			try {
				final int[][] 던전기억 = {
				/** 低等級推薦狩獵場 **/
						{ 34266, 32190, 4 }, // 克拉辛入口
						{ 32507, 32924, 9 }, // 說話之島北部小島
						{ 32491, 32854, 9 }, // 說話之島地下城入口
						{ 32409, 32938, 9 }, // 說話之島獸人崗哨地帶
						{ 32883, 32647, 4 }, // 本土死亡的廢墟
						{ 32875, 32927, 4 }, // 本土亡者之墓
						{ 32726, 32928, 4 }, // 古魯丁地下城1層入口
						{ 32764, 32840, 77 }, // 象牙塔4層
						{ 32708, 33150, 9 }, // 說話之島黑騎士前哨基地
						{ 32599, 32289, 4 }, // 本土獸人村莊
						{ 32908, 33222, 4 }, // 本土沙漠(厄爾札貝)
						{ 32761, 33167, 4 }, // 本土沙漠(沙蟲)
						{ 32806, 32726, 19 }, // 妖精森林地下城1層
						{ 32796, 32753, 809 }, // 古魯丁地下城3層入口
						{ 33429, 32826, 4 }, // 奇岩監獄入口
						{ 32809, 32729, 25 }, // 修煉地下
				/** 中等級推薦狩獵場 **/
						{ 32745, 32427, 4 }, // 無視
						{ 33764, 33314, 4 }, // 本土鏡之森
						{ 33804, 32966, 4 }, // 本土密林地帶
						{ 32710, 32790, 59 }, // 艾娃王國1層
						{ 34251, 33453, 4 }, // 傲慢之塔入口
						{ 32811, 32909, 4 }, // 本土黑騎士出沒地區
						{ 32766, 32798, 20 }, // 妖精森林地下城2層
						{ 32726, 32808, 61 }, // 艾娃王國3層
						{ 32809, 32808, 30 }, // 龍之地下城1層
						{ 32809, 32767, 27 }, // 修煉地下城3層
						{ 32801, 32928, 800 }, //
				/** 高等級推薦狩獵場 **/
						{ 32705, 32822, 32 }, // 龍之谷地下城3層
						{ 33436, 33475, 4 }, // 海音被遺忘的小島船票
						{ 33182, 33006, 4 }, // 本土暗黑龍的傷痕
						{ 34126, 32799, 4 }, // 本土風龍的巢穴
						{ 34126, 32192, 4 }, // 本土奧倫雪壁
						{ 33331, 32459, 4 }, // 本土龍之谷入口
						{ 34051, 32561, 4 }, // 本土艾爾摩攻擊戰地
						{ 33643, 32419, 4 }, // 本土火龍巢穴入口
				};
				int[] b = recommendedHuntingGrounds[BookTel];
				if (b != null) {
					pc.start_teleport(b[0], b[1], b[2], pc.getHeading(), 18339, true, true);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (itemId == 560028) {
			try {
				final int[][] OmangMemories = {
						{ 32735, 32798, 101 }, // 傲慢1
						{ 32727, 32803, 102 }, // 傲慢2
						{ 32726, 32803, 103 }, // 傲慢3
						{ 32620, 32859, 104 }, // 傲慢4
						{ 32601, 32866, 105 }, // 傲慢5
						{ 32611, 32863, 106 }, // 傲慢6
						{ 32618, 32866, 107 }, // 傲慢7
						{ 32602, 32867, 108 }, // 傲慢8
						{ 32613, 32866, 109 }, // 傲慢9
						{ 32730, 32803, 110 }, // 傲慢10
						{ 32646, 32808, 111 }, // 傲慢頂層起始點
						{ 32801, 32963, 111 }, // 傲慢頂層中間點
				};
				int[] c = OmangMemories[BookTel];
				if (c != null) {
					pc.start_teleport(c[0], c[1], c[2], pc.getHeading(), 18339, true, true);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if (itemId == 560028) {
			try {
				final int[][] OmangMemory = {
						{ 32735, 32798, 101 }, // 傲慢1
						{ 32727, 32803, 102 }, // 傲慢2
						{ 32726, 32803, 103 }, // 傲慢3
						{ 32620, 32859, 104 }, // 傲慢4
						{ 32601, 32866, 105 }, // 傲慢5
						{ 32611, 32863, 106 }, // 傲慢6
						{ 32618, 32866, 107 }, // 傲慢7
						{ 32602, 32867, 108 }, // 傲慢8
						{ 32613, 32866, 109 }, // 傲慢9
						{ 32730, 32803, 110 }, // 傲慢10
						{ 32646, 32808, 111 }, // 傲慢頂層起始點
						{ 32801, 32963, 111 }, // 傲慢頂層中間點
				};
				int[] c = OmangMemory[BookTel];
				if (c != null) {
					pc.start_teleport(c[0], c[1], c[2], pc.getHeading(), 18339, true, true);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if (itemId == 4100653) {
			try {
				final int[][] SiegeAmulet = {
						{ 0, 0, 0 }, // 奇岩城 (基本說明)
						{ 33584, 32736, 15482 }, // 城左側盡頭
						{ 33600, 32746, 15482 }, // 城入口左側
						{ 33630, 32749, 15482 }, // 城入口中央
						{ 33665, 32744, 15482 }, // 城入口右側
						{ 33675, 32737, 15482 }, // 城右側盡頭
						{ 33638, 32785, 15482 }, // 奇岩城村莊
				};
				int[] c = SiegeAmulet[BookTel];
				if (c != null) {
					pc.start_teleport(c[0], c[1], c[2], pc.getHeading(), 18339, true, true);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if (itemId == 560029) {
			try {
				final int[][] EncounterMemory = {
					/** 低等級推薦狩獵場 **/
						{ 0, 0, 0 }, // 基本說明
						{ 32643, 32841, 9 }, // 說話之島北部小島
						{ 32491, 32855, 9 }, // 說話之島地下城入口
						{ 32437, 32911, 9 }, // 說話之島獸人崗哨地帶
						{ 32706, 33153, 9 }, // 說話之島黑騎士前哨基地
						{ 32874, 32653, 4 }, // 死亡的廢墟
						{ 32879, 32895, 4 }, // 亡者之墓
						{ 32810, 32729, 807 }, // 古魯丁地下城入口1層
						{ 32723, 32398, 4 }, // 本土獸人村莊
						{ 32767, 33164, 4 }, // 沙蟲
						{ 32805, 32724, 19 }, // 妖精森林地下城1層入口

					/** 中等級推薦狩獵場 **/
						{ 0, 0, 0 }, // 基本說明
						{ 33795, 32774, 4 }, // 叢林地帶
						{ 32844, 32932, 4 }, // 本土黑騎士出沒區域
						{ 32766, 32796, 20 }, // 妖精森林地下城2層
						{ 33768, 33312, 4 }, // 本土鏡子森林地帶
						{ 33168, 32968, 4 }, // 暗黑龍之痕
						{ 34125, 32799, 4 }, // 風龍之巢入口
						{ 34127, 32192, 4 }, // 冰雪壁入口
						{ 32764, 32842, 77 }, // 象牙塔4層（3層）
						{ 32769, 32759, 30 }, // 龍之谷地下城1層
						{ 32706, 32821, 32 }, // 龍之谷地下城3層

						/** 高等級推薦狩獵場 **/
						{ 0, 0, 0 }, // 基本說明
						{ 33429, 32825, 4 }, // 奇岩監獄入口
						{ 32745, 32801, 35 }, // 龍之谷地下城5層
						{ 32762, 32774, 810 }, // 古魯丁地下城入口4層
						{ 33330, 32458, 4 }, // 本土龍之谷入口
						{ 34056, 32547, 4 }, // 本土艾爾摩戰場
						{ 32804, 32726, 812 }, // 古魯丁地下城入口6層
						{ 33645, 32418, 4 }, // 本土火龍之巢入口
						{ 32805, 32267, 4 }, // 本土阿圖巴獸人隱身處入口
						{ 32881, 32652, 4 }, // 暗黑龍地下城入口
						{ 34385, 32309, 4 }, // 被詛咒的礦山
						{ 34469, 32191, 4 }, // 羅文廣場
				};
				int[] c = EncounterMemory[BookTel];
				if (c != null) {
					pc.start_teleport(c[0], c[1], c[2], pc.getHeading(), 18339, true, true);
					pc.getInventory().removeItem(l1iteminstance, 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (itemId == 4100135) {
			pc.set_is_using_items(true);
			try {
				if(BookTel >= 10)
					return;
				
				MJDomTowerNpcActionInfo aInfo = MJDomTowerNpcActionInfo.get_action_info_from_index(BookTel);
				if(aInfo == null)
					return;
				
				if(Config.Login.UseShiftServer && MJShiftObjectManager.getInstance().is_battle_server_domtower()) {
					int map_id = aInfo.get_first_mapid();
					if(pc.is_shift_battle()) {
						MJRectangle rt = MJDomTowerNpcActionInfo.entry_rectangles.get(map_id);
						if(rt != null) {
							MJPoint pt = rt.toRandPoint(50);
							pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, true);
						}
					}else {
						MJShiftObjectManager.getInstance().do_send_battle_server(pc, String.valueOf(map_id));
					}
				}else {
					ActionListener listener = ActionListenerLoader.getInstance().findListener(aInfo.npcid, aInfo.actions[0]);
					if(listener != null)
						listener.to_action(pc, null);					
				}
			} catch (Exception e) {e.printStackTrace();
			}finally {
				pc.set_is_using_items(false);
			}
		}
	}
	
}
