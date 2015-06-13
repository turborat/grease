package gx.io;

import gx.etc.*;

import java.io.*;
import java.nio.channels.*;
import java.nio.file.*;

import static gx.etc.Util.*;
import static java.nio.file.StandardOpenOption.*;

public class FileIO implements Closeable {

	public static final int EOF = -1;

	protected final FileChannel ch;
	protected final Buf lenBuf = new Buf(2);
	protected final Path path;

	public FileIO(String fname, OpenOption... opts) {
		path = FileSystems.getDefault().getPath(fname);
		try {
			// todo: something better
			if (!path.toFile().exists()) {
				path.toFile().createNewFile();
			}
			ch = FileChannel.open(path, opts);
		} catch (IOException e) {
			throw Util.die(e);
		}
	}

	@Override
	public void close() {
		try {
			ch.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		return String.format("%s[%s]", getClass().getName().replaceAll(".*\\.", ""), path);
	}

	public static class Writer extends FileIO {
		public Writer(String fname) {
			super(fname, WRITE, CREATE, APPEND);
		}

		public int write(Buf buf) {
			lenBuf.clear();
			lenBuf.putShort(buf.limit());
			lenBuf.flip();
			_write(lenBuf);
			return _write(buf);
		}

		private int _write(Buf buf) {
			try {
				do {
					ch.write(buf.buf());
				}
				while(buf.buf().hasRemaining());
				return buf.pos();
			} catch (ClosedByInterruptException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw die(e);
			}
		}

		public void flush() {
			try {
				ch.force(false);
			} catch (IOException e) {
				die(e);
			}
		}
	}

	public static class Reader extends FileIO {
		private final boolean follow;

		public Reader(String fname, boolean follow) {
			super(fname, READ, CREATE);
			this.follow = follow;
		}

		public int read(Buf buf) {
				if (_read(lenBuf, 2) == EOF) {
					return EOF;
				}
				return _read(buf, lenBuf.getShort(0));
		}

		private int _read(Buf buf, int len) {
			try {
				buf.clear();
				buf.limit(len);
				do {
					if (ch.read(buf.buf()) == EOF && !follow) {
						return EOF;
					}
				}
				while (buf.buf().hasRemaining());
				return buf.pos();
			} catch (ClosedByInterruptException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw die(e);
			}
		}
	}
}
