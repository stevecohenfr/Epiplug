package net.epitech.other;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FromEndReader {
	private final File file;
	private final RandomAccessFile raf;
	private long maxByte = -1;
	private long maxLine = -1;
	private long position;

	public FromEndReader(final String file) throws IOException {
		super();
		this.file = new File(file);
		raf = new RandomAccessFile(this.file, "r");
		position = raf.length();
	}

	public void setMaxByte(final long maxByte) {
		this.maxByte = maxByte;
	}

	public void setMaxLine(final long maxLine) {
		this.maxLine = maxLine;
	}

	/**
	 * Cette méthode lit une ligne complète si la lecture ne dépasse pas ni le
	 * nombre d'octets ni le nombre lignes fixés. Si le nombre d'octets est
	 * attent mais pas le nombre de lignes, la ligne retournée sera tronquée
	 * pour ne pas dépasser la limité d'octets.
	 * 
	 * @return <code>null</code> si la limite du nombre de lignes est atteint,
	 *         sinon la ligne (complète ou tronquée).
	 * @throws IOException
	 */
	public String readLine() throws IOException {
		StringBuilder line = new StringBuilder();
		byte code = 0;
		boolean end = true;

		while (--position >= 0 && maxByte != 0 && maxLine != 0) {
			end = false;
			raf.seek(position);
			code = raf.readByte();

			if (code == 13 || code == 10) {
				// Si le premier caractère a été atteint on ne remonte pas plus
				if (position == 0) {
					break;
				}

				raf.seek(position - 1);
				int nextCode = raf.readByte();
				maxByte--;
				maxLine--;

				if ((code == 13 && nextCode == 10) || (code == 10 && nextCode == 13)) {
					position--;
				}

				break;
			}

			line.insert(0, (char) code);
			maxByte--;
		}

		if (end) {
			return null;
		} else {
			return line.toString();
		}
	}

	/**
	 * Cette méthode lit un octet.
	 * 
	 * @return <code>null</code> si la limite est atteinte, sinon l'octet lu.
	 * @throws IOException
	 */
	public Byte readByte() throws IOException {
		Byte theByte = null;

		if (position > 0 && maxByte != 0) {
			raf.seek(position - 1);
			theByte = raf.readByte();
			position--;
			maxByte--;
		}

		return theByte;
	}

	public void close() throws IOException {
		raf.close();
	}
}
