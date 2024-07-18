package l1j.server.server.serverpackets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import l1j.server.server.Opcodes;

public class S_Emblem extends ServerBasePacket {

	private static final String S_EMBLEM = "[S] S_Emblem";
	private static final String PATH = "emblem/";
	
/*	public S_Emblem(int emblemId) { 
		BufferedInputStream bis = null;
		try {
			String emblem_file = String.valueOf(emblemId);
			File file = new File("emblem/" + emblem_file);
			if (file.exists()) {
				int data = 0;
				bis = new BufferedInputStream(new FileInputStream(file));
				
				
				writeC(Opcodes.S_EMBLEM);
//				writeD(0x00);
				writeC(0x01);
				writeD(0x64);
				writeD(emblemId);
			
				while ((data = bis.read()) != -1) {
					writeP(data);
				}
			}
		}
		catch (Exception e) {}
		finally {
			if (bis != null) {
				try {
					bis.close();
				}
				catch (IOException ignore) {
					// ignore
				}
			}
		}
	}*/
	


	/*public S_Emblem(int emblemId, int real_emblemId) {
		BufferedInputStream bis = null;
		try {
			String emblem_file = String.valueOf(real_emblemId);
			File file = new File("emblem/" + emblem_file);
			if (file.exists()) {
				int data = 0;
				bis = new BufferedInputStream(new FileInputStream(file));
				writeC(Opcodes.S_EMBLEM);
				writeC(0x01);
				writeD(0x64);
				
//				writeD(0x00);
				writeC(0x01);
				writeC(0x64);
				writeC(0x64);
				writeD(emblemId);
				while ((data = bis.read()) != -1) {
					writeP(data);
				}
			}
		}
		catch (Exception e) {}
		finally {
			if (bis != null) {
				try {
					bis.close();
				}
				catch (IOException ignore) {
					// ignore
				}
			}
		}
	}*/
	

	
	public S_Emblem(int emblemId) {
		BufferedInputStream bis = null;
		FileInputStream	fis = null;
		File readFile = null;
		try {
			String emblem_file = String.valueOf(emblemId);
			readFile = new File(PATH + emblem_file);
			if(readFile.exists()){
				byte[] buff = null;
				fis = new FileInputStream(readFile);
				bis = new BufferedInputStream(fis);
				writeC(Opcodes.S_EMBLEM);
				writeC(0x01);
				writeD(0x0);
//				writeD(id);
				writeD(emblemId);
				
				buff = new byte[(int)fis.getChannel().size()];
				bis.read(buff, 0, buff.length);
				writeByte(buff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bis != null){
				try {
					bis.close();
				} catch (IOException ignore) {}
				bis = null;
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException ignore) {}
				fis = null;
			}
			readFile = null;
		}
	}
	
/*	public S_Emblem(int emblemId, int id) {
		BufferedInputStream bis = null;
		FileInputStream	fis = null;
		File readFile = null;
		try {
			String emblem_file = String.valueOf(emblemId);
			readFile = new File(PATH + emblem_file);
			if(readFile.exists()){
				byte[] buff = null;
				fis = new FileInputStream(readFile);
				bis = new BufferedInputStream(fis);
				writeC(Opcodes.S_EMBLEM);
				writeC(0x01);
				writeD(0x64);
//				writeD(id);
				writeD(emblemId);
				buff = new byte[(int)fis.getChannel().size()];
				bis.read(buff, 0, buff.length);
				writeByte(buff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bis != null){
				try {
					bis.close();
				} catch (IOException ignore) {}
				bis = null;
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException ignore) {}
				fis = null;
			}
			readFile = null;
		}
	}*/
/*	public S_Emblem(int emblemId, int id) {
		BufferedInputStream bis = null;
		FileInputStream	fis = null;
		File readFile = null;
		try {
			String emblem_file = String.valueOf(emblemId);
			readFile = new File(PATH + emblem_file);
			if(readFile.exists()){
				byte[] buff = null;
				fis = new FileInputStream(readFile);
				bis = new BufferedInputStream(fis);
				writeC(Opcodes.S_EMBLEM);
				writeD(id);
				writeD(emblemId);
				buff = new byte[(int)fis.getChannel().size()];
				bis.read(buff, 0, buff.length);
				writeByte(buff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bis != null){
				try {
					bis.close();
				} catch (IOException ignore) {}
				bis = null;
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException ignore) {}
				fis = null;
			}
			readFile = null;
		}
	}
*/

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_EMBLEM;
	}
}


