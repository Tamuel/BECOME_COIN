package taghere.project.helloworld.taghere.AccountFolder;

import java.util.ArrayList;

/**
 * Created by fewfr on 2016-05-23.
 */

//계정들을 모아서 관리하는 클래스
public class AccountManagement {
    private ArrayList<Account> accountList;

    AccountManagement() {accountList = new ArrayList<Account>();}

    public ArrayList<Account> getAccountList() {return accountList;}

    //account를 리스트에 더함
    public void addAccount(Account account) {
        getAccountList().add(account);
    }
}
