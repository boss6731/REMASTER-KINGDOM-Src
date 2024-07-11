 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

















 public class S_OpcodeTest
   extends ServerBasePacket
 {
   private static final String S_OpcodeTest = "[C]  S_OpcodeTest";
   private final L1PcInstance _char;
   private final int _opcode;
   private final int _type;

   public S_OpcodeTest(L1PcInstance character, int oCODE, int type) {
     this._char = character;
     this._opcode = oCODE;
     this._type = type;

     writeC(this._opcode);

     if (this._type == 0) {
       writeD(this._char.getId());
     } else {
       writeD(1);
     }
   }




   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[C]  S_OpcodeTest";
   }
 }


