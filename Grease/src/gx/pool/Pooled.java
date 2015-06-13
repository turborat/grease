package gx.pool;

public interface Pooled {

	void reset();

	void retain();

	void release();

}
