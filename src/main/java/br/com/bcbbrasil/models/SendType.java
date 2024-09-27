package br.com.bcbbrasil.models;

public enum SendType {
    WPP("wpp"),
    TXT("txt");

    private String sendType;

    SendType(String sendType){
        this.sendType = sendType;
    }

    public String getSendType(){
        return sendType;
    }
}
