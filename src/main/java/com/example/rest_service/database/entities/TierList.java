package com.example.rest_service.database.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class TierList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // ✅ Unique ID for the TierList

    private String title;   // ✅ Title of the tier list
    private String subject; // ✅ Subject of the tier list

    @OneToMany(mappedBy = "tierList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TierRanking> rankings; // ✅ List of ranked items in this tier list

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy; // ✅ Links TierList to the User who created it

    // ✅ Constructors
    public TierList() {}

    public TierList(String title, String subject, User createdBy) {
        this.title = title;
        this.subject = subject;
        this.createdBy = createdBy;
    }

    // ✅ Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<TierRanking> getRankings() {
        return rankings;
    }

    public void setRankings(List<TierRanking> rankings) {
        this.rankings = rankings;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//import java.time.LocalDate;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//import java.util.Set;
//
//@Entity
//@Table(name = "tier_list")
//public class TierList {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Integer id;
//
//    @Column(nullable = false)
//    private String title;
//
//    @Column(nullable = false)
//    private String subject;
//
//    // This may be useful later. Thought I would just add it
//    @Column(nullable = false)
//    private LocalDate weekStartDate;
//
//    // Constructors
//    public TierList() {}
//
//    public TierList(String title, String subject, LocalDate weekStartDate) {
//        this.title = title;
//        this.subject = subject;
//        this.weekStartDate = weekStartDate;
//    }
//
//    // Getters and Setters
//    public Integer getId() {
//        return id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//
//    public LocalDate getWeekStartDate() {
//        return weekStartDate;
//    }
//
//    public void setWeekStartDate(LocalDate weekStartDate) {
//        this.weekStartDate = weekStartDate;
//    }
//
//    public void setId(int tierListId) {
//    }
//}
//
//
