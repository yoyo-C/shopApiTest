package com.elephtribe.tools.dataprovider;

import com.elephtribe.tools.StringUtils;

/**
 * Created by Bytes on 2017/7/26.
 */
public class RuntimeContextHolder {


    private static ThreadLocal<CaseContext> caseContextLocal = new ThreadLocal();

    public static void setCaseId(String caseId) {
        CaseContext caseContext = getCaseContext();
        if (caseContext == null) {
            caseContext = new CaseContext();
            setCaseContext(caseContext);
        }
        if (StringUtils.isNotBlank(caseId))
            caseContext.setCaseId(caseId);
    }

    public static String getCaseId()
    {
        if (getCaseContext() != null) {
            return getCaseContext().getCaseId();
        }
        return "";
    }

    public static void setDataProviderPath(String dataProviderPath) {
        CaseContext caseContext = getCaseContext();
        if (caseContext == null) {
            caseContext = new CaseContext();
            setCaseContext(caseContext);
        }
        caseContext.setDataProviderPath(dataProviderPath);
    }

    public static String getDataProviderPath() {
        if (getCaseContext() != null) {
            return getCaseContext().getDataProviderPath();
        }
        return null;
    }

    public static void setCaseContext(CaseContext context)
    {
        caseContextLocal.set(context);
    }

    public static CaseContext getCaseContext() {
        return (CaseContext)caseContextLocal.get();
    }

}
