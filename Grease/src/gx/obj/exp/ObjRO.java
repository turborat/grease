package gx.obj.exp;

import gx.io.Buf;

public abstract class ObjRO extends OBJBase {

	public static final int LEN = 21;

	protected Buf buf;
	private int read;

	// type byte is first

	protected final boolean mustRead(int i) {
		if ((read & 1<<i) == 0) {
			read |= 1<<i ;
			return true;
		}
		return false;
	}

	public long seqNo() {
		if (mustRead(0))
			seqNo = buf.getLong(1);
		return seqNo;
	}

	public long ts() {
		if (mustRead(1))
			ts = buf.getLong(9);
		return ts;
	}

	@Deprecated
	public int addr() {
		if (mustRead(2))
			addr = buf.getInt(17);
		return addr;
	}

	public void from(Buf buf) {
		this.buf = buf;
		this.read = 0;
	}
}
