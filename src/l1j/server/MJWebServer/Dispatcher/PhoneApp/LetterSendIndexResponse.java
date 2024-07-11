package l1j.server.MJWebServer.Dispatcher.PhoneApp;

import java.sql.Timestamp;
import java.util.Map;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_SkillSound;

public class LetterSendIndexResponse extends MJHttpResponse {
	private static String m_page_document;
	private String _receiver;
	private String _content;
	private String _title;
	private String _phoneNumber;

	public LetterSendIndexResponse(MJHttpRequest request) {
		super(request);

		Map<String, String> post_datas = m_request.get_post_datas();
		// System.out.println("\r\npost data");
		for (String k : post_datas.keySet()) {
			switch (k) {
			case "Receiver":
				_receiver = post_datas.get(k);
				break;
			case "Content":
				_content = post_datas.get(k);
				break;
			case "Title":
				_title = post_datas.get(k);
				break;
			case "PhoneNumber":
				_phoneNumber = post_datas.get(k);
				break;
			}
		}
	}

	@Override
	public HttpResponse get_response() {

		StringBuilder sb = new StringBuilder();

		if (Config.Login.ServerGmPhoneNumber.equalsIgnoreCase(_phoneNumber)) {
			sb.append(WritePrivateMail());
		} else {
			sb.append("��� �ڵ������� ���޵� ������ �ƴϹǷ� ó������ �ʽ��ϴ�.");
		}
		
		m_page_document = sb.toString();

		HttpResponse response = null;
		try {
			response = create_response(HttpResponseStatus.OK, m_page_document);
		} catch (MJHttpClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private String WritePrivateMail() {
		try {
			String receiverName = _receiver;
			String subject = _title;
			String content = _content;

			L1PcInstance target = CharacterTable.getInstance().restoreCharacter(receiverName);

			if (target == null) {
				return "�������� �ʴ� �����Դϴ�.";
			}

			int id = LetterTable.getInstance().writeLetter(949, new Timestamp(System.currentTimeMillis()), "��Ƽ��", receiverName, 0, subject, content);
			if (target.getOnlineStatus() == 1) {
				target.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, "��Ƽ��", subject)); // �޴»��
				target.sendPackets(new S_SkillSound(target.getId(), 1091));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "������ �߻��Ͽ����ϴ�. CMDâ Ȯ�ιٶ��ϴ�.";
		}

		return "������ ���������� ���۵Ǿ����ϴ�.";
	}
}
