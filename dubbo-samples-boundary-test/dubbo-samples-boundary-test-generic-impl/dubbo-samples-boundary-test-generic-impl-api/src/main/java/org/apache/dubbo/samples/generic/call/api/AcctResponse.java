package org.apache.dubbo.samples.generic.call.api;

import java.io.Serializable;
import java.util.List;

public class AcctResponse implements Serializable {

    private String acctName;

    private List<AcctInfo> accounts;

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public List<AcctInfo> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AcctInfo> accounts) {
        this.accounts = accounts;
    }
}
