package l1j.server.server.templates;

public class L1Drop {
	int _mobId;
	int _itemId;
	int _min;
	int _max;
	int _chance;
	int _range;

	public L1Drop(int mobId, int itemId, int min, int max, int chance, int range) {
		_mobId = mobId;
		_itemId = itemId;
		_min = min;
		_max = max;
		_chance = chance;
		_range = range;
	}

	public int getChance() {
		return _chance;
	}

	public int getItemid() {
		return _itemId;
	}

	public int getMax() {
		return _max;
	}

	public int getMin() {
		return _min;
	}

	public int getMobid() {
		return _mobId;
	}
	
	public int getRange() {
		return _range;
	}

}
