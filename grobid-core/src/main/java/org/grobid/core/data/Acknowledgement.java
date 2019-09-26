package org.grobid.core.data;

import org.grobid.core.utilities.TextUtilities;

public class Acknowledgement {
    private String educationalInstitution = null;
    private String fundingAgency = null;
    private String grantName = null;
    private String grantNumber = null;
    private String projectName = null;
    private String individual = null;
    private String otherInstitution = null;
    private String researchInstitution = null;
    private String affiliation = null;

    public String getAffiliation() {
        return affiliation;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getEducationalInstitution() {
        return educationalInstitution;
    }

    public void setEducationalInstitution(String educationalInstitution) {
        this.educationalInstitution = educationalInstitution;
    }

    public String getFundingAgency() {
        return fundingAgency;
    }

    public void setFundingAgency(String fundingAgency) {
        this.fundingAgency = fundingAgency;
    }

    public String getGrantName() {
        return grantName;
    }

    public void setGrantName(String grantName) {
        this.grantName = grantName;
    }

    public String getGrantNumber() {
        return grantNumber;
    }

    public void setGrantNumber(String grantNumber) {
        this.grantNumber = grantNumber;
    }

    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    public String getOtherInstitution() {
        return otherInstitution;
    }

    public void setOtherInstitution(String otherInstitution) {
        this.otherInstitution = otherInstitution;
    }

    public String getResearchInstitution() {
        return researchInstitution;
    }

    public void setResearchInstitution(String researchInstitution) {
        this.researchInstitution = researchInstitution;
    }

    public boolean isNotNull() {
        if ((affiliation == null) &&
            (educationalInstitution == null) &&
            (fundingAgency == null) &&
            (grantName == null) &&
            (grantNumber == null) &&
            (individual == null) &&
            (otherInstitution == null) &&
            (projectName == null) &&
            (researchInstitution == null))
            return false;
        else
            return true;
    }

    public String toTEI() {
        StringBuilder tei = new StringBuilder();
        if (!isNotNull()) {
            return null;
        } else {
            tei.append("<acknowledgement>");

            if (affiliation != null) {
                tei.append("<affiliation>").append(TextUtilities.HTMLEncode(affiliation)).append("</affiliation>");
            }

            if (researchInstitution != null) {
                tei.append("<researchInstitution>").append(TextUtilities.HTMLEncode(researchInstitution)).append("</researchInstitution>");
            }

            if (otherInstitution != null) {
                tei.append("<otherInstitution>").append(TextUtilities.HTMLEncode(otherInstitution)).append("</otherInstitution>");
            }

            if (educationalInstitution != null) {
                tei.append("<educationalInstitution>").append(TextUtilities.HTMLEncode(educationalInstitution)).append("</educationalInstitution>");
            }

            if (fundingAgency != null) {
                tei.append("<fundingAgency>").append(TextUtilities.HTMLEncode(fundingAgency)).append("</fundingAgency>");
            }

            if (grantName != null) {
                tei.append("<grantName>").append(TextUtilities.HTMLEncode(grantName)).append("</grantName>");
            }

            if (grantNumber != null) {
                tei.append("<grantNumber>").append(TextUtilities.HTMLEncode(grantNumber)).append("</grantNumber>");
            }

            if (individual != null) {
                tei.append("<individual>").append(TextUtilities.HTMLEncode(individual)).append("</individual>");
            }

            if (projectName != null) {
                tei.append("<projectName>").append(TextUtilities.HTMLEncode(projectName)).append("</projectName>");
            }
            tei.append("</acknowledgement>");

        }
        return tei.toString();

    }


}
