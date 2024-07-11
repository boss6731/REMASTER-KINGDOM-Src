     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;

     public class S_ShowAutoInformation extends ServerBasePacket {
       private static final String S_ShowAutoInformation = "[S] S_SHOW_AUTOINFORMATION";

       public S_ShowAutoInformation(L1PcInstance pc) {
         buildPacket(pc);
       }

         private void buildPacket(L1PcInstance pc) {
             // 發送一個字節248，這可能是一個操作碼或者控制標誌
             writeC(248);
             // 發送一個整數0，這可能是代表某種狀態或標誌
             writeD(0);
             // 發送字符串 "자동사냥"（意為 "自動狩獵"）
             writeS("自動狩獵");
             // 發送字符串 "사용 설명서"（意為 "使用手冊"）
             writeS("使用手冊");
             // 發送一個空字符串
             writeS("");

             // 建立一個物品信息的字符串緩衝區
             StringBuffer itemInfo = new StringBuffer();
             // 添加字符串 "물약 : 신속 체력 회복제(500개)"（意為 "藥水：快速體力恢復劑（500個）"）
             itemInfo.append("藥水：快速體力恢復劑（500個）\r\n");
             // 添加字符串 "물약 : 신속 강력 체력 회복제(500개)"（意為 "藥水：迅速而強效的體力恢復劑（500個）"）
             itemInfo.append("藥水：迅速而強效的體力恢復劑（500個\r\n");
             // 添加字符串 "주문서 : 순간이동 (300장)"（意為 "卷軸：瞬間移動（300張）"）
             itemInfo.append("卷軸：瞬間移動（300張）\r\n");
             // 添加字符串 "주문서 : 기란마을 귀환(5장)"（意為 "卷軸：返回吉蘭村（5張）"）
             itemInfo.append("卷軸：返回奇岩村（5張）\r\n");
             // 添加字符串 "주문서 : 변신 주문서(20장)"（意為 "卷軸：變形卷軸（20張）"）
             itemInfo.append("卷軸：變形卷軸（20張）");

             // 發送物品信息字符串
             writeS(itemInfo.toString());
         }

         if (pc.isCrown()) {
             // 判斷玩家是否是王族
             type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
             type1.append("藥水：惡魔之血30個\r\n"); // 添加 "용기 : 악마의 피(30個)" (勇氣：惡魔之血30個)
         } else if (pc.isKnight()) {
             // 判斷玩家是否是騎士
             type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
             type1.append("藥水：濃縮勇氣5個\r\n"); // 添加 "용기 : 농축 용기(5個)" (勇氣：濃縮勇氣5個)
         } else if (pc.is전사()) {
             // 判斷玩家是否是戰士
             type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
             type1.append("藥水：濃縮勇氣5個\r\n"); // 添加 "용기 : 농축 용기(5個)" (勇氣：濃縮勇氣5個)
             type1.append("材料：結晶體2000個\r\n"); // 添加 "용기 : 결정체(2000個)" (勇氣：結晶體2000個)
         } else if (pc.isDragonknight()) {
             // 判斷玩家是否是龍騎士
             type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
             type1.append("材料：刻印的骨片100個\r\n"); // 添加 "재료 : 각인의 뼈조각(100個)" (材料：刻印的骨片100個)
         } else if (pc.isElf()) {
             // 判斷玩家是否是精靈
             if (pc.getWeapon() == null || pc.getWeapon().getItem().getType1() != 20) {
                 // 判斷玩家是否沒有武器或武器類型不為20
                 type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
                 type1.append("材料：精靈玉100個\r\n"); // 添加 "재료 : 정령옥(100個)" (材料：精靈珠100個)
                 type1.append("材料：精靈餅乾30個\r\n"); // 添加 "재료 : 엘븐와퍼(30個)" (材料：精靈鬆餅30個)
             } else {
                 type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
                 type1.append("藥水：濃縮集中5個\r\n"); // 添加 "용기 : 농축 집중(5個)" (勇氣：濃縮集中5個)
                 type1.append("箭矢：銀箭3000發)\r\n"); // 添加 "화살 : 은 화살(3000發)" (箭矢：銀箭3000發)
             }
         }
     } else if (pc.isDarkelf()) {
         / 判斷玩家是否是黑暗精靈
             type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
             type1.append("材料：黑曜石100個)\r\n"); // 添加 "재료 : 흑요석(100個)" (材料：黑曜石100個)
     } else if (pc.isWizard()) {
         / 判斷玩家是否是巫師
             type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
             type1.append("輔助：濃縮智慧3個\r\n"); // 添加 "보조 : 농축 지혜(3個)" (輔助：濃縮智慧3個)
             type1.append("輔助：濃縮魔力3個\r\n"); // 添加 "보조 : 농축 마력(3個)" (輔助：濃縮魔力3個)
             type1.append("材料：魔力之石100個\r\n"); // 添加 "재료 : 마력의 돌(100個)" (材料：魔力之石100個)
     } else if (pc.isBlackwizard()) {
         / 判斷玩家是否是黑魔法師
             type1.append("瞬移卷軸：強效瞬移5個\r\n"); // 添加 "촐기 : 강촐(5個)" (瞬移卷軸：強效瞬移5個)
             type1.append("輔助：濃縮智慧3個\r\n"); // 添加 "보조 : 농축 지혜(3個)" (輔助：濃縮智慧3個)
             type1.append("材料：尤格德拉300個\r\n"); // 添加 "재료 : 유그드라(300個)" (材料：尤格德拉300個)
     }

     / 發送最終的字符串，包括類型1和類型2的物品信息
             writeS("類型 1.\r\n" + type1Potion + type1.toString() + etcItem + "\r\n類型 2.\r\n" + type2Potion + type1.toString() + etcItem);
}

       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_SHOW_AUTOINFORMATION";
       }
     }


