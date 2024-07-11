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
				args.notify("õö?ÓÛØ§ÜôÒö?Íö£¬ó¦ò¸á´âÍé©4ËÁí®Ý¬¡£");
				throw new Exception();
			}
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(name)) {
				args.notify("ðíÎ³ìÑàóÙ£ÜôÒö?Íö¡£");
				throw new Exception();
			}

			m_pInfo = MJPaymentInfo.newInstance(code.toUpperCase());
			if (m_pInfo == null) {
				args.notify(String.format("Ú±?ÓðÓßëëîÜõö?ãáãÓ£ºÓÛØ§ %s£¬ðíÎ³ìÑàóÙ£ %s¡£", code, name));
				return;
			}
			if (m_pInfo.get_is_use()) {
				args.notify(String.format("ú±ÓÛØ§ì«ù¬ %s ÊÇßäÞÅéÄ¡£", m_pInfo.get_character_name()));
				return;
			}
			args.getOwner().sendPackets(MJSurveySystemLoader.getInstance().registerSurvey("ãÀÜúé©ÞÅéÄú±ÓÛØ§òäú¼õö?£¿£¨ôëî¤15õ©Ò®àÔ÷É£©", args.getOwner().getId(), new MJInterfaceSurvey() {
				@override
				public void handleYes() {
					try {
						m_pInfo.use();
						args.notify("õö?à÷Íí£¡");
					} catch (Exception e) {
						args.notify("õö?ã÷ø¨£¬ôëñìãË¡£");
					}
				}

				@override
				public void handleNo() {
					args.notify("õö?ì«ö¢á¼¡£");
				}
			}));
		} catch (Exception e) {
			args.notify(".õö? [ÓÛØ§] [ðíÎ³ìÑàóÙ£]");
		}
	}

	@Override

	public void survey(L1PcInstance pc, int num, boolean isYes) {

		if (!isYes) {

			pc.sendPackets("?ì«ö¢á¼ÓÛØ§õö?¡£");

			return;

		}

		m_pInfo

				.set_account_name(pc.getAccountName())

				.set_character_name(pc.getName())

				.set_expire_date(MJNSHandler.getLocalTime())

				.set_is_use(true)

				.do_update();

		pc.getInventory().storeItem(m_pInfo.get_itemid(), m_pInfo.get_count());

		pc.sendPackets(String.format("ì«ÌèÛ¡Û¯Öõ %s ËÁéÐû³ÏçÚªù¡¡£", new DecimalFormat("#,##0").format(m_pInfo.get_count())));

	}

	}, 15000L));

} catch (Exception e) {

	args.notify(".ÓÛØ§õö? [ÓÛØ§] [ðíÎ³ìÑàóÙ£]");

}

}
