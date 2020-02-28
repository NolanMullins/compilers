package absyn;

import Java.lang.Integer;

public class IntExp extends Exp {
  public int value;

  public IntExp( int row, int col, String value ) {
    this.row = row;
    this.col = col;
    this.value = Integer.parseInt(value);
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
