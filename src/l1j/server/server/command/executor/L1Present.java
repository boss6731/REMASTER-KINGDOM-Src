         package l1j.server.server.command.executor;

         import java.util.StringTokenizer;
         import l1j.server.server.datatables.CharacterTable;
         import l1j.server.server.datatables.ItemTable;
         import l1j.server.server.model.Instance.L1ItemInstance;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_GMHtml;
         import l1j.server.server.serverpackets.S_SkillSound;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.templates.L1Item;





         public class L1Present
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Present();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer st = new StringTokenizer(arg);
               String name = st.nextToken();
               String nameid = st.nextToken();
               L1PcInstance target = L1World.getInstance().getPlayer(name);

               if (target == null) {
                 target = CharacterTable.getInstance().restoreCharacter(name);

                 if (target == null) {
                   pc.sendPackets("這個角色不存在。");

                   return;
                 }
                 CharacterTable.getInstance().restoreInventory(target);
               }

               int count = 1;
               if (st.hasMoreTokens()) {
                 count = Integer.parseInt(st.nextToken());
               }
               int enchant = 0;
               if (st.hasMoreTokens()) {
                 enchant = Integer.parseInt(st.nextToken());
               }
               int itemid = 0;
               int Attrenchant = 0;
               if (st.hasMoreTokens()) {
                 Attrenchant = Integer.parseInt(st.nextToken());
               }
               int bless = 0;
               if (st.hasMoreTokens()) {
                 bless = Integer.parseInt(st.nextToken());
               }
               try {
                 itemid = Integer.parseInt(nameid);
               } catch (NumberFormatException e) {
                 itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
                 if (itemid == 0) {
                   pc.sendPackets("未找到該項目。");
                   return;
                 }
               }
               L1Item temp = ItemTable.getInstance().getTemplate(itemid);
               if (temp != null) {
                 if (temp.isStackable()) {
                   L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
                   item.setEnchantLevel(0);
                   item.setCount(count);
                   if (target.getInventory().checkAddItem(item, count) == 0) {
                     target.getInventory().storeItem(item);

                     target.sendPackets((ServerBasePacket)new S_GMHtml("梅蒂斯", "管理員", item.getName(), String.format("%,d個", new Object[] { Integer.valueOf(item.getCount()) })));
                     target.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 4856));
                     pc.sendPackets("\\aD[" + target.getName() + "] " + item.getLogName() + " (ID:" + itemid + ") 傳送");
                   }
                 } else {
                   L1ItemInstance item = null;
                   int createCount;
                   for (createCount = 0; createCount < count; createCount++) {
                     item = ItemTable.getInstance().createItem(itemid);
                     item.setEnchantLevel(enchant);
                     item.setAttrEnchantLevel(Attrenchant);
                     if (bless == 129) {
                       item.setBless(bless);
                     }
                     target.getInventory().storeItem(item);
                     if (bless == 129) {
                       item.setBless(bless);
                       target.getInventory().updateItem(item, 512);
                       target.getInventory().saveItem(item, 512);
                     }
                   }
                   if (createCount > 0) {

                     target.sendPackets((ServerBasePacket)new S_GMHtml("梅蒂斯", "管理員", L1ItemInstance.to_simple_description(item), String.format("%,d個", new Object[] { Integer.valueOf(item.getCount()) })));
                     target.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 4856));
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("\\aD[" + target.getName() + "] +" + enchant + " " + temp.getNameId() + "(ID:" + itemid + ") " + count + "個 傳送", true));
                   }
                 }
               } else {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("具有指定 ID 的項目不存在。"));
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(".[禮物][角色][物品ID][數量][附魔][屬性][封印129]"));
             }
           }
         }


