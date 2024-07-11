package l1j.server.MJTemplate;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class MJFiles {


	/**
	 * 如果目錄不存在，則創建該目錄。
	 * @param path 路徑字符串
	 **/
	public static void createDirectory(String path) {
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
	}




	/**
	 * 返回目錄列表。
	 * @param path 路徑字符串
	 * @return 返回目錄名稱數組。如果沒有，則返回空字符串數組。
	 **/
	public static String[] getDirectoriesItems(String path) {
		File f = new File(path);
		return !f.exists() || !f.isDirectory() ? new String[]{} : f.list();
	}




	/**
	 * 讀取文件數據（同步方式）。
	 * 建議在簡單的文件讀取中使用（200MB以內）。
	 * 此方法經過調優，適合小型文件，因此使用了堆緩衝區。
	 * 可能會在大文件中發生異常。
	 * 對於大容量文件，請使用 java.nio.file.Files。
	 * @param path 路徑字符串
	 * @return 讀取的字節數組
	 **/
	public static byte[] readAllBytes(String path) {
		byte[] buff = null;
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}

		int length = (int) f.length();
		try (RandomAccessFile raf = new RandomAccessFile(path, "r");
			 FileChannel channel = raf.getChannel()) {

			ByteBuffer buffer = ByteBuffer.allocate(length);
			buffer.clear();
			raf.seek(0);
			channel.read(buffer);
			buff = buffer.array();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buff;
	}




	/**
	 * 讀取文件數據為字符串（UTF-8）（同步方式）。
	 * 建議在簡單的文件讀取中使用（200MB以內）。
	 * 此方法經過調優，適合小型文件，因此使用了堆緩衝區。
	 * 可能會在大文件中發生異常。
	 * 對於大容量文件，請使用 java.nio.file.Files。
	 * @param path 路徑字符串
	 * @return 讀取的字符串
	 **/
	public static String readAllText(String path) {
		return readAllText(path, MJEncoding.UTF8);
	}

	/**
	 * 讀取文件數據為字符串（同步方式）。
	 * 建議在簡單的文件讀取中使用（200MB以內）。
	 * 此方法經過調優，適合小型文件，因此使用了堆緩衝區。
	 * 可能會在大文件中發生異常。
	 * 對於大容量文件，請使用 java.nio.file.Files。
	 * @param path 路徑字符串
	 * @param MJEncoding 文件編碼
	 * @return 讀取的字符串
	 **/
	public static String readAllText(String path, Charset MJEncoding) {
		byte[] buff = readAllBytes(path);
		if (buff == null) {
			return MJString.EmptyString;
		}

		return new String(buff, MJEncoding);
	}




	/**
	 * {@link WriteType#OVERWRITE}
	 * {@link WriteType#APPEND}
	 **/
	public enum WriteType {
		/**
		 * 從頭開始寫文件。
		 **/
		OVERWRITE,

		/**
		 * 追加寫入文件。
		 **/
		APPEND
	}

	/**
	 * 寫入數據到文件（同步方式）。
	 * 建議在簡單的文件寫入中使用（200MB以內）。
	 * 此方法經過調優，適合小型文件。
	 * 可能會在大文件中發生異常。
	 * 對於大容量文件，請使用 java.nio.file.Files。
	 * @param path 路徑字符串
	 * @param buff 要寫入文件的緩衝區
	 * @param writeType {@link WriteType}
	 **/
	public static void writeAllBytes(String path, byte[] buff, WriteType writeType) {
		ByteBuffer buffer = ByteBuffer.wrap(buff);
		writeAllBytes(path, buffer, writeType);
	}




	/**
	 * 寫入數據到文件（同步方式）。
	 * 建議在簡單的文件寫入中使用（200MB以內）。
	 * 此方法經過調優，適合小型文件。
	 * 可能會在大文件中發生異常。
	 * 對於大容量文件，請使用 java.nio.file.Files。
	 * ByteBuffer 重用用途。
	 * @param path 路徑字符串
	 * @param buffer 要寫入文件的緩衝區
	 * @param writeType {@link WriteType}
	 **/
	public static void writeAllBytes(String path, ByteBuffer buffer, WriteType writeType) {
		try (RandomAccessFile raf = new RandomAccessFile(path, "rw");
			 FileChannel channel = raf.getChannel()) {

			long rafLength = raf.length();
			if (rafLength > 0) {
				if (writeType == WriteType.OVERWRITE) {
					raf.seek(0);
					raf.setLength(0);
				} else {
					raf.seek(rafLength);
				}
			}
			buffer.flip();
			channel.write(buffer);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 寫入文本到文件（同步方式）。
	 * 建議在簡單的文件寫入中使用（200MB以內）。
	 * 此方法經過調優，適合小型文件。
	 * 可能會在大文件中發生異常。
	 * 對於大容量文件，請使用 java.nio.file.Files。
	 * @param path 路徑字符串
	 * @param data 要寫入文件的文本
	 * @param MJEncoding 存儲編碼
	 * @param writeType {@link WriteType}
	 **/
	public static void writeAllText(String path, String data, Charset MJEncoding, WriteType writeType) {
		byte[] buff = data.getBytes(MJEncoding);
		writeAllBytes(path, buff, writeType);
	}




	/**
	 * 寫入文本到文件（同步方式）。
	 * 使用 UTF-8 編碼。
	 * 建議在簡單的文件寫入中使用（200MB以內）。
	 * 此方法經過調優，適合小型文件。
	 * 可能會在大文件中發生異常。
	 * 對於大容量文件，請使用 java.nio.file.Files。
	 * @param path 路徑字符串
	 * @param data 要寫入文件的文本
	 * @param writeType {@link WriteType}
	 **/
	public static void writeAllText(String path, String data, WriteType writeType) {
		writeAllText(path, data, MJEncoding.UTF8, writeType);
	}
