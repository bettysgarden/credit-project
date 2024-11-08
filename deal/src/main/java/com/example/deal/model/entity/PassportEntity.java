package com.example.deal.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Table(name = "passport")
public class PassportEntity extends BaseEntity {

    @Column(name = "series", length = 4)
    private String series;

    @Column(name = "number", length = 6)
    private String number;

    @Column(name = "issue_branch")
    private String issueBranch;

    @Column(name = "issue_date")
    private Date issueDate;
}
