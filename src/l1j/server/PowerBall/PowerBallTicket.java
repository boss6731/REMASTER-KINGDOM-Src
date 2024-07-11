/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.PowerBall;

import java.util.HashMap;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class PowerBallTicket extends L1Item {
	private static final long serialVersionUID = 1L;

	public PowerBallTicket() {
	}

	private boolean _stackable;

	@Override
	public boolean isStackable() {
		return _stackable;
	}

	public void set_stackable(boolean stackable) {
		_stackable = stackable;
	}

	@Override
	public int getMagicHitup() {
		return 0;
	}

	@Override
	public void setMagicHitup(int i) {
	}

	@Override
	public void setSpecialResistance(eKind kind, int value) {
	}

	@Override
	public int getSpecialResistance(eKind kind) {
		return 0;
	}

	@Override
	public void setSpecialResistanceMap(HashMap<Integer, Integer> itemResistance) {
	}

	@Override
	public HashMap<Integer, Integer> getSpecialResistanceMap() {
		return null;
	}

	@Override
	public void setSpecialPierce(eKind kind, int value) {
	}

	@Override
	public int getSpecialPierce(eKind kind) {
		return 0;
	}

	@Override
	public void setSpecialPierceMap(HashMap<Integer, Integer> itemPierce) {
	}

	@Override
	public HashMap<Integer, Integer> getSpecialPierceMap() {
		return null;
	}

	@Override
	public void equipmentItem(L1PcInstance pc, boolean isEquipped) {
	}

}
