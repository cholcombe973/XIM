package xim;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;


public class TcpSend extends Thread {
  private Manager m;
  private String userName;
  private InetSocketAddress toAddress;
  private int port;
  private SocketChannel sc;
  private File send;
  private FileChannel fChannel;
  private CoreReceive cr;

  public TcpSend(String userName, Manager m, int port, File send, CoreReceive cr){
    this.m = m;
    this.userName = userName;
    this.port = port;
    this.send = send;
    this.cr = cr;
    sendConnectionData();
  }
  private void connect(){
    try{
      String ip = m.getIP(userName);
      toAddress = new InetSocketAddress(InetAddress.getByName(ip),port);
      sc.configureBlocking(false);
      sc.connect(toAddress);
      while(!sc.finishConnect()){
        //waste some time
      }
      }catch(UnknownHostException e){System.out.println("UnknownHostException in TcpSend: " + e);}
      catch(IOException e){System.out.println("IOException in TcpSend: " + e);}
  }

  public void run(){
    try{
      sc = SocketChannel.open();
      connect();
      }catch(IOException e){System.out.println("IOException in TcpSend: " + e);}
    ByteBuffer src = ByteBuffer.allocateDirect(1024);
    try{
      FileInputStream in = new FileInputStream(send);
      fChannel = in.getChannel();
      }catch(FileNotFoundException e){System.out.println("FileNotFoundException: " + e);}
      try{
        long fileSize = send.length();
        long position = fChannel.position();
        while(position < fileSize ){
          src.clear();
          fChannel.read(src);
          src.flip();
          sc.write(src);
        }
      }catch(IOException e){System.out.println("IOException in TcpSend: " + e);}
  }
  private void sendConnectionData(){
    String ip = m.getIP(userName);
    String fileName = send.getName();
    System.out.println("FileName to send: " + fileName);
    cr.sendDirectConnectData(fileName,ip,port,send.length());
  }
}