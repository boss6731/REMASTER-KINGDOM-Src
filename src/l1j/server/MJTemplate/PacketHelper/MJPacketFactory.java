package l1j.server.MJTemplate.PacketHelper;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.Builder.MJServerPacketBuilder;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJPacketFactory {
	public static final int MSPF_IDX_OFFTIME 	= 6;
	
	private static ConcurrentHashMap<Integer, S_BuilderPacket> _packs = new ConcurrentHashMap<Integer, S_BuilderPacket>(32);
	
	public static S_BuilderPacket create(int idx) throws Exception{
		S_BuilderPacket pck = _packs.get(idx);
		if(pck != null)
			return pck;
		
		MJServerPacketBuilder builder;
		switch(idx){
		case MSPF_IDX_OFFTIME:
			builder	= new MJServerPacketBuilder(4)
			.addC(Opcodes.S_EVENT)
			.addC(0x48);
			break;
		default:
			throw new NullPointerException(String.format("資料包類型 %d 無效", idx));
		}
		
		pck = builder.build();
		builder.dispose();
		builder = null;
		_packs.put(idx, pck);
		return pck;
	}
	
	@SuppressWarnings("resource")
	public static S_BuilderPacket createTime(long l){
		S_BuilderPacket 				pck 	= null;
		MJServerPacketBuilder 	builder = null;
		try {
			builder = new MJServerPacketBuilder(8)
			.addC(Opcodes.S_EVENT)
			.addC(0x47)
			.addH((short)l);
			pck = builder.build();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(builder != null){
				builder.dispose();
				builder = null;
			}
		}
		return pck;
	}
	
	public static ServerBasePacket[] create_duplicate_message_packets(String message){
		return new ServerBasePacket[]{
				new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message),
				new S_SystemMessage(message),
			};
	}
}
