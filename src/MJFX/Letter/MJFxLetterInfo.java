package MJFX.Letter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MJFxLetterInfo {
	public static MJFxLetterInfo newInstance(int no, String sender, String title, String content, String date, String receiver){
		return new MJFxLetterInfo(
				no,
				sender,
				title,
				content,
				date,
				false,
				receiver
				);
	}
	public static MJFxLetterInfo newInstance(ResultSet rs, String gm_name) throws SQLException{
		return new MJFxLetterInfo(
				rs.getInt("item_object_id"),
				rs.getString("sender"),
				rs.getString("subject"),
				rs.getString("content"),
				rs.getString("date"),
				rs.getInt("isCheck") != 0,
				gm_name
				);
	}
	
	private final SimpleIntegerProperty colNo;
	private final SimpleStringProperty colSender;
	private final SimpleStringProperty colTitle;
	private final SimpleStringProperty colContent;
	private final SimpleStringProperty colDate;
	private final SimpleStringProperty colChecked;
	private String m_gm_name;
	private boolean m_is_checked;
	private MJFxLetterInfo(int id, String sender, String title, String content, String date, boolean is_checked, String gm_name){
		colNo = new SimpleIntegerProperty(id);
		colSender = new SimpleStringProperty(sender);
		colTitle = new SimpleStringProperty(title);
		colContent = new SimpleStringProperty(content);
		colDate = new SimpleStringProperty(date);
		colChecked = new SimpleStringProperty(is_checked ? "O" : "X");
		m_gm_name = gm_name;
		m_is_checked = is_checked;
	}
	
	public Integer getColNo(){
		return colNo.get();
	}
	public String getColSender(){
		return colSender.get();
	}
	public String getColTitle(){
		return colTitle.get();
	}
	public String getColContent(){
		return colContent.get();
	}
	public String getColDate(){
		return colDate.get();
	}
	public String getColChecked(){
		return colChecked.get();
	}
	public String get_gm_name(){
		return m_gm_name;
	}
	public boolean is_checked(){
		return m_is_checked;
	}
	public void on_readed(){
		m_is_checked = true;
		colChecked.set("O");
	}
	
	public SimpleIntegerProperty colNoProperty(){
		return colNo;
	}
	public SimpleStringProperty colSenderProperty(){
		return colSender;
	}
	public SimpleStringProperty colTitleProperty(){
		return colTitle;
	}
	public SimpleStringProperty colContentProperty(){
		return colContent;
	}
	public SimpleStringProperty colDateProperty(){
		return colDate;
	}
	public SimpleStringProperty colCheckedProperty(){
		return colChecked;
	}
}
