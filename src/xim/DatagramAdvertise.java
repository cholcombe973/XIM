package xim;

import java.nio.channels.DatagramChannel;
import java.nio.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class DatagramAdvertise extends Thread {
  private DatagramChannel c;
  private String user;

  public DatagramAdvertise(DatagramChannel dc){
    this.c = dc;
  }
  public void setUser(String user){
    this.user = user;
  }
  public void run(){
    System.out.println("Advertising service started");
    try{
      String inet = InetAddress.getLocalHost().toString();
      final String address = MakeInetString(inet);
      if(c.isOpen()){
        //send advertisments on 15sec intervals
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(
            new TimerTask(){
          ByteBuffer src = ByteBuffer.allocateDirect(1024);
          public void run(){
            try{
              src.clear();
              src.put((byte)'1');  //temp code for advertisement code
              src.put(user.getBytes());
              src.flip();
              //Setup buffer for outgoing advertisement
              SocketAddress target = new InetSocketAddress(InetAddress.getByName(address),4162);
              //Fire off advertisement
              c.send(src,target);
              //System.out.println("\nAdvertisement sent");
              }catch(IOException e){System.out.println("IOException: " + e);}
          }
        },5000,5000);
      }else{
        System.out.println("Channel not yet opened");
        return;
      }
      }catch(UnknownHostException e ){System.out.println("UnknownHostException: " + e);}
  }
  private String MakeInetString(String inet){
    String address = inet.substring(inet.indexOf("/")+1,inet.length()-(inet.length()- inet.lastIndexOf(".") - 1)).concat("255");
    return address;
  }
}
