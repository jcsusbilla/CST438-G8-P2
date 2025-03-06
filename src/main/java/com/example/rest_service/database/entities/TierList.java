package com.example.rest_service.database.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "tier_list")
public class TierList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subject;

    // This may be useful later. Thought I would just add it
    @Column(nullable = false)
    private LocalDate weekStartDate;

    // Constructors
    public TierList() {}

    public TierList(String title, String subject, LocalDate weekStartDate) {
        this.title = title;
        this.subject = subject;
        this.weekStartDate = weekStartDate;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

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

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }
}

