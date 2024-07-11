     package l1j.server.server.serverpackets;

     import MJNCoinSystem.MJNCoinAdenaInfo;
     import MJNCoinSystem.MJNCoinAdenaManager;
     import MJNCoinSystem.MJNCoinCharacterReport;
     import MJNCoinSystem.MJNCoinCreditLoader;
     import java.io.IOException;
     import java.util.List;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.server.model.Instance.L1NpcInstance;



     public class S_NCoinInfo
       extends ServerBasePacket
     {
       private S_NCoinInfo(int opcode) {
         writeC(opcode);
       }

         public static S_NCoinInfo ncoin_adena_show_list(L1NpcInstance board, MJNCoinAdenaManager.NcoinAdenaReport report, List<MJNCoinAdenaInfo> adenas_info) {
            // 創建一個新的 S_NCoinInfo 封包，初始化值為 152
             S_NCoinInfo nInfo = new S_NCoinInfo(152);

            // 寫入封包的初始值和板子的 ID
             nInfo.writeC(0); // 寫入一個字節，值為 0
             nInfo.writeD(board.getId()); // 寫入板子的 ID

            // 寫入最大整數值
             nInfo.writeD(2147483647); // 寫入 2147483647，表示最大整數值

            // 寫入阿德納信息列表大小加 1 的值
             nInfo.writeH(adenas_info.size() + 1); // 寫入阿德納信息列表的大小加 1

            // 寫入固定值 300
             nInfo.writeH(300); // 寫入 300

                // 寫入 0
             nInfo.writeD(0); // 寫入 0

                // 寫入中介管理者標識
             nInfo.writeS("[中介管理者]"); // 중개관리자 意為「中介管理者」

                // 寫入報告的日期，格式為 "yy/MM/dd"
             nInfo.writeS(report.m_date.replace("yy/MM/dd").substring(2));

                // 寫入標題信息
             nInfo.writeS("★ 即時價格信息 ★"); // 실시간 시세 정보 意為「實時行情信息」

                // 遍歷每個阿德納信息，並寫入相應的交易信息
             for (MJNCoinAdenaInfo aInfo : adenas_info) {
                 // 獲取角色信息
                 MJNCoinCharacterReport.MJNCoinCharacterInfo cInfo = MJNCoinCharacterReport.getInstance().get_character_info(aInfo.get_character_object_id());

                 // 獲取信用信息
                 MJNCoinCreditLoader.MJNCoinCreditInfo credit = MJNCoinCreditLoader.getInstance().select_selling_info((cInfo == null) ? 0L : cInfo.get_selling_price());

                 // 寫入交易 ID
                 nInfo.writeD(aInfo.get_trade_id());

                 // 寫入信用描述
                 nInfo.writeS(String.format("[%s]", credit.get_credit_description()));

                 // 寫入生成日期，格式為 "yy/MM/dd"
                 nInfo.writeS(aInfo.get_generate_date().replaceAll("yy/MM/dd").substring(2));

                 // 寫入阿德納數量，單位為萬
                 nInfo.writeS(String.format("%,d我只賣金幣.", aInfo.get_adena_amount() / 10000));
             }

            // 返回封包
             return nInfo;
         }

         public static S_NCoinInfo ncoin_adena_show_report(MJNCoinAdenaManager.NcoinAdenaReport report) {
            // 創建一個新的 S_NCoinInfo 封包，初始化值為 248
             S_NCoinInfo nInfo = new S_NCoinInfo(248);

            // 判斷報告中的當前交易數量是否小於等於 0
             if (report.m_current_trade_count <= 0L) {
                // 如果當前交易數量小於等於 0，寫入錯誤信息
                 nInfo.writeD(0); // 寫入 0 作為交易 ID
                 nInfo.writeS("[管理員]"); // 관리자 意為「管理員」
                 nInfo.writeS("即時交易信息"); // 실시간 거래 정보 意為「實時交易信息」
                 nInfo.writeS(report.m_date.replace("yy/MM/dd").substring(2)); // 寫入日期，格式為 "yy/MM/dd"
                 nInfo.writeS("暫無交易訊息."); // 거래 정보가 없습니다 意為「沒有交易信息」
                 return nInfo;
             }

            // 如果有交易信息，寫入交易相關信息
             nInfo.writeD(0); // 寫入 0 作為交易 ID
             nInfo.writeS("[管理員]"); // 管理員
             nInfo.writeS("即時交易信息"); // 實時交易信息
             nInfo.writeS(report.m_date.replace("yy/MM/dd").substring(2)); // 寫入日期，格式為 "yy/MM/dd"

            // 獲取報告內容字符串
             String content = report.toString();
             StringBuilder sb = new StringBuilder(content.length() + 128);

            // 添加警告信息和報告內容
             sb.append("※不要在公告欄之外的任何網站上使用※").append("\r\n\r\n"); // 「不要在公告欄之外的任何網站上使用」
             sb.append(content);

            // 寫入完整的報告內容
             nInfo.writeS(sb.toString());

            // 返回封包
             return nInfo;
         }

         public static S_NCoinInfo ncoin_adena_show_content(int trade_id) {
             // 創建一個新的 S_NCoinInfo 封包，初始化值為 248
             S_NCoinInfo nInfo = new S_NCoinInfo(248);

             // 從 trade_id 取得 MJNCoinAdenaInfo 對象
             MJNCoinAdenaInfo aInfo = MJNCoinAdenaInfo.from_trade_id(trade_id);

             // 檢查是否取得交易信息
             if (aInfo == null) {
                 // 如果交易信息為空，寫入錯誤信息
                 nInfo.writeD(trade_id);
                 nInfo.writeS("[管理員]"); // 관리자 意為「管理員」
                 nInfo.writeS("無交易信息"); // 거래 정보 없음 意為「無交易信息」
                 nInfo.writeS(MJString.get_current_date().replace("yy/MM/dd").substring(2)); // 當前日期
                 nInfo.writeS("暫無交易訊息."); // 거래 정보가 없습니다 意為「沒有交易信息」
                 return nInfo;
             }

             // 如果有交易信息，寫入交易相關信息
             nInfo.writeD(trade_id);
             nInfo.writeS("[管理員]"); // 管理員
             nInfo.writeS(String.format("%,d我只賣通用金幣.", new Object[] { Integer.valueOf(aInfo.get_adena_amount() / 10000) }));
             // 以上表示「賣出 xxx 萬阿德納」
             nInfo.writeS(aInfo.get_generate_date().replace("yy/MM/dd").substring(2)); // 生成日期

             // 取得交易內容字符串
             String content = aInfo.toString();
             StringBuilder sb = new StringBuilder(content.length() + 128);

             // 添加保證信息和交易內容
             sb.append("※運營者保證的自主交易※").append("\r\n\r\n"); // 「運營者保證的自主交易」
             sb.append(content);

             // 寫入交易內容
             nInfo.writeS(sb.toString());

             // 返回封包
             return nInfo;
         }

         // 獲取封包內容的字節數組
         public byte[] getContent() throws IOException {
             return getBytes();
         }


