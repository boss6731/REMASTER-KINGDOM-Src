         package l1j.server.server.serverpackets;

         import java.io.IOException;
         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import java.sql.SQLException;
         import java.util.ArrayList;
         import java.util.List;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.server.SkillCheck;
         import l1j.server.server.datatables.SkillsTable;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.templates.L1Skills;
         import l1j.server.server.utils.BinaryOutputStream;
         import l1j.server.server.utils.SQLUtil;


         public class S_AddSkill
           extends ServerBasePacket
         {
           private static final String S_ADD_SKILL = "[S] S_AddSkill";
           public static final int ACTIVE_SKILL = 1041;
           public static final int PASSIVE_LOGIN = 401;
           public static final int PASSIVE_LEARN = 402;
           private byte[] _byte;

           public S_AddSkill(int type, int skillnum, boolean on) {
             this._byte = null;



             writeC(19);
             writeH(type);
             switch (type) {
               case 1041:
                 try (BinaryOutputStream os = new BinaryOutputStream()) {

                   writeC(10);
                   writeC(os.getSize());


                   writeC(8);
                   writeBit((skillnum - 1));

                   writeC(16);
                   writeB(on);
                 } catch (IOException e) {
                   e.printStackTrace();
                 }
                 break;
               case 402:
                 writeC(8);
                 writeC(skillnum);
                 if (!on) {
                   writeC(16);
                   writeC(0);

                   writeC(24);
                   writeC(0); break;
                 }  if (skillnum == 5) {
                   writeC(16);
                   writeC(10); break;
                 }  if (skillnum == 38) {
                   writeC(24);
                   writeC(0);
                 }
                 break;
             }
             writeH(0);
           } public S_AddSkill(int type, List<Integer> list) {
             int i;
             this._byte = null;
             writeC(19);
             writeH(type);
             switch (type) {

               case 401:
                 for (i = 0; i < list.size(); i++) {
                   int passiveId = SkillsTable.getInstance().getTemplate(((Integer)list.get(i)).intValue()).getId();
                   writeC(10);
                   writeC((passiveId != 5 && passiveId != 38) ? 2 : 4);
                   writeC(8);
                   writeC(passiveId);
                   if (passiveId == 5) {
                     writeC(16);
                     writeC(10);
                   } else if (passiveId == 38) {
                     writeC(24);
                     writeC(0);
                   }
                 }
                 break;
             }
             writeH(0);
           }

           public S_AddSkill(L1PcInstance pc) {
             this._byte = null;
             List<Integer> skillIdList = new ArrayList<>();
             int count = 0;
             int Id = 0;

             int[] skillnum = null;

             Connection con = null;
             PreparedStatement pstm = null;
             ResultSet rs = null;



             try { con = L1DatabaseFactory.getInstance().getConnection();
               pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=?");
               pstm.setInt(1, pc.getId());
               rs = pstm.executeQuery();

               L1Skills l1skills = null;
               while (rs.next()) {
                 int skillId = rs.getInt("skill_id");
                 l1skills = SkillsTable.getInstance().getTemplate(skillId);

                 if (l1skills != null && l1skills.getSkillLevel() >= 0 && l1skills.getSkillLevel() <= 30) {
                   skillIdList.add(Integer.valueOf(skillId));
                   pc.setSkillMastery(skillId);
                   count++;
                 }
                 SkillCheck.getInstance().AddSkill(pc.getId(), skillIdList);
               }  }
             catch (SQLException sQLException) {  }
             finally
             { SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con); }


             try { con = L1DatabaseFactory.getInstance().getConnection();
               skillnum = new int[count];

               for (int j = 0; j < count; j++) {
                 pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=? AND skill_id=?");
                 Id = ((Integer)skillIdList.get(j)).intValue();
                 pstm.setInt(1, pc.getId());
                 pstm.setInt(2, Id);
                 rs = pstm.executeQuery();
                 while (rs.next()) {
                   skillnum[j] = rs.getInt("skill_id");
                 }
                 SQLUtil.close(rs);
                 SQLUtil.close(pstm);
               }  }
             catch (SQLException sQLException) {  }
             finally
             { SQLUtil.close(rs);
               SQLUtil.close(pstm);
               SQLUtil.close(con); }


             writeC(19);
             writeH(1041);
             for (int i = 0; i < count; i++) {
               try (BinaryOutputStream os = new BinaryOutputStream()) {
                 os.writeC(8);
                 os.writeBit((skillnum[i] - 1));

                 os.writeC(16);
                 os.writeC(1);

                 writeC(10);
                 writeC(os.getSize());

                 writeByte(os.getBytes());
               } catch (IOException e) {
                 e.printStackTrace();
               }
             }
             writeH(0);
           }
           public S_AddSkill(int skillnum) {
             this._byte = null;
             writeC(19);
             writeH(1041);
             try (BinaryOutputStream os = new BinaryOutputStream()) {
               os.writeC(8);
               os.writeBit((skillnum - 1));

               os.writeC(16);
               os.writeC(0);

               writeC(10);
               writeC(os.getSize());

               writeByte(os.getBytes());
             } catch (IOException e) {
               e.printStackTrace();
             }
             writeH(0);
           }



           public byte[] getContent() {
             if (this._byte == null) {
               this._byte = this._bao.toByteArray();
             }
             return this._byte;
           }


           public String getType() {
             return "[S] S_AddSkill";
           }
         }


