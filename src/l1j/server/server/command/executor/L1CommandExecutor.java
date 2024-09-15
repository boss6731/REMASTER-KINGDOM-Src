/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.server.command.executor;

import l1j.server.server.server.model.Instance.L1PcInstance;

/**
 * 命令執行處理介面
 *
 * 命令處理類別，除了這個介面的方法之外<br>
 * 還必須實作 public static L1CommandExecutor getInstance()<br>
 * 通常會返回該類的實例，但根據需要，也可以返回快取的實例或其他類的實例。
 */
public interface L1CommandExecutor {
	/**
	 * 執行此命令。
	 *
	 * @param pc
	 *            執行者
	 * @param cmdName
	 *            執行的命令名稱
	 * @param arg
	 *            參數
	 */
	public void execute(L1PcInstance pc, String cmdName, String arg);
}
