package org.grobid.trainer.sax;

//import com.sun.java.util.jar.pack.Attribute;
import org.grobid.core.analyzers.GrobidAnalyzer;
import org.grobid.core.layout.LayoutToken;
import org.grobid.core.lexicon.Lexicon;
import org.grobid.core.utilities.OffsetPosition;
import org.grobid.core.utilities.TextUtilities;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import scala.math.Ordering;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * SAX parser for affiliation+address sequences encoded in the TEI format data.
 * Segmentation of tokens must be identical as the one from pdf2xml files to that
 * training and online input tokens are identical.
 *
 * @author Patrice Lopez
 */
public class TEIAcknowledgementSaxParser extends DefaultHandler {

    private StringBuffer accumulator = new StringBuffer(); // Accumulate parsed text
    private StringBuffer allContent = new StringBuffer();

    private String currentTag = null;

    private List<String> labeled = null; // store line by line the labeled data

    public int n = 0;

    public TEIAcknowledgementSaxParser() {
        labeled = new ArrayList<String>();
    }

    public void characters(char[] buffer, int start, int length) {
        accumulator.append(buffer, start, length);
        //if (allContent != null) {
        //	allContent.append(buffer, start, length);
        //}
    }

    public String getText() {
        return accumulator.toString().trim();
    }

    public List<String> getLabeledResult() {
        return labeled;
    }

    public void endElement(String uri,
                           String localName,
                           String qName) throws SAXException {
        if ((
            (qName.equals("affiliation")) |
                (qName.equals("educationaInstitution")) |
                (qName.equals("grantName")) |
                (qName.equals("grantNumber")) |
                (qName.equals("otherInstitution")) |
                (qName.equals("individual")) |
                (qName.equals("fundingAgency") |
                (qName.equals("projectName")) |
                (qName.equals("researchInstitution")))
        )) {
            String text = getText();
            writeField(text);
            if (allContent != null) {
                if (allContent.length() != 0) {
                    allContent.append(" ");
                }
                allContent.append(text);
            }
            accumulator.setLength(0);
        } else if (qName.equals("acknowledgement")) {
            String text = getText();
            if (text.length() > 0) {
                currentTag = "<other>";
                writeField(text);
                if (allContent != null) {
                    if (allContent.length() != 0) {
                        allContent.append(" ");
                    }
                    allContent.append(text);
                }
                labeled.add("\n \n");

                String allString = allContent.toString().trim();
                allString = allString.replace("@newline", "\n");
                List<LayoutToken> tokens = GrobidAnalyzer.getInstance().tokenizeWithLayoutToken(allString);
                allContent = null;
                allString = null;
                allContent = null;

                accumulator.setLength(0);
            }
        } else {
            accumulator.setLength(0);
        }
    }

    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts)
        throws SAXException {
            String text = getText();
            if (text.length() > 0) {
                currentTag = "<other>";
                writeField(text);
                if (allContent != null) {
                    if (allContent.length() != 0) {
                        allContent.append(" ");
                    }
                    allContent.append(text);
                }
            }
            accumulator.setLength(0);
        //else {
        //	writeField("+++");
        //}

        if (qName.equals("affiliation")) {
            currentTag = "<affiliation>";
        } else if (qName.equals("grantName")) {
            currentTag = "<grantName>";
        } else if (qName.equals("grantNumber")) {
            currentTag = "<grantNumber>";
        } else if (qName.equals("educationalInstitution")) {
            currentTag = "<educationalInstitution>";
        } else if (qName.equals("researchInstitution")) {
            currentTag = "<researchInstitution>";
        } else if (qName.equals("fundingAgency")) {
            currentTag = "<fundingAgency>";
        } else if (qName.equals("individual")) {
            currentTag = "<individual>";
        } else if (qName.equals("projectName")) {
            currentTag = "<projectName>";
        } else if (qName.equals("otherInstitution")) {
            currentTag = "<otherInstitution>";
        } else if (qName.equals("acknowledgement")) {
            accumulator = new StringBuffer();
            allContent = new StringBuffer();
        } else {
            currentTag = null;
            accumulator.setLength(0);
            n++;
        }
    }

    private void writeField(String text) {
        // we segment the text
        //StringTokenizer st = new StringTokenizer(text, " \n\t" + TextUtilities.fullPunctuations, true);
        List<String> tokens = GrobidAnalyzer.getInstance().tokenize(text);
        boolean begin = true;
        //while (st.hasMoreTokens()) {
        for (String tok : tokens) {
            tok = tok.trim();

            if (tok.length() == 0) {
                continue;
            }
            if ( tok.equals("+L+") || tok.equals("@newline")) {
                labeled.add("@newline");
            } else if (tok.equals("+PAGE+")) {
                // page break - no influence here
                labeled.add("@newline");
            } else {
                String content = tok;
                int i = 0;
                if (content.length() > 0) {
                    if (begin) {
                        labeled.add(content + " I-" + currentTag);
                        begin = false;
                    } else {
                        labeled.add(content + " " + currentTag);
                    }
                }
            }
            begin = false;
        }
    }

}
