package gx.obj;

import gx.etc.*;
import gx.io.*;

public abstract class Obj {

	public interface Recv {
		void receive(Obj obj) ;
	}

	public static final int LEN = 21;

	protected Buf buf;
	private  long set = 0;

	// type byte is first

	private long seqNo;				  // 0:1
	private long ts;					  // 1:9
	private  int addr;					// 2:17

	protected abstract byte type();

	public abstract int len() ;

	public Buf buf() {
		return buf;
	}

	public void to(Buf buf) {
		buf.putByte(type());
		buf.putLong(seqNo());
		buf.putLong(ts());
		buf.putInt(addr());
	}

	public final <O extends Obj> O from(Buf buf) {
		this.buf = buf;
		reset();
		return (O) this;
	}

	public final void reset() {
		set = 0 ;
	}

	public final boolean isSet(int i) {
		return (set & 1<<i) != 0 ;
	}

	protected final void set(int i) {
		set |= 1<<i ;
	}

	public void seqNo(long seqNo) {
		this.seqNo = seqNo;
		set(0);
	}

	public long seqNo() {
		if (!isSet(0)) {
			seqNo(buf != null ? buf.getLong(1) : 0);
		}
		return seqNo;
	}

	public void ts(long ts) {
		this.ts = ts;
		set(1);
	}

	public long ts() {
		if (!isSet(1)) {
			ts(buf != null ? buf.getLong(9) : 0);
		}
		return ts;
	}

	@Deprecated
	public void addr(int addr) {
		this.addr = addr;
		set(2);
	}

	@Deprecated
	public int addr() {
		if (!isSet(2)) {
			addr(buf != null ? buf.getInt(17) : 0);
		}
		return addr;
	}

	public String info() {
		String info = String.format("%s %s :%d", Util.prettyTime(ts()), this, seqNo());
		if (addr() != 0) {
			info += " ->" + Util.str(addr());
		}
		return info;
  }

	public class Dat {
		private int len=0;

		void write(byte...bytes) {
			buf.putByte(len()+len, bytes);
			len += bytes.length;
		}

	}
}
