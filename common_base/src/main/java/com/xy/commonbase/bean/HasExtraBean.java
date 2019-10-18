package com.xy.commonbase.bean;

import java.util.List;

public class HasExtraBean<HEAD,C>{

    private HEAD head;

    private List<C> list;

    public HEAD getHead() {
        return head;
    }

    public void setHead(HEAD head) {
        this.head = head;
    }

    public List<C> getList() {
        return list;
    }

    public void setList(List<C> list) {
        this.list = list;
    }
}
