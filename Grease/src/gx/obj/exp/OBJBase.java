package gx.obj.exp;

import gx.etc.Util;

public abstract class OBJBase {

	protected abstract byte type();

	public abstract int len() ;

	protected abstract long seqNo();

	protected abstract long ts();

	protected abstract int addr();

	protected long seqNo;				  // 0:1
	protected long ts;					  // 1:9
	protected  int addr;					// 2:17

	public String info() {
		String info = String.format("%s %s :%d", Util.prettyTime(ts()), this, seqNo());
		if (addr() != 0) {
			info += " ->" + Util.str(addr());
		}
		return info;
  }

}
