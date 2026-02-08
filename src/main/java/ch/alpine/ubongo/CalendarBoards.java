// code by jph
package ch.alpine.ubongo;

public enum CalendarBoards {
  CAESAR("""
      mmmmmm
      mmmmmm
      ooooooo
      ooooooo
      ooooooo
      ooooooo
      ooowwww
          www
      """),
  KAISER("""
      mmmmmmoooo
      mmmmmmoooo
      oooooooooo
      oowwwwwwwo
      oooooooooo
      """),
  TOWERS("""
      mmooooooo
      mmooooooo
      mmooooooo
      mmooooooo
      mmwwwwooo
      mmwww
      """),
  //
  ;

  private final CalendarBoard calendarBoard;

  private CalendarBoards(String string) {
    calendarBoard = new CalendarBoard(string);
  }

  public CalendarBoard calendarBoard() {
    return calendarBoard;
  }
}
