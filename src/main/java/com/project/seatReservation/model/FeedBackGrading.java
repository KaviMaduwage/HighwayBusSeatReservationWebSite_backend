package com.project.seatReservation.model;

import javax.persistence.*;

@Entity
@Table(name = "feedback_grading")
public class FeedBackGrading {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int feedBackGradingId;
    private String description;

    public int getFeedBackGradingId() {
        return feedBackGradingId;
    }

    public void setFeedBackGradingId(int feedBackGradingId) {
        this.feedBackGradingId = feedBackGradingId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
