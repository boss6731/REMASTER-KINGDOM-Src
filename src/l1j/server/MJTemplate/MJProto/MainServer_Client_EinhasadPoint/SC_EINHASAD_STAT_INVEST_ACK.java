/*package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.AinhasadSpecialStat.Einpointffecttable;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.EinhasadStatDetailsT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eEinhasadStatType;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EINHASAD_STAT_INVEST_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void sendepont(L1PcInstance pc) {
		SC_EINHASAD_STAT_INVEST_ACK noti = SC_EINHASAD_STAT_INVEST_ACK.newInstance();
		EinhasadStatDetailsT info = EinhasadStatDetailsT.newInstance();
		Einpointffecttable eff = Einpointffecttable.getInstance();
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
		int i = 0;
		if(Info.get_bless() != 0) {
			i++;
		}
		if(Info.get_lucky() != 0) {
			i++;
		}
		if(Info.get_vital() != 0) {
			i++;
		}		
		if(Info.get_invoke() != 0) {
			i++;
		}		
		if(Info.get_restore() != 0) {
			i++;
		}		
		if(Info.get_potion() != 0) {
			i++;
		}
		
		for(int j =0; j < i && j >= 0; j++) {
			info = EinhasadStatDetailsT.newInstance();
			info.set_index(eEinhasadStatType.fromInt(j));
			info.set_value(eff.getstate(pc, j));
			info.set_abilValue1(eff.getstate(pc, j) != 0 ? eff.getvalue(i, eff.getstate(pc, j), 0) : 0);
			info.set_abilValue2(eff.getstate(pc, j) != 0 ? eff.getvalue(i, eff.getstate(pc, j), 1) : 0);
			noti.add_stat_result(info);
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_STAT_INVEST_ACK);
		info.dispose();
	}
	
	public static SC_EINHASAD_STAT_INVEST_ACK newInstance(){
		return new SC_EINHASAD_STAT_INVEST_ACK();
	}
	private java.util.LinkedList<EinhasadStatDetailsT> _stat_result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_EINHASAD_STAT_INVEST_ACK(){
	}
	public java.util.LinkedList<EinhasadStatDetailsT> get_stat_result(){
		return _stat_result;
	}
	public void add_stat_result(EinhasadStatDetailsT val){
		if(!has_stat_result()){
			_stat_result = new java.util.LinkedList<EinhasadStatDetailsT>();
			_bit |= 0x1;
		}
		_stat_result.add(val);
	}
	public boolean has_stat_result(){
		return (_bit & 0x1) == 0x1;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_stat_result()){
			for(EinhasadStatDetailsT val : _stat_result){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_stat_result()){
			for(EinhasadStatDetailsT val : _stat_result){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_stat_result()){
			for (EinhasadStatDetailsT val : _stat_result){
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					add_stat_result((EinhasadStatDetailsT)input.readMessage(EinhasadStatDetailsT.newInstance()));
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
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_EINHASAD_STAT_INVEST_ACK();
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}*/

