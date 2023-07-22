package model;

public class Slave {
    private String host;
    private String username;
    private String password;

    public Slave() {

    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String toString() {
        return "Slave :: Host : " + this.host + " , Username : " + this.username + " , Password : " + this.password;
    }
}