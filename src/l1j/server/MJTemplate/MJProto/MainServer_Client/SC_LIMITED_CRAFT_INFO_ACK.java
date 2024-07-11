package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.CraftList.CraftItemInfo;
import l1j.server.CraftList.CraftListLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin.SC_CRAFT_LIST_ALL_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin.SC_CRAFT_LIST_ALL_ACK.Craft;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_LIMITED_CRAFT_INFO_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_LIMITED_CRAFT_INFO_ACK newInstance() {
		return new SC_LIMITED_CRAFT_INFO_ACK();
	}

	public static void send(L1PcInstance pc, int craft_id) {
		SC_LIMITED_CRAFT_INFO_ACK noti = SC_LIMITED_CRAFT_INFO_ACK.newInstance();
		CraftIdInfo cii = CraftIdInfo.newInstance(craft_id);

		/**
		 * 성공여부 구분 처리
		 */
		eCraftIdInfoReqResultType result = eCraftIdInfoReqResultType.RP_SUCCESS;

		// craft info check.
		CraftCommonBin ccb = (CraftCommonBin) CraftCommonBin._PROTO_MESSAGE;
		if (ccb == null)
			return;

		SC_CRAFT_LIST_ALL_ACK sclaa = ccb.getCraftListAllAck(craft_id);

		if (sclaa == null)
			return;

		Craft craft = sclaa.get_craft(craft_id);

		/*
		 * if (craft_id == 3870) {//이렇게 하면 결과템 커스텀가능..
		 * pc.sendPackets("해당 제작은 서비스가 일시적으로 중단되었습니다."); result =
		 * eCraftIdInfoReqResultType.RP_ERROR_CRAFT_ID; return; }
		 */

		if (craft == null) {
			result = eCraftIdInfoReqResultType.RP_ERROR_CRAFT_ID;
		}
		noti.set_eResult(result);
		noti.set_craft_id_info(cii);

		pc.sendPackets(noti, MJEProtoMessages.SC_LIMITED_CRAFT_INFO_ACK, true);
	}

	public static ProtoOutputStream doMakeCreateProto(SC_LIMITED_CRAFT_INFO_ACK ack) {
		ProtoOutputStream stream = ack.writeTo(MJEProtoMessages.SC_LIMITED_CRAFT_INFO_ACK);
		stream.createProtoBytes();
		return stream;
	}

	private SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType _eResult;
	private SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo _craft_id_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_LIMITED_CRAFT_INFO_ACK() {
	}

	public SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType get_eResult() {
		return _eResult;
	}

	public void set_eResult(SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType val) {
		_bit |= 0x1;
		_eResult = val;
	}

	public boolean has_eResult() {
		return (_bit & 0x1) == 0x1;
	}

	public SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo get_craft_id_info() {
		return _craft_id_info;
	}

	public void set_craft_id_info(SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo val) {
		_bit |= 0x2;
		_craft_id_info = val;
	}

	public boolean has_craft_id_info() {
		return (_bit & 0x2) == 0x2;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_eResult()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _eResult.toInt());
		}
		if (has_craft_id_info()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _craft_id_info);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_eResult()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_eResult()) {
			output.writeEnum(1, _eResult.toInt());
		}
		if (has_craft_id_info()) {
			output.writeMessage(2, _craft_id_info);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				case 0x00000008: {
					set_eResult(SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012: {
					set_craft_id_info((SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo) input
							.readMessage(SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo.newInstance()));
					break;
				}
				default: {
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_LIMITED_CRAFT_INFO_ACK();
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class CraftIdInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static CraftIdInfo newInstance() {
			return new CraftIdInfo();
		}

		public static CraftIdInfo newInstance(int craft_id) {
			CraftIdInfo cii = new CraftIdInfo();
			CraftItemInfo limit = CraftListLoader.getInstance().getCraftId(craft_id);

			int max_count = 0;
			int current_count = 0;
			if (limit != null) {
				max_count = limit.get_craft_max_count();
				current_count = limit.get_craft_current_count();
			}

			cii.set_craft_id(craft_id);
			cii.set_max_success_cnt(max_count);
			cii.set_cur_success_cnt(current_count);

			return cii;
		}

		// b4 43 09 08 01 12 07 08 a9 16 10 00 18 00 07 0c
		private int _craft_id; // 0x00000008
		private int _max_success_cnt; // 0x00000010
		private int _cur_success_cnt; // 0x00000018
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private CraftIdInfo() {
		}

		public int get_craft_id() {
			return _craft_id;
		}

		public void set_craft_id(int val) {
			_bit |= 0x1;
			_craft_id = val;
		}

		public boolean has_craft_id() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_max_success_cnt() {
			return _max_success_cnt;
		}

		public void set_max_success_cnt(int val) {
			_bit |= 0x2;
			_max_success_cnt = val;
		}

		public boolean has_max_success_cnt() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_cur_success_cnt() {
			return _cur_success_cnt;
		}

		public void set_cur_success_cnt(int val) {
			_bit |= 0x4;
			_cur_success_cnt = val;
		}

		public boolean has_cur_success_cnt() {
			return (_bit & 0x4) == 0x4;
		}

		@Override
		public long getInitializeBit() {
			return (long) _bit;
		}

		@Override
		public int getMemorizedSerializeSizedSize() {
			return _memorizedSerializedSize;
		}

		@Override
		public int getSerializedSize() {
			int size = 0;
			if (has_craft_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _craft_id);
			}
			if (has_max_success_cnt()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _max_success_cnt);
			}
			if (has_cur_success_cnt()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _cur_success_cnt);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_craft_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_craft_id()) {
				output.wirteInt32(1, _craft_id);
			}
			if (has_max_success_cnt()) {
				output.wirteInt32(2, _max_success_cnt);
			}
			if (has_cur_success_cnt()) {
				output.wirteInt32(3, _cur_success_cnt);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
							message.toInt());
			try {
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
				l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					case 0x00000008: {
						set_craft_id(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_max_success_cnt(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_cur_success_cnt(input.readInt32());
						break;
					}
					default: {
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null) {
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new CraftIdInfo();
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}

	public enum eCraftIdInfoReqResultType {
		RP_SUCCESS(0), RP_ERROR_CRAFT_ID(1), RP_ERROR_UNKNOWN(9999),;

		private int value;

		eCraftIdInfoReqResultType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eCraftIdInfoReqResultType v) {
			return value == v.value;
		}

		public static eCraftIdInfoReqResultType fromInt(int i) {
			switch (i) {
				case 0:
					return RP_SUCCESS;
				case 1:
					return RP_ERROR_CRAFT_ID;
				case 9999:
					return RP_ERROR_UNKNOWN;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eCraftIdInfoReqResultType，%d", i));
			}
		}
	}
}

// package l1j.server.MJTemplate.MJProto.MainServer_Client;
//
// import l1j.server.CraftList.CraftItemInfo;
// import l1j.server.CraftList.CraftListLoader;
// import l1j.server.CraftList.CraftNpcInfo;
// import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
// import
// l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRAFT_ID_LIST_ACK.CraftIdList;
// import l1j.server.server.model.Instance.L1PcInstance;
//
//// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
// public class SC_LIMITED_CRAFT_INFO_ACK implements
// l1j.server.MJTemplate.MJProto.MJIProtoMessage{
// public static SC_LIMITED_CRAFT_INFO_ACK newInstance(){
// return new SC_LIMITED_CRAFT_INFO_ACK();
// }
//
// public static void send(L1PcInstance pc, int craft_id){
// SC_LIMITED_CRAFT_INFO_ACK noti = SC_LIMITED_CRAFT_INFO_ACK.newInstance();
// CraftItemInfo limit = CraftListLoader.getInstance().getCraftId(craft_id);
//
//
// int max_count = 0;
// int current_count = 0;
// if (limit != null) {
// max_count = limit.get_craft_max_count();
// current_count = limit.get_craft_current_count();
// }
//
// // 이렇게 하면 테이블과 연동되서 그걸 읽는다??
// set_craft_id(craft_id);
// set_cur_success_cnt(current_count);
// set_max_success_cnt(max_count);
//
// noti.set_eResult(eCraftIdInfoReqResultType.RP_SUCCESS);
//
// pc.sendPackets(noti, MJEProtoMessages.SC_LIMITED_CRAFT_INFO_ACK, true);
// }
//
// public static void test(L1PcInstance pc, int craft_id, int cur_success_cnt,
// int max_success_cnt){
// CraftIdInfo test = CraftIdInfo.newInstance();
// System.out.println("test!!!!!!!!");
// test.set_craft_id(craft_id);
// test.set_cur_success_cnt(cur_success_cnt);
// test.set_max_success_cnt(max_success_cnt);
// pc.sendPackets(test, MJEProtoMessages.SC_LIMITED_CRAFT_INFO_ACK, true);
// }
//
// private SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType _eResult;
// private java.util.LinkedList<SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo>
// _craft_id_info;
// private int _memorizedSerializedSize = -1;
// private byte _memorizedIsInitialized = -1;
// private int _bit;
// private SC_LIMITED_CRAFT_INFO_ACK(){
// }
// public SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType get_eResult(){
// return _eResult;
// }
// public void set_eResult(SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType
// val){
// _bit |= 0x1;
// _eResult = val;
// }
// public boolean has_eResult(){
// return (_bit & 0x1) == 0x1;
// }
// public java.util.LinkedList<SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo>
// get_craft_id_info(){
// return _craft_id_info;
// }
// public void add_craft_id_info(SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo val){
// if(!has_craft_id_info()){
// _craft_id_info = new
// java.util.LinkedList<SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo>();
// _bit |= 0x2;
// }
// _craft_id_info.add(val);
// }
// public boolean has_craft_id_info(){
// return (_bit & 0x2) == 0x2;
// }
// @Override
// public long getInitializeBit(){
// return (long)_bit;
// }
// @Override
// public int getMemorizedSerializeSizedSize(){
// return _memorizedSerializedSize;
// }
// @Override
// public int getSerializedSize(){
// int size = 0;
// if (has_eResult()){
// size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1,
// _eResult.toInt());
// }
// if (has_craft_id_info()){
// for(SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo val : _craft_id_info){
// size +=
// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2,
// val);
// }
// }
// _memorizedSerializedSize = size;
// return size;
// }
// @Override
// public boolean isInitialized(){
// if(_memorizedIsInitialized == 1)
// return true;
// if (!has_eResult()){
// _memorizedIsInitialized = -1;
// return false;
// }
// if (has_craft_id_info()){
// for(SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo val : _craft_id_info){
// if (!val.isInitialized()){
// _memorizedIsInitialized = -1;
// return false;
// }
// }
// }
// _memorizedIsInitialized = 1;
// return true;
// }
// @Override
// public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
// output) throws java.io.IOException{
// if (has_eResult()){
// output.writeEnum(1, _eResult.toInt());
// }
// if (has_craft_id_info()){
// for (SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo val : _craft_id_info){
// output.writeMessage(2, val);
// }
// }
// }
// @Override
// public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
// writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize()
// + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
// message.toInt());
// try{
// writeTo(stream);
// } catch (java.io.IOException e) {
// e.printStackTrace();
// }
// return stream;
// }
// @Override
// public l1j.server.MJTemplate.MJProto.MJIProtoMessage
// readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws
// java.io.IOException{
// while(!input.isAtEnd()){
// int tag = input.readTag();
// switch(tag){
// case 0x00000008:{
// set_eResult(SC_LIMITED_CRAFT_INFO_ACK.eCraftIdInfoReqResultType.fromInt(input.readEnum()));
// break;
// }
// case 0x00000012:{
// add_craft_id_info((SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo)input.readMessage(SC_LIMITED_CRAFT_INFO_ACK.CraftIdInfo.newInstance()));
// break;
// }
// default:{
// return this;
// }
// }
// }
// return this;
// }
// @Override
// public l1j.server.MJTemplate.MJProto.MJIProtoMessage
// readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
// l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is =
// l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes,
// l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] &
// 0xff) | (bytes[4] << 8 & 0xff00)) +
// l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
// try{
// readFrom(is);
//
// if (!isInitialized())
// return this;
//
// l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
// if (pc == null){
// return this;
// }
//
// // TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.
//
// } catch (Exception e){
// e.printStackTrace();
// }
// return this;
// }
// @Override
// public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
// return new SC_LIMITED_CRAFT_INFO_ACK();
// }
// @Override
// public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
// return newInstance();
// }
// @Override
// public void dispose(){
// _bit = 0;
// _memorizedIsInitialized = -1;
// }
// public static class CraftIdInfo implements
// l1j.server.MJTemplate.MJProto.MJIProtoMessage{
// public static CraftIdInfo newInstance(){
// return new CraftIdInfo();
// }
//
// public static CraftIdInfo newInstance(int craft_id) {
// CraftIdInfo cii = new CraftIdInfo();
//
//
// return cii;
// }
//
// private int _craft_id;
// private int _max_success_cnt;
// private int _cur_success_cnt;
// private int _memorizedSerializedSize = -1;
// private byte _memorizedIsInitialized = -1;
// private int _bit;
// private CraftIdInfo(){
// }
// public int get_craft_id(){
// return _craft_id;
// }
// public void set_craft_id(int val){
// _bit |= 0x1;
// _craft_id = val;
// }
// public boolean has_craft_id(){
// return (_bit & 0x1) == 0x1;
// }
// public int get_max_success_cnt(){
// return _max_success_cnt;
// }
// public void set_max_success_cnt(int val){
// _bit |= 0x2;
// _max_success_cnt = val;
// }
// public boolean has_max_success_cnt(){
// return (_bit & 0x2) == 0x2;
// }
// public int get_cur_success_cnt(){
// return _cur_success_cnt;
// }
// public void set_cur_success_cnt(int val){
// _bit |= 0x4;
// _cur_success_cnt = val;
// }
// public boolean has_cur_success_cnt(){
// return (_bit & 0x4) == 0x4;
// }
// @Override
// public long getInitializeBit(){
// return (long)_bit;
// }
// @Override
// public int getMemorizedSerializeSizedSize(){
// return _memorizedSerializedSize;
// }
// @Override
// public int getSerializedSize(){
// int size = 0;
// if (has_craft_id()){
// size +=
// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1,
// _craft_id);
// }
// if (has_max_success_cnt()){
// size +=
// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2,
// _max_success_cnt);
// }
// if (has_cur_success_cnt()){
// size +=
// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3,
// _cur_success_cnt);
// }
// _memorizedSerializedSize = size;
// return size;
// }
// @Override
// public boolean isInitialized(){
// if(_memorizedIsInitialized == 1)
// return true;
// if (!has_craft_id()){
// _memorizedIsInitialized = -1;
// return false;
// }
// _memorizedIsInitialized = 1;
// return true;
// }
// @Override
// public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
// output) throws java.io.IOException{
// if (has_craft_id()){
// output.wirteInt32(1, _craft_id);
// }
// if (has_max_success_cnt()){
// output.wirteInt32(2, _max_success_cnt);
// }
// if (has_cur_success_cnt()){
// output.wirteInt32(3, _cur_success_cnt);
// }
// }
// @Override
// public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
// writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
// l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize()
// + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
// message.toInt());
// try{
// writeTo(stream);
// } catch (java.io.IOException e) {
// e.printStackTrace();
// }
// return stream;
// }
// @Override
// public l1j.server.MJTemplate.MJProto.MJIProtoMessage
// readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws
// java.io.IOException{
// while(!input.isAtEnd()){
// int tag = input.readTag();
// switch(tag){
// case 0x00000008:{
// set_craft_id(input.readInt32());
// break;
// }
// case 0x00000010:{
// set_max_success_cnt(input.readInt32());
// break;
// }
// case 0x00000018:{
// set_cur_success_cnt(input.readInt32());
// break;
// }
// default:{
// return this;
// }
// }
// }
// return this;
// }
// @Override
// public l1j.server.MJTemplate.MJProto.MJIProtoMessage
// readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
// l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is =
// l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes,
// l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] &
// 0xff) | (bytes[4] << 8 & 0xff00)) +
// l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
// try{
// readFrom(is);
//
// if (!isInitialized())
// return this;
//
// l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
// if (pc == null){
// return this;
// }
//
// // TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.
//
// } catch (Exception e){
// e.printStackTrace();
// }
// return this;
// }
// @Override
// public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
// return new CraftIdInfo();
// }
// @Override
// public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
// return newInstance();
// }
// @Override
// public void dispose(){
// _bit = 0;
// _memorizedIsInitialized = -1;
// }
// }
// public enum eCraftIdInfoReqResultType{
// RP_SUCCESS(0),
// RP_ERROR_CRAFT_ID(1),
// RP_ERROR_UNKNOWN(9999),
// ;
// private int value;
// eCraftIdInfoReqResultType(int val){
// value = val;
// }
// public int toInt(){
// return value;
// }
// public boolean equals(eCraftIdInfoReqResultType v){
// return value == v.value;
// }
// public static eCraftIdInfoReqResultType fromInt(int i){
// switch(i){
// case 0:
// return RP_SUCCESS;
// case 1:
// return RP_ERROR_CRAFT_ID;
// case 9999:
// return RP_ERROR_UNKNOWN;
// default:
// throw new IllegalArgumentException(String.format("invalid arguments
// eCraftIdInfoReqResultType, %d", i));
// }
// }
// }
// }
