package xim;

import java.io.*;
import java.util.*;
import java.util.jar.*;

public class JarResourceLoader {
  private String fileName;
  private JarFile j;

  public JarResourceLoader(String fileName) {
    this.fileName = fileName;
    loadJarFile();
  }
  public InputStream getResourceStream(String name){
    try {
      return j.getInputStream(j.getEntry(name));
    }catch (IOException ex) {System.out.println("IOException in JarResourceLoader getResourceStream(): " + ex);}
    return null;
  }
  private void loadJarFile(){
    try {
      j = new JarFile(fileName);
    }catch (IOException ex) {System.out.println("IOException in JarResourceLoader loadJarFile(): " + ex);}
  }
  public void showResources(){
    try {
      Enumeration e = j.entries();
      while(e.hasMoreElements())
        System.out.println("Entry: " + e.nextElement());
    }catch (Exception ex) {System.out.println("Exception in JarResourceLoader showResources(): " + ex);}
  }
}
