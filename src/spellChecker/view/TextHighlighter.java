package spellChecker.view;

import java.awt.Color;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class TextHighlighter extends DefaultHighlighter.DefaultHighlightPainter {

	public TextHighlighter(Color color) {
		super(color);
	}
	public void highlight(JTextComponent textComp, String[] pattern){
		removeHighlights(textComp);
		try{
			
			Highlighter hl = textComp.getHighlighter();
			Document doc = textComp.getDocument();
			String txt = doc.getText(0, doc.getLength());
			for(int i = 0; i < pattern.length; i++){
				int pos = 0;
				while((pos = txt.indexOf(pattern[i], pos)) >= 0){
					hl.addHighlight(pos, pos + pattern[i].length(),	this);
					
					pos += pattern[i].length();
				}
			}
		}catch(BadLocationException e){
			
		}
	}
	private void removeHighlights(JTextComponent textComp) {
		Highlighter hl = textComp.getHighlighter();
		Highlighter.Highlight[] hls = hl.getHighlights();
		for(int i = 0; i < hls.length; i++){
			if(hls[i].getPainter() instanceof TextHighlighter){
				hl.removeHighlight(hls[i]);
			}
		}
	}

}
