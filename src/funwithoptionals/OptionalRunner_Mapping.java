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
 * We are interested in retrieving the List<Accounts> without throwing a NullPointerException.
 * To access the List<Accounts> we would have to first access the Bank,
 * then the AccountHolder, then the List of Accounts.  The problem is, the Bank and/or
 * AccountHolder variables might be null, which would throw a NullPointerException.
 *
 * In the examples below, we will compare and contrast the old-school traditional way of
 * checking for null values before accessing them with using the java.util.Optional class
 * introduced with Java 8.
 *
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


    /*
     * To simplify things, we're just using a static java main method
     * to execute all of the examples
     */
    public static void main(String[] args) {
        /*
         * Access the account list the 'old' way - without using Optional
         */
        List<Account> accountList = doingItTheOldWay();
        if (accountList.isEmpty()) throw new AssertionError();

        /*
         * Same operation, only this time using java.util.Optional
         */
        accountList = doingItUsingOptional();
        if (accountList.isEmpty()) throw new AssertionError();

    }

    /**
     * Yes, there are a hundred different ways this could have been demonstrated.
     * Here I'm just trying to show an example of old-school traditional null
     * checks that we can tighten up later by using java.util.Optional
     */
    public static List<Account> doingItTheOldWay() {
        if (restServiceResponse != null && restServiceResponse.getBank() != null
                && restServiceResponse.getBank().getAccountHolder() != null) {
            return restServiceResponse.getBank().getAccountHolder().getAccounts();
        }
        return null;
    }

    /**
     * If any of the nodes in the object graph are null, the 'map' response will be
     * Optional.empty (which has a value=null).  The 'map' method firsts checks to see
     * if there is a value present. If that value is null, then it will return
     * Optional.empty.
     *
     * By the time we get to the 'orElse' statement, the value will either contain a
     * legitimate value and be returned to the caller or it will contain an
     * Optional.empty value=null in which case it will return the default value
     * to the caller.  The default value in this case is Collections.emptyList().
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