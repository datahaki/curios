package ch.alpine.curios.dict;

enum Head {
  ;
  public static boolean numbered(String string) {
    if (3 < string.length()) {
      if (string.charAt(1) == '.' && string.charAt(2) == ' ') {
        char h = string.charAt(0);
        if ('0' <= h && h <= '9') {
          return true;
        }
      }
    }
    return false;
  }

  static void main() {
    IO.println(numbered("1. asd"));
  }
}
