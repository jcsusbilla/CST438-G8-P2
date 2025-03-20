package com.example.rest_service.database.controllers;

import java.util.List;
import java.util.Map;

public class TierListRequest {
    private String title;
    private String subject;
    private Integer userId;
    private List<Map<String, String>> rankings;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public List<Map<String, String>> getRankings() { return rankings; }
    public void setRankings(List<Map<String, String>> rankings) { this.rankings = rankings; }
}
