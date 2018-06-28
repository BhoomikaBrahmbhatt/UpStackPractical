package com.giphy.upstackpractical.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Vote {


    @Id
    long id;

    String idVideo;


    public String getUpVote() {
        return upVote;
    }

    public void setUpVote(String upVote) {
        this.upVote = upVote;
    }

    public String getDownVote() {
        return downVote;
    }

    public void setDownVote(String downVote) {
        this.downVote = downVote;
    }

    String upVote;
    String downVote;

    public Vote(long id,String idVideo, String upVote, String downVote) {
        this.idVideo = idVideo;
        this.upVote = upVote;
        this.downVote = downVote;
        this.id = id;
    }

    public Vote() {
    }

    public String getId() {
        return this.idVideo;
    }

    public void setId(String idVideo) {
        this.idVideo = idVideo;
    }


}
