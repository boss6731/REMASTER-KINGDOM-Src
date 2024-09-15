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

import java.io.File;

import l1j.server.MJTemplate.MJString;

/**
 * <b>幫助處理路徑相關工作的輔助類</b>
 * @author mjsoft
 **/
public class FileUtil {
	public static String getExtension(File file) {
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');
		if (index != -1) {
			return fileName.substring(index + 1, fileName.length());
		}
		return "";
	}

	public static String getNameWithoutExtension(File file) {
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');
		if (index != -1) {
			return fileName.substring(0, index);
		}
		return "";
	}
	
	public static final String CONFIG = "./conf/";




	/**
	 * 在路徑字符串之間插入 {@link MJString#DirectorySeparatorChar} 並合併以創建路徑。
	 * @param pathes 路徑字符串
	 * @return 返回合併的路徑字符串。
	 **/
	public static String combine(String...pathes){
		StringBuilder sb = new StringBuilder(256);
		int length = pathes.length - 1;
		for(int i=0; i<length; ++i){
			String path = pathes[i];
			char c = path.charAt(path.length() - 1);
			sb.append(path);
			if(!MJString.isDirectorySeparatorChar(c)){
				sb.append(MJString.DirectorySeparatorChar);
			}
		}		
		sb.append(pathes[length]);
		return sb.toString();
	}




	/**
	 * 從路徑字符串中查找並返回擴展名。
	 * @param path 路徑字符串
	 * @return 找到的擴展名（去掉 . 符號）。
	 **/
	public static String getExtension(String path){
		if(MJString.isNullOrEmpty(path))
			return MJString.EmptyString;

		int indexof = path.lastIndexOf('.');
		return indexof == -1 ? MJString.EmptyString : path.substring(indexof + 1, path.length());
	}




	/**
	 * 從路徑字符串中查找並返回包含擴展名的文件名。
	 * @param path 路徑字符串
	 * @return 包含擴展名的文件名
	 **/
	public static String getFileName(String path){
		if(MJString.isNullOrEmpty(path))
			return MJString.EmptyString;
		
		return new File(path).getName();
	}




	/**
	 * 從路徑字符串中查找並返回不包含擴展名的文件名。
	 * @param path 路徑字符串
	 * @return 不包含擴展名的文件名
	 **/
	public static String getFileNameWithoutExtension(String path){
		String fileName = getFileName(path);
		if(MJString.isNullOrEmpty(fileName))
			return MJString.EmptyString;
		
		int indexof = fileName.lastIndexOf('.');
		return indexof == -1 ? fileName : fileName.substring(0, indexof);
	}
}
