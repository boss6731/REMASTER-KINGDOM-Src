package l1j.server.MJRaidSystem.Compensator;

import java.util.Random;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;

public class ItemCompensator implements CompensatorElement{
	protected static Random	_rnd = new Random(System.nanoTime());
	
	private int	_itemid;
	private int	_count;
	private int _rate;
	
	public ItemCompensator(int itemid, int count, int rate){
		_itemid = itemid;
		_count	= count;
		_rate	= rate;
	}
	
	@Override
	public void compensate(Object obj) {
		if(_itemid < 0 || _count <= 0)
			return;
		
		if(_rnd.nextInt(100) + 1 > _rate)
			return;
		
		try{
			int[] arr 	= (int[])obj;
			int mid		= arr[0];
			int x		= arr[1];
			int y		= arr[2];
			int xi		= 1;
			int yi		= 1;
			if(_rnd.nextBoolean())
				xi = -1;
			if(_rnd.nextBoolean())
				yi = -1;
			
			L1Inventory inv = L1World.getInstance().getInventory(x + (_rnd.nextInt(3) * xi), y + (_rnd.nextInt(3) * yi), (short)mid);
			L1ItemInstance item = ItemTable.getInstance().createItem(_itemid);
			if (item == null) {
				System.out.println(String.format("突襲 獎勵(1) : 找不到 %d 項物品。", _itemid));
				return;
			}
			if (item.isStackable()) {
				for (int i = 0; i < _count; i++) {
					item = ItemTable.getInstance().createItem(_itemid);
					if(item == null){
						System.out.println(String.format("突襲 獎勵(2)：找不到 %d 項物品。", _itemid));
						continue;
					}
					item.setCount(1);
					inv.storeTradeItem(item);
				}
			} else {
				item.setCount(_count);
				inv.storeTradeItem(item);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
