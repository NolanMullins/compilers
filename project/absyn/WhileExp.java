package absyn;

public class WhileExp extends Exp {
  public Exp body;
  public Exp test;

  public WhileExp( int row, int col, Exp test, Exp body ) {
    this.row = row;
    this.col = col;
    this.body = body;
    this.test = test;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
