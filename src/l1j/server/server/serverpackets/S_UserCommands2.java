
package l1j.server.server.serverpackets;
import l1j.server.server.Opcodes;

public class S_UserCommands2 extends ServerBasePacket {

	private static final String S_UserCommands2 = "[C] S_UserCommands2";

    public class S_UserCommands2 extends ServerBasePacket {
        private static final String S_UserCommands2 = "[C] S_UserCommands2";

        public S_UserCommands2(int number) {
            buildPacket(number); // Ó×óÜËï S_UserCommands2 ÓßßÚãÁ£¬Ï°Ëïâ¦ËàøĞ
        }

        private void buildPacket(int number) {
            writeC(Opcodes.S_BOARD_READ); // ŞĞìığÃíÂØ§£¬éÄåÚãÛÜ¬îÏËÁâ¦ËàøĞîÜ×¾úş
            writeD(number); // ŞĞìıìéËÁïÚâ¦×¾úşîÜâ¦?£¬îÏ×ëãÀÛ°ÛöîÜóÑâ¦ number
            writeS(" ¸Ş?ŞÙ "); // ŞĞìıìéËÁí®İ¬Íú£¬?? "¸Ş?ŞÙ" (Ê¦ÒöãÀíÂíº)
            writeS(" ğ²íÂİúÛößö "); // ŞĞìıìéËÁí®İ¬Íú£¬?? "ğ²íÂİúÛößö" (Ê¦ÒöãÀìíÑ¢)
            writeS(""); // ŞĞìıìéËÁÍöí®İ¬Íú (Ê¦ÒöãÀøöğ¹)
            writeS("\n === ù¦ñıÏá === \n" +
                    "\n" +
                    " íşÏá (1ËÁ)\n" +
                    " ù¦×£îÜ×÷ø¸ (15ËÁ)\n" +
                    " ù¦ñı×¨ (50ËÁ)\n" +
                    " Şâö¯â®îÜéâÙ¾ (30ËÁ)\n " +
                    "\n" +
                    " ==========================");
// ŞĞìıÒıú¼í®İ¬Íú£¬øĞùßğ²íÂ¡¸ù¦ñıÏá¡¹îÜî§Öù?Ó¤ûúÜÍõöàãÙ¥
        }

        @override
        public byte[] getContent() {
            return getBytes(); // üòö¢â¦ËàøĞîÜí®ï½â¦ğÚ
        }

        public String getType() {
            return S_UserCommands2; // üòö¢â¦ËàøĞîÜ×¾úş
        }
    }

