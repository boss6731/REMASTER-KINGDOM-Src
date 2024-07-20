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

public class MJFxLetterButtonCell extends TableCell<Disposer.Record, Boolean> {

	private final Button m_btn;

	public MJFxLetterButtonCell() {
		this.m_btn = new Button("刪除");
		this.m_btn.setOnMouseClicked(event -> on_mouse_clicked(event));
	}

	// 當按鈕被點擊時觸發的事件處理方法
	public void on_mouse_clicked(MouseEvent event) {
		// 獲取當前行的信件信息
		MJFxLetterInfo letter = getTableView().getItems().get(getIndex());
		// 刪除信件
		MJFxLetterManager.getInstance().on_delete_letter(letter.getColNo().intValue());
		LetterTable.getInstance().deleteLetter(letter.getColNo().intValue());

		// 給玩家發送更新的信件列表
		L1PcInstance pc = L1World.getInstance().getPlayer(letter.get_gm_name());
		if (pc != null) {
			pc.sendPackets((ServerBasePacket) new S_LetterList(pc, 16, letter.getColNo().intValue(), true));
		}
	}

	// 更新表格單元格的方法	@override
	
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (!empty) {
			setGraphic(this.m_btn); // 如果單元格不為空，設置按鈕作為圖形顯示
		} else {
			setGraphic((Node) null); // 如果單元格為空，設置為空
		}
	}
}


