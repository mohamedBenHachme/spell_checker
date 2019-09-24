package spellChecker.view;


import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.AbstractListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import javax.swing.tree.DefaultMutableTreeNode;

import spellChecker.controller.SpellerController;

import java.awt.Color;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Ui {
	static SpellerController controller =new SpellerController();
	private JFrame frame;
	private static JTextField textField;
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
	TextHighlighter highlight = new TextHighlighter(Color.ORANGE);
	static HashMap<String, ArrayList<String>> suggestionListHashMap = new HashMap<String, ArrayList<String>>();
	@SuppressWarnings("rawtypes")
	static JList list= new JList();

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public Ui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("spellchecker");
		frame.setResizable(false);
		//frame.setType(Type.UTILITY);
		frame.setBounds(100, 100, 605, 377);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 22, 579, 98);
		frame.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	
		panel.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		controller.createDictionnary();
       
		JButton btnCheckSpelling = new JButton("check spelling");
		
		btnCheckSpelling.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				suggestionListHashMap = controller.checkSpelling(textArea, suggestionListHashMap, highlight);
			}
		});
		btnCheckSpelling.setBounds(232, 126, 134, 23);
		getFrame().getContentPane().add(btnCheckSpelling);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(246, 194, 109, 138);
		getFrame().getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, 109, 138);
		panel_1.add(scrollPane_1);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		scrollPane_1.setViewportView(list);
		list.setValueIsAdjusting(true);
		
		list.setSelectedIndex(0);
		
		JButton btnReplace = new JButton("replace");
		btnReplace.setBounds(411, 221, 89, 23);
		getFrame().getContentPane().add(btnReplace);
		btnReplace.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 String oldWord = textField.getText();
				 String newWord = (String) list.getSelectedValue();
				 controller.replaceAll(textArea, oldWord, newWord);
				 suggestionListHashMap.remove(oldWord, suggestionListHashMap.get(oldWord));
				 String[] listOfWords = suggestionListHashMap.keySet().toArray(new String[suggestionListHashMap.size()]);
				 controller.highlight(textArea, listOfWords, highlight);
				 textField.setText("");
				 clearData(list);
			}
		});
		textField = new JTextField();
		textField.setBounds(246, 154, 109, 29);
		textArea.setFont(new Font("Century Gothic", Font.TYPE1_FONT,12));
		textArea.addMouseListener(createPopupListener(textArea));
		frame.getContentPane().add(textField);
		textArea.setLineWrap(true);
		textField.setColumns(10);
		
		JMenuBar menuBar_1 = new JMenuBar();
		menuBar_1.setBounds(0, 0, 75, 22);
		JMenu open = new JMenu("Open a file");
		menuBar_1.add(open);
		frame.getContentPane().add(menuBar_1);
		frame.setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	private static MouseListener createPopupListener(JTextArea t) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menu = popupMenu.add(new JMenuItem("correct"));
        popupMenu.add(menu);
        menu.addActionListener(e -> {
            String word = getWordAtCaret(t);
            
            textField.setText(word);
            list.setModel(new AbstractListModel() {
            	
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
	
	public void clearData(JList dataList) {
		DefaultListModel model=new DefaultListModel();
	    model.clear();
	    dataList.setModel(model);
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
