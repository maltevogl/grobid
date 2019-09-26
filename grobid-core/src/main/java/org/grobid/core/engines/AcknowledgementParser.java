package org.grobid.core.engines;

import org.grobid.core.GrobidModels;
import org.grobid.core.data.Acknowledgement;
import org.grobid.core.exceptions.GrobidException;
import org.grobid.core.features.FeaturesVectorAcknowledgement;
import org.grobid.core.lang.Language;
import org.grobid.core.utilities.TextUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Patrice Lopez
 */
public class AcknowledgementParser extends AbstractParser {

    public AcknowledgementParser() {
        super(GrobidModels.ACKNOWLEDGEMENT);
    }

    /**
     * Processing of authors in header
     */
    public List<Acknowledgement> processing(String input) {
        if (input == null)
            return null;

        List<Acknowledgement> acknowledgements = null;

        List<String> acknowledgementBlocks = new ArrayList<String>();
        try {
            // force English language for the tokenization only
			List<String> tokenizations = analyzer.tokenize(input, new Language("en", 1.0));
			if (tokenizations.size() == 0) 
                return null;
			for(String tok : tokenizations) {
                if (!tok.equals(" ") && !tok.equals("\n")) {
                    // parano final sanitisation
                    tok = tok.replaceAll("[ \n]", "");
                    acknowledgementBlocks.add(tok + " <acknowledgement>");
                }
            }

            String headerAcknowledgement = FeaturesVectorAcknowledgement.addFeaturesAcknowledgment(acknowledgementBlocks);
            String res = label(headerAcknowledgement);
            // extract results from the processed file

            //System.out.print(res.toString());
            StringTokenizer st2 = new StringTokenizer(res, "\n");
            String lastTag = null;
            Acknowledgement acknowledgement = new Acknowledgement();
            int lineCount = 0;
            while (st2.hasMoreTokens()) {
                String line = st2.nextToken();
                if ((line.trim().length() == 0)) {
                    if (acknowledgement.isNotNull()) {
                        if (acknowledgements == null)
                            acknowledgements = new ArrayList<Acknowledgement>();
                        acknowledgements.add(acknowledgement);
                    }
                    acknowledgement = new Acknowledgement();
                    continue;
                }
                StringTokenizer st3 = new StringTokenizer(line, "\t ");
                int ll = st3.countTokens();
                int i = 0;
                String s1 = null;
                String s2 = null;
                while (st3.hasMoreTokens()) {
                    String s = st3.nextToken().trim();
                    if (i == 0) {
                        s2 = s; // string
                    } else if (i == ll - 1) {
                        s1 = s; // label
                    }
                    i++;
                }

                if (s1.equals("<affiliation>") || s1.equals("I-<affiliation>")) {
                    if (acknowledgement.getAffiliation() != null) {
                        if ((s1.equals("I-<affiliation>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<affiliation>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setAffiliation(s2);
                        } else {
                            if (acknowledgement.getAffiliation().length() == 0)
                                acknowledgement.setAffiliation(s2);
                            else
                                acknowledgement.setAffiliation(acknowledgement.getAffiliation() + " " + s2);
                        }
                    } else {
                        acknowledgement.setAffiliation(s2);
                    }
                } else if (s1.equals("<fundingAgency>") || s1.equals("I-<fundingAgency>")) {
                    if (acknowledgement.getFundingAgency() != null) {
                        if ((s1.equals("I-<fundingAgency>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<fundingAgency>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setFundingAgency(s2);
                        } else {
                            if (acknowledgement.getFundingAgency().length() == 0)
                                acknowledgement.setFundingAgency(s2);
                            else
                                acknowledgement.setFundingAgency(acknowledgement.getFundingAgency() + " " + s2);
                        }
                    } else {
                        acknowledgement.setFundingAgency(s2);
                    }

                } else if (s1.equals("<grantName>") || s1.equals("I-<grantName>")) {
                    if (acknowledgement.getGrantName() != null) {
                        if ((s1.equals("I-<grantName>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<grantName>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setGrantName(s2);
                        } else {
                            if (acknowledgement.getGrantName().length() == 0)
                                acknowledgement.setGrantName(s2);
                            else
                                acknowledgement.setGrantName(acknowledgement.getGrantName() + " " + s2);
                        }
                    } else {
                        acknowledgement.setGrantName(s2);
                    }

                } else if (s1.equals("<grantNumber>") || s1.equals("I-<grantNumber>")) {
                    if (acknowledgement.getGrantNumber() != null) {
                        if ((s1.equals("I-<grantNumber>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<grantNumber>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setGrantNumber(s2);
                        } else {
                            if (acknowledgement.getGrantNumber().length() == 0)
                                acknowledgement.setGrantNumber(s2);
                            else
                                acknowledgement.setGrantNumber(acknowledgement.getGrantNumber() + " " + s2);
                        }
                    } else {
                        acknowledgement.setGrantNumber(s2);
                    }

                } else if (s1.equals("<projectName>") || s1.equals("I-<projectName>")) {
                    if (acknowledgement.getProjectName() != null) {
                        if ((s1.equals("I-<projectName>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<projectName>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setProjectName(s2);
                        } else {
                            if (acknowledgement.getProjectName().length() == 0)
                                acknowledgement.setProjectName(s2);
                            else
                                acknowledgement.setProjectName(acknowledgement.getProjectName() + " " + s2);
                        }
                    } else {
                        acknowledgement.setProjectName(s2);
                    }

                } else if (s1.equals("<individual>") || s1.equals("I-<individual>")) {
                    if (acknowledgement.getIndividual() != null) {
                        if ((s1.equals("I-<individual>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<individual>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setIndividual(s2);
                        } else {
                            if (acknowledgement.getIndividual().length() == 0)
                                acknowledgement.setIndividual(s2);
                            else
                                acknowledgement.setIndividual(acknowledgement.getIndividual() + " " + s2);
                        }
                    } else {
                        acknowledgement.setIndividual(s2);
                    }

                } else if (s1.equals("<otherInstitution>") || s1.equals("I-<otherInstitution>")) {
                    if (acknowledgement.getOtherInstitution() != null) {
                        if ((s1.equals("I-<otherInstitution>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<otherInstitution>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setOtherInstitution(s2);
                        } else {
                            if (acknowledgement.getOtherInstitution().length() == 0)
                                acknowledgement.setOtherInstitution(s2);
                            else
                                acknowledgement.setOtherInstitution(acknowledgement.getOtherInstitution() + " " + s2);
                        }
                    } else {
                        acknowledgement.setOtherInstitution(s2);
                    }

                } else if (s1.equals("<researchInstitution>") || s1.equals("I-<researchInstitution>")) {
                    if (acknowledgement.getResearchInstitution() != null) {
                        if ((s1.equals("I-<researchInstitution>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<researchInstitution>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setResearchInstitution(s2);
                        } else {
                            if (acknowledgement.getResearchInstitution().length() == 0)
                                acknowledgement.setResearchInstitution(s2);
                            else
                                acknowledgement.setResearchInstitution(acknowledgement.getResearchInstitution() + " " + s2);
                        }
                    } else {
                        acknowledgement.setResearchInstitution(s2);
                    }

                } else if (s1.equals("<educationalInstitution>") || s1.equals("I-<educationalInstitution>")) {
                    if (acknowledgement.getEducationalInstitution() != null) {
                        if ((s1.equals("I-<educationalInstitution>")) ||
                            (!s1.equals(lastTag) && !lastTag.equals("I-<educationalInstitution>"))
                        ) {
                            // new acknowledgement
                            if (acknowledgement.isNotNull()) {
                                if (acknowledgements == null)
                                    acknowledgements = new ArrayList<Acknowledgement>();
                                acknowledgements.add(acknowledgement);
                            }

                            acknowledgement = new Acknowledgement();
                            acknowledgement.setEducationalInstitution(s2);
                        } else {
                            if (acknowledgement.getEducationalInstitution().length() == 0)
                                acknowledgement.setEducationalInstitution(s2);
                            else
                                acknowledgement.setEducationalInstitution(acknowledgement.getEducationalInstitution() + " " + s2);
                        }
                    } else {
                        acknowledgement.setEducationalInstitution(s2);
                    }

                }


                lastTag = s1;
                lineCount++;
            }
            if (acknowledgement.isNotNull()) {
                if (acknowledgements == null)
                    acknowledgements = new ArrayList<Acknowledgement>();
                acknowledgements.add(acknowledgement);
            }

        } catch (Exception e) {
//			e.printStackTrace();
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
        return acknowledgements;
    }

    /**
     * Extract results from a acknowledgement string in the training format without any string modification.
     */
    private String writeField(String s1,
                              String lastTag0,
                              String s2,
                              String field,
                              String outField,
                              boolean addSpace,
                              int nbIndent) {
        String result = null;
        if ((s1.equals(field)) || (s1.equals("I-" + field))) {
            if ((s1.equals("<other>") || s1.equals("I-<other>"))) {
                if (addSpace)
                    result = " " + s2;
                else
                    result = s2;
            } else if (s1.equals(lastTag0) || s1.equals("I-" + lastTag0)) {
                if (addSpace)
                    result = " " + s2;
                else
                    result = s2;
            } else {
                result = "";
                for (int i = 0; i < nbIndent; i++) {
                    result += "\t";
                }
                if (addSpace)
                    result += " " + outField + s2;
                else
                    result += outField + s2;
            }
        }
        return result;
    }

    private boolean testClosingTag(StringBuilder buffer,
                                   String currentTag0,
                                   String lastTag0) {
        boolean res = false;
        if (!currentTag0.equals(lastTag0)) {
            res = true;
            // we close the current tag
            if (lastTag0.equals("<other>")) {
                buffer.append("");
            } else if (lastTag0.equals("<affiliation>")) {
                buffer.append("</affiliation>");
            } else if (lastTag0.equals("<educationalInstitution>")) {
                buffer.append("</educationalInstitution>");
            } else if (lastTag0.equals("<fundingAgency>")) {
                buffer.append("</fundingAgency>");
            } else if (lastTag0.equals("<grantName>")) {
                buffer.append("</grantName>");
            } else if (lastTag0.equals("<grantNumber>")) {
                buffer.append("</grantNumber>");
            } else if (lastTag0.equals("<individual>")) {
                buffer.append("</individual>");
            } else if (lastTag0.equals("<otherInstitution>")) {
                buffer.append("</otherInstitution>");
            } else if (lastTag0.equals("<projectName>")) {
                buffer.append("</projectName>");
            } else if (lastTag0.equals("<researchInstitution>")) {
                buffer.append("</researchInstitution>");
            } else {
                res = false;
            }

        }
        return res;
    }

}