package l1j.server.Payment;

import java.text.DecimalFormat;

import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJSurveySystem.MJInterfaceSurvey;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJPaymentUserHandler implements MJCommand{
	public static void do_execute(MJCommandArgs args){
		new MJPaymentUserHandler().execute(args);
	}
	
	private MJPaymentInfo m_pInfo;
	private MJPaymentUserHandler(){}
	@Override
	public void execute(MJCommandArgs args) {
		try {
			String code = args.nextString();
			String name = args.nextString();
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(code) || code.length() < 4) {
				args.notify("��?��ا����?����������4���ݬ��");
				throw new Exception();
			}
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(name)) {
				args.notify("��γ����٣����?����");
				throw new Exception();
			}

			m_pInfo = MJPaymentInfo.newInstance(code.toUpperCase());
			if (m_pInfo == null) {
				args.notify(String.format("ڱ?����������?���ӣ���ا %s����γ����٣ %s��", code, name));
				return;
			}
			if (m_pInfo.get_is_use()) {
				args.notify(String.format("����ا��� %s �������ġ�", m_pInfo.get_character_name()));
				return;
			}
			args.getOwner().sendPackets(MJSurveySystemLoader.getInstance().registerSurvey("�������������ا������?�������15��Ү���ɣ�", args.getOwner().getId(), new MJInterfaceSurvey() {
				@override
				public void handleYes() {
					try {
						m_pInfo.use();
						args.notify("��?����");
					} catch (Exception e) {
						args.notify("��?�����������ˡ�");
					}
				}

				@override
				public void handleNo() {
					args.notify("��?���ἡ�");
				}
			}));
		} catch (Exception e) {
			args.notify(".��? [��ا] [��γ����٣]");
		}
	}

	@Override

	public void survey(L1PcInstance pc, int num, boolean isYes) {

		if (!isYes) {

			pc.sendPackets("?������ا��?��");

			return;

		}

		m_pInfo

				.set_account_name(pc.getAccountName())

				.set_character_name(pc.getName())

				.set_expire_date(MJNSHandler.getLocalTime())

				.set_is_use(true)

				.do_update();

		pc.getInventory().storeItem(m_pInfo.get_itemid(), m_pInfo.get_count());

		pc.sendPackets(String.format("���ۡۯ�� %s ��������ڪ����", new DecimalFormat("#,##0").format(m_pInfo.get_count())));

	}

	}, 15000L));

} catch (Exception e) {

	args.notify(".��ا��? [��ا] [��γ����٣]");

}

}
