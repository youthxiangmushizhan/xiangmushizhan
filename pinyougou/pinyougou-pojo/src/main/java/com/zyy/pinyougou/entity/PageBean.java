package com.zyy.pinyougou.entity;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {

    private int pageNo; //当前页
    private int pages;   //总页数
    private int total;   //总条数
    private int pageSize;//每页显示数
    private List<T> list;//要进行分页的list

    public PageBean(int pageNum, int pageSize, List<T> list) {
        super();
        this.pageNo = pageNum;
        this.pageSize = pageSize;
        this.list = list;
        this.total = this.getTotal();
        this.pages = this.getPages();
    }

    public PageBean() {
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNum) {
        this.pageNo = pageNum;
    }

    public int getPages() {
        if(this.total % this.pageSize == 0){
            this.pages = this.total / this.pageSize;
        }else{
            this.pages = this.total / this.pageSize+1;
        }
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        total = list.size();
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        List<T> newList = this.list.subList(pageSize*(pageNo-1), (pageSize*pageNo)>total?total:(pageSize*pageNo));
        return newList;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
