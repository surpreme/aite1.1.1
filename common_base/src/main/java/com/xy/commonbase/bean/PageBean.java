package com.xy.commonbase.bean;

public class PageBean {

    /**
     * hasmore : false
     * page_total : 1
     */

    private boolean hasmore;
    private int page_total;
    private int pageRange;

    public boolean isHasmore() {
        return hasmore;
    }

    public void setHasmore(boolean hasmore) {
        this.hasmore = hasmore;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public int getPageRange() {
        return pageRange;
    }

    public void setPageRange(int pageRange) {
        this.pageRange = pageRange;
    }
}
