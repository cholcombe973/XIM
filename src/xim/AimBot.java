package xim;

import com.wilko.jaim.*;
import java.io.*;
import java.util.*;

public class AimBot extends Thread implements JaimEventListener{
  private JaimConnection j;
  private boolean away = false;
  private String user = new String();
  private String password = new String();
  private Manager m;
  private XimFrame f;
  public boolean aimBotSoundPlaying = false;

  public AimBot(Manager m,XimFrame f){
    this.m = m;
    this.f = f;
  }
  public void run(){
    if(user!=null&&password!=null){
      try{
        j = new JaimConnection("toc.oscar.aol.com",9898);
        j.setDebug(false);
        j.connect();
        j.watchBuddy("unknownbuddy1212");
        j.addEventListener(this);
        j.logIn(user,password,50000);
        while(!j.isLoginComplete()){
          //waste some time
        }
//        j.setIdle((int)7776000L);
        }catch(IOException e){System.out.println("IOException caught: " + e);}
        catch(JaimException e){System.out.println("JaimException caught in run(): " + e);}
    }else{
      System.out.println("UserName or password is null error" );
    }
  }
  private void receiveIM(IMTocResponse im){
    String msg = Utils.stripHTML(im.getMsg());
    String greeting = "Welcome to Chris' XIM bot.\n For commands send ?help message";
    String userFrom = im.getFrom();
    if(msg.equalsIgnoreCase("?help")){
      sendIM(userFrom,sendCommands());
    }
    else if(msg.equalsIgnoreCase("?beep")){
      beepCommand();
    }
    else if(msg.equalsIgnoreCase("?urgent")){
      urgentCommand(im);
    }
    else{
      AimMsgWindow aw = m.getAimWindow(userFrom);
      if(aw!=null){
        aw.update("From: " + userFrom.concat(": " + msg));
        File file = m.getReceiveFile();
        if(file!=null){
          if(!aimBotSoundPlaying){
            aimBotSoundPlaying = true;
            JavaZoom zoom = new JavaZoom(this);
            zoom.play(m.getReceiveFile().toString());
            zoom.start();
          }
        }
      }else{
        AimMsgWindow amw = f.openAimWindow(userFrom);
        amw.update("From: " + userFrom.concat(": " + msg));
      }
    }
  }
  public void sendIM(String user, String msg){
    try{
      j.sendIM(user,msg,false);
    }catch(IOException e){System.out.println("IOException caught at sendIM: " + e);}
  }
  public void setAway(String msg){
    try{
      j.setAway(msg);
    }catch(IOException e){System.out.println("IOException caught in setAway: " + e);}
  }
  public void setAway(boolean awayFlag){
    //this should go hand and hand with the away msg
    away = awayFlag;
  }
  public void receiveEvent(JaimEvent event){
    TocResponse tr = event.getTocResponse();
    String responseType = tr.getResponseType();

    if(responseType.equalsIgnoreCase(BuddyUpdateTocResponse.RESPONSE_TYPE)){
      buddyUpdate((BuddyUpdateTocResponse)tr);
    }
    else if (responseType.equalsIgnoreCase(IMTocResponse.RESPONSE_TYPE)) {
      if(away){
        sendGreetz((IMTocResponse)tr);
      }else{
        receiveIM((IMTocResponse)tr);
      }
    }
    else if(responseType.equalsIgnoreCase(EvilTocResponse.RESPONSE_TYPE)){
      String evil = handleEvils((EvilTocResponse)tr);
      f.createJOptionPane(evil);
    }
    else if(responseType.equalsIgnoreCase(ErrorTocResponse.RESPONSE_TYPE)){
      receiveError((ErrorTocResponse)tr);
    }
    else if(responseType.equalsIgnoreCase(ConnectionLostTocResponse.RESPONSE_TYPE)){
      connectionLost();
    }
    else if(responseType.equalsIgnoreCase(LoginCompleteTocResponse.RESPONSE_TYPE)){
      System.out.println("Aim login complete");
    }
    else if(responseType.equalsIgnoreCase(ConfigTocResponse.RESPONSE_TYPE)){
      receiveConfig();
    }
    else if(responseType.equalsIgnoreCase(GotoTocResponse.RESPONSE_TYPE)){
      handleGoto((GotoTocResponse)tr);
    }
    else{
      System.out.println("Unknown TocResponse: " + tr);
    }
  }
  private void buddyUpdate(BuddyUpdateTocResponse bu) {
    String buddy = bu.getBuddy();
    if (bu.isOnline()) {
      System.out.println(buddy + " is Online from buddyUpdate");
      m.addAimUser(buddy);
    }
    else {
      m.removeAimUser(buddy);
      System.out.println(buddy + " is Offline");
    }
    if (bu.isAway()) {
      System.out.println(buddy + " is Away");
    }
    System.out.println("evil: "+bu.getEvil());
    System.out.println("Idle: "+bu.getIdleTime());
    System.out.println("On since "+bu.getSignonTime().toString());
  }

