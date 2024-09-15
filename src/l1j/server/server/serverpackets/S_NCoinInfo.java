package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import MJNCoinSystem.MJNCoinAdenaInfo;
import MJNCoinSystem.MJNCoinAdenaManager.NcoinAdenaReport;
import MJNCoinSystem.MJNCoinCharacterReport;
import MJNCoinSystem.MJNCoinCreditLoader;
import MJNCoinSystem.MJNCoinCreditLoader.MJNCoinCreditInfo;
import MJNCoinSystem.MJNCoinCharacterReport.MJNCoinCharacterInfo;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_NCoinInfo extends ServerBasePacket{
	private S_NCoinInfo(int opcode){
		writeC(opcode);
	}
	
	public static S_NCoinInfo ncoin_adena_show_list(L1NpcInstance board, NcoinAdenaReport report, List<MJNCoinAdenaInfo> adenas_info){
		S_NCoinInfo nInfo = new S_NCoinInfo(Opcodes.S_BOARD_LIST);
		nInfo.writeC(0x00);
		nInfo.writeD(board.getId());
		nInfo.writeD(0x7FFFFFFF);
		nInfo.writeH(adenas_info.size() + 1);
		nInfo.writeH(300);
		nInfo.writeD(0);
		nInfo.writeS("[仲介管理者]");
		nInfo.writeS(report.m_date.replace("-", "/").substring(2));
		nInfo.writeS("★ 即時行情資訊★");
		for(MJNCoinAdenaInfo aInfo : adenas_info){
			MJNCoinCharacterInfo cInfo = MJNCoinCharacterReport.getInstance().get_character_info(aInfo.get_character_object_id());
			MJNCoinCreditInfo credit = MJNCoinCreditLoader.getInstance().select_selling_info(cInfo == null ? 0 : cInfo.get_selling_price());
			nInfo.writeD(aInfo.get_trade_id());
			nInfo.writeS(String.format("[%s]", credit.get_credit_description()));
			nInfo.writeS(aInfo.get_generate_date().replaceAll("-", "/").substring(2));
			nInfo.writeS(String.format("%,d萬金幣 出售中。", aInfo.get_adena_amount() / 10000));
		}
		return nInfo;
	}
	
	public static S_NCoinInfo ncoin_adena_show_report(NcoinAdenaReport report){
		S_NCoinInfo nInfo = new S_NCoinInfo(Opcodes.S_BOARD_READ);
		if(report.m_current_trade_count <= 0){
			nInfo.writeD(0);
			nInfo.writeS("[管理者]");
			nInfo.writeS("即時交易資訊");
			nInfo.writeS(report.m_date.replace("-", "/").substring(2));
			nInfo.writeS("沒有交易資訊。");
			return nInfo;
		}
		nInfo.writeD(0);
		nInfo.writeS("[管理者]");
		nInfo.writeS("即時交易資訊");
		nInfo.writeS(report.m_date.replace("-", "/").substring(2));
		
		String content = report.toString();
		StringBuilder sb = new StringBuilder(content.length() + 128);
		sb.append("※請勿在公告欄以外的任何網站上使用※").append("\r\n\r\n");
		sb.append(content);
		nInfo.writeS(sb.toString());
		return nInfo;
	}
	
	public static S_NCoinInfo ncoin_adena_show_content(int trade_id){
		S_NCoinInfo nInfo = new S_NCoinInfo(Opcodes.S_BOARD_READ);
		MJNCoinAdenaInfo aInfo = MJNCoinAdenaInfo.from_trade_id(trade_id);
		if(aInfo == null){
			nInfo.writeD(trade_id);
			nInfo.writeS("[管理者]");
			nInfo.writeS("沒有交易資訊");
			nInfo.writeS(MJString.get_current_date().replace("-", "/").substring(2));
			nInfo.writeS("沒有交易資訊。");
			return nInfo;
		}
		
		nInfo.writeD(trade_id);
		nInfo.writeS("[管理者]");
		nInfo.writeS(String.format("%,d萬金幣 出售中。", aInfo.get_adena_amount() / 10000));
		nInfo.writeS(aInfo.get_generate_date().replace("-", "/").substring(2));

		String content = aInfo.toString();
		StringBuilder sb = new StringBuilder(content.length() + 128);
		sb.append("※管理員保障的自律交易※").append("\r\n\r\n");
		sb.append(content);
		nInfo.writeS(sb.toString());
		return nInfo;
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
