package com.qualityControl.Model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Data
@Table(name = "tbl_AcceptedRejectedContainer")
public class AcceptedRejectedContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "OrganizationId")
    private Integer organizationId;

    @Column(name = "SubOrganizationId")
    private Integer subOrganizationId;

    @ManyToOne
    @JoinColumn(name = "purchaseOrderLineId", referencedColumnName = "id")
    private PurchaseOrderLine purchaseOrderLine;

    @ManyToOne
    @JoinColumn(name = "AsnOrderLineId", referencedColumnName = "id")
    private ASNOrderLine ASNOrderLine;

    @Column(name = "acceptedRejectedContainerQuantity")
    private Integer acceptedRejectedContainerQuantity;

    @Column(name = "qnNumber")
    private Integer qnNumber;

    @Column(name = "qcDate")
    private Date qcDate;

    @ManyToOne
    @JoinColumn(name = "qcEngineer", referencedColumnName = "id")
    private Users qcEngineer;

//    @ManyToOne
//    @JoinColumn(name = "dock_id")
//    private Dock dock;

    @Column(name = "startTime")
    private Time startTime;

    @Column(name = "endTime")
    private Time endTime;

//    @Column(name = "status")
//    private Integer status;

    @ManyToOne
    @JoinColumn(name = "StatusId", referencedColumnName = "id")
    private CommonMaster status;

    @Column(name = "isAccepted")
    private Boolean isAccepted;

    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "CreatedOn")
    private Date createdOn;

    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "ModifiedOn")
    private Date modifiedOn;

    //Added By Kamlesh
//    @Column(name="QcAssignDate")
//    private Date qcAssignDate;

}
