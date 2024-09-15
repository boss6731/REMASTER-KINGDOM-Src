package MJFX.Letter;

import com.sun.prism.impl.Disposer.Record;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import l1j.server.server.clientpackets.C_MailBox;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LetterList;

public class MJFxLetterButtonCell extends TableCell<Record, Boolean>{
	private final Button m_btn;

	public MJFxLetterButtonCell() {
		m_btn = new Button("刪除");
		m_btn.setOnMouseClicked(event -> on_mouse_clicked(event));
	}
	
	public void on_mouse_clicked(MouseEvent event){
		MJFxLetterInfo letter = (MJFxLetterInfo)getTableView().getItems().get(getIndex());
		MJFxLetterManager.getInstance().on_delete_letter(letter.getColNo());
		LetterTable.getInstance().deleteLetter(letter.getColNo());

		L1PcInstance pc = L1World.getInstance().getPlayer(letter.get_gm_name());
		if(pc != null) {
			pc.sendPackets(new S_LetterList(pc, C_MailBox.READ_PRIVATE_MAIL, letter.getColNo(), true));
		}
		
	}
	
	@Override
	protected void updateItem(Boolean t, boolean empty){
		super.updateItem(t, empty);
		if(!empty){
			setGraphic(m_btn);
		}else
			setGraphic(null);
	}
}
