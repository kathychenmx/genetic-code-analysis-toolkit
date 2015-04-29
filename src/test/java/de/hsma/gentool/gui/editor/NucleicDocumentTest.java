package de.hsma.gentool.gui.editor;

import static org.junit.Assert.*;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import org.junit.Test;
import de.hsma.gentool.nucleic.Acid;

public class NucleicDocumentTest {
	private static final String EMPTY = "",CURSOR = "|";
	
	@Test public void test() {
		assertDocumentText("AGCTUHX ","AGCTUHX ");
		assertDocumentText("A A","A,;-A");
		assertDocumentText("","!?Z1");
		
		assertDocumentText("","|"," ");
		assertDocumentText("AA","|AA"," ");
		//assertDocumentText("| AA"," "," AA");
		assertDocumentText("AA ","AA|"," ");
		assertDocumentText("AA ","AA |"," ");
		assertDocumentText("AA AA","AA|AA"," ");
		assertDocumentText("AA AA","AA |AA"," ");
		assertDocumentText("AA  AA","AA| AA"," ");
		assertDocumentText("AA  AA","AA | AA"," ");
		assertDocumentText("AA  AA","AA|  AA"," ");
		assertDocumentText("AA  AA","AA  |AA"," ");
		
		assertDocumentText("CC","|","CC");
		assertDocumentText("CCAA","|AA","CC");
		//assertDocumentText("| AA","CC","CC AA");
		assertDocumentText("AACC","AA|","CC");
		assertDocumentText("AA CC","AA |","CC");
		assertDocumentText("AACCAA","AA|AA","CC");
		assertDocumentText("AA CCAA","AA |AA","CC");
		assertDocumentText("AACC AA","AA| AA","CC");
		assertDocumentText("AA CC AA","AA | AA","CC");
		assertDocumentText("AACC  AA","AA|  AA","CC");
		assertDocumentText("AA  CCAA","AA  |AA","CC");
		
		assertDocumentText("CC","|"," CC");
		assertDocumentText("CCAA","|AA"," CC");
		//assertDocumentText("| AA"," CC","CC AA");
		assertDocumentText("AA CC","AA|"," CC");
		assertDocumentText("AA CC","AA |"," CC");
		assertDocumentText("AA CCAA","AA|AA"," CC");
		assertDocumentText("AA CCAA","AA |AA"," CC");
		assertDocumentText("AA CC AA","AA| AA"," CC");
		assertDocumentText("AA CC AA","AA | AA"," CC");
		assertDocumentText("AA CC  AA","AA|  AA"," CC");
		assertDocumentText("AA  CCAA","AA  |AA"," CC");
		
		assertDocumentText("CC ","|","CC ");
		assertDocumentText("CC AA","|AA","CC ");
		//assertDocumentText("| AA","CC ","CC AA");
		assertDocumentText("AACC ","AA|","CC ");
		assertDocumentText("AA CC ","AA |","CC ");
		assertDocumentText("AACC AA","AA|AA","CC ");
		assertDocumentText("AA CC AA","AA |AA","CC ");
		assertDocumentText("AACC  AA","AA| AA","CC ");
		assertDocumentText("AA CC  AA","AA | AA","CC ");
		assertDocumentText("AACC  AA","AA|  AA","CC ");
		assertDocumentText("AA  CC AA","AA  |AA","CC ");
		
		assertDocumentText("CC ","|"," CC ");
		assertDocumentText("CC AA","|AA"," CC ");
		//assertDocumentText("| AA"," CC ","CC AA");
		assertDocumentText("AA CC ","AA|"," CC ");
		assertDocumentText("AA CC ","AA |"," CC ");
		assertDocumentText("AA CC AA","AA|AA"," CC ");
		assertDocumentText("AA CC AA","AA |AA"," CC ");
		assertDocumentText("AA CC  AA","AA| AA"," CC ");
		assertDocumentText("AA CC  AA","AA | AA"," CC ");
		assertDocumentText("AA CC  AA","AA|  AA"," CC ");
		assertDocumentText("AA  CC AA","AA  |AA"," CC ");
		
		assertDocumentText("CC GG ","|"," CC GG ");
		assertDocumentText("CC GG AA","|AA"," CC GG ");
		//assertDocumentText("| AA"," CC GG ","CC GG AA");
		assertDocumentText("AA CC GG ","AA|"," CC GG ");
		assertDocumentText("AA CC GG ","AA |"," CC GG ");
		assertDocumentText("AA CC GG AA","AA|AA"," CC GG ");
		assertDocumentText("AA CC GG AA","AA |AA"," CC GG ");
		assertDocumentText("AA CC GG  AA","AA| AA"," CC GG ");
		assertDocumentText("AA CC GG  AA","AA | AA"," CC GG ");
		assertDocumentText("AA CC GG  AA","AA|  AA"," CC GG ");
		assertDocumentText("AA  CC GG AA","AA  |AA"," CC GG ");
	}
	
