
package l1j.server.server.serverpackets;
import l1j.server.server.Opcodes;

public class S_UserCommands2 extends ServerBasePacket {

	private static final String S_UserCommands2 = "[C] S_UserCommands2";

    public class S_UserCommands2 extends ServerBasePacket {
        private static final String S_UserCommands2 = "[C] S_UserCommands2";

        public S_UserCommands2(int number) {
            buildPacket(number); // ������ S_UserCommands2 ��������ϰ�������
        }

        private void buildPacket(int number) {
            writeC(Opcodes.S_BOARD_READ); // ��������ا��������ܬ�����������׾��
            writeD(number); // �����������׾�����?��������۰������� number
            writeS(" ��?�� "); // ���������ݬ����?? "��?��" (ʦ�������)
            writeS(" ��������� "); // ���������ݬ����?? "���������" (ʦ������Ѣ)
            writeS(""); // �����������ݬ�� (ʦ�������)
            writeS("\n === ������ === \n" +
                    "\n" +
                    " ���� (1��)\n" +
                    " ��ף������ (15��)\n" +
                    " ����ר (50��)\n" +
                    " ���������پ (30��)\n " +
                    "\n" +
                    " ==========================");
// ���������ݬ����������¡������᡹�����?Ӥ��������٥
        }

        @override
        public byte[] getContent() {
            return getBytes(); // ����������������
        }

        public String getType() {
            return S_UserCommands2; // �����������׾��
        }
    }

