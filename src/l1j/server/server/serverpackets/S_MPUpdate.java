 package l1j.server.server.serverpackets;



 // 定義 S_MPUpdate 類，繼承自 ServerBasePacket
 public class S_MPUpdate extends ServerBasePacket {

     // 構造函數，接受當前 MP 和最大 MP 作為參數
     public S_MPUpdate(int currentmp, int maxmp) {
         // 寫入一個字節值 254 作為封包的初始值
         writeC(254);

         // 判斷 currentmp 的值範圍並寫入適當的值
         if (currentmp < 1) {
             writeH(1); // 如果 currentmp 小於 1，寫入 1
         } else if (currentmp > 32767) {
             writeH(32767); // 如果 currentmp 大於 32767，寫入 32767
         } else {
             writeH(currentmp); // 否則寫入 currentmp 的值
         }

         // 判斷 maxmp 的值範圍並寫入適當的值
         if (maxmp < 1) {
             writeH(1); // 如果 maxmp 小於 1，寫入 1
         } else if (maxmp > 32767) {
             writeH(32767); // 如果 maxmp 大於 32767，寫入 32767
         } else {
             writeH(maxmp); // 否則寫入 maxmp 的值
         }
     }

     // 獲取封包內容的字節數組
     public byte[] getContent() {
         return getBytes();
     }
 }


