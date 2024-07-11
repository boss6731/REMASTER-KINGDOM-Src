package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginManager;
import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetSafeSystem.DriveSafe.MJHddIdChecker;
import l1j.server.MJTemplate.Builder.MJServerPacketBuilder;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.Opcodes;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class CS_NP_LOGIN_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_NP_LOGIN_REQ newInstance() {
		return new CS_NP_LOGIN_REQ();
	}

	private int _client_ip;
	private int _otp_security;
	private String _auth_provider;
	private String _authn_token;
	private byte[] _mac_hash;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_NP_LOGIN_REQ() {
	}

	public int get_client_ip() {
		return _client_ip;
	}

	public void set_client_ip(int val) {
		_bit |= 0x1;
		_client_ip = val;
	}

	public boolean has_client_ip() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_otp_security() {
		return _otp_security;
	}

	public void set_otp_security(int val) {
		_bit |= 0x2;
		_otp_security = val;
	}

	public boolean has_otp_security() {
		return (_bit & 0x2) == 0x2;
	}

	public String get_auth_provider() {
		return _auth_provider;
	}

	public void set_auth_provider(String val) {
		_bit |= 0x4;
		_auth_provider = val;
	}

	public boolean has_auth_provider() {
		return (_bit & 0x4) == 0x4;
	}

	public String get_authn_token() {
		return _authn_token;
	}

	public void set_authn_token(String val) {
		_bit |= 0x8;
		_authn_token = val;
	}

	public boolean has_authn_token() {
		return (_bit & 0x8) == 0x8;
	}

	public byte[] get_mac_hash() {
		return _mac_hash;
	}

	public void set_mac_hash(byte[] val) {
		_bit |= 0x10;
		_mac_hash = val;
	}

	public boolean has_mac_hash() {
		return (_bit & 0x10) == 0x10;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_client_ip())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _client_ip);
		if (has_otp_security())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _otp_security);
		if (has_auth_provider())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _auth_provider);
		if (has_authn_token())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _authn_token);
		if (has_mac_hash())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(5, _mac_hash);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_client_ip()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_otp_security()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_auth_provider()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_authn_token()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_mac_hash()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_client_ip()) {
			output.writeUInt32(1, _client_ip);
		}
		if (has_otp_security()) {
			output.writeUInt32(2, _otp_security);
		}
		if (has_auth_provider()) {
			output.writeString(3, _auth_provider);
		}
		if (has_authn_token()) {
			output.writeString(4, _authn_token);
		}
		if (has_mac_hash()) {
			output.writeBytes(5, _mac_hash);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x00000008: {
					set_client_ip(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_otp_security(input.readUInt32());
					break;
				}
				case 0x0000001A: {
					set_auth_provider(input.readString());
					break;
				}
				case 0x00000022: {
					set_authn_token(input.readString());
					break;
				}
				case 0x0000002A: {
					set_mac_hash(input.readBytes());
					break;
				}
			}
		}
		return this;
	}
	// @Override
	// public MJIProtoMessage readFrom(final l1j.server.server.GameClient clnt,
	// byte[] bytes){
	// l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is =
	// l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes,
	// l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] &
	// 0xff) | (bytes[4] << 8 & 0xff00)) +
	// l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
	// try{
	// readFrom(is);
	//
	// if (!isInitialized())
	// return this;
	//
	// //dQzg4NzU0OEItMkFDMS1FMTExLTk1QUEtRTYxRjEzNUU5OTJGOjQ2N0NFMTkwLTkwRTUtNDcwMS04OTAyLTc0MzIyRkZGQTYzMQA=
	// //System.out.println("dQzg4NzU0OEItMkFDMS1FMTExLTk1QUEtRTYxRjEzNUU5OTJGOjQ2N0NFMTkwLTkwRTUtNDcwMS04OTAyLTc0MzIyRkZGQTYzMQA=");
	// //System.out.println(_authn_token);
	// if (!MJHttpLoginManager.getInstance().onAuthLogin(clnt, _authn_token)) {
	// //String[] account = Decrypt(_authn_token,
	// "mOIjQ7ffyEV6w1SodWVqfwoU7qJCxzIhsqw6IM30okU=").split("\n");
	// String[] account = _authn_token.split("\n");
	// int reason = MJHddIdChecker.get_denials(account[2]);
	// if (reason != MJHddIdChecker.DENIALS_TYPE_NONE && reason !=
	// MJHddIdChecker.DENIALS_TYPE_INVALID_HDD_ID) {
	// clnt.close();
	// return this;
	// }
	//
	/// * long ns_client_version =
	// Long.parseLong(Integer.toUnsignedString(MJNetSafeLoadManager.NS_CLIENT_VERSION));
	//
	// if (Config.Login.UseVersionCheck){
	//// System.out.println(clnt.get_version());
	// if (clnt.get_version() != ns_client_version){
	// System.out.println("버전다름");
	// clnt.setStatus(MJClientStatus.CLNT_STS_HANDSHAKE);
	// return this;
	// }
	// }*/
	//
	// MJServerPacketBuilder builder = new MJServerPacketBuilder(128);
	// builder.addC(Opcodes.C_LOGIN);
	// builder.addS(account[0]);
	// builder.addS(account[1]);
	// clnt.getStatus().process(clnt, builder.toArray());
	// builder.close();
	// // clnt.set_Auth_Token(Decrypt(_authn_token,
	// "mOIjQ7ffyEV6w1SodWVqfwoU7qJCxzIhsqw6IM30okU="));
	// clnt.set_Auth_Token(account[0]);
	// }
	//
	//
	// } catch(Exception e){
	// e.printStackTrace();
	// }
	// return this;
	// }

	@Override
	public MJIProtoMessage readFrom(final l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			// dQzg4NzU0OEItMkFDMS1FMTExLTk1QUEtRTYxRjEzNUU5OTJGOjQ2N0NFMTkwLTkwRTUtNDcwMS04OTAyLTc0MzIyRkZGQTYzMQA=
			// System.out.println("dQzg4NzU0OEItMkFDMS1FMTExLTk1QUEtRTYxRjEzNUU5OTJGOjQ2N0NFMTkwLTkwRTUtNDcwMS04OTAyLTc0MzIyRkZGQTYzMQA=");
			// System.out.println(_authn_token);
			if (!MJHttpLoginManager.getInstance().onAuthLogin(clnt, _authn_token)) {
				// String[] account = Decrypt(_authn_token,
				// "mOIjQ7ffyEV6w1SodWVqfwoU7qJCxzIhsqw6IM30okU=").split("\n");
				String[] account = _authn_token.split("\n");
				// int reason = MJHddIdChecker.get_denials(account[2]);
				// if (reason != MJHddIdChecker.DENIALS_TYPE_NONE && reason !=
				// MJHddIdChecker.DENIALS_TYPE_INVALID_HDD_ID) {
				// clnt.close();
				// return this;
				// }

				MJServerPacketBuilder builder = new MJServerPacketBuilder(128);
				builder.addC(Opcodes.C_LOGIN);
				builder.addS(account[0]);
				builder.addS(account[1]);
				clnt.getStatus().process(clnt, builder.toArray());
				builder.close();
				// clnt.set_Auth_Token(Decrypt(_authn_token,
				// "mOIjQ7ffyEV6w1SodWVqfwoU7qJCxzIhsqw6IM30okU="));
				clnt.set_Auth_Token(account[0]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public static String Decrypt(String text, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;

		if (len > keyBytes.length)
			len = keyBytes.length;

		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] results = cipher.doFinal(Base64.getDecoder().decode(text));
		return new String(results, "UTF-8");
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new CS_NP_LOGIN_REQ();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
