package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.utils.SQLUtil;

public class S_Chainfo extends ServerBasePacket {

	private static final String S_Chainfo = "[C] S_Chainfo";

	private static Logger _log = Logger.getLogger(S_Chainfo.class.getName());

	private byte[] _byte = null;

	public S_Chainfo(int number, String cha) {
		buildPacket(number, cha);
	}

	private void buildPacket(int number, String cha) {
		Connection con = null;
		Connection con1 = null;
		Connection con2 = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String info1 = null;//從這裡開始定義變數
		int per = 0;
		String clas = null;
		String ggg = null;
		int oo = 0;
		String ggg1 = null;
		int oo1 = 0;
		String ggg2 = null;
		int oo2 = 0;
		String ggg3 = null;
		int oo3 = 0;
		String ggg4 = null;
		int oo4 = 0;
		String ggg5 = null;
		int oo5 = 0;
		String ggg6 = null;
		int oo6 = 0;
		String ggg7 = null;
		int oo7 = 0;
		String ggg8 = null;
		int oo8 = 0;
		String ggg9 = null;
		int oo9 = 0;
		String ggg10 = null;
		int oo10 = 0;
		String ggg11 = null;
		int oo11 = 0;
		String ggg12 = null;
		int oo12 = 0;
		String ggg13 = null;
		int oo13 = 0;
		
		int rol= 0;
		int	info2 = 0;
		int	info3 = 0;
		int	info4 = 0;
		int	info5 = 0;
		int	info11 = 0;//到此為止
		int x = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, cha);
			rs = pstm.executeQuery();
			while (rs.next()) {
				info1 = rs.getString(2);// 用角色對象ID檢查 <-- 這在下面檢查物品時需要
				info2 = rs.getInt(4);// 等級
				per = rs.getInt(6); // 經驗值
				info3 = rs.getInt(7);// HP
				info4 = rs.getInt(9);// MP
				info5 = rs.getInt(11);// AC
				info11 = rs.getInt(25);// 類別
			}
			con2 = L1DatabaseFactory.getInstance().getConnection();
			pstm2 = con2.prepareStatement("SELECT * FROM character_items WHERE char_id=? AND item_name=?");
			pstm2.setString(1, info1);
			pstm2.setString(2, "金幣");
			rs2 = pstm2.executeQuery();
			
			while (rs2.next()) {
			rol = rs2.getInt(5);
			}
			
			con1 = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con1.prepareStatement("SELECT `enchantlvl`,`item_name` FROM `character_items` WHERE char_id=? ORDER BY `enchantlvl` DESC LIMIT 14");
			pstm1.setString(1, info1);// 用上面檢查過的角色對象再次進行查詢檢查
			rs1 = pstm1.executeQuery();
			while (rs1.next()) {
				++x;
				if (x == 1) {
					ggg = rs1.getString("item_name");// 物品名稱
					oo = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 2) {
					ggg1 = rs1.getString("item_name");// 物品名稱
					oo1 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 3) {
					ggg2 = rs1.getString("item_name");// 物品名稱
					oo2 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 4) {
					ggg3 = rs1.getString("item_name");// 物品名稱
					oo3 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 5) {
					ggg4 = rs1.getString("item_name");// 物品名稱
					oo4 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 6) {
					ggg5 = rs1.getString("item_name");// 物品名稱
					oo5 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 7) {
					ggg6 = rs1.getString("item_name");// 物品名稱
					oo6 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 8) {
					ggg7 = rs1.getString("item_name");// 物品名稱
					oo7 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 9) {
					ggg8 = rs1.getString("item_name");// 物品名稱
					oo8 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 10) {
					ggg9 = rs1.getString("item_name");// 物品名稱
					oo9 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 11) {
					ggg10 = rs1.getString("item_name");// 物品名稱
					oo10 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 12) {
					ggg11 = rs1.getString("item_name");// 物品名稱
					oo11 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 13) {
					ggg12 = rs1.getString("item_name");// 物品名稱
					oo12 = rs1.getInt("enchantlvl");// 這是附魔等級
				} else if (x == 14) {
					ggg13 = rs1.getString("item_name");// 物品名稱
					oo13 = rs1.getInt("enchantlvl");// 這是附魔等級
				}
			}
			if(info11 == 0 || info11 == 1){
				clas = "王族";
			}else if(info11 == 48 || info11 == 20553){
				clas = "騎士";
			}else if(info11 == 37 || info11 == 138){
				clas = "妖精";
			}else if(info11 == 20278 || info11 == 20279){
				clas = "魔法師";
			}else if(info11 == 2786 || info11 == 2796){
				clas = "黑暗妖精";
			}else if(info11 == 6658 || info11 == 6661){
				clas = "龍騎士";
			}else if(info11 == 6671 || info11 == 6650){
				clas = "幻術師";
			}else if(info11 == 20567 || info11 == 20577){
				clas = "戰士";
			}else if(info11 == 18520 || info11 == 18499){
				clas = "劍士";
			}else if(info11 == 19296 || info11 == 19296){
				clas = "黃金槍騎";
			}
			    
				int lv = info2;
				long currentLvExp = ExpTable.getExpByLevel(lv);
				long nextLvExp = ExpTable.getExpByLevel(lv + 1);
				double neededExp = nextLvExp - currentLvExp ;
				double currentExp = per - currentLvExp;
				int per1 = (int)((currentExp / neededExp) * 100.0);
				
                writeC(Opcodes.S_BOARD_READ);
			writeD(number);// 數字
			writeS("角色信息");// 作者?
			writeS("");// 日期?
			writeS(cha);// 標題?
			writeS(" <" + cha +
					" 信息>\n等級: " + info2 + "." + per1 + "   " +
					"職業: " + clas + "\nHP:" + info3 + "  MP:" + info4 + " " +
					"AC:" + info5 + "\n金幣:" + rol + "\n+" + oo + " " + ggg +
					"\n+" + oo1 +" "+ggg1+"\n +"+oo2+" "+ggg2+"\n +"+oo3+" "+ggg3+
					"\n +"+oo4+" "+ggg4+"\n +"+oo5+" "+ggg5+"\n +"+oo6+
					" "+ggg6+"\n +"+oo7+" "+ggg7+"\n +"+oo8+" "+ggg8+
					"\n +"+oo9+" "+ggg9+"\n +"+oo10+" "+ggg10+"\n +"+oo11+
					" "+ggg11+"\n +"+oo12+" "+ggg12+"\n +"+oo13+" "+ggg13);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
			SQLUtil.close(rs1);
			SQLUtil.close(pstm1);
			SQLUtil.close(con1);
			SQLUtil.close(rs2);
			SQLUtil.close(pstm2);
			SQLUtil.close(con2);
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_Chainfo;
	}
}

