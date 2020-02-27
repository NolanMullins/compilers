package absyn;

public class VarExp extends Exp {
    public int row;
    public int col;
    public String value;

    public VarExp( int row, int col, String value ) {
      this.row = row;
      this.col = col;
      this.value = value;
    }

    public void accept( AbsynVisitor visitor, int level ) {
      visitor.visit( this, level );
    }
}
