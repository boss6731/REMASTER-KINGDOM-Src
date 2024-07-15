package l1j.server.swing;

import MJFX.UIAdapter.MJPerformAdapter;
import com.sun.management.OperatingSystemMXBean;
import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.Server;
import l1j.server.server.GameServer;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SystemUtil;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/** 美食製作 */
public class chocco extends JFrame {
	/** 建構子 */
	public static boolean inf;
	public static String[] save_text = new String[7]; // 暫存每個欄位的內容
	public static boolean serverstart; // 判斷服務器是否運行
	public static int check = 0; // 用於通過界面調整大小（用於更改圖像）
	public static int x_size = 0; // 存儲初始管理員大小
	private ImageIcon logo = new ImageIcon("data/img/logo.jpg"); // 管理員logo圖像
	private ImageIcon start = new ImageIcon("data/img/start.jpg"); // 開始按鈕圖像
	private ImageIcon exit = new ImageIcon("data/img/exit.jpg"); // 退出按鈕圖像
	private ImageIcon up = new ImageIcon("data/img/up.jpg"); // 上移圖像
	private ImageIcon down = new ImageIcon("data/img/down.jpg"); // 下移圖像
	private ImageIcon so = new ImageIcon("data/img/so.jpg"); // 向上箭頭圖像
	private ImageIcon chating = new ImageIcon("data/img/chat.jpg"); // 聊天圖像
	private ImageIcon in = new ImageIcon("data/img/in.jpg"); // 向右箭頭圖像

	private javax.swing.JButton buf; // 增益按鈕
	private javax.swing.JTextField chat; // 聊天框
	private javax.swing.JButton ex; // 退出按鈕
	public static javax.swing.JLabel first; // 管理員首部標題
	private javax.swing.JInternalFrame iframe; // 查看各種日誌的框架
	private javax.swing.JInternalFrame iframe2; // 查看用戶列表的框架
	private javax.swing.JInternalFrame iframe3;
	private javax.swing.JButton info; // 服務器製作信息按鈕
	private javax.swing.JButton info1; // 服務器信息按鈕
	private javax.swing.JLabel label1; // 用於顯示當前用戶的標籤
	public static javax.swing.JLabel label2; // --
	private javax.swing.JLabel label4; // 顯示當前使用內存量的標籤
	private javax.swing.JLabel lblThread;
	private javax.swing.JLabel lblCPU;
	private javax.swing.JLabel lblCPUProgressBack;
	private javax.swing.JLabel lblCPUProgress;
	public static javax.swing.JLayeredPane layout1; // 類似菜單的界面佈局
	private javax.swing.JLayeredPane layout2; // --
	private javax.swing.JLayeredPane layout3; // --
	private javax.swing.JLayeredPane layout4; // --
	private javax.swing.JLayeredPane layout5;
	private javax.swing.JButton logcl; // 日誌框（各種文本框）清除按鈕
	private javax.swing.JButton logsv; // 日誌框保存按鈕
	private javax.swing.JTextField name; // 私聊文本框

	private javax.swing.JButton st; // 開始按鈕
	public static javax.swing.JLabel stime; // 顯示服務器運行時間的標籤
	private javax.swing.JButton sv; // 服務器保存按鈕
	private javax.swing.JTabbedPane tabframe; // 查看各個日誌的選項卡

	private javax.swing.JScrollPane spGlobalChat; // 各個日誌框的滾動窗格
	private javax.swing.JScrollPane spClanChat; // --
	private javax.swing.JScrollPane spPartyChat; // --
	private javax.swing.JScrollPane spWhisper; // --
	private javax.swing.JScrollPane spShop; // --
	private javax.swing.JScrollPane spTrade; // --
	private javax.swing.JScrollPane spWarehouse; // --
	private javax.swing.JScrollPane spEnchant;
	private javax.swing.JScrollPane spPickup;
	private javax.swing.JScrollPane spUserList; // --
	public static javax.swing.JTextArea txtGlobalChat; //
	public static javax.swing.JTextArea txtClanChat;
	public static javax.swing.JTextArea txtPartyChat; //
	public static javax.swing.JTextArea txtWhisper; //
	public static javax.swing.JTextArea txtShop; //
	public static javax.swing.JTextArea txtTrade; //
	public static javax.swing.JTextArea txtWarehouse; //
	public static javax.swing.JTextArea txtEnchant; //
	public static javax.swing.JTextArea txtPickup; //