package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.EinhasadStatDetailsT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eEinhasadStatType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.CalcStat;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EINHASAD_STAT_INVEST_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send_point_stat_invest(L1PcInstance pc, int index, AinhasadSpecialStatInfo info) {
		SC_EINHASAD_STAT_INVEST_ACK noti = SC_EINHASAD_STAT_INVEST_ACK.newInstance();
		if (index == 10) {
			for (int i = 0; i < 6; ++i) {
				EinhasadStatDetailsT stat = EinhasadStatDetailsT.newInstance();
				int value = 0;
				int abil_1 = 0;
				int abil_2 = 0;
				stat.set_index(eEinhasadStatType.fromInt(i));
				switch (i) {
					case 0:
						value = info.get_bless();
						abil_1 = CalcStat.calcAinhasadStatFirst(value);
						abil_2 = CalcStat.calcAinhasadStatSecond(value);
						break;
					case 1:
						value = info.get_lucky();
						abil_1 = CalcStat.calcAinhasadStatFirst(value);
						abil_2 = CalcStat.calcAinhasadStatSecond(value);
						break;
					case 2:
						value = info.get_vital();
						abil_1 = CalcStat.calcAinhasadStatFirst(value);
						abil_2 = CalcStat.calcAinhasadStatSecond(value);
						break;
					case 3:
						value = info.get_invoke();
						abil_1 = info.get_invoke_val_1();
						abil_2 = info.get_invoke_val_2();
						break;
					case 4:
						value = info.get_restore();
						abil_1 = info.get_restore_val_2();
						abil_2 = info.get_restore_val_1();
						break;
					case 5:
						value = info.get_potion();
						abil_1 = info.get_potion_val_1();
						abil_2 = info.get_potion_val_2();
						break;
					case 6:
						break;
					case 7:
						break;
					case 8:
						break;
					case 9:
						break;
				}
				stat.set_value(value);
				stat.set_abilValue1(abil_1);
				stat.set_abilValue2(abil_2);
				noti.add_stat_result(stat);
			}
		} else {
			EinhasadStatDetailsT stat = EinhasadStatDetailsT.newInstance();
			stat.set_index(eEinhasadStatType.fromInt(index));
			int value = 0;
			int abil_1 = 0;
			int abil_2 = 0;
			switch (index) {
				case 0:
					value = info.get_bless();
					abil_1 = CalcStat.calcAinhasadStatFirst(value);
					abil_2 = CalcStat.calcAinhasadStatSecond(value);
					break;
				case 1:
					value = info.get_lucky();
					abil_1 = CalcStat.calcAinhasadStatFirst(value);
					abil_2 = CalcStat.calcAinhasadStatSecond(value);
					break;
				case 2:
					value = info.get_vital();
					abil_1 = CalcStat.calcAinhasadStatFirst(value);
					abil_2 = CalcStat.calcAinhasadStatSecond(value);
					break;
				case 3:
					value = info.get_invoke();
					abil_1 = info.get_invoke_val_1();
					abil_2 = info.get_invoke_val_2();
					break;
				case 4:
					value = info.get_restore();
					abil_1 = info.get_restore_val_2();
					abil_2 = info.get_restore_val_1();
					break;
				case 5:
					value = info.get_potion();
					abil_1 = info.get_potion_val_1();
					abil_2 = info.get_potion_val_2();
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				case 9:
					break;
			}
			stat.set_value(value);
			stat.set_abilValue1(abil_1);
			stat.set_abilValue2(abil_2);
			noti.add_stat_result(stat);
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_STAT_INVEST_ACK, true);
	}

	public static SC_EINHASAD_STAT_INVEST_ACK newInstance() {
		return new SC_EINHASAD_STAT_INVEST_ACK();
	}

	private java.util.LinkedList<EinhasadStatDetailsT> _stat_result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EINHASAD_STAT_INVEST_ACK() {
	}

	public java.util.LinkedList<EinhasadStatDetailsT> get_stat_result() {
		return _stat_result;
	}

	public void add_stat_result(EinhasadStatDetailsT val) {
		if (!has_stat_result()) {
			_stat_result = new java.util.LinkedList<EinhasadStatDetailsT>();
			_bit |= 0x1;
		}
		_stat_result.add(val);
	}

	public boolean has_stat_result() {
		return (_bit & 0x1) == 0x1;
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
		if (has_stat_result()) {
			for (EinhasadStatDetailsT val : _stat_result) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_stat_result()) {
			for (EinhasadStatDetailsT val : _stat_result) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_stat_result()) {
			for (EinhasadStatDetailsT val : _stat_result) {
				output.writeMessage(1, val);
			}
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
				case 0x0000000A: {
					add_stat_result((EinhasadStatDetailsT) input.readMessage(EinhasadStatDetailsT.newInstance()));
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
		return new SC_EINHASAD_STAT_INVEST_ACK();
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
