package MJFX;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;

import MJFX.Util.MouseDelta;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class MJMDIPanelHelper {
	private BorderPane m_bpn;
	private Button m_btn_close;
	private Label m_lbl_title;
	private MouseDelta m_mouse_delta;
	private MouseDelta m_size_delta;
	private MouseDelta m_min_size_delta;
	
	private Region m_main_content;

	private Pane m_left_pane;
	private Pane m_top_pane;
	private Pane m_center_pane;
	private Pane m_right_pane;
	private Pane m_bottom_pane;
	private Pane m_se_size_pane;
	
	private CheckMenuItem m_menu_item;
	
	private double m_source_x;
	private double m_source_y;
	private double m_source_width;
	private double m_source_height;
	
	public MJMDIPanelHelper(BorderPane bpn, Region main_content, String title, CheckMenuItem menu_item){
		m_bpn = bpn;
		m_mouse_delta = new MouseDelta();
		m_size_delta = new MouseDelta();
		m_min_size_delta = new MouseDelta();
		setting_pane();
		m_btn_close = create_button("Î¼øÍ", event -> on_close_button_clicked(event));
		m_lbl_title = create_label(title);
		m_main_content = main_content;
		
		m_top_pane.getChildren().add(m_btn_close);
		m_top_pane.getChildren().add(m_lbl_title);
		m_top_pane.setOnMousePressed(event->on_top_mouse_pressed(event));
		m_top_pane.setOnMouseDragged(event->on_top_mouse_dragged(event));
		
		m_center_pane.getChildren().add(main_content);
		m_main_content.setLayoutX(0);
		m_main_content.setLayoutY(0);
		m_bpn.setPrefWidth(m_main_content.getPrefWidth() + 5 + 5);
		m_bpn.setPrefHeight(m_main_content.getPrefHeight() + 32 + 5);
		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		m_lbl_title.setLayoutX(m_bpn.getPrefWidth() - fontLoader.computeStringWidth(m_lbl_title.getText(), m_lbl_title.getFont()) - 8);
		
		m_se_size_pane = create_pane(5, 5, false, "-fx-background-color:transparent");
		m_bottom_pane.getChildren().add(m_se_size_pane);
		m_se_size_pane.setOnMousePressed(event->on_size_mouse_pressed(event));
		m_se_size_pane.setOnMouseDragged(event->on_size_mouse_dragged(event));
		m_se_size_pane.setCursor(Cursor.SE_RESIZE);
		
		m_min_size_delta.x = m_bpn.getPrefWidth();
		m_min_size_delta.y = m_bpn.getPrefHeight();
		for(Node node : m_main_content.getChildrenUnmodifiable()){
			node.setOnMouseClicked(event->on_child_clicked(event));
		}
		
		m_top_pane.setOnMouseClicked(event->on_child_clicked(event));
		m_center_pane.setOnMouseClicked(event->on_child_clicked(event));
		m_right_pane.setOnMouseClicked(event->on_child_clicked(event));
		m_bottom_pane.setOnMouseClicked(event->on_child_clicked(event));
		m_left_pane.setOnMouseClicked(event->on_child_clicked(event));
		m_menu_item = menu_item;
		if(m_menu_item != null){
			m_menu_item.selectedProperty().addListener(new ChangeListener<Boolean>(){
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					m_bpn.setVisible(m_menu_item.isSelected());
				}
			});
			m_menu_item.setSelected(true);
		}
	}
	
	public void append_child_handler(Region child){
		child.setOnMouseClicked(event->on_child_clicked(event));
	}
	
	public void on_child_clicked(MouseEvent event){
		if(event.getButton() == MouseButton.PRIMARY)
			m_bpn.toFront();
	}
	
	private void setting_pane(){
		m_bpn.setTop(m_top_pane = create_pane(1, 32, true, "-fx-background-color:linear-gradient(to bottom, #454548, #202023);"));
		m_bpn.setLeft(m_left_pane = create_pane(5, 1, false, "-fx-background-color:#202023"));
		m_bpn.setRight(m_right_pane = create_pane(5, 1, false, "-fx-background-color:#202023"));
		m_bpn.setBottom(m_bottom_pane = create_pane(1, 5, true, "-fx-background-color:#202023"));
		m_bpn.setCenter(m_center_pane = create_pane(1, 1, false,"-fx-background-color:#303033"));
	}
	
	private Pane create_pane(int width, int height, boolean is_orientation, String color){
		Pane pnl = new Pane();
		pnl.setPrefWidth(width);
		pnl.setPrefHeight(height);
		pnl.setStyle(color);		
		if(is_orientation)
			pnl.nodeOrientationProperty().set(NodeOrientation.RIGHT_TO_LEFT);
		return pnl;
	}
	
	private Button create_button(String text, EventHandler<? super MouseEvent> handler){
		Button btn = new Button();
		btn.setText(text);
		btn.setOnMouseClicked(handler);
		btn.setOnMouseEntered(event->on_button_enter(event));
		btn.setOnMouseExited(event->on_button_leave(event));
		btn.setLayoutY(4);
		btn.setStyle("-fx-background-color:transparent");
		btn.setTextFill(Color.WHITE);
		btn.setLayoutX(3);
		btn.setCursor(Cursor.HAND);
		return btn;
	}
	
	private Label create_label(String text){
		Label lbl = new Label();
		lbl.setText(text);
		lbl.setLayoutY(8);
		lbl.setLayoutX(8);
		lbl.setStyle("-fx-font-weight: bold;-fx-background-color:transparent;-fx-text-fill: white;");
		return lbl;
	}
	
	public void on_top_mouse_pressed(MouseEvent event){
		m_bpn.toFront();
		m_mouse_delta.x = m_bpn.getLayoutX() - event.getScreenX();
		m_mouse_delta.y = m_bpn.getLayoutY() - event.getScreenY();
	}
	
	public void on_top_mouse_dragged(MouseEvent event){
		m_bpn.setLayoutX(event.getScreenX() + m_mouse_delta.x);
		m_bpn.setLayoutY(event.getScreenY() + m_mouse_delta.y);
	}
	
	public void on_size_mouse_pressed(MouseEvent event){
		m_bpn.toFront();
		m_mouse_delta.x = event.getScreenX();
		m_mouse_delta.y = event.getScreenY();
		m_size_delta.x = m_bpn.getPrefWidth();
		m_size_delta.y = m_bpn.getPrefHeight();
	}
	
	public void on_size_mouse_dragged(MouseEvent event){
		double calculate_width = m_size_delta.x + (event.getScreenX() - m_mouse_delta.x);
		double calculate_height = m_size_delta.y + (event.getScreenY() - m_mouse_delta.y);
		calculate_width = Math.max(calculate_width, m_min_size_delta.x);
		calculate_height = Math.max(calculate_height, m_min_size_delta.y);
		if(calculate_width > 1024 || calculate_height > 1024)
			return;
		
		update_size(calculate_width, calculate_height);
	}
	
	public void on_button_enter(MouseEvent event){
		Button btn = (Button)event.getSource();
		btn.setStyle("-fx-background-color:#555558");
	}
	public void on_button_leave(MouseEvent event){
		Button btn = (Button)event.getSource();
		btn.setStyle("-fx-background-color:transparent");		
	}
	
	public void on_close_button_clicked(MouseEvent event){
		if(event.getButton() == MouseButton.PRIMARY){
			if(m_menu_item != null)
				m_menu_item.setSelected(false);
			m_bpn.setVisible(false);
		}
	}
	
	public boolean is_visible(){
		return m_bpn.isVisible();
	}
	
	public void set_left(double val){
		m_bpn.setLayoutX(val);
	}
	public double get_left(){
		return m_bpn.getLayoutX();
	}
	public void set_top(double val){
		m_bpn.setLayoutY(val);
	}
	public double get_top(){
		return m_bpn.getLayoutY();
	}
	public void set_width(double val){
		m_bpn.setPrefWidth(val);
	}
	public double get_width(){
		return m_bpn.getPrefWidth();
	}
	public void set_height(double val){
		m_bpn.setPrefHeight(val);
	}
	public double get_height(){
		return m_bpn.getPrefHeight();
	}
	public void set_title(String s){
		String old = m_lbl_title.getText();
		m_lbl_title.setText(s);
		if(old.length() == s.length())
			return;
		
		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		m_lbl_title.setLayoutX(m_bpn.getPrefWidth() - fontLoader.computeStringWidth(m_lbl_title.getText(), m_lbl_title.getFont()) - 8);
	}
	public void store_current_position(){
		m_source_x = m_bpn.getLayoutX();
		m_source_y = m_bpn.getLayoutY();
		m_source_width = m_bpn.getPrefWidth();
		m_source_height = m_bpn.getPrefHeight();
	}
	public void restore_current_position(){
		m_bpn.setLayoutX(m_source_x);
		m_bpn.setLayoutY(m_source_y);
		m_bpn.prefWidth(m_source_width);
		update_size(m_source_width, m_source_height);
	}
	
	private void update_size(double width, double height){
		m_bpn.setPrefWidth(width);
		m_bpn.setPrefHeight(height);
		m_center_pane.setPrefWidth(m_bpn.getPrefWidth() - 5 - 5);
		m_center_pane.setPrefHeight(m_bpn.getPrefHeight() - 32 - 5);
		m_main_content.setPrefWidth(m_center_pane.getPrefWidth());
		m_main_content.setPrefHeight(m_center_pane.getPrefHeight());
		FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
		m_lbl_title.setLayoutX(m_bpn.getPrefWidth() - fontLoader.computeStringWidth(m_lbl_title.getText(), m_lbl_title.getFont()) - 8);
	}
}
