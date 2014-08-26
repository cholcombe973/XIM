package xim;

import java.util.*;
import java.io.*;

public class Manager {
  private Hashtable users = new Hashtable();
  private Hashtable reverseUsers = new Hashtable();
  private Hashtable privateWindows = new Hashtable();
  private Hashtable aimWindows = new Hashtable();
  private XimFrame f;
  private CoreReceive cr;
  private Vector aimUsers = new Vector();

  public Manager(XimFrame f,CoreReceive cr){
    this.f = f;
    this.cr = cr;
  }
  public boolean addUser(String user,String ip){
    //adds the user to the hashtable with their ip address if not currently in ther
    if(!users.containsKey(user)){
      users.put(user,ip);
      reverseUsers.put(ip,user);
      //add's new user to the buddy list
      f.addBuddy(user);
      return true;
    }
    return false;
  }
  public String getIP(String user){
    if(users.containsKey(user)){
      return users.get(user).toString();
    }
    else{
      return "User's ip not found";
    }
  }
  public String getUser(String ip){
    if(reverseUsers.containsKey(ip)){
     return reverseUsers.get(ip).toString();
    }
    else{
      return "User not found from ip";
    }
  }
  public void registerWindow(String user,PrivateMsgWindow pw){
    privateWindows.put(user,pw);
  }
  public void registerAimWindow(String user,AimMsgWindow aw){
    aimWindows.put(user,aw);
  }
  public PrivateMsgWindow getWindow(String user){
    //return the window reference for the specified user
    return (PrivateMsgWindow)privateWindows.get(user);
  }
  public AimMsgWindow getAimWindow(String user){
    //return a aim window reference for the specified user
    return (AimMsgWindow)aimWindows.get(user);
  }
  public boolean userOnline(String user){
    if(users.containsKey(user)){
      return true;
    }
    return false;
  }

  public void removeUser(String ip){
    if(reverseUsers.containsKey(ip)){
      String user = reverseUsers.get(ip).toString();
      reverseUsers.remove(ip);
      users.remove(user);
      f.removeBuddy(user);
    }
  }
  public void storeReceiveFile(File file){
    cr.setReceiveFile(file);
  }
  public void storeSendFile(File file){
    cr.setSendFile(file);
  }
  public File getReceiveFile(){
    return cr.getReceiveFile();
  }
  public boolean isPrivateOpen(String ip){
    String user = getUser(ip);
    if(privateWindows.containsKey(user)){
     return true;
    }
    else{
      return false;
    }
  }
  public boolean isAimUserOnline(String user){
    if(aimUsers.contains(user)){
      return true;
    }else{
      return false;
    }
  }
  public void addAimUser(String user){
    if(!aimUsers.contains(user)){
      aimUsers.add(user);
      f.addBuddy(user);
    }
  }
  public void removeAimUser(String user){
    if(aimUsers.contains(user)){
      aimUsers.removeElement(user);
      f.removeBuddy(user);
    }
  }

}