package ch.alpine.curios.usr;

import java.util.function.IntBinaryOperator;

enum JavaIntMath {
  FLOOR_MOD(Math::floorMod),
  FLOOR_DIV(Math::floorDiv),
  CEIL_MOD(Math::ceilMod),
  CEIL_DIV(Math::ceilDiv),
  CENTER_MOD((i, j) -> i % j),
  CENTER_DIV((i, j) -> i / j),
  TRYTHIS((x, mod) -> x - (x % mod)),
  //
  ;

  IntBinaryOperator ibo;

  private JavaIntMath(IntBinaryOperator ibo) {
    this.ibo = ibo;
  }
}
