package l1j.server.server.model.item.function;

import MJShiftObject.Battle.DomTower.MJDomTowerNpcActionInfo;
import MJShiftObject.MJShiftObjectManager;
import l1j.server.Config;
import l1j.server.MJActionListener.ActionListener;
import l1j.server.MJActionListener.ActionListenerLoader;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.Lineage2D.MJRectangle;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;


public class TelBook
{
    public static void clickItem(L1PcInstance pc, int itemId, int BookTel, L1ItemInstance l1iteminstance) {
        if (pc.isParalyzed() || pc.isSleeped() || pc.isDead()) {
            return;
        }
        if (!pc.getMap().isEscapable()) {
            pc.sendPackets(647);
            return;
        }
        if (pc.hasSkillEffect(87) || pc
                .hasSkillEffect(123) || pc.hasSkillEffect(5003) || pc
                .hasSkillEffect(70705) || pc.hasSkillEffect(208) || pc
                .hasSkillEffect(230) || pc.hasSkillEffect(157) || pc
                .hasSkillEffect(243) || pc.hasSkillEffect(242) || pc
                .hasSkillEffect(5027) || pc.hasSkillEffect(5002) || pc
                .hasSkillEffect(5056) || pc
                .hasSkillEffect(77)) {
            return;
        }
        // 設置 MassTel 為 true
        pc.set_MassTel(true);

        // 如果物品ID是 560025
        if (itemId == 560025) {
            try {
                // 定義村莊記憶書的位置數組
                int[][] 村莊記憶書 = {
                        { 34060, 32281, 4 }, { 33079, 33390, 4 }, { 32750, 32439, 4 },
                        { 32612, 33188, 4 }, { 33720, 32492, 4 }, { 32872, 32912, 304 },
                { 32612, 32781, 4 }, { 33067, 32803, 4 }, { 33933, 33358, 4 },
                { 33601, 33232, 4 }, { 32574, 32942, 0 }, { 33430, 32815, 4 }
                };

                // 根據 BookTel 索引獲取位置數組
                int[] a = 村莊記憶書[BookTel];
                if (a != null) {
                    // 開始傳送
                    pc.start_teleport(a[0], a[1], a[2], pc.getHeading(), 18339, true, true);
                    // 從玩家背包中移除一個物品
                    pc.getInventory().removeItem(l1iteminstance, 1);
                }
            } catch (Exception e) {
                // 捕獲並打印異常
                e.printStackTrace();
            }

             // 如果物品ID是 560027
        } else if (itemId == 560027) {
            try {
                // 定義地下城記憶的位置數組
                int[][] 地下城記憶 = {
                        { 34266, 32190, 4 }, { 32507, 32924, 9 }, { 32491, 32854, 9 },
                        { 32409, 32938, 9 }, { 32883, 32647, 4 }, { 32875, 32927, 4 },
                        { 32726, 32928, 4 }, { 32764, 32840, 77 }, { 32708, 33150, 9 },
                        { 32599, 32289, 4 }, { 32908, 33222, 4 }, { 32761, 33167, 4 },
                        { 32806, 32726, 19 }, { 32796, 32753, 809 }, { 33429, 32826, 4 },
                        { 32809, 32729, 25 }, { 32745, 32427, 4 }, { 33764, 33314, 4 },
                        { 33804, 32966, 4 }, { 32710, 32790, 59 }, { 34251, 33453, 4 },
                        { 32811, 32909, 4 }, { 32766, 32798, 20 }, { 32726, 32808, 61 },
                        { 32809, 32808, 30 }, { 32809, 32767, 27 }, { 32801, 32928, 800 },
                        { 32705, 32822, 32 }, { 33436, 33475, 4 }, { 33182, 33006, 4 },
                        { 34126, 32799, 4 }, { 34126, 32192, 4 }, { 33331, 32459, 4 },
                        { 34051, 32561, 4 }, { 33643, 32419, 4 }
                };

                // 根據 BookTel 索引獲取地下城記憶的位置數組
                int[] b = 地下城記憶[BookTel];
                if (b != null) {
                    // 開始傳送
                    pc.start_teleport(b[0], b[1], b[2], pc.getHeading(), 18339, true, true);
                    // 從玩家背包中移除一個物品
                    pc.getInventory().removeItem(l1iteminstance, 1);
                }
            } catch (Exception e) {
                // 捕獲並打印異常
                e.printStackTrace();
            }

            // 如果物品ID是 560028
        } else if (itemId == 560028) {
            try {
                // 定義傲慢記憶的位置數組
                int[][] 傲慢之塔記憶 = {
                        { 32735, 32798, 101 }, { 32727, 32803, 102 }, { 32726, 32803, 103 },
                        { 32620, 32859, 104 }, { 32601, 32866, 105 }, { 32611, 32863, 106 },
                        { 32618, 32866, 107 }, { 32602, 32867, 108 }, { 32613, 32866, 109 },
                        { 32730, 32803, 110 }, { 32646, 32808, 111 }, { 32801, 32963, 111 }
                };

                // 根據 BookTel 索引獲取位置數組
                int[] c = 傲慢之塔記憶[BookTel];
                if (c != null) {
                    // 開始傳送
                    pc.start_teleport(c[0], c[1], c[2], pc.getHeading(), 18339, true, true);
                }
            } catch (Exception e) {
                // 捕獲並打印異常
                e.printStackTrace();
            }

            // 如果物品ID是 4100653
        } else if (itemId == 4100653) {
            try {
                // 定義攻城符的位置數組
                int[][] 攻城符 = {
                        { 0, 0, 0 }, { 33584, 32736, 15482 }, { 33600, 32746, 15482 },
                        { 33630, 32749, 15482 }, { 33665, 32744, 15482 }, { 33675, 32737, 15482 },
                        { 33638, 32785, 15482 }
                };

                // 根據 BookTel 索引獲取位置數組
                int[] c = 攻城符[BookTel];
                if (c != null) {
                    // 開始傳送
                    pc.start_teleport(c[0], c[1], c[2], pc.getHeading(), 18339, true, true);
                }
            } catch (Exception e) {
                // 捕獲並打印異常
                e.printStackTrace();
            }

            // 如果物品ID是 560029
        } else if (itemId == 560029) {
            try {
                // 定義遭遇記憶的位置數組
                int[][] 遭遇記憶 = {
                        { 0, 0, 0 }, { 32643, 32841, 9 }, { 32491, 32855, 9 },
                        [09:28]
                { 32437, 32911, 9 }, { 32706, 33153, 9 }, { 32874, 32653, 4 },
                { 32879, 32895, 4 }, { 32810, 32729, 807 }, { 32723, 32398, 4 },
                { 32767, 33164, 4 }, { 32805, 32724, 19 }, { 0, 0, 0 },
                { 33795, 32774, 4 }, { 32844, 32932, 4 }, { 32766, 32796, 20 },
                { 33768, 33312, 4 }, { 33168, 32968, 4 }, { 34125, 32799, 4 },
                { 34127, 32192, 4 }, { 32764, 32842, 77 }, { 32769, 32759, 30 },
                { 32706, 32821, 32 }, { 0, 0, 0 }, { 33429, 32825, 4 },
                { 32745, 32801, 35 }, { 32762, 32774, 810 }, { 33330, 32458, 4 },
                { 34056, 32547, 4 }, { 32804, 32726, 812 }, { 33645, 32418, 4 },
                { 32805, 32267, 4 }, { 32881, 32652, 4 }, { 34385, 32309, 4 },
                { 34469, 32191, 4 }
                ;

               // 根據 BookTel 索引獲取位置數組
               int[] c = 遭遇記憶[BookTel];
               if (c != null) {
                   // 開始傳送
                   pc.start_teleport(c[0], c[1], c[2], pc.getHeading(), 18339, true, true);
                   // 從玩家背包中移除一個物品
                   pc.getInventory().removeItem(l1iteminstance, 1);
               }
           } catch (Exception e) {
               // 捕獲並打印異常
               e.printStackTrace();
           }

       } else if (itemId == 4100135) {
           pc.set_is_using_items(true);

           try { if (BookTel >= 10) {
               return;
           }
           MJDomTowerNpcActionInfo aInfo = MJDomTowerNpcActionInfo.get_action_info_from_index(BookTel);
               if (aInfo == null) {
                   return;
               }
               if (Config.Login.UseShiftServer && MJShiftObjectManager.getInstance().is_battle_server_domtower()) {
                   int map_id = aInfo.get_first_mapid();
                   if (pc.is_shift_battle()) {
                       MJRectangle rt = (MJRectangle)MJDomTowerNpcActionInfo.entry_rectangles.get(Integer.valueOf(map_id));
                       if (rt != null) {
                           MJPoint pt = rt.toRandPoint(50);
                           pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, true);
                       }
                   } else {
                       MJShiftObjectManager.getInstance().do_send_battle_server(pc, String.valueOf(map_id));
                   }
               } else {
                   ActionListener listener = ActionListenerLoader.getInstance().findListener(aInfo.npcid, aInfo.actions[0]);
                   if (listener != null)
                       listener.to_action(pc, null);
               }  }
           catch (Exception e) { e.printStackTrace(); }
           finally
           { pc.set_is_using_items(false); }

       }
   }
 }


