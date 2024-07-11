         package l1j.server.server.serverpackets;

         import java.io.IOException;
         import java.util.ArrayList;
         import java.util.Collections;
         import java.util.Comparator;
         import l1j.server.server.datatables.ItemTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.templates.L1Item;
         import l1j.server.server.templates.L1MarketPrice;
         import l1j.server.server.utils.BinaryOutputStream;

         public class S_MarketPriceList
           extends ServerBasePacket
         {
           public S_MarketPriceList(L1PcInstance pc, ArrayList<L1MarketPrice> list) {
             try {
               writeC(127);
               writeD(0);
               writeH(list.size());
               writeC(5);
               L1Item item = null;
               L1Item template = null;

               Collections.sort(list, new NoAscCompare());
               int size = list.size();
               if (size > 200) {
                 size = 200;
               }
               for (int i = 0; i < size; i++) {
                 L1MarketPrice auction = list.get(i);
                 item = ItemTable.getInstance().getTemplate(auction.getItemId());
                 writeD(auction.getOrder());
                 writeC(item.getType2());
                 writeH(item.getGfxId());
                 writeC(auction.getIden());
                 writeD(auction.getCount());
                 writeC(1);


                 StringBuilder name = new StringBuilder();
                 switch (auction.getAttr()) {
                   case 1:
                     name.append("$6115");
                     break;
                   case 2:
                     name.append("$6116");
                     break;
                   case 3:
                     name.append("$6117");
                     break;
                   case 4:
                     name.append("$14361");
                     break;
                   case 5:
                     name.append("$14365");
                     break;

                   case 6:
                     name.append("$6118");
                     break;
                   case 7:
                     name.append("$6119");
                     break;
                   case 8:
                     name.append("$6120");
                     break;
                   case 9:
                     name.append("$14362");
                     break;
                   case 10:
                     name.append("$14366");
                     break;

                   case 11:
                     name.append("$6121");
                     break;
                   case 12:
                     name.append("$6122");
                     break;
                   case 13:
                     name.append("$6123");
                     break;
                   case 14:
                     name.append("$14363");
                     break;
                   case 15:
                     name.append("$14367");
                     break;

                   case 16:
                     name.append("$6124");
                     break;
                   case 17:
                     name.append("$6125");
                     break;
                   case 18:
                     name.append("$6126");
                     break;
                   case 19:
                     name.append("$14364");
                     break;
                   case 20:
                     name.append("$14368");
                     break;
                 }



                 if (auction.getEnchant() >= 0) {
                   name.append("+" + auction.getEnchant() + " ");
                 } else if (auction.getEnchant() < 0) {
                   name.append(String.valueOf(auction.getEnchant()) + " ");
                 }

                   if (auction.getIden() == 0) {
                       // 如果 auction 的標識符為 0，則寫入字符串：\fH祝福的 + item 名稱
                       writeS("\fH祝福的 " + item.getName()); // 축복받은 意為「祝福的」
                   } else if (auction.getIden() == 2) {
                       // 如果 auction 的標識符為 2，則寫入字符串：\f3詛咒的 + item 名稱
                       writeS("\f3詛咒的 " + item.getName()); // 저주받은 意為「詛咒的」
                   } else {
                       // 否則，寫入字符串：name + item 名稱
                       writeS(name + item.getName());
                   }

                 int type = item.getUseType();
                 if (type < 0) {
                   type = 0;
                 }
                 template = ItemTable.getInstance().getTemplate(item.getItemId());
                 if (template == null) {
                   writeC(0);
                 } else {

                     // 創建一個二進制輸出流實例
                     BinaryOutputStream os = new BinaryOutputStream();

                    // 向輸出流中寫入一個字節值 39
                     os.writeC(39);

                    // 檢查 auction 的 charName 是否不為空
                     if (auction.getCharName() != null) {
                         // 如果 charName 不為空，寫入字符串：\fY賣方 : + 競拍者名稱
                         os.writeS("\fY賣方 : " + auction.getCharName());
                     } else {
                         // 如果 charName 為空，寫入字符串：\fU賣家：管理員商店
                         os.writeS("\fU賣家：管理員商店");
                     }


                   if (auction.getPrice() > 0) {
                     StringBuffer max = null;
                     if (auction.getPrice() >= 1000) {
                       max = reverse(String.valueOf(auction.getPrice()));
                       max.insert(3, ",");
                     }
                     if (auction.getPrice() >= 1000000) {
                       max = reverse(String.valueOf(auction.getPrice()));
                       max.insert(6, ",");
                       max.insert(3, ",");
                     }
                     if (auction.getPrice() > 1000000000) {
                       max = reverse(String.valueOf(auction.getPrice()));
                       max.insert(9, ",");
                       max.insert(6, ",");
                       max.insert(3, ",");
                     }
                       // 將 max 的值反轉，並轉換為字符串
                       max = reverse(max.toString());

                        // 在反轉的 max 字符串前插入 "\aC"
                       max.insert(0, "\aC");

                        // 創建一個二進制輸出流實例
                       BinaryOutputStream os = new BinaryOutputStream();

                        // 向輸出流中寫入一個字節值 39
                       os.writeC(39);

                        // 向輸出流中寫入字符串 "\aG銷售收入 : " 和 max 的字符串值
                       os.writeS("\aG銷售收入 : " + max.toString());

                       os.writeC(39);

                        // 檢查 auction 的 count 是否大於 0
                       if (auction.getCount() > 0) {
                           // 如果 count 大於 0，寫入字符串 "\aH銷售數量 : " 和 count 的值
                           os.writeS("\aH銷售數量 : " + auction.getCount());
                       }

                        // 向輸出流中寫入 os 中的字節數量
                       writeC((os.getBytes()).length);

                       // 循環遍歷 os 中的每個字節，並寫入輸出流中
                       for (byte b : os.getBytes()) {
                           writeC(b);
                       }
                   }
                 }
               writeD(0);
               writeD(0);
               writeH(0);
             }
             catch (Exception e) {
               e.getStackTrace();
             }
           }

           private StringBuffer reverse(String s) {
             return (new StringBuffer(s)).reverse();
           }




           static class NoAscCompare
             implements Comparator<L1MarketPrice>
           {
             public int compare(L1MarketPrice arg0, L1MarketPrice arg1) {
               return (arg0.getPrice() < arg1.getPrice()) ? -1 : ((arg0.getPrice() > arg1.getPrice()) ? 1 : 0);
             }
           }


           public byte[] getContent() throws IOException {
             return getBytes();
           }
         }


