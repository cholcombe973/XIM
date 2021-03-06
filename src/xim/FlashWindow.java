package xim;

import java.awt.*;

public class FlashWindow {
  private Window chatWindow;
  private native void flashWindow(Window chatWindow,boolean flash);

  public FlashWindow(Window chatWindow) {
    this.chatWindow = chatWindow;
  }
  public void flash(boolean flash){
    flashWindow(chatWindow,flash);
  }
  static{
    System.loadLibrary("xim");
  }
}
