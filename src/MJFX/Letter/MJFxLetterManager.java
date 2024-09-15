package MJFX.Letter;

import MJFX.MJFxEntry;
import com.sun.prism.impl.Disposer.Record;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.clientpackets.C_MailBox;
import l1j.server.server.utils.MJCommons;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

public class MJFxLetterManager {
	private static final String[] GM_NAMES = new String[]{
			"梅蒂斯", "微笑菲亞", "卡西歐佩亞"
	};
	
	private static MJFxLetterManager _instance;
	public static MJFxLetterManager getInstance(){
		if(_instance == null)
			_instance = new MJFxLetterManager();
		return _instance;
	}
	
	private VBox m_container;
	private TableView<MJFxLetterInfo> m_tv;
	private ObservableList<MJFxLetterInfo> m_letters;
	private ConcurrentHashMap<Integer, MJFxLetterInfo> m_letters_map;    // 儘量減少 UI 的訪問
	public void initialize(VBox container, TableView<MJFxLetterInfo> tv){
		m_container = container;
		m_letters = FXCollections.observableArrayList();
		m_letters_map = new ConcurrentHashMap<Integer, MJFxLetterInfo>();
		m_tv = tv;
		m_tv.setFixedCellSize(25);
		m_tv.setPlaceholder(new Label("沒有收到的信件。"));
		m_tv.prefHeightProperty().bind(m_container.heightProperty());
		MJFxLetterManager.<Integer>insert_column(m_tv, "colNo", 0.08, "編號");
		MJFxLetterManager.<String>insert_column(m_tv, "colSender", 0.15, "寄件人");
		MJFxLetterManager.<String>insert_column(m_tv, "colTitle", 0.15, "標題");
		MJFxLetterManager.<String>insert_column(m_tv, "colContent", 0.25, "內容");
		MJFxLetterManager.<String>insert_column(m_tv, "colDate", 0.15, "寄件日期");
		MJFxLetterManager.<String>insert_column(m_tv, "colChecked", 0.08, "已讀");
		insert_delete_button_column(m_tv, "colDelete", 0.12, "刪除");

		m_tv.setRowFactory(ttv -> {
			final TableRow<MJFxLetterInfo> row = new TableRow<MJFxLetterInfo>();
			row.setOnMouseClicked(event -> on_row_clicked(event));
			return row;
		});
		m_tv.setItems(m_letters);
	}
	
	private void on_row_clicked(MouseEvent event){
		@SuppressWarnings("unchecked")
		TableRow<MJFxLetterInfo> row = (TableRow<MJFxLetterInfo>)event.getSource();
		if(event.getClickCount() != 2 || event.getButton() != MouseButton.PRIMARY || row.isEmpty())
			return;
		
		try{
			MJFxLetterInfo letterInfo = row.getItem();
			if(letterInfo == null)
				return;
			
			MJFxEntry.getInstance().show_letter_window(letterInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static <T> void insert_column(TableView<MJFxLetterInfo> tv, String column_name, double multiply, String text){
		TableColumn<MJFxLetterInfo, T> col = new TableColumn<MJFxLetterInfo, T>();
		tv.getColumns().add(col);
		col.setText(text);
		col.setCellValueFactory(new PropertyValueFactory<MJFxLetterInfo, T>(column_name));
		col.prefWidthProperty().bind(tv.widthProperty().multiply(multiply));
		col.setResizable(false);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void insert_delete_button_column(TableView<MJFxLetterInfo> tv, String column_name, double multiply, String text){
		TableColumn col = new TableColumn();
		tv.getColumns().add(col);
		col.setStyle("-fx-padding: 0 0 0 0;");
		col.setText(text);
		col.prefWidthProperty().bind(tv.widthProperty().multiply(multiply));
		col.setResizable(false);
		col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>(){
			@Override
			public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> param) {
				return new SimpleBooleanProperty(param.getValue() != null);
			}
		});
		col.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>(){
			@Override
			public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> param) {
				return new MJFxLetterButtonCell();
			}
		});
	}
	
	public void load_letters(){
		m_letters.clear();
		m_letters_map.clear();
		for(final String name : GM_NAMES){
			Selector.exec("select * from letter where receiver=? and template_id=? order by date limit 100", new SelectorHandler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setString(1, name);
					pstm.setInt(2, C_MailBox.TYPE_PRIVATE_MAIL);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					while(rs.next()){
						MJFxLetterInfo lInfo = MJFxLetterInfo.newInstance(rs, name);
						m_letters_map.put(lInfo.getColNo(), lInfo);
						m_letters.add(lInfo);
					}
				}
			});	
		}
	}
	
	private boolean is_gm_name(String receiver){
		for(final String name : GM_NAMES){
			if(name.equalsIgnoreCase(receiver))
				return true;
		}
		return false;
	}
	
	public void on_check_letter(int id){
		final MJFxLetterInfo lInfo = m_letters_map.get(id);
		if(lInfo == null)
			return;
		
		Platform.runLater(()->{
			lInfo.on_readed();
		});
	}
	
	public void on_delete_letter(int id){
		final MJFxLetterInfo lInfo = m_letters_map.remove(id);
		if(lInfo == null)
			return;
		
		Platform.runLater(()->{
			m_letters.remove(lInfo);
		});
	}
	
	public void on_receive_letter(int id, String sender, String title, String content, Timestamp ts, String receiver){
		if(!is_gm_name(receiver))
			return;
		
		final MJFxLetterInfo lInfo = MJFxLetterInfo.newInstance(id, sender, title, content, MJCommons.to_string(ts), receiver);
		m_letters_map.put(lInfo.getColNo(), lInfo);
		Platform.runLater(()->{
			m_letters.add(lInfo);
		});
	}
}
