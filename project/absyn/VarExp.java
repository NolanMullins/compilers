package absyn;

public class VarExp extends Exp {
    public Var value;
    public boolean isAssign = false;

    public VarExp( int row, int col, Var value ) {
      this.row = row;
      this.col = col;
      this.value = value;
    }

    public void accept( AbsynVisitor visitor, int level ) {
      visitor.visit( this, level );
    }
}
