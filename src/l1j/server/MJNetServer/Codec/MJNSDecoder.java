package l1j.server.MJNetServer.Codec;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import l1j.server.Config;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJNetServer.ClientManager.MJNSDenialAddress;
import l1j.server.MJNetServer.Codec.Cryptor.MJCryptor;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.MJHexHelper;

/**********************************
 * 
 * MJ Network Server System Decoder.
 * made by mjsoft, 2017.
 *  
 **********************************/
public class MJNSDecoder extends ByteToMessageDecoder{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		int readable = msg.readableBytes();
		if(readable < 2)
			return;
		
		MJCryptor cryptor = MJNSHandler.getCryptor(ctx);
		if(cryptor == null){
			ctx.close();
			return;
		}
		
		try{
			while(readable > 2){
				msg.markReaderIndex();
				int len = msg.readUnsignedShortLE() - 2;
				readable -= 2;
				if(isBad(cryptor, len)){
					MJNSHandler.print(ctx, "■ 패킷공격(의심) ■");
					String address = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
					MJNSDenialAddress.getInstance().insert_address(address, MJNSDenialAddress.REASON_SIZE_OVER);
					ctx.close();
					MJNSHandler.do_denials(address);
					return;
				}
				cryptor.overPending = 0;
				if(readable < len){
					msg.resetReaderIndex();
					break;
				}
				
				// a 랑 b가 거의 동시에 날라왔는데 간발의 차이로 a 가 먼저날라왔다. 이런경우 에이먼저 처리를 하고 비를 처리해야한다..
				// 2 스레드 1패킷 처리 형식??
				
				byte[] data = new byte[len];
				msg.readBytes(data, 0, len);
				data = cryptor.decrypt(data, len);
				
				int opcode = data[0] & 0xFF;
				int type = data[1] & 0xFF;
				// TODO 클라패킷  
				
				if (Config.Synchronization.FindClientProtoCode){
					if (opcode != 116 && opcode != 19){//케릭상태&버프
						codePrint(opcode, type, data);	
					}
				} 
				
				
				if (opcode > 0) {
					if (opcode == -81) {
						codePrint(opcode, type, data);
					}
					//codePrint(opcode, type, data);
				}
				out.add(data);
				readable -= len;
			}
		}catch(Exception e){
			MJNSHandler.print(ctx, "예외정보.");
			e.printStackTrace();
		}
	}
	
	private void codePrint(int opcode, int type, byte[] data) throws Exception {
		System.out.println(Opcodes.getIns().getOpcodeName(opcode, "C_") + " -->[" + opcode + "] -- TYPE [" + type + "] -- LEN [" + data.length + "]\n" + MJHexHelper.toStringEx(data, data.length)); // 사용 처리
	}
	
	private boolean isBad(MJCryptor cryptor, int len){
		return (len < 2) || (len > MJNetSafeLoadManager.NS_PACKET_MAXSIZE && ++cryptor.overPending >= MJNetSafeLoadManager.NS_PACKET_MAXOVER_COUNT);
	}
}
