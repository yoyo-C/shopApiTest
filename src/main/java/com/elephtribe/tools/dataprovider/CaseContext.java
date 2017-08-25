package com.elephtribe.tools.dataprovider;

/**
 * Created by Bytes on 2017/7/26.
 */
public class CaseContext {
    private String caseId = "";
    private String dataProviderPath;

    public String getCaseId()
    {
        return this.caseId;
    }

    public void setCaseId(String caseId)
    {
        this.caseId = caseId;
    }

    public String getDataProviderPath()
    {
        return this.dataProviderPath;
    }

    public void setDataProviderPath(String dataProviderPath)
    {
        this.dataProviderPath = dataProviderPath;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CaseContext [caseId=");
        builder.append(this.caseId);
        builder.append(",dataProviderPath=");
        builder.append(this.dataProviderPath);
        builder.append("]");
        return builder.toString();
    }
}
