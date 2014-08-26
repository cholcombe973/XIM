package xim;

import java.net.*;
import java.io.*;

public class NewSender extends Thread {
  //Makes a new thread to send the outgoing data across the network.
  private DatagramSocket ds;
  private DatagramPacket p;
  private byte[] outgoingData;
  private InetAddress i;
  private int port;
  private String address;

  public NewSender(int port,String address,byte[] data){
    this.port = port;
    this.address = address;
    this.outgoingData = data;

  }
  public void run(){
    try{
      ds = new DatagramSocket(port);
      i = InetAddress.getByName(address);
      ds.connect(i,port);
      p = new DatagramPacket(outgoingData,outgoingData.length);
      ds.send(p);
      ds.close();
      System.out.println("Data sent");
      }catch(IOException e){System.out.println("IOException: " + e);}
  }
}
