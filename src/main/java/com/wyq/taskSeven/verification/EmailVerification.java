package com.wyq.taskSeven.verification;

public class EmailVerification {

    private String email;
    private String password;
    private String passwordAgain;
    private String verificationCode;

    public void setEmail(String eamil){
        this.email = eamil;
    }

    public String getEmail(){
        return this.email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPasswordAgain(String passwordAgain){
        this.passwordAgain = passwordAgain;
    }

    public String getPasswordAgain(){
        return this.passwordAgain;
    }

    public void setVerificationCode(String verificationCode){
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode(){
        return this.verificationCode;
    }
}
