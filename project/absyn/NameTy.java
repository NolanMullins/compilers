package absyn;

public class NameTy extends Absyn {
    public int row;
    public int col;
    public int type;

    final static int INT = 0;
    final static int VOID = 1;

  public NameTy( int row, int col, int type ) {
      this.row = row;
      this.col = col;
      this.type = type;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}