	private javax.swing.JButton updown; // 上下按鈕
	public static java.awt.List userlist; // 用戶列表
	public static java.awt.List iplist; // 阻止IP
	private javax.swing.JPopupMenu popmenu1; // 用戶列表彈出菜單
	private javax.swing.JPopupMenu popmenu2; // 用戶列表彈出菜單2
	private javax.swing.JMenuItem menu1; // 菜單1（踢出）
	private javax.swing.JMenuItem menu2; // 菜單2 -> ??
	private javax.swing.JMenuItem menu3; // 菜單3 //
	private javax.swing.JMenuItem menu4; // 菜單3 //
	private javax.swing.JMenuItem menu5; // 菜單3 //
	private javax.swing.JMenuItem menu6; // 贈送物品
	private javax.swing.JMenuItem menu7; // 私聊
	private javax.swing.JTabbedPane chocco; // 附加

	private javax.swing.JPopupMenu pmReload;

	public static JFrame setting;
	private javax.swing.JTextField exp;
	private javax.swing.JTextField lawful;
	private javax.swing.JTextField karma;
	private javax.swing.JTextField adena;
	private javax.swing.JTextField item;
	private javax.swing.JTextField enweapon;
	private javax.swing.JTextField enarmor;
	private javax.swing.JTextField weightlimit;
	private javax.swing.JTextField chatlvl;
	private javax.swing.JTextField maxuser;
	private javax.swing.JButton set;

	private javax.swing.JLabel exp1;
	private javax.swing.JLabel lawful1;
	private javax.swing.JLabel karma1;
	private javax.swing.JLabel adena1;
	private javax.swing.JLabel item1;
	private javax.swing.JLabel enweapon1;
	private javax.swing.JLabel enarmor1;
	private javax.swing.JLabel weightlimit1;
	private javax.swing.JLabel chatlvl1;
	private javax.swing.JLabel maxuser1;

	/** 基本構造函數 */
	public chocco() {
		/** 初始化每個菜單/組件 */
		initComponents();
		/** 保存初始管理員大小 */
		x_size = this.getHeight();
		/** 設置初始管理員大小 */
		this.setSize(this.getWidth() + 10, 60);
		/** 設置管理員運行位置 */
		this.setLocation(300, 200);
	}

