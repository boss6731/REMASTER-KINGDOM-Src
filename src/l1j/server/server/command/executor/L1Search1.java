package l1j.server.server.server.command.executor;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

public class L1Search1 implements L1CommandExecutor {
	
	private L1Search1(){ }
	
	public static L1CommandExecutor getInstance(){
		return new L1Search1();
	}
	
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			int type = Integer.parseInt(tok.nextToken()) ;
			String real_name_id_view = tok.nextToken() ;
			searchObject(pc, type, "%" + real_name_id_view + "%") ;			
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(".搜尋 [0~5] [name] 請輸入。")));
			pc.sendPackets(String.valueOf(new S_SystemMessage("0=雜物, 1=武器, 2=盔甲, 3=NPC, 4=變身, 5=NPC(gfxid)")));
		}
	}
	
	private void searchObject( L1PcInstance gm, int type, String real_name_id_view ) {
		java.sql.Connection con = null ;
		PreparedStatement statement = null ;
		ResultSet rs  = null;
		
		try {
			String str1 = null ;
			String str2 = null ;
			String s_note = null ;
			int count = 0 ;
			con = L1DatabaseFactory.getInstance().getConnection() ;
			switch (type) {
			case 0://etcitem
				statement = con.prepareStatement("select item_id, real_name_id_view, real_name_id from etcitem where real_name_id_view Like '" + real_name_id_view + "'");
				break;
			case 1://weapon
				statement = con.prepareStatement("select item_id, real_name_id_view, real_name_id from weapon where real_name_id_view Like '" + real_name_id_view + "'");
				break;
			case 2: // armor
				statement = con.prepareStatement("select item_id, real_name_id_view, real_name_id from armor where real_name_id_view Like '" + real_name_id_view + "'");
				break;
			case 3: // npc
				statement = con.prepareStatement("select npcid, desc_view, note from npc where desc_view Like '" + real_name_id_view + "'");
				break;
			case 4: // polymorphs
				statement = con.prepareStatement("select polyid, name,id from polymorphs where name Like '" + real_name_id_view + "'");
				break;
			case 5: // npc(gfxid)
				statement = con.prepareStatement("select sprite_id, desc_view,note from npc where desc_view Like '" + real_name_id_view + "'");
				break;
			default:
				break;
			}
			rs = statement.executeQuery();
			while (rs.next()) {
				str1 = rs.getString(1);
				str2 = rs.getString(2);
				s_note = rs.getString(3);
				gm.sendPackets(String.valueOf(new S_SystemMessage("[" + str1 + "]--[" + str2 + "]--" + s_note)));
				count++ ;
			}
			gm.sendPackets(String.valueOf(new S_SystemMessage("總共 [" + count + "] 件被搜尋到。")));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, statement, con);
		}
	}

}
