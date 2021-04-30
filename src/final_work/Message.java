package final_work;

import java.io.Serializable;

public class Message implements Serializable {
    private int totalLength;
    private int commandID;
    private String userName;
    private String passwd;
    private byte status;
    private String description;

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public int getTotalLength() {
        return this.totalLength;
    }

    public void setCommandID(int commandID) {
        this.commandID = commandID;
    }

    public int getCommandID() {
        return this.commandID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
