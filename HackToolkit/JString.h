/****************************************************************************
  *cr
  *cr            (C) Copyright 1995-2003 The Board of Trustees of the
  *cr                        University of Illinois
  *cr                         All Rights Reserved
  *cr
  ***************************************************************************/
 
 /***************************************************************************
  * RCS INFORMATION:
  *
  *      $RCSfile: JString.h,v $
  *      $Author: johns $        $Locker:  $             $State: Exp $
  *      $Revision: 1.10 $      $Date: 2003/07/03 16:01:46 $
  *
  ***************************************************************************
  * DESCRIPTION:
  *   A minimalistic string class we use instead of similar classes from the
  *   STL or the GNU libraries for better portability and greatly reduced
  *   code size.  (only implements the functionality we actually need, doesn't
  *   balloon the entire VMD binary as some past string class implementations
  *   did).  Implements regular expression matching methods used by VMD.
  ***************************************************************************/
 
 // JString
 // A class of strings to replace GString.  As of this writing, JString
 // does everything VMD needs GString to do and no more.  
 
 #ifndef JSTRING_H__
 #define JSTRING_H__
 
 #include <string.h>
 
 class JString {
 private:
   static char *defstr;
   char *rep;
   int do_free;
 
 public:
   JString()
   : rep(defstr), do_free(0) {} 
     
   JString(const char *str)
   : rep(defstr), do_free(0) {
     if (str) {
       rep = new char[strlen(str)+1];
       strcpy(rep, str);
       do_free = 1;
     }
   }
   JString(const JString& s) {
     rep = new char[strlen(s.rep)+1];
     strcpy(rep, s.rep);
     do_free = 1;
   }
   ~JString() { if (do_free) delete [] rep; }
 
   int operator==(const char *s) {return !strcmp(s,rep);}
   int operator!=(const char *s) {return strcmp(s,rep);}
   int operator<(const char *s) {return (strcmp(s,rep)<0);}
   int operator>(const char *s) {return (strcmp(s,rep)>0);}
   int operator<=(const char *s) {return (strcmp(s,rep)<=0);}
   int operator>=(const char *s) {return (strcmp(s,rep)>=0);}
 
   JString& operator=(const char *);
   JString& operator=(const JString&);
   JString& operator=(const char);
   JString& operator+=(const char *);
   JString& operator+=(const JString&);
   JString& operator+=(const char);
 
   friend int compare(const JString& s1, const JString& s2) {
     return strcmp(s1.rep, s2.rep);
   }
 
   friend JString operator+(const char*, const JString&);
   JString operator+(const JString&) const;
  
   int length() const { return strlen(rep); }
 
   operator const char *() const {return rep; }
 
   // conver to uppercase
   void upcase();
  
   // Replace all instances of pat with repl
   int gsub(const char *pat, const char *repl);
   
   // remove the last n non-NULL characters from the end of the string
   void chop(int n);
};

#endif
