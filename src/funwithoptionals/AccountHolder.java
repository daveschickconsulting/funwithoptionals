package funwithoptionals;

import java.util.List;

public class AccountHolder {
    private List<Account> accounts;
    private String name;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
