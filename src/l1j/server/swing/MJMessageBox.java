package l1j.server.swing;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MJMessageBox {
	public static void show(Component owner, String message, boolean isInfo){
		JOptionPane.showMessageDialog(owner, message, "伺服器管理員", isInfo ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
	}
	
	public static boolean question(Component owner, String message){
		return JOptionPane.showConfirmDialog(owner, message, "伺服器管理員", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}
}
