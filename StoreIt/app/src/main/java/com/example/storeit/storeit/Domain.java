package com.example.storeit.storeit;

/**
 * Created by shravan on 5/4/17.
 */
public class Domain {

    private String domain;
    private String username;
    private String password;
    private String extraDetails;

    public Domain(){
        extraDetails = "";
        domain = "";
        username = "";
        password = "";
    }
    public void setDomain(String domain){
        this.domain = domain;
    }
    public void setUserName(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setExtraDetails(String extraDetails){
        this.extraDetails = extraDetails;
    }
    public String getUserName(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getDomain(){
        return domain;
    }
}
