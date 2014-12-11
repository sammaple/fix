
package com.control;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

    long userID;
    String string;

    Date time;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        // TODO Auto-generated method stub
        this.userID = userID;

    }

    public void setBirth(Date time) {
        // TODO Auto-generated method stub
        this.time = time;
    }

    public Date getBirth() {

        return this.time;
    }

}
