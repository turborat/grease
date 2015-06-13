package gx.obj.x;

import gx.io.*;
import gx.obj.*;
import gx.test.*;
import org.junit.*;

public class DispatcherTest {

	private Buf buf;
	private Obj obj;

	@Before
	public void setup() throws Exception {
		obj = new Proto();
		buf = new Buf();
		TestObj.populate(obj);
		obj.to(buf);
	}

	@Test
	public void testProcess() throws Exception {
		final int[] called = new int[1];

		Dispatcher d = new Dispatcher() {
			protected void process(Obj obj) {
				called[0] ++;
			}
		};

		d.dispatch(buf);
		Assert.assertEquals(1, called[0]);
	}

	@Test
	public void testDispatch() throws Exception {
		final int[] called = new int[1];
		Dispatcher d = new Dispatcher();

		d.subscribe(new Proto.Recv(){
			@Override
			public void receive(Proto proto) {
				called[0]++;
			}
		});

		d.subscribe(new Obj.Recv(){
			@Override
			public void receive(Obj obj) {
				called[0]++;
			}
		});

		d.dispatch(buf);
		Assert.assertEquals(2, called[0]);
	}
}
