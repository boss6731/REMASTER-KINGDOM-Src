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
					MJNSHandler.print(ctx, "■ 封包攻擊（懷疑）■");
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

				// a 和 b 幾乎同時到達，但 a 稍稍提前。在這種情況下，必須先處理 a 再處理 b。
				// 2 線程 1 數據包處理形式？

				try {
					byte[] data = new byte[len];
					msg.readBytes(data, 0, len);
					data = cryptor.decrypt(data, len);

					int opcode = data[0] & 0xFF;
					int type = data[1] & 0xFF;

					// TODO 處理客戶端數據包
					if (Config.Synchronization.FindClientProtoCode) {
						if (opcode != 116 && opcode != 19) { // 角色狀態 & 增益狀態
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

				} catch (Exception e) {
					MJNSHandler.print(ctx, "例外信息");
					e.printStackTrace();
				}
	
	private void codePrint(int opcode, int type, byte[] data) throws Exception {
		System.out.println(Opcodes.getIns().getOpcodeName(opcode, "C_") + " -->[" + opcode + "] -- TYPE [" + type + "] -- LEN [" + data.length + "]\n" + MJHexHelper.toStringEx(data, data.length)); // 사용 처리
	}
	
	private boolean isBad(MJCryptor cryptor, int len){
		return (len < 2) || (len > MJNetSafeLoadManager.NS_PACKET_MAXSIZE && ++cryptor.overPending >= MJNetSafeLoadManager.NS_PACKET_MAXOVER_COUNT);
	}
}
