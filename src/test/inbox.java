/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author Adrian
 */
public class inbox {

    private String from;
    private String mssg;
    private String conf;
    private String type;
    private String mssgCode;

    public inbox() {
    }

    public inbox(String from, String mssg, String conf, String type, String mssgCode) {
        this.from = from;
        this.mssg = mssg;
        this.conf = conf;
        this.type = type;
        this.mssgCode = mssgCode;
    }

    public String getFrom() {
        return from;
    }

    public String getMssg() {
        return mssg;
    }

    public String getConf() {
        return conf;
    }

    public String getMssgCode() {
        return mssgCode;
    }

    public String getType() {
        return type;
    }
}
