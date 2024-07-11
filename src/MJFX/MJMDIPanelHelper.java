 package MJFX;

 import MJFX.Util.MouseDelta;
 import com.sun.javafx.tk.FontLoader;
 import com.sun.javafx.tk.Toolkit;
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





 public class MJMDIPanelHelper
 {
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

   public MJMDIPanelHelper(BorderPane bpn, Region main_content, String title, CheckMenuItem menu_item) {
     this.m_bpn = bpn;
     this.m_mouse_delta = new MouseDelta();
     this.m_size_delta = new MouseDelta();
     this.m_min_size_delta = new MouseDelta();
     setting_pane();
     this.m_btn_close = create_button("關閉", event -> on_close_button_clicked(event));
     this.m_lbl_title = create_label(title);
     this.m_main_content = main_content;

     this.m_top_pane.getChildren().add(this.m_btn_close);
     this.m_top_pane.getChildren().add(this.m_lbl_title);
     this.m_top_pane.setOnMousePressed(event -> on_top_mouse_pressed(event));
     this.m_top_pane.setOnMouseDragged(event -> on_top_mouse_dragged(event));

     this.m_center_pane.getChildren().add(main_content);
     this.m_main_content.setLayoutX(0.0D);
     this.m_main_content.setLayoutY(0.0D);
     this.m_bpn.setPrefWidth(this.m_main_content.getPrefWidth() + 5.0D + 5.0D);
     this.m_bpn.setPrefHeight(this.m_main_content.getPrefHeight() + 32.0D + 5.0D);
     FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
     this.m_lbl_title.setLayoutX(this.m_bpn.getPrefWidth() - fontLoader.computeStringWidth(this.m_lbl_title.getText(), this.m_lbl_title.getFont()) - 8.0D);

     this.m_se_size_pane = create_pane(5, 5, false, "-fx-background-color:transparent");
     this.m_bottom_pane.getChildren().add(this.m_se_size_pane);
     this.m_se_size_pane.setOnMousePressed(event -> on_size_mouse_pressed(event));
     this.m_se_size_pane.setOnMouseDragged(event -> on_size_mouse_dragged(event));
     this.m_se_size_pane.setCursor(Cursor.SE_RESIZE);

     this.m_min_size_delta.x = this.m_bpn.getPrefWidth();
     this.m_min_size_delta.y = this.m_bpn.getPrefHeight();
     for (Node node : this.m_main_content.getChildrenUnmodifiable()) {
       node.setOnMouseClicked(event -> on_child_clicked(event));
     }

     this.m_top_pane.setOnMouseClicked(event -> on_child_clicked(event));
     this.m_center_pane.setOnMouseClicked(event -> on_child_clicked(event));
     this.m_right_pane.setOnMouseClicked(event -> on_child_clicked(event));
     this.m_bottom_pane.setOnMouseClicked(event -> on_child_clicked(event));
     this.m_left_pane.setOnMouseClicked(event -> on_child_clicked(event));
     this.m_menu_item = menu_item;
     if (this.m_menu_item != null) {
       this.m_menu_item.selectedProperty().addListener(new ChangeListener<Boolean>()
           {
             public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
               MJMDIPanelHelper.this.m_bpn.setVisible(MJMDIPanelHelper.this.m_menu_item.isSelected());
             }
           });
       this.m_menu_item.setSelected(true);
     }
   }

   public void append_child_handler(Region child) {
     child.setOnMouseClicked(event -> on_child_clicked(event));
   }

   public void on_child_clicked(MouseEvent event) {
     if (event.getButton() == MouseButton.PRIMARY)
       this.m_bpn.toFront();
   }

   private void setting_pane() {
     this.m_bpn.setTop(this.m_top_pane = create_pane(1, 32, true, "-fx-background-color:linear-gradient(to bottom, #454548, #202023);"));
     this.m_bpn.setLeft(this.m_left_pane = create_pane(5, 1, false, "-fx-background-color:#202023"));
     this.m_bpn.setRight(this.m_right_pane = create_pane(5, 1, false, "-fx-background-color:#202023"));
     this.m_bpn.setBottom(this.m_bottom_pane = create_pane(1, 5, true, "-fx-background-color:#202023"));
     this.m_bpn.setCenter(this.m_center_pane = create_pane(1, 1, false, "-fx-background-color:#303033"));
   }

   private Pane create_pane(int width, int height, boolean is_orientation, String color) {
     Pane pnl = new Pane();
     pnl.setPrefWidth(width);
     pnl.setPrefHeight(height);
     pnl.setStyle(color);
     if (is_orientation)
       pnl.nodeOrientationProperty().set(NodeOrientation.RIGHT_TO_LEFT);
     return pnl;
   }

   private Button create_button(String text, EventHandler<? super MouseEvent> handler) {
     Button btn = new Button();
     btn.setText(text);
     btn.setOnMouseClicked(handler);
     btn.setOnMouseEntered(event -> on_button_enter(event));
     btn.setOnMouseExited(event -> on_button_leave(event));
     btn.setLayoutY(4.0D);
     btn.setStyle("-fx-background-color:transparent");
     btn.setTextFill(Color.WHITE);
     btn.setLayoutX(3.0D);
     btn.setCursor(Cursor.HAND);
     return btn;
   }

   private Label create_label(String text) {
     Label lbl = new Label();
     lbl.setText(text);
     lbl.setLayoutY(8.0D);
     lbl.setLayoutX(8.0D);
     lbl.setStyle("-fx-font-weight: bold;-fx-background-color:transparent;-fx-text-fill: white;");
     return lbl;
   }

   public void on_top_mouse_pressed(MouseEvent event) {
     this.m_bpn.toFront();
     this.m_mouse_delta.x = this.m_bpn.getLayoutX() - event.getScreenX();
     this.m_mouse_delta.y = this.m_bpn.getLayoutY() - event.getScreenY();
   }

   public void on_top_mouse_dragged(MouseEvent event) {
     this.m_bpn.setLayoutX(event.getScreenX() + this.m_mouse_delta.x);
     this.m_bpn.setLayoutY(event.getScreenY() + this.m_mouse_delta.y);
   }

   public void on_size_mouse_pressed(MouseEvent event) {
     this.m_bpn.toFront();
     this.m_mouse_delta.x = event.getScreenX();
     this.m_mouse_delta.y = event.getScreenY();
     this.m_size_delta.x = this.m_bpn.getPrefWidth();
     this.m_size_delta.y = this.m_bpn.getPrefHeight();
   }

   public void on_size_mouse_dragged(MouseEvent event) {
     double calculate_width = this.m_size_delta.x + event.getScreenX() - this.m_mouse_delta.x;
     double calculate_height = this.m_size_delta.y + event.getScreenY() - this.m_mouse_delta.y;
     calculate_width = Math.max(calculate_width, this.m_min_size_delta.x);
     calculate_height = Math.max(calculate_height, this.m_min_size_delta.y);
     if (calculate_width > 1024.0D || calculate_height > 1024.0D) {
       return;
     }
     update_size(calculate_width, calculate_height);
   }

   public void on_button_enter(MouseEvent event) {
     Button btn = (Button)event.getSource();
     btn.setStyle("-fx-background-color:#555558");
   }
   public void on_button_leave(MouseEvent event) {
     Button btn = (Button)event.getSource();
     btn.setStyle("-fx-background-color:transparent");
   }

   public void on_close_button_clicked(MouseEvent event) {
     if (event.getButton() == MouseButton.PRIMARY) {
       if (this.m_menu_item != null)
         this.m_menu_item.setSelected(false);
       this.m_bpn.setVisible(false);
     }
   }

   public boolean is_visible() {
     return this.m_bpn.isVisible();
   }

   public void set_left(double val) {
     this.m_bpn.setLayoutX(val);
   }
   public double get_left() {
     return this.m_bpn.getLayoutX();
   }
   public void set_top(double val) {
     this.m_bpn.setLayoutY(val);
   }
   public double get_top() {
     return this.m_bpn.getLayoutY();
   }
   public void set_width(double val) {
     this.m_bpn.setPrefWidth(val);
   }
   public double get_width() {
     return this.m_bpn.getPrefWidth();
   }
   public void set_height(double val) {
     this.m_bpn.setPrefHeight(val);
   }
   public double get_height() {
     return this.m_bpn.getPrefHeight();
   }
   public void set_title(String s) {
     String old = this.m_lbl_title.getText();
     this.m_lbl_title.setText(s);
     if (old.length() == s.length()) {
       return;
     }
     FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
     this.m_lbl_title.setLayoutX(this.m_bpn.getPrefWidth() - fontLoader.computeStringWidth(this.m_lbl_title.getText(), this.m_lbl_title.getFont()) - 8.0D);
   }
   public void store_current_position() {
     this.m_source_x = this.m_bpn.getLayoutX();
     this.m_source_y = this.m_bpn.getLayoutY();
     this.m_source_width = this.m_bpn.getPrefWidth();
     this.m_source_height = this.m_bpn.getPrefHeight();
   }
   public void restore_current_position() {
     this.m_bpn.setLayoutX(this.m_source_x);
     this.m_bpn.setLayoutY(this.m_source_y);
     this.m_bpn.prefWidth(this.m_source_width);
     update_size(this.m_source_width, this.m_source_height);
   }

   private void update_size(double width, double height) {
     this.m_bpn.setPrefWidth(width);
     this.m_bpn.setPrefHeight(height);
     this.m_center_pane.setPrefWidth(this.m_bpn.getPrefWidth() - 5.0D - 5.0D);
     this.m_center_pane.setPrefHeight(this.m_bpn.getPrefHeight() - 32.0D - 5.0D);
     this.m_main_content.setPrefWidth(this.m_center_pane.getPrefWidth());
     this.m_main_content.setPrefHeight(this.m_center_pane.getPrefHeight());
     FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
     this.m_lbl_title.setLayoutX(this.m_bpn.getPrefWidth() - fontLoader.computeStringWidth(this.m_lbl_title.getText(), this.m_lbl_title.getFont()) - 8.0D);
   }
 }


