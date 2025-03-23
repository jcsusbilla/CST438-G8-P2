package com.example.rest_service.database.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
public class TierListRequest {
    private String title;
    private String subject;
    private Integer userId;
    private String weekStartDate;

    @JsonProperty("rankings") // ✅ Ensures JSON maps correctly
    private List<Map<String, String>> rankings; // ✅ Still using Map

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(String weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public List<Map<String, String>> getRankings() {
        return rankings;
    }

    public void setRankings(List<Map<String, String>> rankings) {
        this.rankings = rankings;
    }
}
