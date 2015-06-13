package gx.pool;

public class Refs implements Pooled {

	private final Pool<Pooled> pool;
	private final Pooled obj;
	private int refs;

	public Refs(Pool pool, Pooled obj) {
		this.pool = pool;
		this.obj  = obj;
	}

	protected Refs(Pool pool) {
		this.pool = pool;
		this.obj  = this;
	}

	@Override
	public void reset() {
		refs = 0;
	}

	@Override
	public final void retain() {
		refs++;
	}

	@Override
	public final void release() {
		if (--refs == 0) {
			pool.release(obj);
		}
	}

	public final int refs() {
		return refs;
	}
}
