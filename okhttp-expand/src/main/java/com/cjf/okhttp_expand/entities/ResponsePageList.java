package com.cjf.okhttp_expand.entities;


import java.io.Serializable;
import java.util.List;

/**
 * User: ljx
 * Date: 2018/10/21
 * Time: 13:16
 */
public class ResponsePageList<T> implements Serializable {

    private static final long serialVersionUID = 2766813904012682050L;
    private int currentPager; //当前页数
    private int pagerCount; //总页数
    private long total; //总条数
    private List<T> dataList;

    public int getCurrentPager() {
        return currentPager;
    }

    public void setCurrentPager(int currentPager) {
        this.currentPager = currentPager;
    }

    public int getPagerCount() {
        return pagerCount;
    }

    public void setPagerCount(int pagerCount) {
        this.pagerCount = pagerCount;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "ResponsePageList{" +
                "currentPager=" + currentPager +
                ", pagerCount=" + pagerCount +
                ", total=" + total +
                ", dataList=" + dataList +
                '}';
    }
}
