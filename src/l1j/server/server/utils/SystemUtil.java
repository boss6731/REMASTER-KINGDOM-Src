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
package l1j.server.server.utils;

import java.util.Calendar;
import java.util.Date;

public class SystemUtil {
	/**
	 * 返回系統正在使用的堆大小（以兆字節為單位）。<br>
	 * 此值不包括棧的大小。
	 *
	 * @return 正在使用的堆大小
	 */
	public static long getUsedMemoryMB() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L / 1024L;
	}
	
	static private Date	date = new Date(0);
	static private String[] weekDay = {"日", "一", "二", "三", "四", "五", "六"};
	/**
	 * 返回對應時間的星期幾。
	 * @param time 時間
	 * @return 對應的星期幾
	 */
	static public String getYoil(long time) {
		date.setTime(time);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return weekDay[ c.get(Calendar.DAY_OF_WEEK)-1 ];
	}
}
