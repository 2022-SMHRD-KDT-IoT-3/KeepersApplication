package com.company.keepers_test;

public class ValueVO {

    private String v_seq ;
    private String d_seq ;
    private String v_weight ;
    private String v_signdate ;
    private String v_bat;

    public ValueVO() {
    }

    public ValueVO(String v_seq, String d_seq, String v_weight, String v_signdate, String v_bat) {
        this.v_seq = v_seq;
        this.d_seq = d_seq;
        this.v_weight = v_weight;
        this.v_signdate = v_signdate;
        this.v_bat = v_bat;
    }

    public ValueVO(String v_weight, String v_signdate) {
        this.v_weight = v_weight;
        this.v_signdate = v_signdate;
    }

    @Override
    public String toString() {
        return "ValueVO{" +
                "v_seq='" + v_seq + '\'' +
                ", d_seq='" + d_seq + '\'' +
                ", v_weight='" + v_weight + '\'' +
                ", v_signdate='" + v_signdate + '\'' +
                ", v_bat='" + v_bat + '\'' +
                '}';
    }

    public String getV_seq() {
        return v_seq;
    }

    public void setV_seq(String v_seq) {
        this.v_seq = v_seq;
    }

    public String getD_seq() {
        return d_seq;
    }

    public void setD_seq(String d_seq) {
        this.d_seq = d_seq;
    }

    public String getV_weight() {
        return v_weight;
    }

    public void setV_weight(String v_weight) {
        this.v_weight = v_weight;
    }

    public String getV_signdate() {
        return v_signdate;
    }

    public void setV_signdate(String v_signdate) {
        this.v_signdate = v_signdate;
    }

    public String getV_bat() {
        return v_bat;
    }

    public void setV_bat(String v_bat) {
        this.v_bat = v_bat;
    }
}
