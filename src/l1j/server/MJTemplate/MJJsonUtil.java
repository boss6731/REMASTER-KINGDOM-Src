package l1j.server.MJTemplate;

import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import l1j.server.MJTemplate.MJFiles.WriteType;

public class MJJsonUtil {


	/**
	 * 從 JSON 格式文件中讀取對象。
	 * 僅限 UTF-8。
	 * @param path 文件的路徑
	 * @param classOf 要轉換的對象類型 Class<T>
	 * @return 轉換後的對象 classOf
	 **/
	public static <T> T fromFile(String path, Class<T> classOf) {
		return fromFile(path, classOf, MJEncoding.UTF8);
	}

	/**
	 * 從 JSON 格式文件中讀取對象。
	 * @param path 文件的路徑
	 * @param classOf 要轉換的對象類型 Class<T>
	 * @param MJEncoding 文件的編碼
	 * @return 轉換後的對象 classOf
	 **/
	public static <T> T fromFile(String path, Class<T> classOf, Charset MJEncoding) {
		String text = MJFiles.readAllText(path, MJEncoding);
		return MJString.isNullOrEmpty(text) ? null : fromJson(text, classOf);
	}




	/**
	 * 從 JSON 格式字符串中讀取對象。
	 * @param json JSON 格式字符串
	 * @param classOf 要轉換的對象類型 Class<T>
	 * @return 轉換後的對象 classOf
	 **/
	public static <T> T fromJson(String json, Class<T> classOf) {
		Gson gson = new Gson();
		return gson.fromJson(json, classOf);
	}

	/**
	 * 將對象轉換為 JSON 並寫入文件。
	 * @param path 保存的文件路徑
	 * @param object 要轉換為 JSON 字符串的對象
	 * @param MJEncoding 保存編碼
	 * @param prettyPrinting 設置為 true 時，應用換行和縮進；設置為 false 時，不應用換行和縮進，並進行優化
	 **/
	public static void toFile(String path, Object object, Charset MJEncoding, boolean prettyPrinting) {
		String json = toJson(object, prettyPrinting);
		MJFiles.writeAllText(path, json, MJEncoding, WriteType.OVERWRITE);
	}




	/**
	 * 將對象轉換為 JSON 並寫入文件。
	 * 使用 UTF-8 編碼。
	 * @param path 保存的文件路徑
	 * @param object 要轉換為 JSON 字符串的對象
	 * @param prettyPrinting 設置為 true 時，應用換行和縮進；設置為 false 時，不應用換行和縮進，並進行優化
	 **/
	public static void toFile(String path, Object object, boolean prettyPrinting) {
		toFile(path, object, MJEncoding.UTF8, prettyPrinting);
	}

	/**
	 * 將對象轉換為 JSON 格式字符串。
	 * @param object 要轉換為 JSON 字符串的對象
	 * @param prettyPrinting 設置為 true 時，應用換行和縮進；設置為 false 時，不應用換行和縮進，並進行優化
	 * @return JSON 格式字符串
	 **/
	public static String toJson(Object object, boolean prettyPrinting) {
		Gson gson = prettyPrinting ?
				new GsonBuilder().setPrettyPrinting().create() : new Gson();
		return gson.toJson(object);
	}
	
	
	
	public static abstract class MJToJsonable{
		public String toJson(boolean prettyPrinting){
			return MJJsonUtil.toJson(this, prettyPrinting);
		}
	}
}
