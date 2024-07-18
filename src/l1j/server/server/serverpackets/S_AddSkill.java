/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.serverpackets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.SkillCheck;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_AddSkill extends ServerBasePacket {
	private static final String S_ADD_SKILL = "[S] S_AddSkill";

	public static final int ACTIVE_SKILL	= 0x0411;
	public static final int PASSIVE_LOGIN	= 0x0191;
	public static final int PASSIVE_LEARN	= 0x0192;
	private byte[] _byte = null;

	/** 當學習技能時 **/
	public S_AddSkill(int type, int skillnum, boolean on) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
			case ACTIVE_SKILL:
				try (BinaryOutputStream os = new BinaryOutputStream()) {
				// int skillId = getSkillId(skillnum);
					writeC(0x0a);
					writeC(os.getSize());
				// writeC(getBitSize(skillId) + 3); // 總長度

					writeC(0x08);
					writeBit(skillnum - 1);

					writeC(0x10);
					writeB(on); // 1: 添加, 0: 刪除
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case PASSIVE_LEARN:
				writeC(0x08);
				writeC(skillnum);
				if (!on) {
					writeC(0x10);
					writeC(0x00);

					writeC(0x18);
					writeC(0x00);
				} else if (skillnum == 5) { // 防禦技能
					writeC(0x10);
					writeC(0x0a);
				} else if (skillnum == 38) { // 靈魂技能
					writeC(0x18);
					writeC(0x00);
				}
				break;
		}
		writeH(0x00);
	}

	public S_AddSkill(int type, List<Integer> list) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch(type) {
        /*
        case ACTIVE_SKILL:
            for(int i = 0; i < list.size(); i++) {
                int skillId = getSkillId(list.get(i));
                writeC(0x0a);
                writeC(getBitSize(skillId) + 3); // 長度

                writeC(0x08);
                writeBit(skillId);

                writeC(0x10);
                writeC(0x01);
            }
            break;
        */
			case PASSIVE_LOGIN:
				for(int i = 0; i < list.size(); i++) {
					int passiveId = SkillsTable.getInstance().getTemplate(list.get(i)).getId();
					writeC(0x0a);
					writeC(passiveId != 5 && passiveId != 38 ? 0x02 : 0x04); // 長度
					writeC(0x08);
					writeC(passiveId);

					if(passiveId == 5) { // 防禦技能
						writeC(0x10);
						writeC(0x0a);
					} else if(passiveId == 38) { // 血之靈魂
						writeC(0x18);
						writeC(0x00);
					}
				}
				break;
		}
		writeH(0x00);
	}

	/** 登錄時 **/
	public S_AddSkill(L1PcInstance pc) {
		List<Integer> skillIdList = new ArrayList<Integer>();
		int count = 0;
		int Id = 0;

		int[] skillnum = null;

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		/** 獲取角色的技能信息 **/
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			L1Skills l1skills = null;
			while (rs.next()) {
				int skillId = rs.getInt("skill_id");
				l1skills = SkillsTable.getInstance().getTemplate(skillId);
		/** 加載主動技能 **/
				if (l1skills != null && l1skills.getSkillLevel() >= 0 && l1skills.getSkillLevel() <= 30) {
					skillIdList.add(skillId);
					pc.setSkillMastery(skillId); // 設置角色掌握的技能
					count++;
				}
				SkillCheck.getInstance().AddSkill(pc.getId(), skillIdList); // 添加技能到檢查器中
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

		/** 목록마다 정보를 불러온다 **/
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			skillnum = new int[count];

			for (int i = 0; i < count; ++i) {
				pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=? AND skill_id=?");
				Id = skillIdList.get(i);
				pstm.setInt(1, pc.getId());
				pstm.setInt(2, Id);
				rs = pstm.executeQuery();
				while (rs.next()) {
					skillnum[i] = rs.getInt("skill_id");
				}
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
			}
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x0411);
		for (int i = 0; i < count; ++i) {
			try (BinaryOutputStream os = new BinaryOutputStream()){
				os.writeC(0x08);
				os.writeBit(skillnum[i] - 1);

				os.writeC(0x10);
				os.writeC(0x01);

				writeC(0x0a);
				writeC(os.getSize()); // 토탈길이

				writeByte(os.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        writeH(0x00);
	}
	
	public S_AddSkill(int skillnum) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x0411);
		try (BinaryOutputStream os = new BinaryOutputStream()){
			os.writeC(0x08);
			os.writeBit(skillnum - 1);

			os.writeC(0x10);
			os.writeC(0x00);

			writeC(0x0a);
			writeC(os.getSize()); // 토탈길이

			writeByte(os.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
        writeH(0x00);
	}
	
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_ADD_SKILL;
	}

}