	/** 初始化每個菜單/組件... 任何一個都不能缺失..這將導致編譯和運行時的錯誤。 */
	private void initComponents() {
		layout1 = new javax.swing.JLayeredPane();
		first = new javax.swing.JLabel();
		st = new javax.swing.JButton(start);
		ex = new javax.swing.JButton(exit);
		sv = new javax.swing.JButton();
		buf = new javax.swing.JButton();
		info = new javax.swing.JButton();
		updown = new javax.swing.JButton(down);
		info1 = new javax.swing.JButton();
		logsv = new javax.swing.JButton();
		logcl = new javax.swing.JButton();
		label1 = new javax.swing.JLabel();
		label2 = new javax.swing.JLabel();
		label4 = new javax.swing.JLabel();
		lblThread = new javax.swing.JLabel();
		lblCPU = new javax.swing.JLabel();
		layout2 = new javax.swing.JLayeredPane();
		iframe = new javax.swing.JInternalFrame();
		tabframe = new javax.swing.JTabbedPane();
		spGlobalChat = new javax.swing.JScrollPane();
		txtGlobalChat = new javax.swing.JTextArea();
		spShop = new javax.swing.JScrollPane();
		txtShop = new javax.swing.JTextArea();
		spTrade = new javax.swing.JScrollPane();
		txtTrade = new javax.swing.JTextArea();
		spClanChat = new javax.swing.JScrollPane();
		txtClanChat = new javax.swing.JTextArea();
		spPartyChat = new javax.swing.JScrollPane();
		txtPartyChat = new javax.swing.JTextArea();
		spWhisper = new javax.swing.JScrollPane();
		txtWhisper = new javax.swing.JTextArea();
		spWarehouse = new javax.swing.JScrollPane();
		txtWarehouse = new javax.swing.JTextArea();
		spEnchant = new javax.swing.JScrollPane();
		txtEnchant = new javax.swing.JTextArea();

		spPickup = new javax.swing.JScrollPane();
		txtPickup = new javax.swing.JTextArea();

		layout3 = new javax.swing.JLayeredPane();
		layout5 = new javax.swing.JLayeredPane();
		iframe2 = new javax.swing.JInternalFrame();
		iframe3 = new javax.swing.JInternalFrame();
		spUserList = new javax.swing.JScrollPane();
		userlist = new java.awt.List();
		iplist = new java.awt.List();
		layout4 = new javax.swing.JLayeredPane();
		name = new javax.swing.JTextField();
		chat = new javax.swing.JTextField();
		stime = new javax.swing.JLabel();
		popmenu1 = new javax.swing.JPopupMenu();
		popmenu2 = new javax.swing.JPopupMenu();
		pmReload = new javax.swing.JPopupMenu();
		menu1 = new javax.swing.JMenuItem();
		menu2 = new javax.swing.JMenuItem();
		menu3 = new javax.swing.JMenuItem();
		menu4 = new javax.swing.JMenuItem();
		menu5 = new javax.swing.JMenuItem();
		menu6 = new javax.swing.JMenuItem();
		menu7 = new javax.swing.JMenuItem();
		chocco = new javax.swing.JTabbedPane();

		setting = new javax.swing.JFrame("Server Setting");
		chatlvl = new javax.swing.JTextField("", 5);
		maxuser = new javax.swing.JTextField("", 5);
		exp = new javax.swing.JTextField("", 5);
		adena = new javax.swing.JTextField("", 5);
		item = new javax.swing.JTextField("", 5);
		weightlimit = new javax.swing.JTextField("", 5);
		lawful = new javax.swing.JTextField("", 5);
		karma = new javax.swing.JTextField("", 5);
		enweapon = new javax.swing.JTextField("", 5);
		enarmor = new javax.swing.JTextField("", 5);
		set = new javax.swing.JButton("Setting Save");

		chatlvl1 = new javax.swing.JLabel("　聊天等級");
		maxuser1 = new javax.swing.JLabel("　最大用戶");
		exp1 = new javax.swing.JLabel("經驗倍率");
		adena1 = new javax.swing.JLabel("金幣倍率");
		item1 = new javax.swing.JLabel("物品倍率");
		weightlimit1 = new javax.swing.JLabel("負重上限");
		lawful1 = new javax.swing.JLabel("善惡值倍率");
		karma1 = new javax.swing.JLabel("業報倍率");
		enweapon1 = new javax.swing.JLabel("武器附魔率");
		enarmor1 = new javax.swing.JLabel("防具附魔率");
// 加入管理員設置窗口的各個組件
	setting.add(chatlvl1); // 聊天等級標籤
	setting.add(chatlvl); // 聊天等級文本框
	setting.add(maxuser1); // 最大用戶標籤
	setting.add(maxuser); // 最大用戶文本框
	setting.add(exp1); // 經驗倍率標籤
	setting.add(exp); // 經驗倍率文本框
	setting.add(adena1); // 金幣倍率標籤
	setting.add(adena); // 金幣倍率文本框
	setting.add(item1); // 物品倍率標籤
	setting.add(item); // 物品倍率文本框
	setting.add(weightlimit1); // 負重上限標籤
	setting.add(weightlimit); // 負重上限文本框
	setting.add(lawful1); // 善惡值倍率標籤
	setting.add(lawful); // 善惡值倍率文本框
	setting.add(karma1); // 業報倍率標籤
	setting.add(karma); // 業報倍率文本框
	setting.add(enweapon1); // 武器附魔率標籤
	setting.add(enweapon); // 武器附魔率文本框
	setting.add(enarmor1); // 防具附魔率標籤
	setting.add(enarmor); // 防具附魔率文本框
	setting.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        setMouseClicked(evt); // 設置管理員設置窗口的滑鼠點擊事件
    }
});
	setting.add(set); // 設置按鈕

// 設置彈出菜單1的各個選項
	menu1.setIcon(in);
	menu1.setText("強制關閉");
	menu1.setToolTipText("通過角色名強制關閉");
	menu1.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        menu1ActionPerformed(evt); // 設置彈出菜單1中“強制關閉”選項的點擊事件
    }
});
popmenu1.add(menu1);

menu2.setIcon(in);
menu2.setText("封鎖IP");
menu2.setToolTipText("通過角色名封鎖IP");
menu2.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        menu2ActionPerformed(evt); // 設置彈出菜單1中“封鎖IP”選項的點擊事件
    }
});
popmenu1.add(menu2);

