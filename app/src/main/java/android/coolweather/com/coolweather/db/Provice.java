package android.coolweather.com.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by liurong on 2017/6/11.
 */

public class Provice extends DataSupport {
    private int id;
    private String proviceName;
    private int proviceCode;
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getProviceName(){
        return this.proviceName;
    }
    public void setProviceName(String proviceName){
        this.proviceName=proviceName;
    }
    public int getProviceCode(){
        return this.proviceCode;
    }
    public void setProviceCode(int proviceCode){
        this.proviceCode=proviceCode;
    }
}
