package l1j.server.MJTemplate;

import java.io.File;

public class MJPath {
	/**
	 * î¤ÖØÌÓí®İ¬ÍúñıÊà?ìı {@link MJString#DirectorySeparatorChar} ?ùê?ßæà÷ÖØÌÓ¡£
	 * @param paths ÖØÌÓí®İ¬Íúâ¦ğÚ
	 * @return ùê?ı­îÜÖØÌÓí®İ¬Íú
	 **/
	public static String combine(String... paths) {
		StringBuilder sb = new StringBuilder(256);
		int length = paths.length - 1;
		for (int i = 0; i < length; ++i) {
			String path = paths[i];
			char c = path.charAt(path.length() - 1);
			if (MJString.isDirectorySeparatorChar(c)) {
				sb.append(path);
			} else {
				sb.append(path).append(MJString.DirectorySeparatorChar);
			}
		}
		sb.append(paths[length]);
		return sb.toString();
	}




	/**
	 * ğôÖØÌÓí®İ¬Íúñé???Ú÷üŞüªî÷Ù£¡£
	 * @param path ÖØÌÓí®İ¬Íú
	 * @return ??ÓğîÜüªî÷Ù££¨ì¹ğ¶¡°.¡±£©
	 **/
	public static String getExtension(String path) {
		if (MJString.isNullOrEmpty(path))
			return MJString.EmptyString;

		int indexof = path.lastIndexOf('.');
		return indexof == -1 ? MJString.EmptyString : path.substring(indexof + 1, path.length());
	}




	/**
	 * ğôÖØÌÓí®İ¬Íúñé???Ú÷üŞøĞùßüªî÷Ù£îÜÙşËìÙ£¡£
	 * @param path ÖØÌÓí®İ¬Íú
	 * @return øĞùßüªî÷Ù£îÜÙşËìÙ£
	 **/
	public static String getFileName(String path) {
		if (MJString.isNullOrEmpty(path))
			return MJString.EmptyString;

		return new File(path).getName();
	}




	/**
	 *
	 * ğôÖØÌÓí®İ¬Íúñé???Ú÷üŞÜôøĞùßüªî÷Ù£îÜÙşËìÙ£¡£
	 * @param path ÖØÌÓí®İ¬Íú
	 * @return ÜôøĞùßüªî÷Ù£îÜÙşËìÙ£
	 **/
	public static String getFileNameWithoutExtension(String path) {
		String fileName = getFileName(path);
		if (MJString.isNullOrEmpty(fileName))
			return MJString.EmptyString;

		int indexof = fileName.lastIndexOf('.');
		return indexof == -1 ? fileName : fileName.substring(0, indexof);
	}

