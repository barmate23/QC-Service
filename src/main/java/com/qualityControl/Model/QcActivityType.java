package com.qualityControl.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tbl_qcActivityType")
public class QcActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    private String activityName;

    private Boolean isDeleted;

}
