package gx.obj.exp;

import gx.io.Buf;

public abstract class ObjRW extends OBJBase {

	public interface Recv {
		void receive(ObjRW obj) ;
	}

	private long set = 0;

	public void to(Buf buf) {
		buf.putByte(type());
		buf.putLong(seqNo());
		buf.putLong(ts());
		buf.putInt(addr());
	}

	public void reset() {
		set = 0 ;
	}

	protected boolean isSet(int i) {
		return (set & 1<<i) != 0 ;
	}

	protected void set(int i) {
		set |= 1<<i ;
	}

	public void seqNo(long seqNo) {
		this.seqNo = seqNo;
		set(0);
	}

	public void ts(long ts) {
		this.ts = ts;
		set(1);
	}

	@Deprecated
	public void addr(int addr) {
		this.addr = addr;
		set(2);
	}

}
