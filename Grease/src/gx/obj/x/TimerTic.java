package gx.obj.x ;

import gx.io.* ;

import gx.etc.* ;
import gx.obj.* ;

public final class TimerTic extends Obj {

  static final byte TYPE = 4 ;
  static final int   LEN = 33 ;

  public interface Recv {
    void receive(TimerTic timertic) ;
  }

  private   long t;         // 4:21
  private    int id;        // 5:29

  protected byte type() {
    return TYPE;
  }

  public int len() {
    return LEN;
  }

  public void to(Buf buf) {
    super.to(buf) ;
    buf.putLong(t()) ;
    buf.putInt(id()) ;
  }

  public void t(long t) {
    this.t = t ;
    set(4) ;
  }

  public long t() {
    if (!isSet(4)) {
      t(buf.getLong(21)) ;
    }
    return t ;
  }

  public void id(int id) {
    this.id = id ;
    set(5) ;
  }

  public int id() {
    if (!isSet(5)) {
      id(buf.getInt(29)) ;
    }
    return id ;
  }

  public String toString() {
    return "TimerTic["
      + "t:" + Util.prettyTime(t()) + " "
      + "id:" + Util.str(id())
      + "]";
  }
}