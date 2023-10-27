package funwithoptionals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
* In this scenario, we have called a REST API and received a response (RestServiceResponse).
* This response consists of a nested object graph that looks like this:
*
*    RestServiceResponse
*      Bank
*        AccountHolder
*          List<Accounts>
*
*
* We are interested in copying the List<Accounts> to a Collection<Accounts>.  To access the List<Accounts> we would have to first access the Bank,
* then the AccountHolder, then the List of Accounts.  The problem is, the Bank and/or AccountHolder variables might be null, which would throw a
* NullPointerException.
*
* Traditionally you may have seen a long and ugly statement (or some variation of the same) that looks something like this:
*
* if (restServiceResponse != null && restServiceResponse.getBank() != null && restServiceResponse.getBank().getAccountHolder() !=null) {
* return restServiceResponse.getBank().getAccountHolder().getAccounts();
*
* In this scenario we will use a feature that was introduced in Java 8 - the java.util.Optional.
 */
public class OptionalRunner_Mapping {
    private static RestServiceResponse restServiceResponse = new RestServiceResponse();
    private static Bank bank = new Bank();
    private static AccountHolder accountHolder = new AccountHolder();
    private static List<Account> accounts = new ArrayList<>();
    private static Account account = new Account();

    static {
        accounts.add(account);
        accountHolder.setAccounts(accounts);
        bank.setAccountHolder(accountHolder);
        restServiceResponse.setBank(bank);
    }


    public static void main(String[] args) {
        /*
         * Access the account list the 'old' way - without using Optional
         */
        List<Account> accountList = doingItTheOldWay();
        if ((accountList.isEmpty())) throw new AssertionError();

        /*
         * Same operation, only this time using java.util.Optional
         */
        accountList = doingItUsingOptional();
        if ((accountList.isEmpty())) throw new AssertionError();

    }

    public static List<Account> doingItTheOldWay() {
        if (restServiceResponse != null && restServiceResponse.getBank() != null && restServiceResponse.getBank().getAccountHolder() != null) {
            return restServiceResponse.getBank().getAccountHolder().getAccounts();
        }
        return null;
    }

    /**
     * If any of the nodes in the object graph are null, processing will fall through to the
     * orElse statement and an empty list will be returned.
     *
     * If none of the nodes in the object graph are null, processing will reach the
     * orElse and it will return the value in the Optional of List<Account>.
     *
     */

    public static List<Account> doingItUsingOptional() {
        return Optional.ofNullable(restServiceResponse)
                .map(RestServiceResponse::getBank)
                .map(Bank::getAccountHolder)
                .map(AccountHolder::getAccounts)
                .orElse(Collections.emptyList());
    }
}
