package l1j.server.server.serverpackets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class S_BannedMessageBox extends ServerBasePacket{
	public static S_BannedMessageBox do_banned(String message, String title){
		S_BannedMessageBox mbox = new S_BannedMessageBox();
		try {
			byte[] buff = message.getBytes("MS949");
			mbox.writeD(buff.length);
			mbox.writeByte(buff);
			mbox.writeC(0x00);
			
			buff = title.getBytes("MS949");
			mbox.writeD(buff.length);
			mbox.writeByte(buff);
			mbox.writeC(0x00);
			mbox.writeC(0x00);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mbox;
	}
	
	private S_BannedMessageBox(){
		writeC(0x03);
	}
	
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}
