package vParser;

public class JSONTokener {
   private int a = 0;
   private String a;

   public JSONTokener(String var1) {
      this.a = var1;
   }

   public void back() {
      if (this.a > 0) {
         --this.a;
      }

   }

   public char next() {
      if (this.a < this.a.length()) {
         char var1 = this.a.charAt(this.a);
         ++this.a;
         return var1;
      } else {
         return '\u0000';
      }
   }

   private String a(int var1) {
      int var2;
      int var3;
      if ((var3 = (var2 = this.a) + var1) >= this.a.length()) {
         throw this.syntaxError("Substring bounds error");
      } else {
         this.a += var1;
         return this.a.substring(var2, var3);
      }
   }

   public char nextClean() {
      label50:
      while(true) {
         char var1;
         if ((var1 = this.next()) == '/') {
            switch(this.next()) {
            case '*':
               while((var1 = this.next()) != 0) {
                  if (var1 == '*') {
                     if (this.next() == '/') {
                        continue label50;
                     }

                     this.back();
                  }
               }

               throw this.syntaxError("Unclosed comment.");
            case '/':
               while(true) {
                  if ((var1 = this.next()) == '\n' || var1 == '\r' || var1 == 0) {
                     continue label50;
                  }
               }
            default:
               this.back();
               return '/';
            }
         } else if (var1 == '#') {
            while((var1 = this.next()) != '\n' && var1 != '\r' && var1 != 0) {
            }
         } else if (var1 == 0 || var1 > ' ') {
            return var1;
         }
      }
   }

   public Object nextValue() {
      char var1;
      char var3;
      switch(var1 = this.nextClean()) {
      case '"':
      case '\'':
         char var2 = var1;
         JSONTokener var9 = this;
         StringBuffer var4 = new StringBuffer();

         while(true) {
            switch(var3 = var9.next()) {
            case '\u0000':
            case '\n':
            case '\r':
               throw var9.syntaxError("Unterminated string");
            case '\\':
               switch(var3 = var9.next()) {
               case 'b':
                  var4.append('\b');
                  continue;
               case 'c':
               case 'd':
               case 'e':
               case 'g':
               case 'h':
               case 'i':
               case 'j':
               case 'k':
               case 'l':
               case 'm':
               case 'o':
               case 'p':
               case 'q':
               case 's':
               case 'v':
               case 'w':
               default:
                  var4.append(var3);
                  continue;
               case 'f':
                  var4.append('\f');
                  continue;
               case 'n':
                  var4.append('\n');
                  continue;
               case 'r':
                  var4.append('\r');
                  continue;
               case 't':
                  var4.append('\t');
                  continue;
               case 'u':
                  var4.append((char)Integer.parseInt(var9.a(4), 16));
                  continue;
               case 'x':
                  var4.append((char)Integer.parseInt(var9.a(2), 16));
                  continue;
               }
            default:
               if (var3 == var2) {
                  return var4.toString();
               }

               var4.append(var3);
            }
         }
      case '[':
         this.back();
         return new JSONArray(this);
      case '{':
         this.back();
         return new JSONObject(this);
      default:
         StringBuffer var11 = new StringBuffer();

         for(var3 = var1; var1 >= ' ' && ",:]}/\\\"[{;=#".indexOf(var1) < 0; var1 = this.next()) {
            var11.append(var1);
         }

         this.back();
         String var10;
         if ((var10 = var11.toString().trim()).equals("")) {
            throw this.syntaxError("Missing value.");
         } else if (var10.equals("null")) {
            return JSONObject.NULL;
         } else {
            if (var3 >= '0' && var3 <= '9' || var3 == '.' || var3 == '-' || var3 == '+') {
               if (var3 == '0') {
                  if (var10.length() > 2 && (var10.charAt(1) == 'x' || var10.charAt(1) == 'X')) {
                     try {
                        return new Integer(Integer.parseInt(var10.substring(2), 16));
                     } catch (Exception var8) {
                     }
                  } else {
                     try {
                        return new Integer(Integer.parseInt(var10, 8));
                     } catch (Exception var7) {
                     }
                  }
               }

               try {
                  return Integer.valueOf(var10);
               } catch (Exception var6) {
                  try {
                     return new Long(Long.parseLong(var10));
                  } catch (Exception var5) {
                  }
               }
            }

            return var10;
         }
      }
   }

   public JSONException syntaxError(String var1) {
      return new JSONException(var1 + this.toString());
   }

   public String toString() {
      return " at character " + this.a + " of " + this.a;
   }
}
