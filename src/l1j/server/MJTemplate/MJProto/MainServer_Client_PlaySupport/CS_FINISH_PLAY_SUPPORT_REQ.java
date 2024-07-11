package l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_FINISH_PLAY_SUPPORT_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static CS_FINISH_PLAY_SUPPORT_REQ newInstance(){
		return new CS_FINISH_PLAY_SUPPORT_REQ();
	}
	
	private CS_FINISH_PLAY_SUPPORT_REQ.eReason _reason;
	
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
		private CS_FINISH_PLAY_SUPPORT_REQ(){
	}
	
		
	public CS_FINISH_PLAY_SUPPORT_REQ.eReason get_reason(){
		return _reason;
	}
	public void set_reason(CS_FINISH_PLAY_SUPPORT_REQ.eReason val){
		_bit |= 0x1;
		_reason = val;
	}
	
	
	public boolean has_reason(){
		return (_bit & 0x1) == 0x1;
	}
	
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_reason()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _reason.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_reason()){
			output.writeEnum(1, _reason.toInt());
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x00000008:{
					set_reason(CS_FINISH_PLAY_SUPPORT_REQ.eReason.fromInt(input.readEnum()));
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			L1PcInstance pc = clnt.getActiveChar();
			if(pc == null || !pc.get_is_client_auto())
				return this;
			
			pc.do_finish_client_auto_ack();
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new CS_FINISH_PLAY_SUPPORT_REQ();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

    public enum eReason {
        ETC(0),                          // 其他
        DEAD(1),                         // 死亡
        PEACE(2),                        // 和平
        COMEBACK(3),                     // 回來
        HALT(4),                         // 停止
        RESTART(5),                      // 重啟
        BUY_FAIL(6),                     // 購買失敗
        MOVE_FAIL(7),                    // 移動失敗
        MAP_TIME(8),                     // 地圖時間
        NOT_ENOUGH_REST_GAUGE(9),        // 休息計量不足
        END_TOTAL_PATH(10);              // 總路徑結束

        private int value;

        // 枚舉的構造函數
        eReason(int val) {
            value = val;
        }

        // 返回枚舉的整數值
        public int toInt() {
            return value;
        }

        // 比較兩個枚舉是否相等
        public boolean equals(eReason v) {
            return value == v.value;
        }

        // 根據整數值返回對應的枚舉
        public static eReason fromInt(int i) {
            switch (i) {
                case 0:
                    return ETC;
                case 1:
                    return DEAD;
                case 2:
                    return PEACE;
                case 3:
                    return COMEBACK;
                case 4:
                    return HALT;
                case 5:
                    return RESTART;
                case 6:
                    return BUY_FAIL;
                case 7:
                    return MOVE_FAIL;
                case 8:
                    return MAP_TIME;
                case 9:
                    return NOT_ENOUGH_REST_GAUGE;
                case 10:
                    return END_TOTAL_PATH;
                default:
                    throw new IllegalArgumentException(String.format("無效的 eReason，%d", i));
            }
        }
    }
