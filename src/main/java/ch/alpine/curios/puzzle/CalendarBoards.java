// code by jph
package ch.alpine.curios.puzzle;

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
  /** fine */
  KEISER("""
      oommmmmmoo
      oommmmmmoo
      oooooooooo
      oowwwwwwwo
      oooooooooo
      """),
  CHEESE("""
      momomomomo
      omomomomom
      mooooomooo
      oowwwwwwwo
      oooooooooo
      """),
  CHEESY("""
      momomomomo
      omomomomom
      mooooomooo
      oowowowowo
      ooowowowoo
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
