package l1j.server.server.templates;

import l1j.server.server.model.Instance.L1ItemInstance;

public class L1GmShopList {
	private L1ItemInstance _item;

	private int _Count;

	private int _Price;

	public L1GmShopList(L1ItemInstance i, int c, int p) {
		_item = i;
		_Count = c;
		_Price = p;
	}

	public L1ItemInstance getItem() {
		return _item;
	}

	public void setCount(int i) {
		_Count = i;
	}

	public int getCount() {
		return _Count;
	}

	public int getPrice() {
		return _Price;
	}

}
