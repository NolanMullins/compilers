import absyn.*;

import java.lang.String;
import java.util.*;

public class ASMDecEntry 
{
    String name; 
    Dec dec; 
    int depth;
    int type;
    public ArrayList<Dec> params = null;
    public ASMDecEntry(String name, Dec dec, int depth, int type) 
    {
        this.name = name;
        this.dec = dec;
        this.depth = depth;
        this.type = type;
    }
    @Override
    public String toString() {
        return name+", "+NameTy.types[type]+", "+dec;
    }
}