	@Test public void testDefaultAcid() {
		NucleicDocument document = new NucleicDocument();
		document.setDefaultAcid(Acid.RNA);
		assertEquals(Acid.RNA,document.getDefaultAcid());
		
		try {			
			document.insertString(0,"AGCTUHX ",null);
			assertEquals("AGCUUHX ",document.getText(0,document.getLength()));
			
			document.setDefaultAcid(Acid.DNA); document.adaptText();
			assertEquals("AGCTTHX",document.getText(0,document.getLength()));
			
			document.setDefaultAcid(null); document.adaptText();
			document.insertString(0,"UT",null);
			assertEquals("UT",document.getText(0,2));			
		} catch(BadLocationException e) { fail("Failed to set text, bad location"); }
	}
	
	@Test public void testDefaultTupleLength() {
		NucleicDocument document = new NucleicDocument();
		document.setDefaultTupleLength(3);
		assertEquals(3,document.getDefaultTupleLength());
		
		try {			
			document.insertString(document.getLength(),"A",null);
			assertEquals("A",document.getText(0,document.getLength()));
			document.insertString(document.getLength(),"G",null);
			assertEquals("AG",document.getText(0,document.getLength()));
			document.insertString(document.getLength(),"C",null);
			assertEquals("AGC ",document.getText(0,document.getLength()));
			
			document.remove(0,document.getLength());
			document.insertString(document.getLength(),"AAA",null);
			assertEquals("AAA ",document.getText(0,document.getLength()));
			document.insertString(document.getLength(),"GGG CCC",null);
			assertEquals("AAA GGG CCC ",document.getText(0,document.getLength()));
			
			document.remove(0,document.getLength());
			document.insertString(document.getLength(),"AAAGGGCCC",null);
			assertEquals("AAA GGG CCC ",document.getText(0,document.getLength()));
			
			document.setDefaultTupleLength(2); document.adaptText();
			assertEquals("AA AG GG CC C",document.getText(0,document.getLength()));
			document.insertString(document.getLength(),"C",null);
			assertEquals("AA AG GG CC CC ",document.getText(0,document.getLength()));
			
			document.setDefaultTupleLength(0); document.adaptText();
			assertEquals("AAAGGGCCCC",document.getText(0,document.getLength()));
		} catch(BadLocationException e) { fail("Failed to set text, bad location"); }
	}
	
	private void assertDocumentText(String expected,String text) {
		assertEquals(expected,documentText(text));
	}
	private void assertDocumentText(String expected,String text,String insert) {
		assertEquals(expected,documentText(text.replaceAll(Pattern.quote(CURSOR),EMPTY),text.indexOf(CURSOR),insert));
	}
	
	private static NucleicDocument document(String text) throws BadLocationException {
		NucleicDocument document = new NucleicDocument();
		document.setDefaultAcid(null);
		document.insertString(0,text,null);
		return document;
	}
	private static String documentText(String text) {
		try {
			NucleicDocument document = document(text);
			return document.getText(0,document.getLength());
		} catch(BadLocationException e) { fail("Failed to set text, bad location"); return null; }
	}
	private static String documentText(String text,int offset,String insert) {
		try {
			NucleicDocument document = document(text);
			if(text.contains("  ")) document.insertString(text.indexOf("  ")," ",null);
			assertEquals("Initialization of document text failed",text,document.getText(0,document.getLength()));
			document.insertString(offset,insert,null);
			return document.getText(0,document.getLength());
		} catch(BadLocationException e) { fail("Failed to set text, bad location"); return null; }
	}
}