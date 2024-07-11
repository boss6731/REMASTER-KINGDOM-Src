/*package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.Einpointffecttable;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.EinhasadStatDetailsT;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eEinhasadStatType;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EINHASAD_POINT_STAT_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
//	public static void send_point(L1PcInstance pc, int bless, int lucky, int vital, int total, int enchant, long point){
//		SC_EINHASAD_POINT_STAT_INFO_NOTI noti = SC_EINHASAD_POINT_STAT_INFO_NOTI.newInstance();
//		noti.set_stat_bless(bless);
//		noti.set_stat_lucky(lucky);
//		noti.set_stat_vital(vital);
//		noti.set_total_stat(total);
//		noti.set_enchant_level(enchant);
//		noti.set_point(point);
//		pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_POINT_STAT_INFO_NOTI, true);
//	}
	
	public static void send(L1PcInstance pc, AinhasadSpecialStatInfo Info) {

		if (pc == null || pc.getAccount() == null || pc.noPlayerCK || Info == null)
			return;

		SC_EINHASAD_POINT_STAT_INFO_NOTI noti = newInstance();
		EinhasadStatDetailsT info = EinhasadStatDetailsT.newInstance();
		Einpointffecttable eff = Einpointffecttable.getInstance();
		noti.set_total_stat(Info.get_total_stat());
		noti.set_enchant_level(0);
		noti.set_point(0);
		if(Info.get_bless() != 0) {
			info = EinhasadStatDetailsT.newInstance();
			info.set_index(eEinhasadStatType.fromInt(0));
			info.set_value(eff.getstate(pc, 0));
			info.set_abilValue1(eff.getstate(pc, 0) != 0 ? eff.getvalue(0, eff.getstate(pc, 0), 0) : 0);
			info.set_abilValue2(eff.getstate(pc, 0) != 0 ? eff.getvalue(0, eff.getstate(pc, 0), 1) : 0);
			noti.add_each_stat(info);
		}
		if(Info.get_lucky() != 0) {
			info = EinhasadStatDetailsT.newInstance();
			info.set_index(eEinhasadStatType.fromInt(1));
			info.set_value(eff.getstate(pc, 1));
			info.set_abilValue1(eff.getstate(pc, 1) != 0 ? eff.getvalue(1, eff.getstate(pc, 1), 0) : 0);
			info.set_abilValue2(eff.getstate(pc, 1) != 0 ? eff.getvalue(1, eff.getstate(pc, 1), 1) : 0);
			noti.add_each_stat(info);
		}
		if(Info.get_vital() != 0) {
			info = EinhasadStatDetailsT.newInstance();
			info.set_index(eEinhasadStatType.fromInt(2));
			info.set_value(eff.getstate(pc, 2));
			info.set_abilValue1(eff.getstate(pc, 2) != 0 ? eff.getvalue(2, eff.getstate(pc, 2), 0) : 0);
			info.set_abilValue2(eff.getstate(pc, 2) != 0 ? eff.getvalue(2, eff.getstate(pc, 2), 1) : 0);
			noti.add_each_stat(info);
		}		
		if(Info.get_invoke() != 0) {
			info = EinhasadStatDetailsT.newInstance();
			info.set_index(eEinhasadStatType.fromInt(3));
			info.set_value(eff.getstate(pc, 3));
			info.set_abilValue1(Info.get_invoke_per1());
			info.set_abilValue2(Info.get_invoke_per2());
			noti.add_each_stat(info);
		}		
		if(Info.get_restore() != 0) {
			info = EinhasadStatDetailsT.newInstance();
			info.set_index(eEinhasadStatType.fromInt(4));
			info.set_value(eff.getstate(pc, 4));
			info.set_abilValue1(eff.getstate(pc, 4) != 0 ? eff.getvalue(4, eff.getstate(pc, 4), 0) : 0);
			info.set_abilValue2(eff.getstate(pc, 4) != 0 ? eff.getvalue(4, eff.getstate(pc, 4), 1) : 0);
			noti.add_each_stat(info);
		}		
		if(Info.get_potion() != 0) {
			info = EinhasadStatDetailsT.newInstance();
			info.set_index(eEinhasadStatType.fromInt(5));
			info.set_value(eff.getstate(pc, 5));
			info.set_abilValue1(Info.get_potion_per1());
			info.set_abilValue2(Info.get_potion_per2());
			noti.add_each_stat(info);
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_POINT_STAT_INFO_NOTI, true);
		if(info != null) {
			info.dispose();
		}
		if(noti != null) {
			noti.dispose();
		}
	}
	public static SC_EINHASAD_POINT_STAT_INFO_NOTI newInstance(){
		return new SC_EINHASAD_POINT_STAT_INFO_NOTI();
	}
	private int _stat_bless;
	private int _stat_lucky;
	private int _stat_vital;
	private int _total_stat;
	private int _enchant_level;
	private long _point;
	private java.util.LinkedList<EinhasadStatDetailsT> _each_stat;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_EINHASAD_POINT_STAT_INFO_NOTI(){
	}
	public int get_stat_bless(){
		return _stat_bless;
	}
	public void set_stat_bless(int val){
		_bit |= 0x1;
		_stat_bless = val;
	}
	public boolean has_stat_bless(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_stat_lucky(){
		return _stat_lucky;
	}
	public void set_stat_lucky(int val){
		_bit |= 0x2;
		_stat_lucky = val;
	}
	public boolean has_stat_lucky(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_stat_vital(){
		return _stat_vital;
	}
	public void set_stat_vital(int val){
		_bit |= 0x4;
		_stat_vital = val;
	}
	public boolean has_stat_vital(){
		return (_bit & 0x4) == 0x4;
	}
	public int get_total_stat(){
		return _total_stat;
	}
	public void set_total_stat(int val){
		_bit |= 0x8;
		_total_stat = val;
	}
	public boolean has_total_stat(){
		return (_bit & 0x8) == 0x8;
	}
	public int get_enchant_level(){
		return _enchant_level;
	}
	public void set_enchant_level(int val){
		_bit |= 0x10;
		_enchant_level = val;
	}
	public boolean has_enchant_level(){
		return (_bit & 0x10) == 0x10;
	}
	public long get_point(){
		return _point;
	}
	public void set_point(long val){
		_bit |= 0x20;
		_point = val;
	}
	public boolean has_point(){
		return (_bit & 0x20) == 0x20;
	}
	public java.util.LinkedList<EinhasadStatDetailsT> get_each_stat(){
		return _each_stat;
	}
	public void add_each_stat(EinhasadStatDetailsT val){
		if(!has_each_stat()){
			_each_stat = new java.util.LinkedList<EinhasadStatDetailsT>();
			_bit |= 0x40;
		}
		_each_stat.add(val);
	}
	public boolean has_each_stat(){
		return (_bit & 0x40) == 0x40;
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
		if (has_stat_bless()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _stat_bless);
		}
		if (has_stat_lucky()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _stat_lucky);
		}
		if (has_stat_vital()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _stat_vital);
		}
		if (has_total_stat()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _total_stat);
		}
		if (has_enchant_level()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _enchant_level);
		}
		if (has_point()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(6, _point);
		}
		if (has_each_stat()){
			for(EinhasadStatDetailsT val : _each_stat){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_total_stat()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enchant_level()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_point()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_each_stat()){
			for(EinhasadStatDetailsT val : _each_stat){
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
		if (has_stat_bless()){
			output.wirteInt32(1, _stat_bless);
		}
		if (has_stat_lucky()){
			output.wirteInt32(2, _stat_lucky);
		}
		if (has_stat_vital()){
			output.wirteInt32(3, _stat_vital);
		}
		if (has_total_stat()){
			output.wirteInt32(4, _total_stat);
		}
		if (has_enchant_level()){
			output.wirteInt32(5, _enchant_level);
		}
		if (has_point()){
			output.writeInt64(6, _point);
		}
		if (has_each_stat()){
			for (EinhasadStatDetailsT val : _each_stat){
				output.writeMessage(7, val);
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
				case 0x00000008:{
					set_stat_bless(input.readInt32());
					break;
				}
				case 0x00000010:{
					set_stat_lucky(input.readInt32());
					break;
				}
				case 0x00000018:{
					set_stat_vital(input.readInt32());
					break;
				}
				case 0x00000020:{
					set_total_stat(input.readInt32());
					break;
				}
				case 0x00000028:{
					set_enchant_level(input.readInt32());
					break;
				}
				case 0x00000030:{
					set_point(input.readInt64());
					break;
				}
				case 0x0000003A:{
					add_each_stat((EinhasadStatDetailsT)input.readMessage(EinhasadStatDetailsT.newInstance()));
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
		return new SC_EINHASAD_POINT_STAT_INFO_NOTI();
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
public class SC_EINHASAD_POINT_STAT_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send_point(L1PcInstance pc, AinhasadSpecialStatInfo info) {
		SC_EINHASAD_POINT_STAT_INFO_NOTI noti = SC_EINHASAD_POINT_STAT_INFO_NOTI.newInstance();
		noti.set_stat_bless(info.get_bless());
		noti.set_stat_lucky(info.get_lucky());
		noti.set_stat_vital(info.get_vital());
		noti.set_total_stat(info.get_total_stat());
		noti.set_enchant_level(0);
		noti.set_point(0);

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
			noti.add_each_stat(stat);
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_POINT_STAT_INFO_NOTI, true);
	}

	public static SC_EINHASAD_POINT_STAT_INFO_NOTI newInstance() {
		return new SC_EINHASAD_POINT_STAT_INFO_NOTI();
	}

	private int _stat_bless;
	private int _stat_lucky;
	private int _stat_vital;
	private int _total_stat;
	private int _enchant_level;
	private long _point;
	private java.util.LinkedList<EinhasadStatDetailsT> _each_stat;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EINHASAD_POINT_STAT_INFO_NOTI() {
	}

	public int get_stat_bless() {
		return _stat_bless;
	}

	public void set_stat_bless(int val) {
		_bit |= 0x1;
		_stat_bless = val;
	}

	public boolean has_stat_bless() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_stat_lucky() {
		return _stat_lucky;
	}

	public void set_stat_lucky(int val) {
		_bit |= 0x2;
		_stat_lucky = val;
	}

	public boolean has_stat_lucky() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_stat_vital() {
		return _stat_vital;
	}

	public void set_stat_vital(int val) {
		_bit |= 0x4;
		_stat_vital = val;
	}

	public boolean has_stat_vital() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_total_stat() {
		return _total_stat;
	}

	public void set_total_stat(int val) {
		_bit |= 0x8;
		_total_stat = val;
	}

	public boolean has_total_stat() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_enchant_level() {
		return _enchant_level;
	}

	public void set_enchant_level(int val) {
		_bit |= 0x10;
		_enchant_level = val;
	}

	public boolean has_enchant_level() {
		return (_bit & 0x10) == 0x10;
	}

	public long get_point() {
		return _point;
	}

	public void set_point(long val) {
		_bit |= 0x20;
		_point = val;
	}

	public boolean has_point() {
		return (_bit & 0x20) == 0x20;
	}

	public java.util.LinkedList<EinhasadStatDetailsT> get_each_stat() {
		return _each_stat;
	}

	public void add_each_stat(EinhasadStatDetailsT val) {
		if (!has_each_stat()) {
			_each_stat = new java.util.LinkedList<EinhasadStatDetailsT>();
			_bit |= 0x40;
		}
		_each_stat.add(val);
	}

	public boolean has_each_stat() {
		return (_bit & 0x40) == 0x40;
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
		if (has_stat_bless()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _stat_bless);
		}
		if (has_stat_lucky()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _stat_lucky);
		}
		if (has_stat_vital()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _stat_vital);
		}
		if (has_total_stat()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _total_stat);
		}
		if (has_enchant_level()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _enchant_level);
		}
		if (has_point()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(6, _point);
		}
		if (has_each_stat()) {
			for (EinhasadStatDetailsT val : _each_stat) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_total_stat()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enchant_level()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_point()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_each_stat()) {
			for (EinhasadStatDetailsT val : _each_stat) {
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
		if (has_stat_bless()) {
			output.wirteInt32(1, _stat_bless);
		}
		if (has_stat_lucky()) {
			output.wirteInt32(2, _stat_lucky);
		}
		if (has_stat_vital()) {
			output.wirteInt32(3, _stat_vital);
		}
		if (has_total_stat()) {
			output.wirteInt32(4, _total_stat);
		}
		if (has_enchant_level()) {
			output.wirteInt32(5, _enchant_level);
		}
		if (has_point()) {
			output.writeInt64(6, _point);
		}
		if (has_each_stat()) {
			for (EinhasadStatDetailsT val : _each_stat) {
				output.writeMessage(7, val);
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
				case 0x00000008: {
					set_stat_bless(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_stat_lucky(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_stat_vital(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_total_stat(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_enchant_level(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_point(input.readInt64());
					break;
				}
				case 0x0000003A: {
					add_each_stat((EinhasadStatDetailsT) input.readMessage(EinhasadStatDetailsT.newInstance()));
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
		return new SC_EINHASAD_POINT_STAT_INFO_NOTI();
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
