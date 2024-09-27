package br.com.bcbbrasil.models;

public enum BalanceType {
    PREPAID("prepaid"),
    POSTPAID("postpaid");

    private String balanceType;

    BalanceType(String balanceType){
        this.balanceType = balanceType;
    }

    public String getBalanceType(){
        return balanceType;
    }
}
