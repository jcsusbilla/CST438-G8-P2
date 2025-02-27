package com.example.rest_service.database.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "tier_ranking",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tier", "item", "tier_list_id"}))

public class TierRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // Want Tier to be Restricted to S,A,B,C,D,F so used enum. Definitely had to look up @Enumerated annotation.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tier tier;

    // item name
    @Column(nullable = false)
    private String item;

    // I think that this is how I want to go about it. Each Ranking Belongs to a specific tier list
    @ManyToOne
    @JoinColumn(name = "tier_list_id", nullable = false)
    private TierList tierList;


    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public TierList getTierList() {
        return tierList;
    }

    public void setTierList(TierList tierList) {
        this.tierList = tierList;
    }

    public enum Tier {
        S, A, B, C, D, F
    }
}
