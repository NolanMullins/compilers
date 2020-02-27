package absyn;

public class IndexVar extends Var {
    public int row;
    public int col;
    public Exp index;

    public IndexVar( int pos, String name, Exp index ) {
        this.row = row;
        this.col = col;
        this.exp = index;
      }
    
      public void accept( AbsynVisitor visitor, int level ) {
        visitor.visit( this, level );
      }
}
