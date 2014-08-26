package xim;

import javazoom.jl.player.*;
import javazoom.jl.decoder.JavaLayerException;
import java.io.*;

public class JavaZoom extends Thread{
  private Player player = null;
  private FileInputStream stream;
  private String fileName;
  private File file;
  private CoreReceive cr;
  private PrivateMsgWindow pw;
  private AimBot bot;
  private MsgWindow mw;
  private AimMsgWindow aw;
  private InputStream in;

  public JavaZoom(CoreReceive cr){
    this.cr = cr;
  }
  public JavaZoom(MsgWindow mw){
    this.mw = mw;
  }
  public JavaZoom(PrivateMsgWindow pw){
    this.pw = pw;
  }
  public JavaZoom(AimBot bot){
    this.bot = bot;
  }
  public JavaZoom(AimMsgWindow aw){
    this.aw = aw;
  }
  public void play(String Filename){
    fileName = Filename;
  }
  public void playStream(InputStream in){
    this.in = in;
  }
  public void stopPlaying(){
    player.close();
  }
  public void StartPlaying(){
    try{
      player.play();
    }catch(JavaLayerException e){System.out.println("JavaLayerException in JavaZoom StartPlaying(): " + e);}
  }
  public void run(){
    try{
      if(fileName!=null){
        stream = new FileInputStream(fileName);
        player = new Player(stream);
        player.play();
      }
      else{
        System.out.println("Playing Stream");
        player = new Player(in);
        player.play();
      }
      //file = new File(fileName);
      //player = new Player(stream);
      //player.play();

      //      stream.close();
      //      player.close();

      if(cr!=null){
        if(cr.soundPlaying)
          cr.soundPlaying = false;
      }
      if(pw!=null){
        if(pw.privateMsgWindowSoundPlaying)
          pw.privateMsgWindowSoundPlaying = false;
      }
      if(bot!=null){
        if(bot.aimBotSoundPlaying)
          bot.aimBotSoundPlaying = false;
      }
      if(mw !=null){
        if(mw.msgWindowSoundPlaying)
          mw.msgWindowSoundPlaying = false;
      }
      if(aw!=null){
        if(aw.aimMsgWindowSoundPlaying)
          aw.aimMsgWindowSoundPlaying = false;
      }
    }catch(IOException e){System.out.println("IOException in JavaZoom run(): " + e);}
    catch (Exception e){System.out.println("Exception in JavaZoom run(): " + e);}
  }
}
