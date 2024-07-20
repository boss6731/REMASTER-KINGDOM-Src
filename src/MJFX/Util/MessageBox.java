package MJFX.Util;

import MJFX.MJFxEntry;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.function.Consumer;


public class MessageBox {

	// 顯示確認對話框的方法
	public static void do_question_box(Window owner, String title, String header_text, String content, Consumer<? super ButtonType> consumer) {
		// 創建一個確認類型的Alert對象，並設置內容和按鈕
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);

		// 設定對話框的所有者
		alert.initOwner(owner);

		// 設定對話框的標題和標頭文本
		alert.setTitle(title);
		alert.setHeaderText(header_text);

		// 將主舞台的圖標添加到對話框中
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(MJFxEntry.getInstance().get_main_stage().getIcons().get(0));

		// 顯示對話框並處理用戶的選擇
		alert.showAndWait().ifPresent(consumer);
	}

	// 顯示訊息對話框的方法
	public static void do_information_box(Window owner, String title, String header_text, String content) {
		// 創建一個訊息類型的Alert對象，並設置內容和按鈕
		Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);

		// 設定對話框的所有者
		alert.initOwner(owner);

		// 設定對話框的標題和標頭文本
		alert.setTitle(title);
		alert.setHeaderText(header_text);

		// 將主舞台的圖標添加到對話框中
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(MJFxEntry.getInstance().get_main_stage().getIcons().get(0));

		// 顯示對話框
		alert.showAndWait();
	}

	// 顯示錯誤對話框的方法
	public static void do_error_box(Window owner, String title, String header_text, String content) {
		// 創建一個錯誤類型的Alert對象，並設置內容和按鈕
		Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);

		// 設定對話框的所有者
		alert.initOwner(owner);

		// 設定對話框的標題和標頭文本
		alert.setTitle(title);
		alert.setHeaderText(header_text);

		// 將主舞台的圖標添加到對話框中
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.getIcons().add(MJFxEntry.getInstance().get_main_stage().getIcons().get(0));

		// 顯示對話框
		alert.showAndWait();
	}
}


