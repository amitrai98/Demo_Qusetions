package android.com.demo_qusetions.bin;

/**
 * Created by amitrai on 13/5/16.
 */
public class UserBin {

    private String userName, userAddress;

    public UserBin(String userName, String userAddress){
        this.userName = userName;
        this.userAddress = userAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}
