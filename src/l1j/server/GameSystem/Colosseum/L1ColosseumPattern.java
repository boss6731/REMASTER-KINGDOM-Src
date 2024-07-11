/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.GameSystem.Colosseum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class L1ColosseumPattern {
	private boolean _isFrozen = false;
	private Map<Integer, ArrayList<L1ColosseumSpawn>> _groups = new HashMap<Integer, ArrayList<L1ColosseumSpawn>>();

	public void addSpawn(int groupNumber, L1ColosseumSpawn spawn) {
		if (_isFrozen) {
			return;
		}

		ArrayList<L1ColosseumSpawn> spawnList = _groups.get(groupNumber);
		if (spawnList == null) {
			spawnList = new ArrayList<L1ColosseumSpawn>();
			_groups.put(groupNumber, spawnList);
		}

		spawnList.add(spawn);
	}

	public void freeze() {
		if (_isFrozen) {
			return;
		}

		
		for (ArrayList<L1ColosseumSpawn> spawnList : _groups.values()) {
			Collections.sort(spawnList);
		}

		_isFrozen = true;
	}

	public boolean isFrozen() {
		return _isFrozen;
	}

	public ArrayList<L1ColosseumSpawn> getSpawnList(int groupNumber) {
		if (!_isFrozen) {
			return null;
		}

		return _groups.get(groupNumber);
	}
}
