
package com.sanskar.tv.EPG;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Event {

    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("start_time_milliseconds")
    @Expose
    private Integer startTimeMilliseconds;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("duration_milliseconds")
    @Expose
    private Integer durationMilliseconds;
    @SerializedName("program_title")
    @Expose
    private String programTitle;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("end_time_milliseconds")
    @Expose
    private Integer endTimeMilliseconds;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getStartTimeMilliseconds() {
        return startTimeMilliseconds;
    }

    public void setStartTimeMilliseconds(Integer startTimeMilliseconds) {
        this.startTimeMilliseconds = startTimeMilliseconds;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getDurationMilliseconds() {
        return durationMilliseconds;
    }

    public void setDurationMilliseconds(Integer durationMilliseconds) {
        this.durationMilliseconds = durationMilliseconds;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getEndTimeMilliseconds() {
        return endTimeMilliseconds;
    }

    public void setEndTimeMilliseconds(Integer endTimeMilliseconds) {
        this.endTimeMilliseconds = endTimeMilliseconds;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

}
