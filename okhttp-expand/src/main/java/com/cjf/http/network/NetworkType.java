package com.cjf.http.network;

/**
 * <p>Title: NetwordType </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2020/7/20 17:10
 */
public enum NetworkType {

    Default(0),Wifi(1), Wired(2), Mobile(3),
    Mobile2G(4), Mobile3G(5), Mobile4G(6), Mobile5G(7);

    private final int type;

    NetworkType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
