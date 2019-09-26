package org.grobid.core.features;

import org.grobid.core.exceptions.GrobidException;
import org.grobid.core.layout.LayoutToken;
import org.grobid.core.utilities.OffsetPosition;
import org.grobid.core.utilities.TextUtilities;

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

/**
 * Class for features used for parsing a block corresponding to affiliation and address.
 *
 * @author Patrice Lopez
 */
public class FeaturesVectorAcknowledgement {
    public String string = null; // lexical feature
    public String label = null; // label if known
    public String lineStatus = null; // one of LINESTART, LINEIN, LINEEND
    public boolean bold = false;
    public boolean italic = false;
    public String capitalisation = null; // one of INITCAP, ALLCAPS, NOCAPS
    public String digit;  // one of ALLDIGIT, CONTAINDIGIT, NODIGIT
    public boolean singleChar = false;
    public boolean properName = false;
    public boolean commonName = false;
    public boolean affiliation = false;
    public boolean educationalInstitution = false;
    public boolean fundingAgency= false;
    public boolean grantName = false;
    public boolean grantNumber = false;
    public boolean individual = false;
    public boolean otherInstitution = false;
    public boolean projectName = false;
    public boolean researchInstitution= false;
    public String punctType = null;
    public String wordShape = null;
    // one of NOPUNCT, OPENBRACKET, ENDBRACKET, DOT, COMMA, HYPHEN, QUOTE, PUNCT (default)

    public String printVector() {
        if (string == null) return null;
        if (string.length() == 0) return null;
        StringBuffer res = new StringBuffer();

        // token string (1)
        res.append(string);

        // lowercase string
        res.append(" " + string.toLowerCase());

        // prefix (4)
        res.append(" " + TextUtilities.prefix(string, 1));
        res.append(" " + TextUtilities.prefix(string, 2));
        res.append(" " + TextUtilities.prefix(string, 3));
        res.append(" " + TextUtilities.prefix(string, 4));

        // suffix (4)
        res.append(" " + TextUtilities.suffix(string, 1));
        res.append(" " + TextUtilities.suffix(string, 2));
        res.append(" " + TextUtilities.suffix(string, 3));
        res.append(" " + TextUtilities.suffix(string, 4));

        // line information (1)
        res.append(" " + lineStatus);

        // capitalisation (1)
        if (digit.equals("ALLDIGIT"))
            res.append(" NOCAPS");
        else
            res.append(" " + capitalisation);

        // digit information (1)
        res.append(" " + digit);

        // character information (1)
        if (singleChar)
            res.append(" 1");
        else
            res.append(" 0");

        if (properName)
            res.append(" 1");
        else
            res.append(" 0");

        if (commonName)
            res.append(" 1");
        else
            res.append(" 0");

        // lexical information (9)
        if (affiliation)
            res.append(" 1");
        else
            res.append(" 0");

        if (fundingAgency)
            res.append(" 1");
        else
            res.append(" 0");

        if (researchInstitution)
            res.append(" 1");
        else
            res.append(" 0");

        if (otherInstitution)
            res.append(" 1");
        else
            res.append(" 0");

        if (grantName)
            res.append(" 1");
        else
            res.append(" 0");

        if (grantNumber)
            res.append(" 1");
        else
            res.append(" 0");

        if (projectName)
            res.append(" 1");
        else
            res.append(" 0");

        if (educationalInstitution)
            res.append(" 1");
        else
            res.append(" 0");

        if (individual)
            res.append(" 1");
        else
            res.append(" 0");

        // punctuation information (1)
        res.append(" " + punctType); // in case the token is a punctuation (NO otherwise)

        res.append(" ").append(wordShape);

        // label - for training data (1)
        if (label != null)
            res.append(" " + label + "\n");
        else
            res.append(" 0\n");



        return res.toString();
    }

    /**
     * Add the features for the affiliation+address model.
     */
    static public String addFeaturesAcknowledgment(List<String> lines
                                                      ) throws Exception {

        StringBuffer result = new StringBuffer();
        List<String> block = null;
        String lineStatus = "LINESTART";
        String line = null;

        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);
			if (line.equals("\n")) {
				result.append("\n \n");
				continue;
			}

