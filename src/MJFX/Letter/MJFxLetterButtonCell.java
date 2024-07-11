 package MJFX.Letter;

 import com.sun.prism.impl.Disposer;
 import javafx.scene.Node;
 import javafx.scene.control.Button;
 import javafx.scene.control.TableCell;
 import javafx.scene.input.MouseEvent;
 import l1j.server.server.datatables.LetterTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_LetterList;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class MJFxLetterButtonCell
   extends TableCell<Disposer.Record, Boolean> {
   public MJFxLetterButtonCell() {
     this.m_btn = new Button("刪除");
     this.m_btn.setOnMouseClicked(event -> on_mouse_clicked(event));
   }
   private final Button m_btn;
   public void on_mouse_clicked(MouseEvent event) {
     MJFxLetterInfo letter = getTableView().getItems().get(getIndex());
     MJFxLetterManager.getInstance().on_delete_letter(letter.getColNo().intValue());
     LetterTable.getInstance().deleteLetter(letter.getColNo().intValue());

     L1PcInstance pc = L1World.getInstance().getPlayer(letter.get_gm_name());
     if (pc != null) {
       pc.sendPackets((ServerBasePacket)new S_LetterList(pc, 16, letter.getColNo().intValue(), true));
     }
   }



   protected void updateItem(Boolean t, boolean empty) {
     super.updateItem(t, empty);
     if (!empty) {
       setGraphic(this.m_btn);
     } else {
       setGraphic((Node)null);
     }
   }
 }


