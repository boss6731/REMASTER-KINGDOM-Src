//import l1j.server.server.serverpackets.S_SystemMessage;
//
//package l1j.server.server;
//
//import java.util.Calendar;
//
//import l1j.server.server.Controller.DungeonQuitController;
//import l1j.server.server.model.L1Teleport;
//import l1j.server.server.model.L1World;
//import l1j.server.server.model.Instance.L1PcInstance;
//import l1j.server.server.serverpackets.S_ChatPacket;
//import l1j.server.server.serverpackets.S_SystemMessage;
//
//public class DungeonTimer implements Runnable {
//
//    private static DungeonTimer instance;
//
//    public static final int SleepTime = 1 * 60 * 1000; // 每分鐘檢查一次
//
//    public static DungeonTimer getInstance() {
//        if (instance == null) {
//            instance = new DungeonTimer();
//        }
//        return instance;
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            for (L1PcInstance use : L1World.getInstance().getAllPlayers()) {
//                if (use == null || use.getNetConnection() == null || use.noPlayerCK || use.noPlayerck2) {
//                    continue;
//                } else {
//                    try {
//                        // if (use.getMapId() >= 53 && use.getMapId() <= 56 || use.getMapId() >= 807
//                        // && use.getMapId() <= 813) { // 奇岩
//                        // GiranTimeCheck(use);
//                        // }
//                        if (use.getMapId() >= 78 && use.getMapId() <= 82) { // 奧倫
//                            OrenTimeCheck(use);
//                        }
//                        if (use.getMapId() >= 30 && use.getMapId() <= 33
//                                || use.getMapId() >= 35 && use.getMapId() <= 37
//                                || use.getMapId() == 814) { // 龍之谷
//                            DrageonTimeCheck(use);
//                        }
//                        if (use.getMapId() >= 451 && use.getMapId() <= 456
//                                || use.getMapId() >= 460 && use.getMapId() <= 466
//                                || use.getMapId() >= 470 && use.getMapId() <= 478
//                                || use.getMapId() >= 490 && use.getMapId() <= 496
//                                || use.getMapId() >= 530 && use.getMapId() <= 534
//                                || use.getMapId() == 479) {
//                            RadungeonTimeCheck(use);
//                        }
//                        if (use.getMapId() == 303) { // 夢幻之島
//                            SomeTimeCheck(use);
//                        }
//                        if (use.getMapId() == 430 || use.getMapId() == 400) { // 精靈之墓 , 古代之墓
//                            SoulTimeCheck(use);
//                        }
//                        if (use.getMapId() == 280 || use.getMapId() == 281 || use.getMapId() == 282
//                                || use.getMapId() == 283 || use.getMapId() == 284) { // 巴洛克陣營
//                            newdodungeonTimeCheck(use);
//                        }
//                        if (use.getMapId() == 285 || use.getMapId() == 286 || use.getMapId() == 287 || use.getMapId() == 288
//                                || use.getMapId() == 289) { // 亞希陣營
//                            OrenTimeCheck(use);
//                        }
//                        if (use.getMapId() == 5555 || use.getMapId() == 5556) { // 冰PC
//                            icedungeonTimeCheck(use);
//                        }
//                        if (use.getMapId() == 1 || use.getMapId() == 2) { // 說話之島
//                            islanddungeonTimeCheck(use);
//                        }
//                        initialize(); // 初始化
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    } finally {
//                        try {
//                            Thread.sleep(60000); // 整體地下城計時器每分鐘檢查一次
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//
//            }
//        }
//    }
//
//    // 進行初始化操作
//    private void initialize() {
//        try {
//            Calendar cal = Calendar.getInstance();
//            int hour = Calendar.HOUR;
//            int minute = Calendar.MINUTE;
//            /** 0 上午, 1 下午 **/
//            String amPm = "PM";
//            if (cal.get(Calendar.AM_PM) == 0) {
//                amPm = "AM";
//            }
//            // 檢查遊戲是否未開始
//            if (!DungeonQuitController.getInstance().isgameStart) {
//                // 每天上午 8:59 初始化
//                if ((amPm.equals("AM") && cal.get(hour) == 8 && cal.get(minute) == 59)){
//                    DungeonQuitController.getInstance().isgameStart = true;
//                    System.out.println("■CMD MSG■: " + amPm + " " + cal.get(hour) + " 點 " + cal.get(minute) + " 分 初始化完成。");
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("初始化錯誤時間：" + e);
//        }
//    }
//
//
//    private void GiranTimeCheck(L1PcInstance pc) {
//        if (pc.getGirandungeonTime() == 179) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_SystemMessage("警告: [奇岩] 地下城時間已經到期。"));
//        }
//        pc.setGirandungeonTime(pc.getGirandungeonTime() + 1);
//    }
//
//    private void OrenTimeCheck(L1PcInstance pc) {
//        if (pc.getOrendungeonTime() == 59) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_SystemMessage("帳號內亞希陣營地下城時間已經到期。"));
//        }
//        pc.setOrendungeonTime(pc.getOrendungeonTime() + 1);
//    }
//
//
//    private void DrageonTimeCheck(L1PcInstance pc) {
//        if (pc.getDrageonTime() == 119) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_SystemMessage("\\aA警告:\\aG[龍族]\\aA地下城時間已經到期。"));
//        }
//        pc.setDrageonTime(pc.getDrageonTime() + 1);
//    }
//
//
//    private void SomeTimeCheck(L1PcInstance pc) {
//        if (pc.getSomeTime() == 29) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_SystemMessage("\\aA警告:\\aG[夢幻之島]\\aA地下城時間已經到期。"));
//        }
//        pc.setSomeTime(pc.getSomeTime() + 1);
//    }
//
//    private void SoulTimeCheck(L1PcInstance pc) {
//        if (pc.getSoulTime() == 29) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_ChatPacket(pc, "墓地停留時間已經到期。"));
//        }
//        pc.setSoulTime(pc.getSoulTime() + 1);
//    }
//
//
//    private void RadungeonTimeCheck(L1PcInstance pc) {
//        if (pc.getRadungeonTime() == 119) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_ChatPacket(pc, "拉斯塔巴德地下城時間已經到期。"));
//        }
//        pc.setRadungeonTime(pc.getRadungeonTime() + 1);
//    }
//
//    private void newdodungeonTimeCheck(L1PcInstance pc) {
//        if (pc.getnewdodungeonTime() == 59) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_SystemMessage("\\aA警告: \\aG[巴洛克陣營]\\aA地下城時間已經到期。"));
//        }
//        pc.setnewdodungeonTime(pc.getnewdodungeonTime() + 1);
//    }
//
//    private void icedungeonTimeCheck(L1PcInstance pc) {
//        if (pc.geticedungeonTime() == 29) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_SystemMessage("\\aA警告: \\aG[冰PC]\\aA地下城時間已經到期。"));
//        }
//        pc.seticedungeonTime(pc.geticedungeonTime() + 1);
//    }
//
//    private void islanddungeonTimeCheck(L1PcInstance pc) {
//        if (pc.getislandTime() == 119) {
//            // 傳送到指定座標
//            pc.start_teleport(33419, 32810, 4, 5, 169, true, false);
//            pc.sendPackets(new S_SystemMessage("\\aA警告: \\aG[說話之島]\\aA地下城時間已經到期。"));
//        }
//        pc.setislandTime(pc.getislandTime() + 1);
//    }
//}