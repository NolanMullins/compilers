package absyn;

public class FunctionDec extends Dec {
  public NameTy type;
  public String func;
  public VarDecList params;
  public CompoundExp body;

  public FunctionDec( int row, int col, NameTy type, String func, VarDecList params, CompoundExp body ) {
    this.row = row;
    this.col = col;
    this.type = type;
    this.func = func;
    this.params = params;
    this.body = body;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}

