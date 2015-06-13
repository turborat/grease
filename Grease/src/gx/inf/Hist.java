package gx.inf;

import gx.etc.*;
import gx.io.*;
import gx.obj.*;

import java.io.*;

public class Hist extends GX implements Obj.Recv {

	public final static String FNAME = "gx.hist";

	private final FileIO.Writer writer ;

	public Hist(Args args) throws IOException {
		writer = new FileIO.Writer(FNAME);
	}

	@Override
	public void receive(Obj obj) {
		writer.write(obj.buf());
	}

	@Override
	protected String cleanup() {
		writer.close();
		return "close("+FNAME+")";
	}

}
