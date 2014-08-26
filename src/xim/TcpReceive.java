package xim;

import java.nio.*;
import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class TcpReceive extends Thread {
  private int port;
  private XimFrame f;
  private String fileName;
  private long fileSize;

  public TcpReceive(int port,XimFrame f,String fileName, long fileSize){
    this.port = port;
    this.f = f;
    this.fileName = fileName;
    this.fileSize = fileSize;
  }
  public void run(){

    try{
      ServerSocketChannel sc = ServerSocketChannel.open();
      sc.configureBlocking(false);
      ServerSocket ss = new ServerSocket(port);
      InetAddress ia = InetAddress.getLocalHost();
      InetSocketAddress isa = new InetSocketAddress(ia,port);
      ss.bind(isa);
      Selector selector = Selector.open();
      sc.register(selector,SelectionKey.OP_ACCEPT);
      while(selector.isOpen()){
        int n = selector.select(0);
        Iterator it = selector.selectedKeys().iterator();
        while(selector.selectedKeys().size() > 0){
          SelectionKey key = (SelectionKey)it.next();
          if(key.isAcceptable()){
            f.createJOptionPane("File Incoming.");
            ServerSocketChannel channel = (ServerSocketChannel)key.channel();
            SocketChannel s = channel.accept();

            File f = new File(fileName);
            FileOutputStream fout = new FileOutputStream(f);
            FileChannel fc = fout.getChannel();

            long transfered = fc.transferFrom(s,0,fileSize);
            while(transfered < fileSize){
              transfered = fc.transferFrom(s,transfered,fileSize);
            }
            s.close();
          }
        }
      }
      }catch(IOException e){System.out.println("IOException in TcpReceive: " + e );}
  }
}



