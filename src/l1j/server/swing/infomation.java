package l1j.server.swing;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class infomation extends javax.swing.JFrame {
    private static int cnt = 0;
    private static final int size = 580;
    private ImageIcon logo = new ImageIcon("data/img/logo.jpg"); // 管理員圖示
    private ImageIcon king_e;

    private L1PcInstance pc;
    private ArrayList<L1ItemInstance> pet;

    /** 創建新表單 */
    public infomation(L1PcInstance pcc) {
        pet = new ArrayList<L1ItemInstance>();
        pc = pcc;
        initComponents();
        this.setSize(274, this.getHeight()); // 設置大小
        this.setLocation(300, 300);
        this.setResizable(false);
        this.setVisible(true);
    }

    /** 此方法在構造函數內部調用以初始化表單。 */
    @SuppressWarnings({ "unchecked", "serial" })
    private void initComponents() {
        popup = new javax.swing.JPopupMenu(); // 右鍵彈出菜單
        jMenuItem1 = new javax.swing.JMenuItem(); // 菜單項目1
        jMenuItem2 = new javax.swing.JMenuItem(); // 菜單項目2
        jMenuItem3 = new javax.swing.JMenuItem(); // 菜單項目3
        layout1 = new javax.swing.JLayeredPane(); // 佈局層
        iframe1 = new javax.swing.JInternalFrame(); // 內部框架
        img = new javax.swing.JLabel(); // 圖片
        account = new javax.swing.JLabel(); // 帳號
        name = new javax.swing.JLabel(); // 名稱
        level = new javax.swing.JLabel(); // 等級
        hp = new javax.swing.JLabel(); // 生命值
        mp = new javax.swing.JLabel(); // 魔法值
        ac = new javax.swing.JLabel(); // 防禦力
        mr = new javax.swing.JLabel(); // 魔法防禦
        account2 = new javax.swing.JLabel(); // 帳號（第二行）
        name2 = new javax.swing.JLabel(); // 名稱（第二行）
        level2 = new javax.swing.JLabel(); // 等級（第二行）
        jLabel12 = new javax.swing.JLabel(); // 標籤12
        hp2 = new javax.swing.JLabel(); // 生命值（第二行）
        mp2 = new javax.swing.JLabel(); // 魔法值（第二行）
        ac2 = new javax.swing.JLabel(); // 防禦力（第二行）
        jLabel16 = new javax.swing.JLabel(); // 標籤16
        jLabel17 = new javax.swing.JLabel(); // 標籤17
        jLabel18 = new javax.swing.JLabel(); // 標籤18
        jLabel19 = new javax.swing.JLabel(); // 標籤19
        jLabel20 = new javax.swing.JLabel(); // 標籤20
        jLabel21 = new javax.swing.JLabel(); // 標籤21
        jLabel22 = new javax.swing.JLabel(); // 標籤22
        jLabel23 = new javax.swing.JLabel(); // 標籤23
        jLabel24 = new javax.swing.JLabel(); // 標籤24
        jLabel25 = new javax.swing.JLabel(); // 標籤25
        jLabel26 = new javax.swing.JLabel(); // 標籤26
        jLabel27 = new javax.swing.JLabel(); // 標籤27
        jLabel28 = new javax.swing.JLabel(); // 標籤28
        jLabel29 = new javax.swing.JLabel(); // 標籤29
        jLabel2 = new javax.swing.JLabel(); // 標籤2
        jLayeredPane1 = new javax.swing.JLayeredPane(); // 層次面板1
        jInternalFrame1 = new javax.swing.JInternalFrame(); // 內部框架1
        jTabbedPane1 = new javax.swing.JTabbedPane(); // 選項卡面板1
        jLayeredPane2 = new javax.swing.JLayeredPane(); // 層次面板2
        jLabel1 = new javax.swing.JLabel(); // 標籤1
        jLabel3 = new javax.swing.JLabel(); // 標籤3
        jLabel4 = new javax.swing.JLabel(); // 標籤4
        jLabel5 = new javax.swing.JLabel(); // 標籤5
        jLabel6 = new javax.swing.JLabel(); // 標籤6
        jLabel7 = new javax.swing.JLabel(); // 標籤7
        jLabel8 = new javax.swing.JLabel(); // 標籤8
        jLabel9 = new javax.swing.JLabel(); // 標籤9
        jLabel10 = new javax.swing.JLabel(); // 標籤10
        jLabel11 = new javax.swing.JLabel(); // 標籤11
        jLayeredPane3 = new javax.swing.JLayeredPane(); // 層次面板3
        jScrollPane5 = new javax.swing.JScrollPane(); // 滾動窗格5
        jTable5 = new javax.swing.JTable(); // 表格5
        jScrollPane1 = new javax.swing.JScrollPane(); // 滾動窗格1
        jTable1 = new javax.swing.JTable(); // 表格1
        jScrollPane2 = new javax.swing.JScrollPane(); // 滾動窗格2
        jTable2 = new javax.swing.JTable(); // 表格2
        jScrollPane3 = new javax.swing.JScrollPane(); // 滾動窗格3
        jTable3 = new javax.swing.JTable(); // 表格3
        jScrollPane4 = new javax.swing.JScrollPane(); // 滾動窗格4
        jTable4 = new javax.swing.JTable(); // 表格4

        jMenuItem1.setText("詳細資訊"); // 設定選單項目文字為"詳細資訊"
        jMenuItem1.setToolTipText("展開或縮小資訊視窗。"); // 設定工具提示為"展開或縮小資訊視窗。"
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() { // 設定動作監聽器
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt); // 當選單項目1被點擊時觸發的動作
            }
        });
        popup.add(jMenuItem1); // 將選單項目1新增至彈出式選單中
        
        jMenuItem2.setText("重新整理"); // 設定選單項目文字為"重新整理"
        jMenuItem2.setToolTipText("更新資訊。"); // 設定工具提示為"更新資訊。"
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() { // 設定動作監聽器
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt); // 當選單項目2被點擊時觸發的動作
            }
        });
        popup.add(jMenuItem2); // 將選單項目2新增至彈出式選單中
        
        jMenuItem3.setText("退出"); // 設定選單項目文字為"退出"
        jMenuItem3.setToolTipText("退出"); // 設定工具提示為"退出"
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() { // 設定動作監聽器
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt); // 當選單項目3被點擊時觸發的動作
            }
        });
        popup.add(jMenuItem3); // 將選單項目3新增至彈出式選單中
        
        setTitle("伺服器角色資訊"); // 設定視窗標題為"伺服器角色資訊"
        setBounds(new java.awt.Rectangle(0, 0, 0, 0)); // 設定視窗大小及位置
        setResizable(false); // 設定視窗不可調整大小
        
        iframe1.setForeground(new java.awt.Color(0, 0, 255)); // 設定iframe1前景色為藍色
        iframe1.setTitle("角色資訊"); // 設定iframe1標題為"角色資訊"
        iframe1.setToolTipText("角色資訊視窗"); // 設定iframe1工具提示為"角色資訊視窗"
        iframe1.setFrameIcon(logo); // 設定iframe1圖示為logo
        iframe1.setVisible(true); // 設定iframe1可見
        iframe1.addMouseListener(new java.awt.event.MouseAdapter() { // 設定滑鼠監聽器
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iframe1MouseClicked(evt); // 當滑鼠點擊iframe1時觸發的動作
        }
        
        });
        img.setBackground(new java.awt.Color(0, 0, 0)); // 設定背景顏色為黑色
        img.setForeground(new java.awt.Color(255, 255, 255)); // 設定前景顏色為白色
        img.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // 水平對齊方式為中央
        
        String ClassId = "";
        switch(pc.getClassId()){
            case 0: ClassId = "data/img/男領主.jpg"; break; // 如果職業編號為0，則設定ClassId為男領主圖片路徑
            case 1: ClassId = "data/img/女領主.jpg"; break; // 如果職業編號為1，則設定ClassId為女領主圖片路徑
            case 20553: ClassId = "data/img/男騎士.jpg"; break; // 如果職業編號為20553，則設定ClassId為男騎士圖片路徑
            case 48: ClassId = "data/img/女騎士.jpg"; break; // 如果職業編號為48，則設定ClassId為女騎士圖片路徑
            case 138: ClassId = "data/img/男妖精.jpg"; break; // 如果職業編號為138，則設定ClassId為男妖精圖片路徑
            case 37: ClassId = "data/img/女妖精.jpg"; break; // 如果職業編號為37，則設定ClassId為女妖精圖片路徑
            case 20278: ClassId = "data/img/男法師.jpg"; break; // 如果職業編號為20278，則設定ClassId為男法師圖片路徑
            case 20279: ClassId = "data/img/女法師.jpg"; break; // 如果職業編號為20279，則設定ClassId為女法師圖片路徑
            case 2786: ClassId = "data/img/男達爾.jpg"; break; // 如果職業編號為2786，則設定ClassId為男達爾圖片路徑
            case 2796: ClassId = "data/img/女達爾.jpg"; break; // 如果職業編號為2796，則設定ClassId為女達爾圖片路徑
            case 6658: ClassId = "data/img/男勇士.jpg"; break; // 如果職業編號為6658，則設定ClassId為男勇士圖片路徑
            case 6661: ClassId = "data/img/女勇士.jpg"; break; // 如果職業編號為6661，則設定ClassId為女勇士圖片路徑
            case 6671: ClassId = "data/img/男幻術師.jpg"; break; // 如果職業編號為6671，則設定ClassId為男幻術師圖片路徑
            case 6650: ClassId = "data/img/女幻術師.jpg"; break; // 如果職業編號為6650，則設定ClassId為女幻術師圖片路徑
            case 20567: ClassId = "data/img/男戰士.jpg"; break; // 如果職業編號為20567，則設定ClassId為男戰士圖片路徑
            case 20577: ClassId = "data/img/女戰士.jpg"; break; // 如果職業編號為20577，則設定ClassId為女戰士圖片路徑
            case 18520: ClassId = "data/img/男劍士.jpg"; break; // 如果職業編號為18520，則設定ClassId為男劍士圖片路徑
            case 18499: ClassId = "data/img/女劍士.jpg"; break; // 如果職業編號為18499，則設定ClassId為女劍士圖片路徑
            case 19296: ClassId = "data/img/男槍士.jpg"; break; // 如果職業編號為19296，則設定ClassId為男槍士圖片路徑
            case 19299: ClassId = "data/img/女槍士.jpg"; break; // 如果職業編號為19299，則設定ClassId為女槍士圖片路徑
        
        }
        king_e = new ImageIcon(ClassId);
        img.setIcon(king_e);
        img.setHorizontalTextPosition(javax.swing.JLabel.CENTER);
        img.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    }
    
    king_e = new ImageIcon(ClassId);
    img.setIcon(king_e); // 設定圖片
    img.setHorizontalTextPosition(javax.swing.JLabel.CENTER); // 文字水平置中
    img.setBorder(javax.swing.BorderFactory.createEtchedBorder()); // 設定邊框
    
    account.setFont(new java.awt.Font("標楷體", 1, 12)); // 設定字型
    account.setForeground(new java.awt.Color(0, 0, 255)); // 設定字型顏色
    account.setText("帳號名稱 : "); // 設定文字內容
    
    name.setFont(new java.awt.Font("標楷體", 1, 12));
    name.setForeground(new java.awt.Color(0, 0, 255));
    name.setText("角色名稱 : ");
    
    level.setFont(new java.awt.Font("標楷體", 1, 12));
    level.setForeground(new java.awt.Color(0, 0, 255));
    level.setText("等　　級 : ");
    
    hp.setFont(new java.awt.Font("標楷體", 1, 12));
    hp.setForeground(new java.awt.Color(0, 0, 255));
    hp.setText("生　　命 : ");
    
    mp.setFont(new java.awt.Font("標楷體", 1, 12));
    mp.setForeground(new java.awt.Color(0, 0, 255));
    mp.setText("魔　　力 : ");
    
    ac.setFont(new java.awt.Font("標楷體", 1, 12));
    ac.setForeground(new java.awt.Color(0, 0, 255));
    ac.setText("防禦力 : ");
    
    mr.setFont(new java.awt.Font("標楷體", 1, 12));
    mr.setForeground(new java.awt.Color(0, 0, 255));
    mr.setText("魔法防禦力 : ");
    
    account2.setText(pc.getAccountName()); // 設定帳號名稱
    
    name2.setText(pc.getName());    // 設定角色名稱
    
    level2.setText(Integer.toString(pc.getLevel())); // 設定等級
    
    jLabel12.setText("Lv"); // 設定文字
    
    hp2.setText(Integer.toString(pc.getMaxHp())); // 設定生命值
    
    mp2.setText(Integer.toString(pc.getMaxMp())); // 設定魔力值
    
    ac2.setText(Integer.toString(pc.getAC().getAc())); // 設定防禦力
    
    jLabel16.setText(Integer.toString(pc.getResistance().getMr())); // 設定魔法防禦力
    
    jLabel17.setFont(new java.awt.Font("標楷體", 1, 12)); // 設定字型
    jLabel17.setForeground(new java.awt.Color(0, 153, 51)); // 設定字型顏色
    jLabel17.setText("* 角色屬性資訊 *"); // 設定文字
    
    jLabel18.setFont(new java.awt.Font("標楷體", 1, 12));
    jLabel18.setForeground(new java.awt.Color(0, 0, 255));
    jLabel18.setText("力量 : ");
    
    jLabel19.setFont(new java.awt.Font("標楷體", 1, 12));
    jLabel19.setForeground(new java.awt.Color(0, 0, 255));
    jLabel19.setText("敏捷 : ");
    
    jLabel20.setFont(new java.awt.Font("標楷體", 1, 12));
    jLabel20.setForeground(new java.awt.Color(0, 0, 255));
    jLabel20.setText("體力 : ");
    
    jLabel21.setFont(new java.awt.Font("標楷體", 1, 12));
    jLabel21.setForeground(new java.awt.Color(0, 0, 255));
    jLabel21.setText("智慧 : ");
    
    jLabel22.setFont(new java.awt.Font("標楷體", 1, 12));
    jLabel22.setForeground(new java.awt.Color(0, 0, 255));
    jLabel22.setText("智力 : ");
    
    jLabel23.setFont(new java.awt.Font("標楷體", 1, 12));
    jLabel23.setForeground(new java.awt.Color(0, 0, 255));
    jLabel23.setText("魅力 : ");
    
    jLabel24.setText(Integer.toString(pc.getAbility().getStr()));
    
    jLabel25.setText(Integer.toString(pc.getAbility().getDex()));
    
    jLabel26.setText(Integer.toString(pc.getAbility().getCon()));
    
    jLabel27.setText(Integer.toString(pc.getAbility().getWis()));
    
    jLabel28.setText(Integer.toString(pc.getAbility().getInt()));
    
    jLabel29.setText(Integer.toString(pc.getAbility().getCha()));
    jLabel2.setFont(new java.awt.Font("標楷體", 1, 12)); // 設定字型
    jLabel2.setForeground(new java.awt.Color(255, 0, 0)); // 設定字型顏色
    String clas;    
        switch(pc.getType()){
            case 0: clas = "王者"; break; // 類型為0時顯示為王者
            case 1: clas = "騎士"; break; // 類型為1時顯示為騎士
            case 2: clas = "精靈"; break; // 類型為2時顯示為精靈
            case 3: clas = "法師"; break; // 類型為3時顯示為法師
            case 4: clas = "黑暗精靈"; break; // 類型為4時顯示為黑暗精靈
        case 5: clas = "龍騎士"; break; // 類型為5時顯示為龍騎士5-9 新增..
        case 6: clas = "幻術師"; break; // 類型為6時顯示為幻術師
        case 7: clas = "戰士"; break; // 類型為7時顯示為戰士
        case 8: clas = "劍士"; break; // 類型為8時顯示為劍士
        case 9: clas = "黃金槍騎"; break; // 類型為9時顯示為黃金槍騎
            default: clas = "(錯誤)"; break; // 其他類型顯示為錯誤
        }
        jLabel2.setText(clas); // 設定文字
        
        javax.swing.GroupLayout iframe1Layout = new javax.swing.GroupLayout(iframe1.getContentPane());
        iframe1.getContentPane().setLayout(iframe1Layout);
        iframe1Layout.setHorizontalGroup(
            iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(iframe1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(iframe1Layout.createSequentialGroup()
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(img, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(account)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(account2, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(name)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(name2, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(level)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(level2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(hp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hp2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(mp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mp2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(ac)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ac2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(mr)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(105, 105, 105))
                    .addComponent(jLabel17)
                    .addGroup(iframe1Layout.createSequentialGroup()
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel25))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel26)))
                        .addGap(30, 30, 30)
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel29))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28))
                            .addGroup(iframe1Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel27)))))
                .addContainerGap())
        );
        iframe1Layout.setVerticalGroup(
            iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(iframe1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(iframe1Layout.createSequentialGroup()
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(account)
                            .addComponent(account2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(name)
                            .addComponent(name2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(level)
                            .addComponent(level2)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hp)
                            .addComponent(hp2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mp)
                            .addComponent(mp2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ac)
                            .addComponent(ac2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mr)
                            .addComponent(jLabel16)))
                    .addGroup(iframe1Layout.createSequentialGroup()
                        .addComponent(img, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel21)
                    .addComponent(jLabel24)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel22)
                    .addComponent(jLabel25)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(iframe1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel23)
                    .addComponent(jLabel26)
                    .addComponent(jLabel29))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        iframe1.setBounds(0, 0, 270, 310);
        layout1.add(iframe1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        try {
            iframe1.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        jInternalFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE); // 設定內部視窗關閉動作為不執行任何動作
        jInternalFrame1.setTitle("其他資訊"); // 設定內部視窗標題為「其他資訊」
        jInternalFrame1.setToolTipText("其他資訊視窗"); // 設定滑鼠移到內部視窗上時顯示提示訊息為「其他資訊視窗」
        jInternalFrame1.setFrameIcon(logo); // 設定內部視窗圖示為 logo
        jInternalFrame1.setVisible(true); // 設定內部視窗可見
        
        jLabel1.setFont(new java.awt.Font("標楷體", 1, 12)); // 設定字型
        jLabel1.setForeground(new java.awt.Color(0, 0, 255)); // 設定字型顏色
        jLabel1.setText("城堡："); // 設定文字為「城堡：」
        jLabel1.setBounds(10, 90, 70, 15); // 設定位置
        jLayeredPane2.add(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER); // 將元件加入至 JLayeredPane 中
        
        jLabel3.setFont(new java.awt.Font("標楷體", 1, 12)); // 設定字型
        jLabel3.setForeground(new java.awt.Color(0, 0, 255)); // 設定字型顏色
        jLabel3.setText("血盟ID："); // 設定文字為「血盟ID：」
        jLabel3.setBounds(10, 30, 70, 15); // 設定位置
        jLayeredPane2.add(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER); // 將元件加入至 JLayeredPane 中
        
        jLabel4.setFont(new java.awt.Font("標楷體", 1, 12)); // 設定字型
        jLabel4.setForeground(new java.awt.Color(0, 0, 255)); // 設定字型顏色
        jLabel4.setText("血盟盟主："); // 設定文字為「血盟盟主：」
        jLabel4.setBounds(10, 50, 70, 15); // 設定位置
        jLayeredPane2.add(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER); // 將元件加入至 JLayeredPane 中
        
        jLabel5.setFont(new java.awt.Font("標楷體", 1, 12)); // 設定字型
        jLabel5.setForeground(new java.awt.Color(0, 0, 255)); // 設定字型顏色
        jLabel5.setText("擁有城堡："); // 設定文字為「擁有城堡：」
        jLabel5.setBounds(10, 70, 70, 15); // 設定位置
        jLayeredPane2.add(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER); // 將元件加入至 JLayeredPane 中
        
        jLabel6.setFont(new java.awt.Font("標楷體", 1, 12)); // 設定字型
        jLabel6.setForeground(new java.awt.Color(0, 0, 255)); // 設定字型顏色
        jLabel6.setText("血盟名稱："); // 設定文字為「血盟名稱：」
        jLabel6.setBounds(10, 10, 70, 15); // 設定位置
        jLayeredPane2.add(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER); // 將元件加入至 JLayeredPane 中
        
        L1Clan clan = null;
        if (pc.getClanname() != null){
            clan = L1World.getInstance().getClan(pc.getClanid());
        }
        String Castle = "";
        String House = "";
        String ClanName = "";
        String ClanId = "";
        String LeaderName = "";
        if(clan != null){        
            House = mesod(Integer.toString(clan.getHouseId())); // 取得城堡
            ClanName = clan.getClanName(); // 取得血盟名稱
            ClanId = Integer.toString(clan.getClanId()); // 取得血盟ID
            LeaderName = clan.getLeaderName(); // 取得盟主名稱
            switch(clan.getCastleId()){
                case 1: Castle = "肯特城"; break; // 血盟擁有肯特城
                case 2: Castle = "奧克城堡"; break; // 血盟擁有奧克城堡
                case 3: Castle = "伊甸城"; break; // 血盟擁有伊甸城
                case 4: Castle = "守護城"; break; // 血盟擁有守護城
                case 5: Castle = "海音城"; break; // 血盟擁有海音城
                case 6: Castle = "吉魯城"; break; // 血盟擁有吉魯城
                case 7: Castle = "亞丁城"; break; // 血盟擁有亞丁城
                case 8: Castle = "迪亞德城堡"; break; // 血盟擁有迪亞德城堡
                default: Castle = "(無)"; break; // 血盟沒有擁有城堡
            }
        } else if(clan == null){
            ClanName = "(尚未加入血盟)"; // 若沒有血盟則顯示「尚未加入血盟」
        }
        
        jLabel7.setText(House); // 設定城堡
        
        jLabel7.setBounds(80, 90, 110, 15);
        jLayeredPane2.add(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
       
        jLabel8.setText(ClanName);        
        jLabel8.setBounds(80, 10, 110, 15);
        jLayeredPane2.add(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        jLabel9.setText(ClanId);
        jLabel9.setBounds(80, 30, 110, 15);
        jLayeredPane2.add(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel10.setText(LeaderName);
        jLabel10.setBounds(80, 50, 110, 15);
        jLayeredPane2.add(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);		

        jLabel11.setText(Castle);
        jLabel11.setBounds(80, 70, 110, 15);
        jLayeredPane2.add(jLabel11, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
		Object[][] layta1 = new Object[180][4];
		if(clan != null){
		int pp0 = -1;
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		for (Object itemObject1 : clanWarehouse.getItems()) {
			L1ItemInstance itemm = (L1ItemInstance) itemObject1;						
			layta1[++pp0][0] = itemm.getName();
			layta1[pp0][1] = itemm.getEnchantLevel();
			if(itemm.getItem().getBless() == 0){layta1[pp0][2] = true;
		} else {
			layta1[pp0][2] = false;
            layta1[pp0][3] = itemm.getCount(); // 將物品數量放入資料列中

            jTable5.setModel(new javax.swing.table.DefaultTableModel(layta1,          
                new String [] {
                    "名稱", "強化", "祝福", "數量"
                }
            ) {
                @SuppressWarnings("rawtypes")
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Integer.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
                };
            
                @SuppressWarnings("rawtypes")
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jTable5.getTableHeader().setReorderingAllowed(false); // 設定表頭不可重新排序
            jScrollPane5.setViewportView(jTable5); // 將 jTable5 放置在 jScrollPane5 中
            jTable5.getColumnModel().getColumn(0).setResizable(false); // 設定第一欄不可調整大小
            jTable5.getColumnModel().getColumn(0).setPreferredWidth(220); // 設定第一欄首選寬度為 220
            jTable5.getColumnModel().getColumn(1).setResizable(false); // 設定第二欄不可調整大小
            jTable5.getColumnModel().getColumn(2).setResizable(false); // 設定第三欄不可調整大小
            jTable5.getColumnModel().getColumn(3).setResizable(false); // 設定第四欄不可調整大小
            
            jScrollPane5.setBounds(0, 0, 280, 140); // 設定 jScrollPane5 的位置和大小
            jLayeredPane3.add(jScrollPane5, javax.swing.JLayeredPane.DEFAULT_LAYER); // 將 jScrollPane5 加入到 jLayeredPane3 中
            
            jLayeredPane3.setBounds(0, 110, 280, 140); // 設定 jLayeredPane3 的位置和大小
            jLayeredPane2.add(jLayeredPane3, javax.swing.JLayeredPane.DEFAULT_LAYER); // 將 jLayeredPane3 加入到 jLayeredPane2 中
            
            jTabbedPane1.addTab("血盟", jLayeredPane2); // 在 jTabbedPane1 中新增一個標籤頁，標題為「血盟」
            
            // 設定 jTable1 的資料模型和欄位設定
            jTable1.setModel(new javax.swing.table.DefaultTableModel(skills(pc),
                new String [] {
                    "技能名稱", "魔法類型"
                }
            ) {
                @SuppressWarnings("rawtypes")
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false
                };
            
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jTable1.getTableHeader().setReorderingAllowed(false); // 設定表頭不可重新排序
            jScrollPane1.setViewportView(jTable1); // 將 jTable1 放置在 jScrollPane1 中
            jTable1.getColumnModel().getColumn(0).setResizable(false); // 設定第一欄不可調整大小
            jTable1.getColumnModel().getColumn(1).setResizable(false); // 設定第二欄不可調整大小
            
            jTabbedPane1.addTab("技能", jScrollPane1); // 在 jTabbedPane1 中新增一個標籤頁，標題為「技能」
            
            Object[][] layta = new Object[180][4]; // 創建一個二維陣列用於存放道具資訊
            int p = -1; // 初始化計數器為 -1
            for (Object itemObject : pc.getInventory().getItems()) { // 遍歷角色背包中的道具
                L1ItemInstance itemf = (L1ItemInstance) itemObject; // 獲取道具實例
                if (itemf.getItem().getItemId() == 40314 || itemf.getItem().getItemId() == 40316) { // 判斷是否為寵物物品
                    pet.add(itemf); // 將寵物物品添加到 pet 列表中
                }
                layta[++p][0] = itemf.getName(); // 將道具名稱放入陣列
                layta[p][1] = itemf.getEnchantLevel(); // 將強化等級放入陣列
                if (itemf.getItem().getBless() == 0) { // 判斷是否為祝福道具
                    layta[p][2] = true; // 是祝福道具
                } else {
                    layta[p][2] = false; // 不是祝福道具
                }
                layta[p][3] = itemf.getCount(); // 將道具數量放入陣列
            }
            
            jTable2.setModel(new javax.swing.table.DefaultTableModel(layta,           
                new String [] {
                    "名稱", "強化", "祝福", "數量"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Integer.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
                };
            
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jTable2.getTableHeader().setReorderingAllowed(false); // 設定表頭不可重新排序
            jScrollPane2.setViewportView(jTable2); // 將 jTable2 放置在 jScrollPane2 中
            jTable2.getColumnModel().getColumn(0).setResizable(false); // 設定第一欄不可調整大小
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(220); // 設定第一欄首選寬度為 220
            jTable2.getColumnModel().getColumn(1).setResizable(false); // 設定第二欄不可調整大小
            jTable2.getColumnModel().getColumn(2).setResizable(false); // 設定第三欄不可調整大小
            jTable2.getColumnModel().getColumn(3).setResizable(false); // 設定第四欄不可調整大小
            
            jTabbedPane1.addTab("物品", jScrollPane2); // 在 jTabbedPane1 中新增一個標籤頁，標題為「物品」
            
            // 創建一個二維陣列用於存放個人倉庫中的道具資訊
            Object[][] layta2 = new Object[180][4];
            int pp = -1; // 初始化計數器為 -1
            PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName()); // 獲取角色個人倉庫
            if (warehouse == null) return; // 若個人倉庫為空則返回
            for (Object itemObjectr : warehouse.getItems()) { // 遍歷個人倉庫中的道具
                L1ItemInstance itemm = (L1ItemInstance) itemObjectr; // 獲取道具實例
                if (itemm.getItem().getItemId() == 40314 || itemm.getItem().getItemId() == 40316) { // 判斷是否為寵物物品
                    pet.add(itemm); // 將寵物物品添加到 pet 列表中
                }
                layta2[++pp][0] = itemm.getName(); // 將道具名稱放入陣列
                layta2[pp][1] = itemm.getEnchantLevel(); // 將強化等級放入陣列
                if (itemm.getItem().getBless() == 0) { // 判斷是否為祝福道具
                    layta2[pp][2] = true; // 是祝福道具
                } else {
                    layta2[pp][2] = false; // 不是祝福道具
                }
                layta2[pp][3] = itemm.getCount(); // 將道具數量放入陣列
            }
            
            // 設定 jTable3 的資料模型和欄位設定
            jTable3.setModel(new javax.swing.table.DefaultTableModel(layta2,           
                new String [] {
                    "名稱", "強化", "祝福", "數量"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Integer.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
                };
            
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jTable3.getTableHeader().setReorderingAllowed(false); // 設定表頭不可重新排序
            jScrollPane3.setViewportView(jTable3); // 將 jTable3 放置在 jScrollPane3 中
            jTable3.getColumnModel().getColumn(0).setResizable(false); // 設定第一欄不可調整大小
            jTable3.getColumnModel().getColumn(0).setPreferredWidth(220); // 設定第一欄首選寬度為 220
            jTable3.getColumnModel().getColumn(1).setResizable(false); // 設定第二欄不可調整大小
            jTable3.getColumnModel().getColumn(2).setResizable(false); // 設定第三欄不可調整大小
            jTable3.getColumnModel().getColumn(3).setResizable(false); // 設定第四欄不可調整大小
            
            jTabbedPane1.addTab("倉庫", jScrollPane3); // 在 jTabbedPane1 中新增一個標籤頁，標題為「倉庫」
            
            // 創建一個二維陣列用於存放寵物資訊
            Object[][] layta4 = new Object[100][4];
            int lj = -1; // 初始化計數器為 -1
            for (int i = 0; i < pet.size(); i++) { // 遍歷寵物列表
                L1ItemInstance itemv = pet.get(i); // 獲取寵物道具實例
                L1Pet pett = PetTable.getInstance().getTemplate(itemv.getId()); // 獲取寵物模板
                if (pett != null) { // 若寵物模板存在
                    layta4[++lj][0] = pett.get_name(); // 將寵物名稱放入陣列
                    L1Npc l1npc = NpcTable.getInstance().getTemplate(pett.get_npcid()); // 獲取寵物對應的 NPC
                    layta4[lj][1] = l1npc.get_name(); // 將 NPC 名稱放入陣列
                    layta4[lj][2] = pett.get_level(); // 將寵物等級放入陣列
                    layta4[lj][3] = pett.get_exp(); // 將寵物經驗值放入陣列
                }
            }
            
        
            jTable4.setModel(new javax.swing.table.DefaultTableModel(layta4,          
            new String [] {
                "名稱", "種類", "等級", "經驗值"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };
        
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.getTableHeader().setReorderingAllowed(false); // 表頭不可重新排序
        jScrollPane4.setViewportView(jTable4); // 放置在 jScrollPane4 中
        jTable4.getColumnModel().getColumn(0).setResizable(false); // 第一欄不可調整大小
        jTable4.getColumnModel().getColumn(0).setPreferredWidth(200); // 第一欄首選寬度為 200
        jTable4.getColumnModel().getColumn(1).setResizable(false); // 第二欄不可調整大小
        jTable4.getColumnModel().getColumn(1).setPreferredWidth(150); // 第二欄首選寬度為 150
        jTable4.getColumnModel().getColumn(2).setResizable(false); // 第三欄不可調整大小
        jTable4.getColumnModel().getColumn(3).setResizable(false); // 第四欄不可調整大小
        jTable4.getColumnModel().getColumn(3).setPreferredWidth(120); // 第四欄首選寬度為 120
        
        jTabbedPane1.addTab("寵物", jScrollPane4); // 在 jTabbedPane1 中新增一個標籤頁，標題為「寵物」
        
        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
        );
        
        jInternalFrame1.setBounds(0, 0, 290, 290);
        jLayeredPane1.add(jInternalFrame1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        try {
            jInternalFrame1.setMaximum(true); // 設置內部框架最大化
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(layout1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
            .addComponent(layout1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
        );
        
        pack();
        }// </editor-fold>//GEN-END:initComponents
        
        private void iframe1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iframe1MouseClicked
        // TODO add your handling code here:
        if(evt.getButton() == evt.BUTTON3){
            popup.show(iframe1, evt.getX(), evt.getY());
        }
        }//GEN-LAST:event_iframe1MouseClicked
        /** 放大縮小 */
        private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        /** 放大 */
        if(cnt == 0){
            jMenuItem1.setText("簡易模式");
            cnt = 1;
            this.setSize(size, this.getHeight());
        }else{
            jMenuItem1.setText("詳細模式");
            cnt = 0;
            this.setSize(274, this.getHeight());
        }
        }//GEN-LAST:event_jMenuItem1ActionPerformed
        
        private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        /** 如果伺服器正在運行 */
        if(chocco.serverstart){
            javax.swing.JOptionPane.showMessageDialog(this, "服務準備中。", "伺服器訊息", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        /** 如果伺服器沒有運行 */
        }else{
            javax.swing.JOptionPane.showMessageDialog(this, "伺服器未運行。", "伺服器訊息", javax.swing.JOptionPane.ERROR_MESSAGE);
        }     
        }
        
        private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.setVisible(false);	
        chocco.inf = false;
        }
        
        private String mesod(String id) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String bong = "";
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT location FROM house WHERE house_id=?");
            pstm.setString(1, id);
            rs = pstm.executeQuery();
            bong = rs.getString("location");
        } catch (Exception e) {
            e.getLocalizedMessage();
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return bong;
        }
        
        private String[][] skills(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String[][] ttl = new String[100][2];
        int i = -1;String leaf= "";
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=? ");
            pstm.setInt(1, pc.getId());
            rs = pstm.executeQuery();			
            while (rs.next()) {
                int skillId = rs.getInt("skill_id");
                L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
                ttl[++i][0] = l1skills.getName();
                if(l1skills.getSkillId() >= 1 && l1skills.getSkillId() <= 80){leaf = "一般魔法";}
                else if(l1skills.getSkillId() >= 87 && l1skills.getSkillId() <= 91){leaf = "騎士魔法";}
                else if(l1skills.getSkillId() >= 97 && l1skills.getSkillId() <= 111){leaf = "黑妖魔法";}
                else if(l1skills.getSkillId() >= 113 && l1skills.getSkillId() <= 120){leaf = "領主魔法";}
                else if(l1skills.getSkillId() >= 129 && l1skills.getSkillId() <= 176){leaf = "元素魔法";}
                else if(l1skills.getSkillId() >= 177 && l1skills.getSkillId() <= 200){leaf = "龍族魔法";}
                else if(l1skills.getSkillId() >= 201 && l1skills.getSkillId() <= 224){leaf = "幻術魔法";}
                else{leaf = "錯誤資訊";}
                    ttl[i][1] = leaf;				   
            }
                
        } catch (Exception e) {
            e.getLocalizedMessage();
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return ttl;
        }
        
        
        private javax.swing.JLabel ac;
        private javax.swing.JLabel ac2;
        private javax.swing.JLabel account;
        private javax.swing.JLabel account2;
        private javax.swing.JLabel hp;
        private javax.swing.JLabel hp2;
        private javax.swing.JInternalFrame iframe1;
        private javax.swing.JLabel img;
        private javax.swing.JInternalFrame jInternalFrame1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel16;
        private javax.swing.JLabel jLabel17;
        private javax.swing.JLabel jLabel18;
        private javax.swing.JLabel jLabel19;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel20;
        private javax.swing.JLabel jLabel21;
        private javax.swing.JLabel jLabel22;
        private javax.swing.JLabel jLabel23;
        private javax.swing.JLabel jLabel24;
        private javax.swing.JLabel jLabel25;
        private javax.swing.JLabel jLabel26;
        private javax.swing.JLabel jLabel27;
        private javax.swing.JLabel jLabel28;
        private javax.swing.JLabel jLabel29;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JLayeredPane jLayeredPane1;
        private javax.swing.JLayeredPane jLayeredPane2;
        private javax.swing.JLayeredPane jLayeredPane3;
        private javax.swing.JMenuItem jMenuItem1;
        private javax.swing.JMenuItem jMenuItem2;
        private javax.swing.JMenuItem jMenuItem3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JScrollPane jScrollPane5;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JTable jTable2;
        private javax.swing.JTable jTable3;
        private javax.swing.JTable jTable4;
        private javax.swing.JTable jTable5;
        private javax.swing.JLayeredPane layout1;
        private javax.swing.JLabel level;
        private javax.swing.JLabel level2;
        private javax.swing.JLabel mp;
        private javax.swing.JLabel mp2;
        private javax.swing.JLabel mr;
        private javax.swing.JLabel name;
        private javax.swing.JLabel name2;
        private javax.swing.JPopupMenu popup;
}        