package com.company.keepers_test;

import java.io.Serializable;

public class BoardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int b_seq;
    private String b_title;
    private String b_content;
    private String b_id;
    private String b_signdate;

    public BoardVO() {
    }

    public BoardVO(int b_seq, String b_title, String b_id, String b_signdate) {
        this.b_seq = b_seq;
        this.b_title = b_title;
        this.b_id = b_id;
        this.b_signdate = b_signdate;
    }

    public BoardVO(int b_seq, String b_title, String b_content, String b_id, String b_signdate) {
        this.b_seq = b_seq;
        this.b_title = b_title;
        this.b_content = b_content;
        this.b_id = b_id;
        this.b_signdate = b_signdate;
    }

    @Override
    public String toString() {
        return "BoardVO{" +
                "b_seq=" + b_seq +
                ", b_title='" + b_title + '\'' +
                ", b_content='" + b_content + '\'' +
                ", b_id='" + b_id + '\'' +
                ", b_signdate='" + b_signdate + '\'' +
                '}';
    }

    public int getB_seq() {
        return b_seq;
    }

    public void setB_seq(int b_seq) {
        this.b_seq = b_seq;
    }

    public String getB_title() {
        return b_title;
    }

    public void setB_title(String b_title) {
        this.b_title = b_title;
    }

    public String getB_content() {
        return b_content;
    }

    public void setB_content(String b_content) {
        this.b_content = b_content;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getB_signdate() {
        return b_signdate;
    }

    public void setB_signdate(String b_signdate) {
        this.b_signdate = b_signdate;
    }
}
