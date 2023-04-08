package com.sanskar.tv.jwPlayer;

public class MessageModel {
    private  String reciever_name;
    private  String tv_receiver_body;
    private  String reciever_time;
    private  String sender_name;
    private  String sender_time;
    private  String tv_sender_body;
    private  String sender_id;
    private  String reciever_id;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public MessageModel(String reciever_name, String tv_receiver_body, String reciever_time, String sender_name, String sender_time, String tv_sender_body, String sender_id, String reciever_id) {
        this.reciever_name = reciever_name;
        this.tv_receiver_body = tv_receiver_body;
        this.reciever_time = reciever_time;
        this.sender_name = sender_name;
        this.sender_time = sender_time;
        this.tv_sender_body = tv_sender_body;
        this.sender_id = sender_id;
        this.reciever_id = reciever_id;
    }


/*
    public MessageModel(String reciever_name, String tv_receiver_body, String reciever_time, String sender_name, String sender_time, String tv_sender_body) {
        this.reciever_name = reciever_name;
        this.tv_receiver_body = tv_receiver_body;
        this.reciever_time = reciever_time;
        this.sender_name = sender_name;
        this.sender_time = sender_time;
        this.tv_sender_body = tv_sender_body;
    }*/

    public String getReciever_name() {
        return reciever_name;
    }

    public void setReciever_name(String reciever_name) {
        this.reciever_name = reciever_name;
    }

    public String getTv_receiver_body() {
        return tv_receiver_body;
    }

    public void setTv_receiver_body(String tv_receiver_body) {
        this.tv_receiver_body = tv_receiver_body;
    }

    public String getReciever_time() {
        return reciever_time;
    }

    public void setReciever_time(String reciever_time) {
        this.reciever_time = reciever_time;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_time() {
        return sender_time;
    }

    public void setSender_time(String sender_time) {
        this.sender_time = sender_time;
    }

    public String getTv_sender_body() {
        return tv_sender_body;
    }

    public void setTv_sender_body(String tv_sender_body) {
        this.tv_sender_body = tv_sender_body;
    }
}
