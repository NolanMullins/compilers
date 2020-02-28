package absyn;

public class IntExp extends Exp {
  public int value;

  public IntExp( int row, int col, String value ) {
    this.row = row;
    this.col = col;

    this.value = 0;
    int pow = 1;
    for (int a = 0; a < value.length(); a++)
        this.value = (value.charAt(a) - '0')*(pow*=10);
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
