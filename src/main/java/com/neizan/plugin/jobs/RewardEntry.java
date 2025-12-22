package com.neizan.plugin.jobs;

import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public class RewardEntry {
    private final String title;
    private final Material icon;
    private final double pay;
    private final List<String> details;

    public RewardEntry(String title, Material icon, double pay, List<String> details) {
        this.title = title;
        this.icon = icon;
        this.pay = pay;
        this.details = details == null ? Collections.emptyList() : List.copyOf(details);
    }

    public String getTitle() { return title; }
    public Material getIcon() { return icon; }
    public double getPay() { return pay; }
    public List<String> getDetails() { return details; }
}