  private String handleEvils(EvilTocResponse evil){
    if(evil.isAnonymous()){
      return "I've been warned anonymously, that jerk!";
    }
    else{
      try{
        j.sendEvil(evil.getEvilBy(),false);  // warn them back dammit
        }catch(IOException e){System.out.println("Exception occured in Evil: " + e);}
        return "You've been warned by " + evil.getEvilBy() + "\n Response measures have been taken.";
    }
  }
  public void sendGreetz(IMTocResponse im){
    String user = im.getFrom();
    String greeting = "Welcome to Chris' XIM bot.\n Working in silent mode \n For commands send ?help message";
    try{
      j.sendIM(user,greeting,false);
      }catch(IOException e){System.out.println("IOException caught at sendGreetz: " + e);}
  }
  private String sendCommands(){
    return "\n Send ?beep to make some noise \n Send ?urgent to set put your window in Focus.";
  }
  public void beepCommand(){
    java.awt.Toolkit.getDefaultToolkit().beep();
  }
  private void urgentCommand(IMTocResponse im){
    //some kind of urgent command.  Possibly make the window visible on top of all others.
    String user = im.getFrom();
    AimMsgWindow aw = m.getAimWindow(user);
    aw.requestFocus();
  }
  public void setUser(String user){
    this.user = user;
  }
  public void setPassword(String password){
    this.password = password;
  }
  private void receiveConfig() {
    try {
      Iterator it = j.getGroups().iterator();
      while (it.hasNext()) {
        Group g = (Group)it.next();
        System.out.println("Group: "+g.getName());
        Enumeration e = g.enumerateBuddies();
        while (e.hasMoreElements()) {
          Buddy b =(Buddy)e.nextElement();
          b.setDeny(false);
          b.setPermit(false);
          j.watchBuddy(b.getName());
          if (b.getDeny()) {
            j.addBlock(b.getName());
          }
          if (b.getPermit()) {
            j.addPermit(b.getName());
          }
        }
      }
      j.saveConfig();
      System.out.println("Config is now valid.");
    }
    catch (Exception e) {System.out.println("Exception caught: " + e);}
  }
  private void receiveError(ErrorTocResponse et) {
    System.out.println("Error: "+et.getErrorDescription());
  }
  private void connectionLost(){
    System.out.println("Connection lost!");
    System.out.println("Not attempting to reconnect");
    /*
    if(user!=null&&password!=null){
      try{
        j.connect();
        j.logIn(user,password,50000);
        while(!j.isLoginComplete()){
          //waste some time
        }
        }catch(IOException e){System.out.println("IOException in connectionLost: " + e);}
        catch(JaimException e){System.out.println("JaimException in connectionLost: " + e);}
    }else{
      System.out.println("Username or password is null in connectionLost");
    }
    */
  }
  private void handleGoto(GotoTocResponse gr){
    System.out.println("Attempting to access "+gr.getURL());
    try {
      InputStream is=j.getURL(gr.getURL());
      if (is!=null) {
        InputStreamReader r=new InputStreamReader(is);
        int chr=0;
        while (chr!=-1) {
          chr=r.read();
          System.out.print((char)chr);
        }
      }
    }
    catch (IOException e) {System.out.println("IOException caught: " + e);}
  }
  public void shutdownAimBot(){
    j.logOut();
  }
  public void getInfo(String user){
    try{
      j.getInfo(user);
    }catch(IOException e){System.out.println("IOException caught at getInfo: " + e);}
  }
}
