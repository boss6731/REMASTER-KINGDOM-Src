package l1j.server.swing;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.sun.management.OperatingSystemMXBean;

import MJFX.UIAdapter.MJPerformAdapter;
import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.Server;
import l1j.server.server.GameServer;
import l1j.server.server.GameServerSetting;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SystemUtil;

/** 加載成員 */

public class chocco extends JFrame {
	/** 構造函數 */
	public static boolean inf;
	public static String[] save_text = new String[7]; // 用於保存各字段內容的臨時空間
	public static boolean serverstart; // 判斷伺服器是否在運行
	public static int check = 0; // 用於通過界面調整大小（為了更改圖像）
	public static int x_size = 0; // 用於保存初始經理大小
	private ImageIcon logo = new ImageIcon("data/img/logo.jpg"); // 管理員標誌圖像
	private ImageIcon start = new ImageIcon("data/img/start.jpg"); // 開始按鈕圖像
	private ImageIcon exit = new ImageIcon("data/img/exit.jpg"); // 關閉按鈕圖像
	private ImageIcon up = new ImageIcon("data/img/up.jpg"); // 向上箭頭圖像
	private ImageIcon down = new ImageIcon("data/img/down.jpg"); // 向下箭頭圖像
	private ImageIcon so = new ImageIcon("data/img/so.jpg"); // 上箭頭圖像
	private ImageIcon chating = new ImageIcon("data/img/chat.jpg"); // 聊天圖像
	private ImageIcon in = new ImageIcon("data/img/in.jpg"); // 右箭頭圖像

	private javax.swing.JButton buf; // 全部增益按鈕
	private javax.swing.JTextField chat; // 聊天字段
	private javax.swing.JButton ex; // 關閉按鈕
	public static javax.swing.JLabel first; // 用於顯示管理員的第一句話
	private javax.swing.JInternalFrame iframe; // 用於查看各種日誌的框架
	private javax.swing.JInternalFrame iframe2; // 用於查看用戶列表的框架
	private javax.swing.JInternalFrame iframe3;
	private javax.swing.JButton info; // 伺服器製作信息按鈕
	private javax.swing.JButton info1; // 伺服器信息按鈕
	private javax.swing.JLabel label1; // 用於顯示當前用戶的標籤
	public static javax.swing.JLabel label2; // ----
	// private javax.swing.JLabel label3; // --
	private javax.swing.JLabel label4; // 用於顯示當前使用的內存量標籤
	private javax.swing.JLabel lblThread;
	private javax.swing.JLabel lblCPU;
	private javax.swing.JLabel lblCPUProgressBack;
	private javax.swing.JLabel lblCPUProgress;
	public static javax.swing.JLayeredPane layout1; // 用於類似菜單界面的佈局
	private javax.swing.JLayeredPane layout2; // --
	private javax.swing.JLayeredPane layout3; // --
	private javax.swing.JLayeredPane layout4; // --
	private javax.swing.JLayeredPane layout5;
	private javax.swing.JButton logcl; // 日誌窗口（各種文本窗口）初始化按鈕
	private javax.swing.JButton logsv; // 日誌窗口保存按鈕
	private javax.swing.JTextField name; // 悄悄話字段

	private javax.swing.JButton st; // 開始按鈕
	public static javax.swing.JLabel stime; // 用於顯示伺服器運行時間的標籤
	private javax.swing.JButton sv; // 伺服器保存按鈕
	private javax.swing.JTabbedPane tabframe; // 用於分別查看各日誌窗口的標籤面板

