package gx.obj.x ;

import gx.io.* ;
import gx.obj.* ;

public class Dispatcher {

  private final Proto proto = new Proto() ;
  private final Ping ping = new Ping() ;
  private final TimerReq timerreq = new TimerReq() ;
  private final TimerTic timertic = new TimerTic() ;

  private Proto.Recv protoRecv;
  private Ping.Recv pingRecv;
  private TimerReq.Recv timerreqRecv;
  private TimerTic.Recv timerticRecv;
  private Obj.Recv all;

  public Obj dispatch(Buf buf) {
    Obj obj;
    byte type = buf.getByte(0);

    switch(type) {
      case Proto.TYPE: 
        obj = proto.from(buf) ;
        process(obj);
        if (protoRecv != null) {
          protoRecv.receive(proto) ;
        }
        break;

      case Ping.TYPE: 
        obj = ping.from(buf) ;
        process(obj);
        if (pingRecv != null) {
          pingRecv.receive(ping) ;
        }
        break;

      case TimerReq.TYPE: 
        obj = timerreq.from(buf) ;
        process(obj);
        if (timerreqRecv != null) {
          timerreqRecv.receive(timerreq) ;
        }
        break;

      case TimerTic.TYPE: 
        obj = timertic.from(buf) ;
        process(obj);
        if (timerticRecv != null) {
          timerticRecv.receive(timertic) ;
        }
        break;

      default:
        throw new java.lang.IllegalArgumentException("type:"+type) ;

    }

    if (all != null) {
      all.receive(obj) ;
    }

    return obj;
  }

  protected void process(Obj obj) {
  }

  public void subscribe(Object...recvr) {
    for (int i=0 ; i<recvr.length ; ++i) {
      if (recvr[i] instanceof Proto.Recv) {
        protoRecv = (Proto.Recv) recvr[i];
      }
      if (recvr[i] instanceof Ping.Recv) {
        pingRecv = (Ping.Recv) recvr[i];
      }
      if (recvr[i] instanceof TimerReq.Recv) {
        timerreqRecv = (TimerReq.Recv) recvr[i];
      }
      if (recvr[i] instanceof TimerTic.Recv) {
        timerticRecv = (TimerTic.Recv) recvr[i];
      }
      if (recvr[i] instanceof Obj.Recv) {
        all = (Obj.Recv) recvr[i];
      }
    }
  }
}