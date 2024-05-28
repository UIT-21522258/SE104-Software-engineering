package model;

public class LogedInUser {
    private static Account CurentAcc;
    public void setCurentAcc(Account acc){
        CurentAcc = acc;
    }
    public Account getCurentAcc(){
        return CurentAcc;
    }
}
