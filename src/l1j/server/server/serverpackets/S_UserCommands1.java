
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands1 extends ServerBasePacket {

    public class S_UserCommands1 extends ServerBasePacket {
        private static final String S_UserCommands1 = "[C] S_UserCommands1";

        public S_UserCommands1(int number) {
            buildPacket(number); // 當創建 S_UserCommands1 對象時，構建數據包
        }

        public class S_UserCommands1 extends ServerBasePacket {
            private static final String S_UserCommands1 = "[C] S_UserCommands1";

            public S_UserCommands1(int number) {
                buildPacket(number); // 當創建 S_UserCommands1 對象時，構建數據包
            }

            private void buildPacket(int number) {
                writeC(Opcodes.S_BOARD_READ); // 寫入操作碼，用於識別這個數據包的類型
                writeD(number); // 寫入一個整數類型的數值，這裡是方法的參數 number
                writeS(" 梅蒂斯 "); // 寫入一個字符串，值為 "메蒂斯" (可能是作者)
                writeS(" 傳說武器製作秘本 "); // 寫入一個字符串，值為 "製作秘法書" (可能是日期)
                writeS(""); // 寫入一個空字符串 (可能是標題)
                    writeS("\n=== 戰士之劍 ===" +
                            "最高級紅寶石(5個)\n" +
                            "最高級祖母綠(5個)\n" +
                            "最高級藍寶石(5個)\n" +
                            " 最高級鑽石(5個)\n" +
                            " 奧里哈魯根箭 (500個)\n" +
                            " 阿西塔吉奧的灰燼(30個)\n" +
                            "\n" +
                            " ==========================");
                // 寫入多行字符串，包含製作「戰士之劍」的材料清單和補充說明
            }

            @override
            public byte[] getContent() {
                return getBytes(); // 獲取數據包的字節數組
            }

            public String getType() {
                return S_UserCommands1; // 獲取數據包的類型
            }
        }

