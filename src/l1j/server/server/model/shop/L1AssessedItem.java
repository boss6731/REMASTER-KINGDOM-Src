 package l1j.server.server.model.shop;


















 public class L1AssessedItem
 {
   private final int _targetId;
   private final int _assessedPrice;

   L1AssessedItem(int targetId, int assessedPrice) {
     this._targetId = targetId;
     this._assessedPrice = assessedPrice;
   }

   public int getTargetId() {
     return this._targetId;
   }

   public int getAssessedPrice() {
     return this._assessedPrice;
   }
 }


