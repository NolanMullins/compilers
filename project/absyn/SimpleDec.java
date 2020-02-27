package absyn;

public class SimpleDec extends VarDec {
  public String name;

  public SimpleDec( int row, int col, String name ) {
    this.row = row;
    this.col = col;
    this.name = name;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}

