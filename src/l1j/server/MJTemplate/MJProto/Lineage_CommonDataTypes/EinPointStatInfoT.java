package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPEED_BONUS_NOTI.Bonus;

// TODO : 這是自動產生的 PROTO 程式碼。 made by Nature.
public class EinPointStatInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static EinPointStatInfoT newInstance() {
		return new EinPointStatInfoT();
	}

	private int _blessMax;
	private int _luckyMax;
	private int _vitalMax;
	private java.util.LinkedList<EinPointStatInfoT.EnchantCostT> _EnchantCost;
	private java.util.LinkedList<EinPointStatInfoT.StatT> _StatBless;
	private java.util.LinkedList<EinPointStatInfoT.StatT> _StatLucky;
	private java.util.LinkedList<EinPointStatInfoT.StatT> _StatVital;
	private int _totalStatMax;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private EinPointStatInfoT() {
	}

	public int get_blessMax() {
		return _blessMax;
	}

	public void set_blessMax(int val) {
		_bit |= 0x1;
		_blessMax = val;
	}

	public boolean has_blessMax() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_luckyMax() {
		return _luckyMax;
	}

	public void set_luckyMax(int val) {
		_bit |= 0x2;
		_luckyMax = val;
	}

	public boolean has_luckyMax() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_vitalMax() {
		return _vitalMax;
	}

	public void set_vitalMax(int val) {
		_bit |= 0x4;
		_vitalMax = val;
	}

	public boolean has_vitalMax() {
		return (_bit & 0x4) == 0x4;
	}

	public java.util.LinkedList<EinPointStatInfoT.EnchantCostT> get_EnchantCost() {
		return _EnchantCost;
	}

	public void add_EnchantCost(EinPointStatInfoT.EnchantCostT val) {
		if (!has_EnchantCost()) {
			_EnchantCost = new java.util.LinkedList<EinPointStatInfoT.EnchantCostT>();
			_bit |= 0x8;
		}
		_EnchantCost.add(val);
	}

	public boolean has_EnchantCost() {
		return (_bit & 0x8) == 0x8;
	}

	public java.util.LinkedList<EinPointStatInfoT.StatT> get_StatBless() {
		return _StatBless;
	}

	public void add_StatBless(EinPointStatInfoT.StatT val) {
		if (!has_StatBless()) {
			_StatBless = new java.util.LinkedList<EinPointStatInfoT.StatT>();
			_bit |= 0x10;
		}
		_StatBless.add(val);
	}

	public boolean has_StatBless() {
		return (_bit & 0x10) == 0x10;
	}

	public java.util.LinkedList<EinPointStatInfoT.StatT> get_StatLucky() {
		return _StatLucky;
	}

	public void add_StatLucky(EinPointStatInfoT.StatT val) {
		if (!has_StatLucky()) {
			_StatLucky = new java.util.LinkedList<EinPointStatInfoT.StatT>();
			_bit |= 0x20;
		}
		_StatLucky.add(val);
	}

	public boolean has_StatLucky() {
		return (_bit & 0x20) == 0x20;
	}

	public java.util.LinkedList<EinPointStatInfoT.StatT> get_StatVital() {
		return _StatVital;
	}

	public void add_StatVital(EinPointStatInfoT.StatT val) {
		if (!has_StatVital()) {
			_StatVital = new java.util.LinkedList<EinPointStatInfoT.StatT>();
			_bit |= 0x40;
		}
		_StatVital.add(val);
	}

	public boolean has_StatVital() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_totalStatMax() {
		return _totalStatMax;
	}

	public void set_totalStatMax(int val) {
		_bit |= 0x80;
		_totalStatMax = val;
	}

	public boolean has_totalStatMax() {
		return (_bit & 0x80) == 0x80;
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
		if (has_blessMax()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _blessMax);
		}
		if (has_luckyMax()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _luckyMax);
		}
		if (has_vitalMax()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _vitalMax);
		}
		if (has_EnchantCost()) {
			for (EinPointStatInfoT.EnchantCostT val : _EnchantCost) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		if (has_StatBless()) {
			for (EinPointStatInfoT.StatT val : _StatBless) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
			}
		}
		if (has_StatLucky()) {
			for (EinPointStatInfoT.StatT val : _StatLucky) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
			}
		}
		if (has_StatVital()) {
			for (EinPointStatInfoT.StatT val : _StatVital) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, val);
			}
		}
		if (has_totalStatMax()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _totalStatMax);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_blessMax()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_luckyMax()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_vitalMax()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_EnchantCost()) {
			for (EinPointStatInfoT.EnchantCostT val : _EnchantCost) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_StatBless()) {
			for (EinPointStatInfoT.StatT val : _StatBless) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_StatLucky()) {
			for (EinPointStatInfoT.StatT val : _StatLucky) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_StatVital()) {
			for (EinPointStatInfoT.StatT val : _StatVital) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_totalStatMax()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_blessMax()) {
			output.wirteInt32(1, _blessMax);
		}
		if (has_luckyMax()) {
			output.wirteInt32(2, _luckyMax);
		}
		if (has_vitalMax()) {
			output.wirteInt32(3, _vitalMax);
		}
		if (has_EnchantCost()) {
			for (EinPointStatInfoT.EnchantCostT val : _EnchantCost) {
				output.writeMessage(4, val);
			}
		}
		if (has_StatBless()) {
			for (EinPointStatInfoT.StatT val : _StatBless) {
				output.writeMessage(5, val);
			}
		}
		if (has_StatLucky()) {
			for (EinPointStatInfoT.StatT val : _StatLucky) {
				output.writeMessage(6, val);
			}
		}
		if (has_StatVital()) {
			for (EinPointStatInfoT.StatT val : _StatVital) {
				output.writeMessage(7, val);
			}
		}
		if (has_totalStatMax()) {
			output.wirteInt32(8, _totalStatMax);
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
					set_blessMax(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_luckyMax(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_vitalMax(input.readInt32());
					break;
				}
				case 0x00000022: {
					add_EnchantCost((EinPointStatInfoT.EnchantCostT) input
							.readMessage(EinPointStatInfoT.EnchantCostT.newInstance()));
					break;
				}
				case 0x0000002A: {
					add_StatBless((EinPointStatInfoT.StatT) input.readMessage(EinPointStatInfoT.StatT.newInstance()));
					break;
				}
				case 0x00000032: {
					add_StatLucky((EinPointStatInfoT.StatT) input.readMessage(EinPointStatInfoT.StatT.newInstance()));
					break;
				}
				case 0x0000003A: {
					add_StatVital((EinPointStatInfoT.StatT) input.readMessage(EinPointStatInfoT.StatT.newInstance()));
					break;
				}
				case 0x00000040: {
					set_totalStatMax(input.readInt32());
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

			// TODO : 從下面開始插入您的處理程式碼。 made by Nature.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new EinPointStatInfoT();
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

	public static class EnchantCostT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static EnchantCostT newInstance() {
			return new EnchantCostT();
		}

		private int _value;
		private int _point;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private EnchantCostT() {
		}

		public int get_value() {
			return _value;
		}

		public void set_value(int val) {
			_bit |= 0x1;
			_value = val;
		}

		public boolean has_value() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_point() {
			return _point;
		}

		public void set_point(int val) {
			_bit |= 0x2;
			_point = val;
		}

		public boolean has_point() {
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
			if (has_value()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _value);
			}
			if (has_point()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _point);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_value()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_point()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_value()) {
				output.wirteInt32(1, _value);
			}
			if (has_point()) {
				output.wirteInt32(2, _point);
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
						set_value(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_point(input.readInt32());
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

				// TODO : 從下面開始插入您的處理程式碼。 made by Nature.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new EnchantCostT();
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

	public static class StatT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static StatT newInstance() {
			return new StatT();
		}

		private int _value;
		private Bonus _BonusOption;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private StatT() {
		}

		public int get_value() {
			return _value;
		}

		public void set_value(int val) {
			_bit |= 0x1;
			_value = val;
		}

		public boolean has_value() {
			return (_bit & 0x1) == 0x1;
		}

		public Bonus get_BonusOption() {
			return _BonusOption;
		}

		public void set_BonusOption(Bonus val) {
			_bit |= 0x2;
			_BonusOption = val;
		}

		public boolean has_BonusOption() {
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
			if (has_value()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _value);
			}
			if (has_BonusOption()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _BonusOption);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_value()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_BonusOption()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_value()) {
				output.wirteInt32(1, _value);
			}
			if (has_BonusOption()) {
				output.writeMessage(2, _BonusOption);
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
						set_value(input.readInt32());
						break;
					}
					case 0x00000012: {
						set_BonusOption((Bonus) input.readMessage(Bonus.newInstance()));
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

				// TODO : 從下面開始插入您的處理程式碼。 made by Nature.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new StatT();
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
}
