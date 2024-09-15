package MJFX.Util;

import MJFX.MJFxEntry;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.function.Consumer;

public class MessageBox {
	public static void do_question_box(Window owner, String title, String header_text, String content, Consumer<? super ButtonType> consumer){
		Alert alert = new Alert(AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
		alert.initOwner(owner);
		alert.setTitle(title);
		alert.setHeaderText(header_text);
		((Stage)(alert.getDialogPane().getScene().getWindow())).getIcons().add(MJFxEntry.getInstance().get_main_stage().getIcons().get(0));
		alert.showAndWait().ifPresent(consumer);
	}
	
	public static void do_information_box(Window owner, String title, String header_text, String content){
		Alert alert = new Alert(AlertType.INFORMATION, content, ButtonType.OK);
		alert.initOwner(owner);
		alert.setTitle(title);
		alert.setHeaderText(header_text);
		((Stage)(alert.getDialogPane().getScene().getWindow())).getIcons().add(MJFxEntry.getInstance().get_main_stage().getIcons().get(0));
		alert.showAndWait();
	}
	
	public static void do_error_box(Window owner, String title, String header_text, String content){
		Alert alert = new Alert(AlertType.ERROR, content, ButtonType.OK);
		alert.initOwner(owner);
		alert.setTitle(title);
		alert.setHeaderText(header_text);
		((Stage)(alert.getDialogPane().getScene().getWindow())).getIcons().add(MJFxEntry.getInstance().get_main_stage().getIcons().get(0));
		alert.showAndWait();
	}
}
