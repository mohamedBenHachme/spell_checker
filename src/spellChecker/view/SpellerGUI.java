package spellChecker.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Utilities;
import javax.swing.tree.DefaultMutableTreeNode;

import spellChecker.controller.SpellerController;


public class SpellerGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7370010902344736470L;
	static SpellerController controller =new SpellerController();
	JFrame frame;
	static JTextField textField;
	static JTextArea textArea; 
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
	TextHighlighter highlight = new TextHighlighter(Color.ORANGE);
	static HashMap<String, ArrayList<String>> suggestionListHashMap = new HashMap<String, ArrayList<String>>();
	static JList listOfSuggestions= new JList();
	@SuppressWarnings("deprecation")
	public SpellerGUI() {
		frame = new JFrame("spellchecker");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(true);
		JPanel outputPanel = new JPanel();
		outputPanel.add(listOfSuggestions);
		
		
		textArea = new JTextArea("", 5, 10);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(true);
		textArea.setFont(new Font("Century Gothic", Font.TYPE1_FONT,12));
		textArea.setTabSize(2);
		textArea.addMouseListener(createPopupListener(textArea));
		frame.add(textArea);
		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		TextLineNumber tln = new TextLineNumber(textArea);
        scroller.setRowHeaderView(tln);
        JPanel inputpanel = new JPanel();
        inputpanel.setLayout(new FlowLayout());
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JButton spellcheck = new JButton("check spelling");
        spellcheck.addActionListener(this);
        panel.add(scroller);
        panel.add(inputpanel);
        panel.add(outputPanel);
        inputpanel.add(spellcheck);
        
        
         
        
        
        try { 
            
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 
            MetalLookAndFeel.setCurrentTheme(new OceanTheme()); 
        } 
        catch (Exception e) { 
        }
        
        
        
        controller.createDictionnary();
        JMenuBar menuBar = new JMenuBar(); 
        JMenuItem open = new JMenuItem("Open a file");
        open.addActionListener(this);
        menuBar.add(open);
        
        listOfSuggestions.setSelectedIndex(0);
		JButton btnReplace = new JButton("replace");
		btnReplace.addActionListener(this);
		btnReplace.setBounds(430, 320, 89, 23);
		frame.getContentPane().add(btnReplace);
		
		textField = new JTextField();
		textField.setBounds(217, 220, 150, 30);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
        
        
        
		
		
		
		
		frame.setJMenuBar(menuBar); 
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(600, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        frame.show();
	}
	
	


	 private static MouseListener createPopupListener(JTextArea t) {
	        JPopupMenu popupMenu = new JPopupMenu();
	        JMenuItem menu = popupMenu.add(new JMenuItem("correct"));
	        popupMenu.add(menu);
	        menu.addActionListener(e -> {
	            String word = getWordAtCaret(t);
//	            for( String s : suggestionListHashMap.keySet().toArray(new String[suggestionListHashMap.size()]))
//        			System.out.println(s);
	            textField.setText(word);
	            listOfSuggestions.setModel(new AbstractListModel() {
	            	
	            	ArrayList<String> listOfWords = suggestionListHashMap.get(word);
	            	
	    			String[] values = listOfWords.toArray(new String[listOfWords.size()]);
	    			public int getSize() {
	    				return values.length;
	    			}
	    			public Object getElementAt(int index) {
	    				return values[index];
	    			}
	    		});

	        });
	        return new MouseAdapter() {
	            @Override
	            public void mouseReleased(MouseEvent e) {
	                if (e.isPopupTrigger()) {
	                    int rightClickPosition = t.viewToModel2D(e.getPoint());
	                    t.setCaretPosition(rightClickPosition);
	                    popupMenu.show(t, e.getX(), e.getY());
	                }
	            }
	        };
	    }
	
	
	private static String getWordAtCaret(JTextArea t) {
	      try {
	          int caretPosition = t.getCaretPosition();
	          int start = Utilities.getWordStart(t, caretPosition);
	          int end = Utilities.getWordEnd(t, caretPosition);
	          return t.getText(start, end - start);
	      } catch (BadLocationException e) {
	          System.err.println(e);
	      }

	      return null;
	  }
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand(); 
		
		 if (s.equals("check spelling")) { 
			 suggestionListHashMap = controller.checkSpelling(textArea, suggestionListHashMap, highlight);
		 }
		 else if (s.equals("Open a file")) { 
        	controller.fileExplorer(frame, textArea);
           }
		 else if(s.equals("replace")) {
			 String oldWord = textField.getText();
			 String newWord = (String) listOfSuggestions.getSelectedValue();
			 System.out.println(oldWord +" "+ newWord);
			 controller.replaceAll(textArea, oldWord, newWord);
			 suggestionListHashMap = controller.checkSpelling(textArea, suggestionListHashMap, highlight);
		 }
        
     } 
		
	
	
	
	public void insert (String string) {
		controller.getTrie().insert(string);
	}
	
	public boolean search(String string) {
		return controller.getTrie().search(string); 
	}
	
	
	
	

}
