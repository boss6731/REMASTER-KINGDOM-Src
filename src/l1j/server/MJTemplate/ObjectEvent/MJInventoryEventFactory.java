package l1j.server.MJTemplate.ObjectEvent;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJInventoryEventFactory {
	MJInventoryEventFactory(){	
	}
	
	public MJAttrKey<MJObjectEventComposite<MJInventoryItemChangedArgs>> inventoryItemChangedKey(){
		return inventoryItemChangedKey;
	}
	
	public void fireInventoryItemChanged(L1PcInstance pc, L1ItemInstance item){
		pc.eventHandler().fire(inventoryItemChangedKey(), new MJInventoryItemChangedArgs(pc, item));		
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJInventoryItemChangedArgs>> inventoryItemChangedKey = MJAttrKey.newInstance("mj-inventory-item-changed");
	public static class MJInventoryItemChangedArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		public final L1ItemInstance item;
		private MJInventoryItemChangedArgs(L1PcInstance pc, L1ItemInstance item){
			super();
			this.pc = pc;
			this.item = item;
		}
	}
	
	
	public MJAttrKey<MJObjectEventComposite<MJInventoryItemUsedArgs>> inventoryItemUsedKey(){
		return inventoryItemUsedKey;
	}

	public void fireInventoryItemUsed(L1PcInstance pc, L1ItemInstance item){
		pc.eventHandler().fire(inventoryItemUsedKey(), new MJInventoryItemUsedArgs(pc, item));		
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJInventoryItemUsedArgs>> inventoryItemUsedKey = MJAttrKey.newInstance("mj-inventory-item-used");
	public static class MJInventoryItemUsedArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		public final L1ItemInstance item;
		private MJInventoryItemUsedArgs(L1PcInstance pc, L1ItemInstance item){
			super();
			this.pc = pc;
			this.item = item;
		}
	}
	
	
	public MJAttrKey<MJObjectEventComposite<MJInventoryItemClickedArgs>> inventoryItemClickedKey(){
		return inventoryItemClickedKey;
	}

	public void fireInventoryItemClicked(L1PcInstance pc, L1ItemInstance item){
		pc.eventHandler().fire(inventoryItemClickedKey(), new MJInventoryItemClickedArgs(pc, item));		
	}
	
	private static final MJAttrKey<MJObjectEventComposite<MJInventoryItemClickedArgs>> inventoryItemClickedKey = MJAttrKey.newInstance("mj-inventory-item-clicked");
	public static class  MJInventoryItemClickedArgs extends MJObjectEventArgs{
		public final L1PcInstance pc;
		public final L1ItemInstance item;
		private MJInventoryItemClickedArgs(L1PcInstance pc, L1ItemInstance item){
			super();
			this.pc = pc;
			this.item = item;
		}
	}
}
