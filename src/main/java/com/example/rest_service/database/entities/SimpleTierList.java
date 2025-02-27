package com.example.rest_service.database.entities;


import jakarta.persistence.*;
import java.util.Map;

@Entity
public class SimpleTierList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;


    //https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html
    @ElementCollection
    @CollectionTable(name = "tier_list_ranking", joinColumns = @JoinColumn(name = "tier_list_id"))
    @MapKeyColumn(name = "tier")
    @Column(name = "item")
    private Map<String, String> rankings;

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

    public Map<String, String> getRankings() {
        return rankings;
    }

    public void setRankings(Map<String, String> rankings) {
        this.rankings = rankings;
    }
}