            if (line.trim().equals("@newline")) {
                lineStatus = "LINESTART";
                continue;
            }

            if (line.trim().length() == 0) {
                result.append("\n");
                lineStatus = "LINESTART";
            } else {
                // look ahead for line status update
                if ((i + 1) < lines.size()) {
                    String nextLine = lines.get(i + 1);
                    if ((nextLine.trim().length() == 0) || (nextLine.trim().equals("@newline"))) {
                        lineStatus = "LINEEND";
                    }
                } else if ((i + 1) == lines.size()) {
                    lineStatus = "LINEEND";
                }

                FeaturesVectorAcknowledgement vector = addFeaturesAcknowledgment(line, lineStatus);
                result.append(vector.printVector());

                if (lineStatus.equals("LINESTART")) {
                    lineStatus = "LINEIN";
                }
            }
        }

        return result.toString();
    }

    static private FeaturesVectorAcknowledgement addFeaturesAcknowledgment(String line, String lineStatus) {
        FeatureFactory featureFactory = FeatureFactory.getInstance();
        FeaturesVectorAcknowledgement featuresVectorAcknowledgement = new FeaturesVectorAcknowledgement();

        StringTokenizer st = new StringTokenizer(line.trim(), "\t ");
        if (st.hasMoreTokens()) {
            String word = st.nextToken();

            String label = null;
            if (st.hasMoreTokens())
                label = st.nextToken();

            featuresVectorAcknowledgement.string = word;
            featuresVectorAcknowledgement.label = label;

            featuresVectorAcknowledgement.lineStatus = lineStatus;

            if (word.length() == 1) {
                featuresVectorAcknowledgement.singleChar = true;
            }

            if (featureFactory.test_all_capital(word))
                featuresVectorAcknowledgement.capitalisation = "ALLCAPS";
            else if (featureFactory.test_first_capital(word))
                featuresVectorAcknowledgement.capitalisation = "INITCAP";
            else
                featuresVectorAcknowledgement.capitalisation = "NOCAPS";

            if (featureFactory.test_number(word))
                featuresVectorAcknowledgement.digit = "ALLDIGIT";
            else if (featureFactory.test_digit(word))
                featuresVectorAcknowledgement.digit = "CONTAINDIGIT";
            else
                featuresVectorAcknowledgement.digit = "NODIGIT";

            if (featureFactory.test_common(word))
                featuresVectorAcknowledgement.commonName = true;

            if (featureFactory.test_names(word))
                featuresVectorAcknowledgement.properName = true;

            Matcher m0 = featureFactory.isPunct.matcher(word);
            if (m0.find()) {
                featuresVectorAcknowledgement.punctType = "PUNCT";
            }
            if ((word.equals("(")) | (word.equals("["))) {
                featuresVectorAcknowledgement.punctType = "OPENBRACKET";
            } else if ((word.equals(")")) | (word.equals("]"))) {
                featuresVectorAcknowledgement.punctType = "ENDBRACKET";
            } else if (word.equals(".")) {
                featuresVectorAcknowledgement.punctType = "DOT";
            } else if (word.equals(",")) {
                featuresVectorAcknowledgement.punctType = "COMMA";
            } else if (word.equals("-")) {
                featuresVectorAcknowledgement.punctType = "HYPHEN";
            } else if (word.equals("\"") | word.equals("\'") | word.equals("`")) {
                featuresVectorAcknowledgement.punctType = "QUOTE";
            }

            if (featuresVectorAcknowledgement.capitalisation == null)
                featuresVectorAcknowledgement.capitalisation = "NOCAPS";

            if (featuresVectorAcknowledgement.digit == null)
                featuresVectorAcknowledgement.digit = "NODIGIT";

            if (featuresVectorAcknowledgement.punctType == null)
                featuresVectorAcknowledgement.punctType = "NOPUNCT";

            featuresVectorAcknowledgement.wordShape = TextUtilities.wordShape(word);
        }

        return featuresVectorAcknowledgement;
    }
}