package com.example.rest_service.database.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "tier_ranking",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tier", "item", "tier_list_id"}))

public class TierRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tier; // ✅ The ranking tier (S, A, B, C, D, F)
    private String item; // ✅ The ranked item name

    @ManyToOne
    @JoinColumn(name = "tier_list_id", nullable = false)
    private TierList tierList; // ✅ Links this ranking to a TierList

    // ✅ Default constructor (required by JPA)
    public TierRanking() {}

    // ✅ Constructor for creating a new ranking
    public TierRanking(String tier, String item, TierList tierList) {
        this.tier = tier;
        this.item = item;
        this.tierList = tierList;
    }

    // ✅ Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
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
}
