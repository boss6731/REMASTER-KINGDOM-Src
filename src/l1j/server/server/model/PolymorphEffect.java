 package l1j.server.server.model;

 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;



 class PolymorphEffect
         implements L1ArmorSetEffect
 {
     private int _gfxId;

     public PolymorphEffect(int gfxId) {
         this._gfxId = gfxId;
     }


     public void giveEffect(L1PcInstance pc) {
         if (this._gfxId == 6080 || this._gfxId == 6094) {
             if (pc.get_sex() == 0) {
                 this._gfxId = 6094;
             } else {
                 this._gfxId = 6080;
             }
             if (!isRemainderOfCharge(pc)) {
                 return;
             }
         }
         L1PolyMorph.doPoly((L1Character)pc, this._gfxId, -1, 1, false, false);
     }


     public void cancelEffect(L1PcInstance pc) {
         if (this._gfxId == 6080 &&
                 pc.get_sex() == 0) {
             this._gfxId = 6094;
         }

         if (pc.getCurrentSpriteId() != this._gfxId) {
             return;
         }
         L1PolyMorph.undoPoly((L1Character)pc);
     }

     private boolean isRemainderOfCharge(L1PcInstance pc) {
         boolean isRemainderOfCharge = false;
         if (pc.getInventory().checkItem(20383, 1)) {
             L1ItemInstance item = pc.getInventory().findItemId(20383);
             if (item != null &&
                     item.getChargeCount() != 0) {
                 isRemainderOfCharge = true;
             }
         }

         return isRemainderOfCharge;
     }
 }


