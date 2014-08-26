package xim;

import java.awt.event.*;

public class KeyAdapter implements KeyListener{
  private MsgWindow w;
  private PrivateMsgWindow pw;
  private AimMsgWindow aw;

  public KeyAdapter(MsgWindow w){
    this.w = w;
  }
  public KeyAdapter(PrivateMsgWindow w){
    this.pw = w;
  }
  public KeyAdapter(AimMsgWindow w){
    this.aw = w;
  }
  public void keyTyped(KeyEvent e){}
  public void keyReleased(KeyEvent e){}
  public void keyPressed(KeyEvent e){
    if((e.getKeyCode() == KeyEvent.VK_ENTER)&&(e.getModifiers() == InputEvent.CTRL_MASK)){
      //If user hits control+enter send the msg
      if(w!=null){
        w.jButtonSend_actionPerformed();
      }
      if(pw!=null){
        pw.jButtonSend_actionPerformed();
      }
      if(aw!=null){
        aw.jButtonSend_actionPerformed();
      }
    }
  }
}
