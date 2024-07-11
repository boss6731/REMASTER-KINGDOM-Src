 package MJFX.Letter;

 import MJFX.MJFxEntry;
 import com.sun.prism.impl.Disposer;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.util.concurrent.ConcurrentHashMap;
 import javafx.application.Platform;
 import javafx.beans.property.SimpleBooleanProperty;
 import javafx.beans.value.ObservableValue;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.scene.control.Label;
 import javafx.scene.control.TableCell;
 import javafx.scene.control.TableColumn;
 import javafx.scene.control.TableRow;
 import javafx.scene.control.TableView;
 import javafx.scene.control.cell.PropertyValueFactory;
 import javafx.scene.input.MouseButton;
 import javafx.scene.input.MouseEvent;
 import javafx.scene.layout.VBox;
 import javafx.util.Callback;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.utils.MJCommons;



 public class MJFxLetterManager
 {
   private static final String[] GM_NAMES = new String[] { "梅蒂斯", "米斯皮", "仙后座" };

   private static MJFxLetterManager _instance;
   private VBox m_container;

   public static MJFxLetterManager getInstance() {
     if (_instance == null)
       _instance = new MJFxLetterManager();
     return _instance;
   }

   private TableView<MJFxLetterInfo> m_tv;
   private ObservableList<MJFxLetterInfo> m_letters;
   private ConcurrentHashMap<Integer, MJFxLetterInfo> m_letters_map;

   public void initialize(VBox container, TableView<MJFxLetterInfo> tv) {
     this.m_container = container;
     this.m_letters = FXCollections.observableArrayList();
     this.m_letters_map = new ConcurrentHashMap<>();
     this.m_tv = tv;
     this.m_tv.setFixedCellSize(25.0D);
     this.m_tv.setPlaceholder(new Label("沒有收到任何信件。"));
     this.m_tv.prefHeightProperty().bind(this.m_container.heightProperty());
     insert_column(this.m_tv, "colNo", 0.08D, "數位");
     insert_column(this.m_tv, "colSender", 0.15D, "由..發送");
     insert_column(this.m_tv, "colTitle", 0.15D, "標題");
     insert_column(this.m_tv, "colContent", 0.25D, "細節");
     insert_column(this.m_tv, "colDate", 0.15D, "發送日期");
     insert_column(this.m_tv, "colChecked", 0.08D, "查看");
     insert_delete_button_column(this.m_tv, "colDelete", 0.12D, "刪除");

     this.m_tv.setRowFactory(ttv -> {
           TableRow<MJFxLetterInfo> row = new TableRow<>();
           row.setOnMouseClicked(());
           return row;
         });
     this.m_tv.setItems(this.m_letters);
   }


   private void on_row_clicked(MouseEvent event) {
     TableRow<MJFxLetterInfo> row = (TableRow<MJFxLetterInfo>)event.getSource();
     if (event.getClickCount() != 2 || event.getButton() != MouseButton.PRIMARY || row.isEmpty()) {
       return;
     }
     try {
       MJFxLetterInfo letterInfo = row.getItem();
       if (letterInfo == null) {
         return;
       }
       MJFxEntry.getInstance().show_letter_window(letterInfo);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private static <T> void insert_column(TableView<MJFxLetterInfo> tv, String column_name, double multiply, String text) {
     TableColumn<MJFxLetterInfo, T> col = new TableColumn<>();
     tv.getColumns().add(col);
     col.setText(text);
     col.setCellValueFactory(new PropertyValueFactory<>(column_name));
     col.prefWidthProperty().bind(tv.widthProperty().multiply(multiply));
     col.setResizable(false);
   }

   private static void insert_delete_button_column(TableView<MJFxLetterInfo> tv, String column_name, double multiply, String text) {
     TableColumn<Object, Object> col = new TableColumn<>();
     tv.getColumns().add(col);
     col.setStyle("-fx-padding: 0 0 0 0;");
     col.setText(text);
     col.prefWidthProperty().bind(tv.widthProperty().multiply(multiply));
     col.setResizable(false);
     col.setCellValueFactory((Callback)new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>()
         {
           public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> param) {
             return new SimpleBooleanProperty((param.getValue() != null));
           }
         });
     col.setCellFactory((Callback)new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>()
         {
           public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> param) {
             return new MJFxLetterButtonCell();
           }
         });
   }

   public void load_letters() {
     this.m_letters.clear();
     this.m_letters_map.clear();
     for (String name : GM_NAMES) {
       Selector.exec("select * from letter where receiver=? and template_id=? order by date limit 100", new SelectorHandler()
           {
             public void handle(PreparedStatement pstm) throws Exception {
               pstm.setString(1, name);
               pstm.setInt(2, 0);
             }


             public void result(ResultSet rs) throws Exception {
               while (rs.next()) {
                 MJFxLetterInfo lInfo = MJFxLetterInfo.newInstance(rs, name);
                 MJFxLetterManager.this.m_letters_map.put(lInfo.getColNo(), lInfo);
                 MJFxLetterManager.this.m_letters.add(lInfo);
               }
             }
           });
     }
   }

   private boolean is_gm_name(String receiver) {
     for (String name : GM_NAMES) {
       if (name.equalsIgnoreCase(receiver))
         return true;
     }
     return false;
   }

   public void on_check_letter(int id) {
     MJFxLetterInfo lInfo = this.m_letters_map.get(Integer.valueOf(id));
     if (lInfo == null) {
       return;
     }
     Platform.runLater(() -> lInfo.on_readed());
   }



   public void on_delete_letter(int id) {
     MJFxLetterInfo lInfo = this.m_letters_map.remove(Integer.valueOf(id));
     if (lInfo == null) {
       return;
     }
     Platform.runLater(() -> this.m_letters.remove(lInfo));
   }



   public void on_receive_letter(int id, String sender, String title, String content, Timestamp ts, String receiver) {
     if (!is_gm_name(receiver)) {
       return;
     }
     MJFxLetterInfo lInfo = MJFxLetterInfo.newInstance(id, sender, title, content, MJCommons.to_string(ts), receiver);
     this.m_letters_map.put(lInfo.getColNo(), lInfo);
     Platform.runLater(() -> this.m_letters.add(lInfo));
   }
 }


