package l1j.server.MJNetServer.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import l1j.server.Config;
import l1j.server.MJNetServer.Codec.Cryptor.MJCryptor;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.MJHexHelper;

/**********************************
 * 
 * MJ Network Server System Encoder.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJNSEncoder extends MessageToByteEncoder<byte[]>{
	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
		MJCryptor cryptor = MJNSHandler.getCryptor(ctx);
		if(cryptor == null){
			ctx.close();
			return;
		}
		
		//TODO 서버패킷
		int opcode = msg[0] & 0xFF;
		int type = msg[1] & 0xFF;
		
		if (Config.Synchronization.FindServerProtoCode){
			if (opcode != 116 && opcode != 19){//케릭상태&버프
				codePrint(opcode, type, msg);	
			}
		} else if (Config.Synchronization.FindServerProtoCodeFull){
			codePrint(opcode, type, msg);
		}
		
		
		if (opcode == -180 && (type == 252 ||type == 239)) {
			codePrint(opcode, type, msg);
		}
		if (opcode == -180 && type == 142) {
			codePrint(opcode, type, msg);
		}
		if (opcode == -180) {
			codePrint(opcode, type, msg);
		}
		//codePrint(opcode, type, msg);
		cryptor.encrypt(msg, out);
	}
	private void codePrint(int opcode, int type, byte[] data) throws Exception {
		System.out.println(Opcodes.getIns().getOpcodeName(opcode, "S_") + " -->[" + opcode + "] -- TYPE [" + type + "] -- LEN [" + data.length + "]\n" + MJHexHelper.toString(data, data.length)); // 사용 처리
	}
}
