package com.sanskar.tv.EPG;

/**
 * Created by Kristoffer.
 */
public class EPGChannel {

    private final String channelID;
    private final String name;
    private final String imageURL;
    private final String Url;

    public String getUrl() {
        return Url;
    }

    public EPGChannel(String imageURL, String name, String channelID, String Url) {
        this.imageURL = imageURL;
        this.name = name;
        this.channelID = channelID;
        this.Url = Url;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }
}