	private javax.swing.JScrollPane spGlobalChat; // 各日誌窗口的滾動面板
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
	public static java.awt.List userlist; // 列表
	public static java.awt.List iplist; // 封鎖IP
	private javax.swing.JPopupMenu popmenu1; // 列表窗用彈出菜單
	private javax.swing.JPopupMenu popmenu2; // 列表窗用彈出菜單2
	private javax.swing.JMenuItem menu1; // 菜單1(驅逐)
	private javax.swing.JMenuItem menu2; // 菜單2 -> ??
	private javax.swing.JMenuItem menu3; // 菜單3 //
	private javax.swing.JMenuItem menu4; // 菜單3 //
	private javax.swing.JMenuItem menu5; // 菜單3 //
	private javax.swing.JMenuItem menu6; // 贈送禮物
	private javax.swing.JMenuItem menu7; // 悄悄話
	private javax.swing.JTabbedPane chocco; // 添加用
	public static int count = 0;

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
		/** 設置每個菜單/組件 */
		initComponents();
		/** 保存初始經理大小 */
		x_size = this.getHeight();
		/** 設置初始經理大小 */
		this.setSize(this.getWidth() + 10, 60);
		/** 設置經理執行位置 */
		this.setLocation(300, 200);
	}

	/** 設置每個菜單/組件... 不能漏掉任何一個。不僅是編譯，還有可能在執行過程中出現錯誤。 */
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

		chatlvl1 = new javax.swing.JLabel(" 聊天等級");
		maxuser1 = new javax.swing.JLabel(" 最大人數");
		exp1 = new javax.swing.JLabel("經驗值倍率");
		adena1 = new javax.swing.JLabel("金幣倍率");
		item1 = new javax.swing.JLabel("道具倍率");
		weightlimit1 = new javax.swing.JLabel("最大重量");
		lawful1 = new javax.swing.JLabel("正義值倍率");
		karma1 = new javax.swing.JLabel("業力值倍率");
		enweapon1 = new javax.swing.JLabel("武器強化率");
		enarmor1 = new javax.swing.JLabel("防具強化率");

		setting.add(chatlvl1);
		setting.add(chatlvl);
		setting.add(maxuser1);
		setting.add(maxuser);
		setting.add(exp1);
		setting.add(exp);
		setting.add(adena1);
		setting.add(adena);
		setting.add(item1);
		setting.add(item);
		setting.add(weightlimit1);
		setting.add(weightlimit);
		setting.add(lawful1);
		setting.add(lawful);
		setting.add(karma1);
		setting.add(karma);
		setting.add(enweapon1);
		setting.add(enweapon);
		setting.add(enarmor1);
		setting.add(enarmor);
		set.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				setMouseClicked(evt);
			}
		});
		setting.add(set);

		menu1.setIcon(in);
		menu1.setText("強制關閉");
		menu1.setToolTipText("以角色名強制關閉");
		menu1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu1ActionPerformed(evt);
			}
		});
		popmenu1.add(menu1);

		menu2.setIcon(in);
		menu2.setText("封鎖IP");
		menu2.setToolTipText("以角色名封鎖IP");
		menu2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu2ActionPerformed(evt);
			}
		});
		popmenu1.add(menu2);

		menu7.setIcon(in);
		menu7.setText("悄悄話");
		menu7.setToolTipText("對該角色悄悄話");
		menu7.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu7ActionPerformed(evt);
			}
		});
		popmenu1.add(menu7);

		menu3.setIcon(in);
		menu3.setText("角色信息");
		menu3.setToolTipText("角色信息");
		menu3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu3ActionPerformed(evt);
			}
		});
		popmenu1.add(menu3);

		menu4.setIcon(in);
		menu4.setText("刪除IP (List)");
		menu4.setToolTipText("刪除列表中被封鎖的IP");
		menu4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu4ActionPerformed(evt);
			}
		});
		popmenu2.add(menu4);

		menu5.setIcon(in);
		menu5.setText("刪除IP (dB, List)");
		menu5.setToolTipText("刪除資料庫和列表中被封鎖的IP");
		menu5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu5ActionPerformed(evt);
			}
		});
		popmenu2.add(menu5);

		menu6.setIcon(in);
		menu6.setText("送禮");
		menu6.setToolTipText("給指定角色送禮");
		menu6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menu6ActionPerformed(evt);
			}
		});
		popmenu1.add(menu6);

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Lineage Server");
		setName("frame");
		setResizable(false);

		// 這裡

		first.setFont(new java.awt.Font("Dotum", 1, 13));
		first.setText("運行狀態：off");
		first.setForeground(Color.black);
		first.setBounds(10, 10, 100, 16);
		layout1.add(first, javax.swing.JLayeredPane.DEFAULT_LAYER);

		st.setToolTipText("Server Start");
		st.setContentAreaFilled(false);
		st.setMaximumSize(new java.awt.Dimension(21, 18));
		st.setMinimumSize(new java.awt.Dimension(21, 18));
		st.setPreferredSize(new java.awt.Dimension(21, 18));
		st.setRequestFocusEnabled(false);
		st.setSelected(true);
		st.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				stMouseClicked(evt);
			}
		});
		st.setBounds(110, 10, 21, 18);
		layout1.add(st, javax.swing.JLayeredPane.DEFAULT_LAYER);

		ex.setToolTipText("Server Exit");
		ex.setContentAreaFilled(false);
		ex.setMaximumSize(new java.awt.Dimension(21, 18));
		ex.setMinimumSize(new java.awt.Dimension(21, 18));
		ex.setPreferredSize(new java.awt.Dimension(21, 18));
		ex.setRequestFocusEnabled(false);
		ex.setSelected(true);
		ex.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				exMouseClicked(evt);
			}
		});
		ex.setBounds(140, 10, 21, 18);
		layout1.add(ex, javax.swing.JLayeredPane.DEFAULT_LAYER);

		sv.setText("R");
		sv.setToolTipText("Reload");
		sv.setContentAreaFilled(false);
		sv.setMargin(new java.awt.Insets(0, 0, 0, 0));
		sv.setMaximumSize(new java.awt.Dimension(21, 18));
		sv.setMinimumSize(new java.awt.Dimension(21, 18));
		sv.setPreferredSize(new java.awt.Dimension(21, 18));
		sv.setRequestFocusEnabled(false);
		sv.setSelected(true);
		sv.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				reloadMouseClicked(evt);
			}
		});
		sv.setBounds(170, 10, 21, 18);
		layout1.add(sv, javax.swing.JLayeredPane.DEFAULT_LAYER);

		buf.setText("E");
		buf.setToolTipText("Event");
		buf.setContentAreaFilled(false);
		buf.setMargin(new java.awt.Insets(0, 0, 0, 0));
		buf.setMaximumSize(new java.awt.Dimension(21, 18));
		buf.setMinimumSize(new java.awt.Dimension(21, 18));
		buf.setPreferredSize(new java.awt.Dimension(21, 18));
		buf.setRequestFocusEnabled(false);
		buf.setSelected(true);
		buf.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				bufMouseClicked(evt);
			}
		});
		buf.setBounds(200, 10, 21, 18);
		layout1.add(buf, javax.swing.JLayeredPane.DEFAULT_LAYER);

		info.setText("C");
		info.setToolTipText("Creat Info");
		info.setContentAreaFilled(false);
		info.setMargin(new java.awt.Insets(0, 0, 0, 0));
		info.setMaximumSize(new java.awt.Dimension(21, 18));
		info.setMinimumSize(new java.awt.Dimension(21, 18));
		info.setPreferredSize(new java.awt.Dimension(21, 18));
		info.setRequestFocusEnabled(false);
		info.setSelected(true);
		info.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				infoMouseClicked(evt);
			}
		});
		info.setBounds(260, 10, 21, 18);
		layout1.add(info, javax.swing.JLayeredPane.DEFAULT_LAYER);

		updown.setToolTipText("Page Up / Down");
		updown.setContentAreaFilled(false);
		updown.setMargin(new java.awt.Insets(0, 0, 0, 0));
		updown.setMaximumSize(new java.awt.Dimension(25, 18));
		updown.setMinimumSize(new java.awt.Dimension(25, 18));
		updown.setPreferredSize(new java.awt.Dimension(21, 18));
		updown.setRequestFocusEnabled(false);
		updown.setSelected(true);
		updown.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				updownMouseClicked(evt);
			}
		});
		updown.setBounds(350, 10, 21, 18);
		layout1.add(updown, javax.swing.JLayeredPane.DEFAULT_LAYER);

		info1.setText("!");
		info1.setToolTipText("Server Info");
		info1.setContentAreaFilled(false);
		info1.setMargin(new java.awt.Insets(0, 0, 0, 0));
		info1.setMaximumSize(new java.awt.Dimension(21, 18));
		info1.setMinimumSize(new java.awt.Dimension(21, 18));
		info1.setPreferredSize(new java.awt.Dimension(21, 18));
		info1.setRequestFocusEnabled(false);
		info1.setSelected(true);
		info1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				info1MouseClicked(evt);
			}
		});
		info1.setBounds(230, 10, 21, 18);
		layout1.add(info1, javax.swing.JLayeredPane.DEFAULT_LAYER);

		logsv.setText("S");
		logsv.setToolTipText("Server Setting");
		logsv.setContentAreaFilled(false);
		logsv.setMargin(new java.awt.Insets(0, 0, 0, 0));
		logsv.setMaximumSize(new java.awt.Dimension(25, 18));
		logsv.setMinimumSize(new java.awt.Dimension(25, 18));
		logsv.setPreferredSize(new java.awt.Dimension(21, 18));
		logsv.setRequestFocusEnabled(false);
		logsv.setSelected(true);
		logsv.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				logsvMouseClicked(evt);
			}
		});
		logsv.setBounds(290, 10, 21, 18);
		layout1.add(logsv, javax.swing.JLayeredPane.DEFAULT_LAYER);

		logcl.setText("L");
		logcl.setToolTipText("Log Clear");
		logcl.setContentAreaFilled(false);
		logcl.setMargin(new java.awt.Insets(0, 0, 0, 0));
		logcl.setMaximumSize(new java.awt.Dimension(25, 18));
		logcl.setMinimumSize(new java.awt.Dimension(25, 18));
		logcl.setPreferredSize(new java.awt.Dimension(21, 18));
		logcl.setRequestFocusEnabled(false);
		logcl.setSelected(true);
		logcl.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				logclMouseClicked(evt);
			}
		});
		logcl.setBounds(320, 10, 21, 18);
		layout1.add(logcl, javax.swing.JLayeredPane.DEFAULT_LAYER);

		label1.setFont(new java.awt.Font("Gulim", 1, 11));
		label1.setText("Users:");
		label1.setForeground(Color.red);
		label1.setToolTipText("All Players");
		label1.setBounds(400, 10, 50, 15);
		layout1.add(label1, javax.swing.JLayeredPane.DEFAULT_LAYER);

		label2.setFont(new java.awt.Font("Gulim", 1, 11));
		label2.setText(" " + count);
		label2.setForeground(Color.red);
		label2.setBounds(450, 10, 70, 16);
		layout1.add(label2, javax.swing.JLayeredPane.DEFAULT_LAYER);

		label4.setFont(new java.awt.Font("Gulim", 1, 11));
		label4.setText("Memory : 1024MB");
		label4.setForeground(Color.red);
		label4.setToolTipText("Use Memory");
		label4.setBounds(520, 10, 130, 15);
		layout1.add(label4, javax.swing.JLayeredPane.DEFAULT_LAYER);

		lblThread.setFont(new java.awt.Font("Gulim", 1, 11));
		lblThread.setText("Thread : 1000");
		lblThread.setToolTipText("Use Thread Count.");
		lblThread.setForeground(Color.red);
		lblThread.setBounds(660, 10, 100, 16);
		layout1.add(lblThread, javax.swing.JLayeredPane.DEFAULT_LAYER);

		lblCPU.setFont(new java.awt.Font("Gulim", 1, 11));
		lblCPU.setText("CPU : 100%");
		lblCPU.setToolTipText("Use CPU");
		lblCPU.setForeground(Color.red);
		lblCPU.setBounds(770, 10, 100, 16);
		layout1.add(lblCPU, javax.swing.JLayeredPane.DEFAULT_LAYER);

		iframe.setTitle("Main Game Server");
		iframe.setFrameIcon(chating);
		iframe.setVisible(true);

		txtGlobalChat.setColumns(20);
		txtGlobalChat.setEditable(false);
		txtGlobalChat.setRows(5);
		spGlobalChat.setViewportView(txtGlobalChat);
		tabframe.addTab("全體聊天", spGlobalChat);

		txtClanChat.setColumns(20);
		txtClanChat.setEditable(false);
		txtClanChat.setRows(5);
		spClanChat.setViewportView(txtClanChat);
		tabframe.addTab("血盟聊天", spClanChat);

		txtPartyChat.setColumns(20);
		txtPartyChat.setEditable(false);
		txtPartyChat.setRows(5);
		spPartyChat.setViewportView(txtPartyChat);
		tabframe.addTab("隊伍聊天", spPartyChat);

		txtWhisper.setColumns(20);
		txtWhisper.setEditable(false);
		txtWhisper.setRows(5);
		spWhisper.setViewportView(txtWhisper);
		tabframe.addTab("悄悄話", spWhisper);

		txtShop.setColumns(20);
		txtShop.setEditable(false);
		txtShop.setRows(5);
		spShop.setViewportView(txtShop);

		tabframe.addTab("NPC 商店", spShop);

		txtTrade.setColumns(20);
		txtTrade.setEditable(false);
		txtTrade.setRows(5);
		spTrade.setViewportView(txtTrade);
		tabframe.addTab("玩家間交易", spTrade);

		txtWarehouse.setColumns(20);
		txtWarehouse.setEditable(false);
		txtWarehouse.setRows(5);
		spWarehouse.setViewportView(txtWarehouse);
		tabframe.addTab("倉庫(綜合)", spWarehouse);

		txtEnchant.setColumns(20);
		txtEnchant.setEditable(false);
		txtEnchant.setRows(5);
		spEnchant.setViewportView(txtEnchant);
		tabframe.addTab("強化", spEnchant);

		txtPickup.setColumns(20);
		txtPickup.setEditable(false);
		txtPickup.setRows(5);
		spPickup.setViewportView(txtPickup);
		tabframe.addTab("物品日誌", spPickup);

		javax.swing.GroupLayout iframeLayout = new javax.swing.GroupLayout(iframe.getContentPane());
		iframe.getContentPane().setLayout(iframeLayout);
		iframeLayout.setHorizontalGroup(iframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(iframeLayout.createSequentialGroup()
						.addComponent(tabframe, javax.swing.GroupLayout.PREFERRED_SIZE, 700,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		iframeLayout.setVerticalGroup(iframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(tabframe, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE));

		iframe.setBounds(0, 0, 700, 350);
		layout2.add(iframe, javax.swing.JLayeredPane.DEFAULT_LAYER);
		try {
			iframe.setMaximum(true);
		} catch (java.beans.PropertyVetoException e1) {
			e1.printStackTrace();
		}

		iframe2.setTitle("Server List");
		iframe2.setFrameIcon(logo);
		iframe2.setVisible(true);

		userlist.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				userlistMouseClicked(evt);
			}
		});

		iplist.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				iplistMouseClicked(evt);
			}
		});
		iplist.setFont(new Font("Gulim", 0, 11));

		spUserList.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spUserList.setViewportView(userlist);
		userlist.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				userlistActionPerformed(evt);
			}
		});
		chocco.addTab("Pc", so, userlist);
		chocco.addTab("Ip", so, iplist);

		javax.swing.GroupLayout iframe2Layout = new javax.swing.GroupLayout(iframe2.getContentPane());
		iframe2.getContentPane().setLayout(iframe2Layout);
		iframe2Layout.setHorizontalGroup(iframe2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(iframe2Layout.createSequentialGroup()
						.addComponent(chocco, javax.swing.GroupLayout.PREFERRED_SIZE, 115,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		iframe2Layout.setVerticalGroup(iframe2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(chocco, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE));

		iframe2.setBounds(0, 0, 115, 340);
		layout3.add(iframe2, javax.swing.JLayeredPane.DEFAULT_LAYER);
		try {
			iframe2.setMaximum(true);
		} catch (java.beans.PropertyVetoException e1) {
			e1.printStackTrace();
		}
		iframe3.setVisible(true);
		javax.swing.GroupLayout iframe3Layout = new javax.swing.GroupLayout(iframe3.getContentPane());
		iframe3.getContentPane().setLayout(iframe3Layout);
		iframe3.setBounds(0, 0, 50, 340);
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
			iframe3.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		name.setBounds(0, 0, 80, 21);
		layout4.add(name, javax.swing.JLayeredPane.DEFAULT_LAYER);

		chat.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				chatKeyPressed(evt);
			}
		});
		chat.setBounds(80, 0, 390, 21);
		layout4.add(chat, javax.swing.JLayeredPane.DEFAULT_LAYER);

		stime.setFont(new java.awt.Font("Gulim", 1, 12));
		stime.setText("");
		stime.setBounds(480, 0, 180, 20);
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
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(layout4,
								javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));

		pack();
		new Thread(new PerformReader()).start();
	}
	/**
	 * 管理各個事件的地方
	 */

	/** 伺服器啟動事件 */
	private void stMouseClicked(java.awt.event.MouseEvent evt) {
		/** 如果伺服器未在運行... */
		if (!serverstart) {
			first.setText("運行狀態: on");
			first.setForeground(Color.red);
			try {
				new Thread(new Runnable() {
					@override
					public void run() {
						new Server();
						new ServerStart(evt.getXOnScreen(), evt.getYOnScreen());
						return null;
					}
				}).start();
			} catch (Exception e) {
			}
			serverstart = true;
			/** 如果伺服器已在運行 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器已在運行。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 伺服器關閉事件 */
	private void exMouseClicked(java.awt.event.MouseEvent evt) {
		/** 如果伺服器正在運行 */
		if (serverstart) {
			/**
			 * 保存每個角色和伺服器數據後.. 進行安全關閉的編碼
			 */
			int a = JOptionPane.showConfirmDialog(this, "確定要關閉嗎？", "伺服器訊息", 2,
					JOptionPane.INFORMATION_MESSAGE);
			if (a == JOptionPane.YES_OPTION) {
				GameServer.getInstance().shutdownWithCountdown(0);
			}

		} else {
			System.exit(0);
		}
	}

	public static boolean _isManagerCommands = false;

	private void reloadMouseClicked(java.awt.event.MouseEvent evt) {
		if (serverstart) {
			if (!_isManagerCommands) {
				_isManagerCommands = true;
				new l1j.server.swing.ManagerCommands(evt.getXOnScreen(), evt.getYOnScreen());
			} else
				l1j.server.swing.MJMessageBox.show(this, "窗口已經激活。", false);
		} else
			l1j.server.swing.MJMessageBox.show(this, "伺服器未在運行。", false);
	}

	/** 事件 */
	private void bufMouseClicked(java.awt.event.MouseEvent evt) {
		/** 如果伺服器正在運行 */
		if (serverstart) {
			/** 處理各種事件 */
			Object smallList[] = { "全體增益", "障礙賽跑", "無限對戰", "全體禁言", "禁言解除" };
			String value = (String) javax.swing.JOptionPane.showInputDialog(this, "是否要開始事件?", " 伺服器訊息",
					JOptionPane.QUESTION_MESSAGE, null, smallList, smallList[0]);
			if (value != null) {
				// javax.swing.JOptionPane.showMessageDialog(this, "服務尚在準備中..", " 伺服器訊息,
				// javax.swing.JOptionPane.INFORMATION_MESSAGE);
				if (value == "障礙賽跑") {
					GameServerSetting _GameServerSetting = GameServerSetting.getInstance();
					// _GameServerSetting.BugRaceRestart = true;
				} else if (value == "全體增益") {
					int[] allBuffSkill = { 26, 42, 48, 57, 68, 79, 158, 163, 168 };
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						L1SkillUse l1skilluse = new L1SkillUse();
						for (int i = 0; i < allBuffSkill.length; i++) {
							l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
						}
					}
				} else if (value == "全體禁言") {
					L1World.getInstance().set_worldChatElabled(false);
					txtGlobalChat.append("全體禁言執行");
				} else if (value == "禁言解除") {
					L1World.getInstance().set_worldChatElabled(true);
					txtGlobalChat.append("禁言解除執行");
				} else {
					javax.swing.JOptionPane.showMessageDialog(this, "服務尚在準備中..", " 伺服器訊息",
							javax.swing.JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", " 伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 伺服器製作信息事件 */
	private void infoMouseClicked(java.awt.event.MouseEvent evt) {
		javax.swing.JOptionPane.showMessageDialog(this, "製作: MJCodes", "伺服器訊息",
				javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}

	/** 管理器大小調整事件 */
	private void updownMouseClicked(java.awt.event.MouseEvent evt) {
		/** 如果是小的狀態 */
		if (check == 0) {
			/** 重設圖像 */
			updown.setIcon(up);
			/** 調整大小 */
			this.setSize(this.getWidth(), x_size);
			check = 1;
			/** 如果是大的狀態 */
		} else {
			updown.setIcon(down);
			this.setSize(this.getWidth(), 60);
			check = 0;
		}
	}

	/** 伺服器信息事件 */
	private void info1MouseClicked(java.awt.event.MouseEvent evt) {
		/** 顯示線程數量、數據庫池大小及伺服器相關未顯示部分 */
		try {
			if (serverstart)
				javax.swing.JOptionPane.showMessageDialog(this, "Pool : 未使用 - Max : 未使用", "伺服器訊息",
						javax.swing.JOptionPane.INFORMATION_MESSAGE);
			else
				javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
						javax.swing.JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
		}
	}

	/** 伺服器設置事件 */
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
			setting.setSize(340, 200);// 修改的部分
			setting.setLocation(250, 250);
			setting.setResizable(false);
			setting.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setting.setVisible(true);
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 伺服器日誌初始化事件 */
	private void logclMouseClicked(java.awt.event.MouseEvent evt) {
		if (serverstart) {
			try {
				LoggerInstance.getInstance().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 伺服器設置事件 */
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
		txtGlobalChat.append("\n[  伺服器設置  ]");
		txtGlobalChat.append("\n[設定] 聊天等級 : " + Config.ServerAdSetting.GLOBALCHATLEVEL);
		txtGlobalChat.append("\n[設定] 最大在線用戶 : " + Config.Login.MaximumOnlineUsers);
		txtGlobalChat.append("\n[設定] 經驗值倍率 : " + Exprate2 + " - 金幣倍率 : " + Aden2 + " - 物品倍率 : " + Droprate2);
		txtGlobalChat.append("\n[設定] 負重限制 : " + weight2 + " - 正義值倍率 : " + lawful2 + " - 業力率 : " + karma2);
		txtGlobalChat.append("\n[設定] [強化] 武器倍率 : " + enchant + " - 防具倍率 : " + armor);
		txtGlobalChat.append("\n[  伺服器設置完成  ]");
				setting.setVisible(false);
	}

	/** 伺服器聊天事件 */
	private void chatKeyPressed(java.awt.event.KeyEvent evt) {

		/** 如果按下的鍵是Enter鍵 */
		if (evt.getKeyCode() == evt.VK_ENTER) {
			if (serverstart) {
				/** 如果有聊天內容... */
				if (!chat.getText().equalsIgnoreCase("")) {
					/** 如果未指定特定角色名，即為普通聊天 */
					if (name.getText().equalsIgnoreCase("")) {
						/** 發送到全體聊天區塊 */
						txtTrade.append("\n[******]  :  " + chat.getText());
						/** 向世界地圖上所有存在的用戶發送聊天包 */
						L1World world = L1World.getInstance();
						world.broadcastServerMessage("[******] " + chat.getText());
						/** 如果指定了特定角色名... */
					} else {
						/** 在世界地圖中找到對應角色名的對象後，向該對象發送聊天包 */
						L1PcInstance pc = L1World.getInstance().getPlayer(name.getText());
						if (pc != null) {
							/** 發送到密語聊天區塊 */
							txtWhisper.append("\n[******] -> [" + name.getText() + "]  :  " + chat.getText());
							pc.sendPackets(new S_SystemMessage("[******] -> " + chat.getText()));
						} else {
							javax.swing.JOptionPane.showMessageDialog(this,
									"在世界中不存在 " + name.getText() + "。", "伺服器訊息",
									javax.swing.JOptionPane.ERROR_MESSAGE);
						}
					}
					chat.setText("");
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
						javax.swing.JOptionPane.ERROR_MESSAGE);
				chat.setText("");
			}
		}

	}

	/** 指定特定角色名 -> 先從角色列表窗口中選擇 -> 聊天文本框關聯 */
	private void userlistActionPerformed(java.awt.event.ActionEvent evt) {
		/** 滑鼠指向的事件值，如果有返回值，為了以防萬一 */
		if (!evt.getActionCommand().equalsIgnoreCase("")) {
			/** 設置name字段 */
			name.setText(evt.getActionCommand());
		}
	}

	/** 角色列表彈出菜單 */
	private void userlistMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getButton() == evt.BUTTON3) {
			popmenu1.show(userlist, evt.getX(), evt.getY());
		}
	}

	/** IP列表彈出菜單 */
	private void iplistMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getButton() == evt.BUTTON3) {
			popmenu2.show(iplist, evt.getX(), evt.getY());
		}
	}

	/** 彈出菜單 - 個人強制驅逐 */
	private void menu1ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果伺服器正在運行 */
		if (serverstart) {
			if ((!userlist.getSelectedItem().equalsIgnoreCase("")) && (userlist.getSelectedItem() != null)) {
				try {
					L1PcInstance players = L1World.getInstance().getPlayer(userlist.getSelectedItem());
					if (players != null) {
						GameServer.disconnectChar(userlist.getSelectedItem());
						javax.swing.JOptionPane.showMessageDialog(this, players.getName() + " 被強制驅逐。",
								"伺服器訊息", javax.swing.JOptionPane.INFORMATION_MESSAGE);
						userlist.remove(userlist.getSelectedItem());
					} else {
						javax.swing.JOptionPane.showMessageDialog(this,
						"在世界中不存在 " + userlist.getSelectedItem() + "。", "伺服器訊息",
								javax.swing.JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					javax.swing.JOptionPane.showMessageDialog(this, userlist.getSelectedItem() + " 的強制驅逐失敗。",
							"伺服器訊息", javax.swing.JOptionPane.INFORMATION_MESSAGE);
				} finally {
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(this, "未指定角色名。", "伺服器訊息",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
		/** 如果伺服器未啟動 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 彈出菜單 - 封鎖個人IP */
	private void menu2ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果伺服器正在運行 */
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
					javax.swing.JOptionPane.showMessageDialog(this, pc.getName() + " 被強制驅逐並封禁。",
							"伺服器訊息", javax.swing.JOptionPane.INFORMATION_MESSAGE);
				} else {
					javax.swing.JOptionPane.showMessageDialog(this,
							"在世界中不存在 " + userlist.getSelectedItem() + "。", "伺服器訊息",
							javax.swing.JOptionPane.ERROR_MESSAGE);
				}
			}
			/** 如果伺服器未啟動 */
		} else {

			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 彈出菜單 - 個人角色信息 */
	private void menu3ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果伺服器正在運行 */
		if (serverstart) {
			if (!inf) {
				L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
				if (pc != null) {
					new infomation(pc);
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(this, "已經在運行中。", "伺服器訊息",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			/** 如果伺服器未啟動 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 彈出菜單 - 刪除封鎖的IP地址列表 */
	private void menu4ActionPerformed(java.awt.event.ActionEvent evt) {
	/** 如果伺服器正在運行 */
		if (serverstart) {
			iplist.remove(iplist.getSelectedItem());
			javax.swing.JOptionPane.showMessageDialog(this, "封鎖名單(List) 刪除:" + iplist.getSelectedItem(),"伺服器訊息",
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
				/** 如果伺服器未啟動 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 彈出菜單 - 刪除封鎖的IP地址資料庫 */
	private void menu5ActionPerformed(java.awt.event.ActionEvent evt) {
		/** 如果伺服器正在運行 */
		if (serverstart) {
			IpTable.getInstance().liftBanIp(iplist.getSelectedItem());
			iplist.remove(iplist.getSelectedItem());
			javax.swing.JOptionPane.showMessageDialog(this, "封鎖名單(dB, List) 刪除:" + iplist.getSelectedItem(),"伺服器訊息",
					javax.swing.JOptionPane.INFORMATION_MESSAGE);
			/** 如果伺服器沒有在運行 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 彈出菜單 - 密語 */
	private void menu7ActionPerformed(java.awt.event.ActionEvent evt) {
	/** 如果伺服器正在運行 */
		if (serverstart) {
			if (!inf) {
				L1PcInstance pc = L1World.getInstance().getPlayer(userlist.getSelectedItem());
				if (pc != null) {
					name.setText(pc.getName());
				}
			} else {
				javax.swing.JOptionPane.showMessageDialog(this, "已經在運行中。", "伺服器訊息",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			/** 如果伺服器沒有在執行中 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
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
				javax.swing.JOptionPane.showMessageDialog(this, "已經在運行中。", "伺服器訊息",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			/** 如果伺服器未在運行 */
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "伺服器未啟動。", "伺服器訊息",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String args[]) {
		/** 通過線程處理事件查詢。 */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				Config.load();
				FatigueProperty.getInstance();
				new chocco().setVisible(true);
				return null;
			}
		});
	}

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
					label4.setText(String.format("Memory : %d", mem));
					lblThread.setText(String.format("Thread : %d", thread));
					lblCPU.setText(String.format("CPU : %d%%", cpu));
					lblCPUProgressBack.setBounds(0, 0, 50, 330 - (int) (cpu * perToheight));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		private double getUseCpu() {
			return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
		}
	}
}
/**
 * 將來的事件，例如用戶禁言、封鎖、驅逐、變身、物品等事件相關操作，會以彈出菜單或小按鈕形式的單獨組件提供，
 * 以不大幅改變現有界面為範圍進行製作
 */