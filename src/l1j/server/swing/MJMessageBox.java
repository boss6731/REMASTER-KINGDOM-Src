package l1j.server.swing;

import javax.swing.*;
import java.awt.*;

public class MJMessageBox {
	// 顯示訊息對話框的方法
	public static void show(Component owner, String message, boolean isInfo){
		JOptionPane.showMessageDialog(owner, message, "伺服器管理員", isInfo ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
	}
	
	// 顯示詢問對話框的方法
	public static boolean question(Component owner, String message){
		return JOptionPane.showConfirmDialog(owner, message, "伺服器管理員", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}
}
