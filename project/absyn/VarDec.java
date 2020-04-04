package absyn;

abstract public class VarDec extends Dec {
    public int offset;
    //Either 0 for global or 1 for local
    public int nestLevel;
}
