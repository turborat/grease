package gx.inf;

import gx.etc.*;
import gx.obj.*;

public class Snooper extends GX implements Obj.Recv {

	private final boolean bytes;

	public Snooper(Args args) {
		bytes = args.has("-b", "display bytes");
	}

	@Override
	public void receive(Obj obj) {
		if (bytes) {
			System.out.printf("%10s - type:%d seqNo:%d len:%d\n%s\n",
				Util.prettyTime(obj.ts()),
				obj.buf().getByte(0),
				obj.seqNo(),
				obj.buf().limit(),
				obj.buf().picture()
			);
		}
		else {
			System.out.println(obj.info());
		}
	}

}
