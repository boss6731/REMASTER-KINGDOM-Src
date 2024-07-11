 package l1j.server.server.model.shop;

 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.List;
 import l1j.server.Config;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1TaxCalculator;
 import l1j.server.server.templates.L1ShopItem;

 public class L1ShopBuyOrderList
 {
   private final L1Shop _shop;
   private final List<L1ShopBuyOrder> _list = new ArrayList<>();

   private final L1TaxCalculator _taxCalc;
   private int _totalWeight = 0;
   private int _totalPrice = 0;
   private int _totalPriceTaxIncluded = 0;
   private int bugok = 0;


   Calendar rightNow = Calendar.getInstance();
   int day = this.rightNow.get(5);
   int hour = this.rightNow.get(10);
   int min = this.rightNow.get(12);
   int year = this.rightNow.get(1);
   int month = this.rightNow.get(2) + 1;
   String totime = "[" + this.year + ":" + this.month + ":" + this.day + ":" + this.hour + ":" + this.min + "]";

   L1ShopBuyOrderList(L1Shop shop) {
     this._shop = shop;
     this._taxCalc = new L1TaxCalculator(shop.getNpcId());
   }


   public void add(int orderNumber, int count, L1PcInstance pc) {
     if (this._shop.getSellingItems().size() < orderNumber) {
       return;
     }


     if (this._shop.getSellingItems().size() <= 0 || this._shop.getSellingItems().size() <= orderNumber) {
       return;
     }
     L1ShopItem shopItem = this._shop.getSellingItems().get(orderNumber);

     int price = (int)(shopItem.getPrice() * Config.ServerRates.RateShopSellingPrice);

     for (int j = 0; j < count; j++) {
       if (price * j < 0) {
         return;
       }
     }
            // 計算總價格
       this._totalPrice += price * count;
            // 計算稅後總價格
       this._totalPriceTaxIncluded += this._taxCalc.layTax(price) * count;
            // 計算總重量
       this._totalWeight += shopItem.getItem().getWeight() * count * shopItem.getPackCount();
            // 獲取總價格
       long totalprice = this._totalPrice;

            // 檢查NPC ID為370041的情況
       if (this._shop.getNpcId() == 370041) {

           // 檢查購買數量是否在合理範圍內
           if (count <= 0 || count > 9999) {
               pc.sendPackets("1~9999個範圍內才能購買。");

               this.bugok = 1;

               return;
           }
       } else if (count <= 0 || count > 9999) {
           // 檢查購買數量是否在合理範圍內
           pc.sendPackets("1~9999個範圍內才能購買。");

           this.bugok = 1;

           return;
       }

        // 檢查總價格和單價是否為負數
       if (totalprice < 0L || price < 0) {

           this.bugok = 1;

           return;
       }
     if (shopItem.getItem().isStackable()) {
       this._list.add(new L1ShopBuyOrder(shopItem, count * shopItem.getPackCount(), orderNumber));

       return;
     }
     for (int i = 0; i < count * shopItem.getPackCount(); i++) {
       this._list.add(new L1ShopBuyOrder(shopItem, 1, orderNumber));
     }
   }

   public List<L1ShopBuyOrder> getList() {
     return this._list;
   }


   public int BugOk() {
     return this.bugok;
   }



   public int getTotalWeight() {
     return this._totalWeight;
   }

   public int getTotalPrice() {
     return this._totalPrice;
   }

   public int getTotalPriceTaxIncluded() {
     return this._totalPriceTaxIncluded;
   }

   L1TaxCalculator getTaxCalculator() {
     return this._taxCalc;
   }
 }