menu7.setIcon(in);
menu7.setText("私聊");
menu7.setToolTipText("對指定角色進行私聊");
menu7.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        menu7ActionPerformed(evt); // 設置彈出菜單1中“私聊”選項的點擊事件
    }
});
popmenu1.add(menu7);

menu3.setIcon(in);
menu3.setText("角色信息");
menu3.setToolTipText("查看角色信息");
menu3.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        menu3ActionPerformed(evt); // 設置彈出菜單1中“角色信息”選項的點擊事件
    }
});
popmenu1.add(menu3);

// 設置彈出菜單2的各個選項
menu4.setIcon(in);
menu4.setText("刪除IP（列表）");
menu4.setToolTipText("從列表中刪除被封鎖的IP");
menu4.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        menu4ActionPerformed(evt); // 設置彈出菜單2中“刪除IP（列表）”選項的點擊事件
    }
});
popmenu2.add(menu4);

menu5.setIcon(in);
menu5.setText("刪除IP（數據庫和列表）");
menu5.setToolTipText("從數據庫和列表中刪除被封鎖的IP");
menu5.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        menu5ActionPerformed(evt); // 設置彈出菜單2中“刪除IP（數據庫和列表）”選項的點擊事件
    }
});
popmenu2.add(menu5);

menu6.setIcon(in);
menu6.setText("贈送物品");
menu6.setToolTipText("對指定角色贈送物品");
menu6.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        menu6ActionPerformed(evt); // 設置彈出菜單1中“贈送物品”選項的點擊事件
    }
});
popmenu1.add(menu6);

// 設置管理員窗口的基本屬性
setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE); // 設置關閉操作為不執行任何操作
setTitle("Lineage Server"); // 設置標題為“Lineage Server”
setName("frame"); // 設置名稱為“frame”
setResizable(false); // 不可調整大小
// 設置 iframe2 窗口的基本屬性
iframe2.setTitle("伺服器列表"); // 設置標題為“伺服器列表”
iframe2.setFrameIcon(logo); // 設置窗口圖標為 logo
iframe2.setVisible(true); // 設置窗口可見

// 添加用戶列表滑鼠點擊事件
userlist.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        userlistMouseClicked(evt); // 用戶列表的滑鼠點擊事件
    }
});

// 添加 IP 列表滑鼠點擊事件
iplist.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        iplistMouseClicked(evt); // IP 列表的滑鼠點擊事件
    }
});
iplist.setFont(new Font("新細明體", 0, 11)); // 設置字體

// 設置用戶列表滾動面板
spUserList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // 不顯示水平滾動條
spUserList.setViewportView(userlist); // 設置視圖為用戶列表
userlist.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        userlistActionPerformed(evt); // 用戶列表的動作事件
    }
});
chocco.addTab("電腦", so, userlist); // 添加“電腦”選項卡，包含用戶列表
chocco.addTab("IP", so, iplist); // 添加“IP”選項卡，包含 IP 列表

