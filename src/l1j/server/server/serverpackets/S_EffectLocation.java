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
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Location;
import l1j.server.server.types.Point;

public class S_EffectLocation extends ServerBasePacket {

	/**
	 * 構建在指定位置顯示效果的封包。
	 *
	 * @param pt - 包含顯示效果位置的 Point 物件
	 * @param gfxId - 顯示效果的 ID
	 */
	public S_EffectLocation(Point pt, int gfxId) {
		this(pt.getX(), pt.getY(), gfxId); // 調用使用 X 和 Y 坐標的構造函數
	}

	/**
	 * 構建在指定位置顯示效果的封包。
	 *
	 * @param loc - 包含顯示效果位置的 L1Location 物件
	 * @param gfxId - 顯示效果的 ID
	 */
	public S_EffectLocation(L1Location loc, int gfxId) {
		this(loc.getX(), loc.getY(), gfxId); // 調用使用 X 和 Y 坐標的構造函數
	}

	/**
	 * 構建在指定位置顯示效果的封包。
	 *
	 * @param x - 顯示效果位置的 X 坐標
	 * @param y - 顯示效果位置的 Y 坐標
	 * @param gfxId - 顯示效果的 ID
	 */
	public S_EffectLocation(int x, int y, int gfxId) {
		// 在這裡加入具體的封包構建邏輯
	}
}
	public S_EffectLocation(int x, int y, int gfxId) {
		super(16);
		writeC(Opcodes.S_EFFECT_LOC);
		writeH(x);
		writeH(y);
		writeH(gfxId);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


