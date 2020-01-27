class Token {

    public final static int ERROR = 0;
    public final static int WORD = 1;
    public final static int NUMBER = 2;
    public final static int APOSTROPHIZED = 3;
    public final static int HYPHENATED = 4;
    public final static int OPEN = 5;
    public final static int CLOSE = 6;
    public final static int PUNCTUATION = 7;
    public final static int NL = 8;

    public int m_type;
    public String m_value;
    public int m_line;
    public int m_column;

    Token(int type, String value, int line, int column) {
        m_type = type;
        m_value = value;
        m_line = line;
        m_column = column;
    }

    public String toString() {
        switch (m_type) {
        case WORD:
        case NUMBER:
        case APOSTROPHIZED:
        case HYPHENATED:
        case OPEN:
        case CLOSE:
        case NL:
        case PUNCTUATION:
            return m_value;
        default:
            return "UNKNOWN(" + m_value + ")";
        }
    }
}