// 設置 iframe2 窗口的布局
javax.swing.GroupLayout iframe2Layout = new javax.swing.GroupLayout(iframe2.getContentPane());
iframe2.getContentPane().setLayout(iframe2Layout);
iframe2Layout.setHorizontalGroup(iframe2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(iframe2Layout.createSequentialGroup()
        .addComponent(chocco, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
iframe2Layout.setVerticalGroup(iframe2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addComponent(chocco, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE));

iframe2.setBounds(0, 0, 115, 340); // 設置窗口大小和位置
try {
    iframe2.setMaximum(true); // 最大化窗口
} catch (java.beans.PropertyVetoException e1) {
    e1.printStackTrace();
}

// 顯示 iframe3 窗口
iframe3.setVisible(true);
javax.swing.GroupLayout iframe3Layout = new javax.swing.GroupLayout(iframe3.getContentPane());
iframe3.getContentPane().setLayout(iframe3Layout);
iframe3.setBounds(0, 0, 50, 340); // 設置窗口大小和位置
lblCPUProgressBack = new JLabel();
lblCPUProgressBack.setOpaque(true);
lblCPUProgressBack.setBackground(Color.black);
lblCPUProgressBack.setBounds(0, 0, 50, 330);
lblCPUProgressBack.setText("");
iframe3.add(lblCPUProgressBack);

lblCPUProgress = new JLabel();
lblCPUProgress.setOpaque(true);
lblCPUProgress.setBackground(Color.green);
lblCPUProgress.setBounds(0, 0, 50, 330);
lblCPUProgress.setText("");
iframe3.add(lblCPUProgress);
layout5.add(iframe3, javax.swing.JLayeredPane.DEFAULT_LAYER);
try {
    iframe3.setMaximum(true); // 最大化窗口
} catch (PropertyVetoException e) {
    e.printStackTrace();
}

// 設置用戶名標籤位置和大小
name.setBounds(0, 0, 80, 21);
layout4.add(name, javax.swing.JLayeredPane.DEFAULT_LAYER);

// 添加聊天文本框的鍵盤按下事件
chat.addKeyListener(new java.awt.event.KeyAdapter() {
    public void keyPressed(java.awt.event.KeyEvent evt) {
        chatKeyPressed(evt); // 聊天文本框的鍵盤按下事件
    }
});
chat.setBounds(80, 0, 390, 21); // 設置聊天文本框位置和大小
layout4.add(chat, javax.swing.JLayeredPane.DEFAULT_LAYER);

stime.setFont(new java.awt.Font("新細明體", 1, 12)); // 設置字體
stime.setText(""); // 設置文字為空
stime.setBounds(480, 0, 180, 20); // 設置位置和大小
layout4.add(stime, javax.swing.JLayeredPane.DEFAULT_LAYER);
javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
getContentPane().setLayout(layout);
layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(layout.createSequentialGroup().addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layout4, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(layout2, javax.swing.GroupLayout.PREFERRED_SIZE, 680,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(layout3, javax.swing.GroupLayout.PREFERRED_SIZE, 123,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(layout5, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                    javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(layout1, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE))
        .addContainerGap()));
layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    .addGroup(layout.createSequentialGroup()
        .addComponent(layout1, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
            javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layout5, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
            .addComponent(layout3, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
            .addComponent(layout2, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(layout4,
            javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap()));

pack(); // 包裝視窗以適應所有元件的大小和佈局
new Thread(new PerformReader()).start(); // 啟動執行緒以讀取性能信息

/**
 * 每個事件的管理處
 */

/** 服務器啟動事件 */
private void stMouseClicked(java.awt.event.MouseEvent evt) {
    /** 如果服務器尚未運行... */
    if (!serverstart) {
        first.setText("執行狀態 : 開啟");
        first.setForeground(Color.red);
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Server(); // 創建新的服務器實例
                    new ServerStart(evt.getXOnScreen(), evt.getYOnScreen()); // 顯示服務器啟動消息
                }
            }).start();
        } catch (Exception e) {
        }
        serverstart = true;
        /** 如果服務器已經在運行中 */
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "服務器已經在運行中。", "Server Message",
            javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 服務器關閉事件 */
private void exMouseClicked(java.awt.event.MouseEvent evt) {
    /** 如果服務器正在運行 */
    if (serverstart) {
        /**
         * 保存每個角色和服務器數據，然後安全關閉
         */
        int a = JOptionPane.showConfirmDialog(this, "確定要關閉服務器嗎？", "Server Message", 2,
            JOptionPane.INFORMATION_MESSAGE);
        if (a == JOptionPane.YES_OPTION) {
            GameServer.getInstance().shutdownWithCountdown(0); // 帶倒計時的關閉服務器
        }

    } else {
        System.exit(0); // 否則退出應用程序
    }
}
public static boolean _isManagerCommands = false;

/** 重新載入按鈕點擊事件 */
private void reloadMouseClicked(java.awt.event.MouseEvent evt) {
    if (serverstart) {
        if (!_isManagerCommands) {
            _isManagerCommands = true;
            new ManagerCommands(evt.getXOnScreen(), evt.getYOnScreen());
        } else
            MJMessageBox.show(this, "窗口已經是活動的。", false);
    } else
        MJMessageBox.show(this, "伺服器未運行。", false);
}

/** 緩衝按鈕點擊事件 */
private void bufMouseClicked(java.awt.event.MouseEvent evt) {
    /** 如果伺服器正在運行 */
    if (serverstart) {
        /** 處理各種事件 */
        Object smallList[] = { "全屬性增益", "致命蟲賽跑", "無盡戰鬥", "全域禁言", "解除全域禁言" };
        String value = (String) javax.swing.JOptionPane.showInputDialog(this, "要啟動哪個事件？", " Server Message",
                JOptionPane.QUESTION_MESSAGE, null, smallList, smallList[0]);
        if (value != null) {
            if (value.equals("致命蟲賽跑")) {
                GameServerSetting _GameServerSetting = GameServerSetting.getInstance();
            } else if (value.equals("全屬性增益")) {
                int[] allBuffSkill = { 26, 42, 48, 57, 68, 79, 158, 163, 168 };
                for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
                    L1SkillUse l1skilluse = new L1SkillUse();
                    for (int i = 0; i < allBuffSkill.length; i++) {
                        l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
                                L1SkillUse.TYPE_GMBUFF);
                    }
                }
            } else if (value.equals("全域禁言")) {
                L1World.getInstance().set_worldChatElabled(false);
                txtGlobalChat.append("全域禁言已啟動");
            } else if (value.equals("解除全域禁言")) {
                L1World.getInstance().set_worldChatElabled(true);
                txtGlobalChat.append("全域禁言已解除");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "服務準備中...", " Server Message",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器未運行。", " Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 伺服器製作資訊點擊事件 */
private void infoMouseClicked(java.awt.event.MouseEvent evt) {
    javax.swing.JOptionPane.showMessageDialog(this, "製作：MJCodes", "Server Message",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
}

/** 管理員視窗大小調整點擊事件 */
private void updownMouseClicked(java.awt.event.MouseEvent evt) {
    /** 如果是小狀態 */
    if (check == 0) {
        /** 設置圖標 */
        updown.setIcon(up);
        /** 調整大小 */
        this.setSize(this.getWidth(), x_size);
        check = 1;
        /** 如果是大狀態 */
    } else {
        updown.setIcon(down);
        this.setSize(this.getWidth(), 60);
        check = 0;
    }
}

/** 伺服器資訊點擊事件 */
private void info1MouseClicked(java.awt.event.MouseEvent evt) {
    /** 顯示線程數量、數據庫池大小以及其他不可見的伺服器相關信息 */
    try {
        if (serverstart)
            javax.swing.JOptionPane.showMessageDialog(this, "池：未使用 - 最大：未使用", "Server Message",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        else
            javax.swing.JOptionPane.showMessageDialog(this, "伺服器未運行。", "Server Message",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
    }
}

/** 伺服器設置點擊事件 */
private void logsvMouseClicked(java.awt.event.MouseEvent evt) {
    /** 如果伺服器正在運行 */
    if (serverstart) {
        chatlvl.setText("" + Config.ServerAdSetting.GLOBALCHATLEVEL);
        maxuser.setText("" + Config.Login.MaximumOnlineUsers);
        exp.setText("" + Config.ServerRates.RateXp);
        adena.setText("" + Config.ServerRates.RateDropAdena);
        item.setText("" + Config.ServerRates.RateDropItems);
        weightlimit.setText("" + Config.ServerRates.RateWeightLimit);
        lawful.setText("" + Config.ServerRates.RateLawful);
        karma.setText("" + Config.ServerRates.RateKarma);
        enweapon.setText("" + Config.ServerRates.EnchantChanceWeapon);
        enarmor.setText("" + Config.ServerRates.EnchantChanceArmor);

        setting.setLayout(new FlowLayout());
        setting.setSize(340, 200);
        setting.setLocation(250, 250);
        setting.setResizable(false);
        setting.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setting.setVisible(true);
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器未運行。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 伺服器日誌重置點擊事件 */
private void logclMouseClicked(java.awt.event.MouseEvent evt) {
    if (serverstart) {
        try {
            LoggerInstance.getInstance().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器未運行。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 伺服器設置點擊事件 */
private void setMouseClicked(java.awt.event.MouseEvent evt) {
    int chatlevel = Integer.parseInt(chatlvl.getText());
    short chatlevel2 = (short) chatlevel;
    Config.ServerAdSetting.GLOBALCHATLEVEL = chatlevel2;
    int Max = Integer.parseInt(maxuser.getText());
    short Max2 = (short) Max;
    Config.Login.MaximumOnlineUsers = Max2;
    Float Exprate = Float.parseFloat(exp.getText());
    double Exprate2 = (double) Exprate;
    Config.ServerRates.RateXp = Exprate2;
    Float Aden = Float.parseFloat(adena.getText());
    double Aden2 = (double) Aden;
    Config.ServerRates.RateDropAdena = Aden2;
    Float Droprate = Float.parseFloat(item.getText());
    double Droprate2 = (double) Droprate;
    Config.ServerRates.RateDropItems = Droprate2;
    Float weight = Float.parseFloat(weightlimit.getText());
    double weight2 = (double) weight;
    Config.ServerRates.RateWeightLimit = weight2;
    Float lawfulrate = Float.parseFloat(lawful.getText());
    double lawful2 = (double) lawfulrate;
    Config.ServerRates.RateLawful = lawful2;
    Float karmarate = Float.parseFloat(karma.getText());
    double karma2 = (double) karmarate;
    Config.ServerRates.RateKarma = karma2;
    int armor = Integer.parseInt(enarmor.getText());
    Config.ServerRates.EnchantChanceArmor = armor;
    int enchant = Integer.parseInt(enweapon.getText());
    Config.ServerRates.EnchantChanceWeapon = enchant;
    txtGlobalChat.append("\n [  伺服器設置  ]");
    txtGlobalChat.append("\n [SET] 聊天等級 : " + Config.ServerAdSetting.GLOBALCHATLEVEL);
    txtGlobalChat.append("\n [SET] 最大在線用戶 : " + Config.Login.MaximumOnlineUsers);
    txtGlobalChat.append("\n [SET] 經驗倍率 : " + Exprate2 + " - 金幣 : " + Aden2 + " - 物品掉落 : " + Droprate2);
    txtGlobalChat.append("\n [SET] 負重限制 : " + weight2 + " - 法律率 : " + lawful2 + " - 業力率 : " + karma2);
    txtGlobalChat.append("\n [SET] [強化] 武器 : " + enchant + " - 盔甲 : " + armor);
    txtGlobalChat.append("\n [  伺服器設置完成 ]");
    setting.setVisible(false);
}
/** 伺服器聊天事件 */
private void chatKeyPressed(java.awt.event.KeyEvent evt) {

    /** 如果按下的是 Enter 鍵 */
    if (evt.getKeyCode() == evt.VK_ENTER) {
        if (serverstart) {
            /** 如果聊天內容不為空 */
            if (!chat.getText().equalsIgnoreCase("")) {
                /** 如果沒有指定特定角色名稱，即為一般聊天 */
                if (name.getText().equalsIgnoreCase("")) {
                    /** 發送全域聊天訊息 */
                    txtTrade.append("\n[******]  :  " + chat.getText());
                    L1World world = L1World.getInstance();
                    world.broadcastServerMessage("[******] " + chat.getText());
                /** 如果指定了特定角色名稱 */
                } else {
                    /** 查找指定角色名稱的角色並向其發送密語 */
                    L1PcInstance pc = L1World.getInstance().getPlayer(name.getText());
                    if (pc != null) {
                        txtWhisper.append("\n[******] -> [" + name.getText() + "]  :  " + chat.getText());
                        pc.sendPackets(new S_SystemMessage("[******] -> " + chat.getText()));
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(this,
                                "在世界中找不到名為 " + name.getText() + " 的角色。", "Server Message",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
                chat.setText("");
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "伺服器尚未啟動。", "Server Message",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            chat.setText("");
        }
    }

}

/** 允許指定特定角色名稱，暫時尚未實現，留作以後擴展 */
private void userlistActionPerformed(java.awt.event.ActionEvent evt) {
    if (!evt.getActionCommand().equalsIgnoreCase("")) {
        name.setText(evt.getActionCommand());
    }
}

/** 角色列表彈出菜單 */
private void userlistMouseClicked(java.awt.event.MouseEvent evt) {
    if (evt.getButton() == evt.BUTTON3) {
        popmenu1.show(userlist, evt.getX(), evt.getY());
    }
}

/** IP 列表彈出菜單 */
private void iplistMouseClicked(java.awt.event.MouseEvent evt) {
    if (evt.getButton() == evt.BUTTON3) {
        popmenu2.show(iplist, evt.getX(), evt.getY());
    }
}

/** 彈出菜單 - 個別強制踢出 */
private void menu1ActionPerformed(java.awt.event.ActionEvent evt) {
    if (serverstart) {
        if ((!userlist.getSelectedItem().equalsIgnoreCase("")) && (userlist.getSelectedItem() != null)) {
            try {
                L1PcInstance players = L1World.getInstance().getPlayer(userlist.getSelectedItem());
                if (players != null) {
                    GameServer.disconnectChar(userlist.getSelectedItem());
                    javax.swing.JOptionPane.showMessageDialog(this, "已成功強制踢出 " + players.getName(),
                            "Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    userlist.remove(userlist.getSelectedItem());
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "世界中不存在名為 " + userlist.getSelectedItem() + " 的角色。", "Server Message",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "強制踢出 " + userlist.getSelectedItem() + " 失敗。",
                        "Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } finally {
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "未指定角色名稱。", "Server Message",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器尚未啟動。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 彈出菜單 - 個別 IP 封鎖 */
private void menu2ActionPerformed(java.awt.event.ActionEvent evt) {
    if (serverstart) {
        if ((!userlist.getSelectedItem().equalsIgnoreCase("")) && (userlist.getSelectedItem() != null)) {
            L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
            if (pc != null) {
                if (pc.getNetConnection() != null) {
                    IpTable.getInstance().banIp(pc.getNetConnection().getIp());
                    iplist.add(pc.getNetConnection().getIp());
                }
                GameServer.disconnectChar(pc);
                userlist.remove(userlist.getSelectedItem());
                javax.swing.JOptionPane.showMessageDialog(this, "已成功強制踢出、封鎖 " + pc.getName(),
                        "Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "世界中不存在名為 " + userlist.getSelectedItem() + " 的角色。", " Server Message",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器尚未啟動。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 彈出菜單 - 查看個別角色資訊 */
private void menu3ActionPerformed(java.awt.event.ActionEvent evt) {
    if (serverstart) {
        if (!inf) {
            L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
            if (pc != null) {
                new infomation(pc);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "已有其他操作正在進行。", "Server Message",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器尚未啟動。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 彈出菜單 - 刪除封鎖 IP 列表中的 IP */
private void menu4ActionPerformed(java.awt.event.ActionEvent evt) {
    if (serverstart) {
        iplist.remove(iplist.getSelectedItem());
        javax.swing.JOptionPane.showMessageDialog(this, "已刪除封鎖列表中的 IP：" + iplist.getSelectedItem(),
                "Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器尚未啟動。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 彈出菜單 - 刪除封鎖 IP 列表中的 IP 並更新至數據庫 */
private void menu5ActionPerformed(java.awt.event.ActionEvent evt) {
    if (serverstart) {
        IpTable.getInstance().liftBanIp(iplist.getSelectedItem());
        iplist.remove(iplist.getSelectedItem());
        javax.swing.JOptionPane.showMessageDialog(this, "已從封鎖列表和數據庫中刪除 IP：" + iplist.getSelectedItem(),
                "Server Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器尚未啟動。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

/** 彈出菜單 - 密語對話 */
private void menu7ActionPerformed(java.awt.event.ActionEvent evt) {
    if (serverstart) {
        if (!inf) {
            L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
            if (pc != null) {
                name.setText(pc.getName());
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "已有其他操作正在進行。", "Server Message",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器尚未啟動。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
/** 彈出菜單 - 贈送禮物 */
private void menu6ActionPerformed(java.awt.event.ActionEvent evt) {
    /** 如果伺服器正在運行 */
    if (serverstart) {
        if (!inf) {
            L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
            if (pc != null) {
                new Give(pc);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "操作已在進行中。", "Server Message",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "伺服器尚未啟動。", "Server Message",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

public static void main(String args[]) {
    /** 使用線程進行事件處理 */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            Config.load();
            FatigueProperty.getInstance();
            new chocco().setVisible(true);
        }
    });
}

/** 每個高斯模糊像素的高度 */
private static final double perToheight = 3.3D;

class PerformReader implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                // TODO 管理員同步時間
                Thread.sleep(500L);
                int cpu = MJPerformAdapter.CPU_USAGE = (int) (getUseCpu() * 100D);
                int mem = MJPerformAdapter.MEM_USAGE = (int)SystemUtil.getUsedMemoryMB();
                int thread = MJPerformAdapter.THREAD_USAGE = Thread.activeCount();
                label4.setText(String.format("記憶體 : %d", mem));
                lblThread.setText(String.format("執行緒 : %d", thread));
                lblCPU.setText(String.format("CPU : %d%%", cpu));
                lblCPUProgressBack.setBounds(0, 0, 50, 330 - (int) (cpu * perToheight));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getUseCpu() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
    }
}
