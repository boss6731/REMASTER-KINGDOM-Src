package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.StringTokenizer;

public class S_TamWindow extends ServerBasePacket {

	private static final String S_TAMWINDOW = "S_TAMWINDOW";


	// 定义常量来标识不同类型的操作
	public static final int ACCOUNT_TAM = 0xCD; // 계정 탐 정보창 (账户探测信息窗口)
	public static final int Buff = 0x6e; // Buff 類型的編碼

	// 构造函数，接受时间和类型作为参数
	public S_TamWindow(long time, int type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF); // 寫入擴充操作碼
		writeC(Buff); // 写入 Buff 操作码

		// 初始写入的字符串数据
		String s = "00 08 02 10 e0 11 18";
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16)); // 将字符串拆分成字节并写入
		}

		// 将时间转换为秒并写入
		byteWrite(time / 1000);

		// 根据类型写入不同的数据
		if (type == 1) {
			s = "20 08 28 d4 2f 30 00 38 03 40";
		} else if (type == 2) {
			s = "20 08 28 93 33 30 00 38 03 40";
		} else if (type == 3) {
			s = "20 08 28 92 33 30 00 38 03 40";
		} else {
			s = "20 08 28 d4 2f 30 00 38 03 40";
		}

		// 将字符串拆分成字节并写入
		st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}

		// 根据不同的类型写入额外的数据
		if (type == 1) {
			writeH(0x1ec9); // 写入操作码
			s = "48 d5 20 50 00 58 01";
		} else if (type == 2) {
			writeH(0x20d2); // 写入操作码
			s = "48 d6 20 50 00 58 01";
		} else if (type == 3) {
			writeH(0x20d3); // 写入操作码
			s = "48 d7 20 50 00 58 01";
		} else {
			writeH(0x1ec9); // 写入操作码
			s = "48 d5 20 50 00 58 01";
		}

		// 将字符串拆分成字节并写入
		st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}


		// 写入额外的数据
		writeH(0x0060); // 写入操作码
		writeH(0x0068); // 写入操作码
		writeH(0x0070); // 写入操作码
		writeH(0); // 写入时间值
	}

	// 构造函数，接受账户名称作为参数
	public S_TamWindow(String account) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(ACCOUNT_TAM);
		writeC(0x01); // 未知用途的数据

		// 初始化数据库连接和查询变量
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		int _level = 0;
		int _class = 0;
		int _sex = 0;
		String _Name = null;
		Timestamp tamtime = null;
		int _objid = 0;
		int objidcount;
		int tamcount;
		long time = 0;
		long sysTime = System.currentTimeMillis();

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT objid, TamEndTime, level, char_name, Type, Sex FROM characters WHERE account_name=? ORDER BY TamEndTime DESC, EXP DESC");
			pstm.setString(1, account);
			rs = pstm.executeQuery();

			while (rs.next()) {
				tamcount = 0;
				objidcount = 0;
				time = 0;
				tamtime = null;

				_objid = rs.getInt("objid");
				_level = rs.getInt("level");
				_class = rs.getInt("Type");
				_sex = rs.getInt("Sex");
				_Name = rs.getString("char_name");
				tamtime = rs.getTimestamp("TamEndTime");

				if (tamtime != null) {
					if (sysTime < tamtime.getTime()) {
						time = tamtime.getTime() - sysTime;
					}
				}
				if (time == 0) {
					tamcount = 1;
				} else {
					tamcount = byteWriteCount(time / 1000);
				}
				objidcount = byteWriteCount(_objid);

				writeC(0x0a); // 封包标识符
				writeC(_Name.getBytes().length + 14 + objidcount + tamcount); // 封包总长度
				writeC(0x08); // 常量
				writeC(0x00); // 变化 (服务器可能会更改)
				writeC(0x10); // 常量
				byteWrite(_objid); // 写入 objid

				writeC(0x18); // 常量
				if (time == 0) {
					writeC(0x00); // 探索时间为0时
				} else {
					byteWrite(time / 1000); // 写入探索剩余时间
				}

				// 根据角色属性写入额外数据
				writeC(0x20);
				writeC(tamwaitcount(_objid)); // 这里写入 tamwaitcount 计算结果
				writeC(0x2a); // 常量
				writeC(_Name.getBytes().length); // 名字的长度
				writeByte(_Name.getBytes()); // 写入字节形式的名字
				writeC(0x30); // 常量
				writeC(_level); // 写入等级
				writeC(0x38); // 常量
				writeC(_class); // 写入类型
				writeC(0x40); // 常量
				writeC(_sex); // 写入性别
			}

			writeC(0x10); // 常量
			writeC(0x03); // 常量
			writeC(0x18); // 常量
			writeC(0x00); // 常量
			writeC(0x20); // 常量
			writeC(0x00); // 常量
			writeH(0); // 常量
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs); // 关闭 ResultSet
			SQLUtil.close(pstm); // 关闭 PreparedStatement
			SQLUtil.close(con); // 关闭 Connection
		}
	}

	// 其他方法和类型定义...

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return S_TAMWINDOW;
	}
}

	public static final int[] hextable = {
			0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89,
			0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90, 0x91, 0x92, 0x93,
			0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b, 0x9c, 0x9d,
			0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7,
			0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1,
			0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb,
			0xbc, 0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5,
			0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf,
			0xd0, 0xd1, 0xd2, 0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9,
			0xda, 0xdb, 0xdc, 0xdd, 0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3,
			0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 0xea, 0xeb, 0xec, 0xed,
			0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7,
			0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe, 0xff
	};

	private void byteWrite(long value) {
		long temp = value / 128;
		if (temp > 0) {
			writeC(hextable[(int) value % 128]);
			while (temp >= 128) {
				writeC(hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0) {
				writeC((int) temp);
			}
		} else {
			if (value == 0) {
				writeC(0);
			} else {
				writeC(hextable[(int) value]);
				writeC(0);
			}
		}
	}
	private int byteWriteCount(long value) {
		long temp = value / 128;
		int count = 0;
		if (temp > 0) {
			count++;
			while (temp >= 128) {
				count++;
				temp = temp / 128;
			}
			if (temp > 0) {
				count++;
			}
		} else {
			if (value == 0) {
				count++;
			} else {
				count += 2;
			}
		}
		return count;
	}
	private int tamwaitcount(int obj) {
		int count = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM tam WHERE objid = ?");



			pstm.setInt(1, obj);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return count;
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_TAMWINDOW;
	}
}


