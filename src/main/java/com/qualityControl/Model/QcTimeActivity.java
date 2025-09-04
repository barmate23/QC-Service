package com.qualityControl.Model;


import lombok.Data;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Data
@Table(name = "tbl_qcTimeActivity")
public class QcTimeActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "qcEngineer", referencedColumnName = "id")
    private Users qcEngineer;

    @ManyToOne
    @JoinColumn(name = "qcActivityType", referencedColumnName = "id")
    private QcActivityType qcActivityType;

    @ManyToOne
    @JoinColumn(name = "ItemId")
    private Item item;

    @Column(name = "startTime")
    private Time startTime;

    @Column(name = "endTime")
    private Time endTime;

    @Column(name = "OrganizationId")
    private Integer organizationId;

    @Column(name = "SubOrganizationId")
    private Integer subOrganizationId;

    @Column(name = "IsDeleted")
    private Boolean isDeleted;


}
