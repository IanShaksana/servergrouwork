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
public class createJob {

    private String title;
    private String desc;
    private String time;
    private String slotmax;
    private String slotnow;
    private String status;
    private String owner;

    public createJob() {
    }


    public createJob(String title, String desc, String time, String slotmax, String slotnow, String status, String owner) {
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.slotmax = slotmax;
        this.slotnow = slotnow;
        this.status = status;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getSlotmax() {
        return slotmax;
    }

    public String getSlotnow() {
        return slotnow;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }
}