/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grp;


/**
 *
 * @author Adrian
 */
public class createTask {
private String title;
    private String desc;
    private String diff;
    private String type;
    private String time;
    private String time2;
    private String worker;
    private String status;

    public createTask() {
    }


    public createTask(String title, String desc, String diff, String type, String time, String time2, String worker, String status) {
        this.title = title;
        this.desc = desc;
        this.diff = diff;
        this.type = type;
        this.time = time;
        this.time2 = time2;
        this.worker = worker;
        this.status = status;
    }

    public String getDiff() {
        return diff;
    }

    public String getWorker() {
        return worker;
    }

    public String getType() {
        return type;
    }

    public String getTime2() {
        return time2;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getStatus() {
        return status;
    }
}