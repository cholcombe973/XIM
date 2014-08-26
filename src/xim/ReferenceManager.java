package xim;

import java.util.Hashtable;

public class ReferenceManager {
  private Hashtable ht = new Hashtable();

  public Object getReference(String className){
    if(ht.get(className)==null)
      throw new NullPointerException("Null Pointer Exception: Reference table returned null");
    return ht.get(className);
  }
  public void putReference(String className,Object ref){
    ht.put(className,ref);
  }
}
