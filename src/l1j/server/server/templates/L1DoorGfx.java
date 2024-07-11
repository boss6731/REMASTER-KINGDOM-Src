 package l1j.server.server.templates;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public class L1DoorGfx
 {
   private final int _gfxId;
   private final int _direction;
   private final int _rightEdgeOffset;
   private final int _leftEdgeOffset;

   public L1DoorGfx(int gfxId, int direction, int rightEdgeOffset, int leftEdgeOffset) {
     this._gfxId = gfxId;
     this._direction = direction;
     this._rightEdgeOffset = rightEdgeOffset;
     this._leftEdgeOffset = leftEdgeOffset;
   }

   public int getGfxId() {
     return this._gfxId;
   }

   public int getDirection() {
     return this._direction;
   }

   public int getRightEdgeOffset() {
     return this._rightEdgeOffset;
   }

   public int getLeftEdgeOffset() {
     return this._leftEdgeOffset;
   }

   public static L1DoorGfx findByGfxId(int gfxId) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("SELECT * FROM door_gfxs WHERE gfx_id = ?");
       pstm.setInt(1, gfxId);
       rs = pstm.executeQuery();
       if (!rs.next()) {
         return null;
       }
       int id = rs.getInt("gfx_id");
       int dir = rs.getInt("direction");
       int rEdge = rs.getInt("right_edge_offset");
       int lEdge = rs.getInt("left_edge_offset");
       return new L1DoorGfx(id, dir, rEdge, lEdge);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return null;
   }
 }


