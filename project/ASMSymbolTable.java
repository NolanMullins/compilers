import absyn.*;

import java.lang.String;
import java.util.*;


public class ASMSymbolTable
{
    public HashMap<String, ArrayList<ASMDecEntry>> symtable;
    private int sFlag = 0;
    private static int SPACES = 4;

    public ASMSymbolTable(int sFlag)
    {
        symtable = new HashMap<>();
        this.sFlag = sFlag;
    }

    private void indent(int level) {
        if (sFlag == 1) {
            for (int i = 0; i < level * SPACES; i++)
                System.out.print(" ");
        }
    }

    public ASMDecEntry addEntryToTable(Dec dec, String name, int type, int depth) {
        indent(depth);

        String decType = "declaration";
        if (dec instanceof FunctionDec)
            decType = "function";

        //Check if its already defined
        if (symtable.containsKey(name) && symtable.get(name).size() > 0) {
            ArrayList<ASMDecEntry> list = symtable.get(name);
            if (list.get(list.size()-1).depth == depth) {
                //Redefinition error
                System.out.println("[ERROR] Redefined "+decType+": " + name + " [row: "+dec.row + " col: "+dec.col+"]");
                return null;
            }
        }

        //Output declaration msg if the -s was set
        if (sFlag == 1) {
            if (dec instanceof FunctionDec)
                System.out.println("Function declaration: "+name);
            else
                System.out.println(name+": "+NameTy.types[type]+(dec instanceof ArrayDec ? "*" : ""));
        }

        //Add declaration to symtable
        ArrayList<ASMDecEntry> entries;
        if (symtable.containsKey(name)) {
            entries = symtable.get(name);
        } else {
            entries = new ArrayList<>();
            symtable.put(name, entries);

        }
        ASMDecEntry entry = new ASMDecEntry(name, dec, depth, type);
        entries.add(entry);
        return entry;
    }

    public void clearSymTable(int depth) {
        for (Map.Entry<String, ArrayList<ASMDecEntry>> var : symtable.entrySet()) {
            ASMDecEntry index = null;
            for (ASMDecEntry entry : var.getValue()) {
               if (entry.depth == depth) {
                   index = entry;
                   break;
               }
            }
            if (index != null) {
                var.getValue().remove(index);
            }
        }
    }

    public int getVarType(String name) {
        if (symtable.containsKey(name)) {
            ArrayList<ASMDecEntry> list = symtable.get(name);
            if (list.size() <= 0)
                return -1;
            return list.get(list.size()-1).type;
        }
        return -1;
    }

    public String formatParamsString(ArrayList<Dec> args) {
        String s = "";
        for (Dec dec : args) {
            s += NameTy.types[dec.type.type] +(dec instanceof ArrayDec ? "*" : "")+", ";
        }
        if (s.length() > 0)
            return s.substring(0, s.length()-2);
        return s;
    }

    public String formatArgsString(ArrayList<Exp> args) {
        String s = "";
        for (Exp e : args) {
            String type;
            if (e instanceof VarExp) {
                String varName = ((VarExp)e).value.name;
                if (symtable.containsKey(varName) && symtable.get(varName).size() > 0) {
                    Dec dec = symtable.get(varName).get(symtable.get(varName).size()-1).dec;
                    type = NameTy.types[dec.type.type] +(dec instanceof ArrayDec ? "*" : "");
                } else {
                    type = "Undefined";
                }
            } else {
                type = NameTy.types[e.type];
            }
            s += type+", ";
        }
        if (s.length() > 0)
            return s.substring(0, s.length()-2);
        return s;
    }

}