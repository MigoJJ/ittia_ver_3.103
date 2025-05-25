package je.pense.doro.chartplate.filecontrol;

import javax.swing.*;		
import javax.swing.text.*;

public class IndentedTextArea extends JTextArea {
    private static final String INDENT = "   ";
    
    public IndentedTextArea() {
        super();
        setDocument(new IndentDocument());
    }

    private class IndentDocument extends PlainDocument {
        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }
            int len = getLength();
            if (len == 0) {
                super.insertString(offset, INDENT + str, a);
            } else {
                int lineStart = getLineStartOffset(getLineCount() - 1);
                String lastLine = getText(lineStart, len - lineStart);
                if (lastLine.endsWith("\n")) {
                    super.insertString(offset, INDENT + str, a);
                } else {
                    super.insertString(offset, str, a);
                }
            }
        }
    }
}

