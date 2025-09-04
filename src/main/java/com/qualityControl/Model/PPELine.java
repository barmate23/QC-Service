
package com.qualityControl.Model;
import lombok.Data;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "tbl_PPELine")
public class PPELine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "OrganizationId")
    private Integer organizationId;

    @Column(name = "SubOrganizationId")
    private Integer subOrganizationId;

    @ManyToOne
    @JoinColumn(name = "bom_line_id")
    private BOMLine bomLine;

    @ManyToOne
    @JoinColumn(name = "PPEHeadId")
    private PPEHead ppeHead;

    @ManyToOne
    @JoinColumn(name = "itemId", referencedColumnName = "id")
    private Item item;

//    //add new column
//    @ManyToOne
//    @JoinColumn(name = "equipmentId", referencedColumnName = "id")
//    private Equipment equipment;

    //add new column
    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "id")
    private CommonMaster status;

    //add new column
    @ManyToOne
    @JoinColumn(name = "storeOperatorId", referencedColumnName = "id")
    private Users storeOperator;

    //add new column
    @Column(name = "issueQuantity")
    private Integer issueQuantity;

    @ManyToOne
    @JoinColumn(name = "qcEngineer", referencedColumnName = "id")
    private Users qcEngineer;

    @Column(name = "qcDate")
    private Date qcDate;

    //add new column
    @Column(name = "startTime")
    private Time startTime;

    //add new column
    @Column(name = "endTime")
    private Time endTime;

    //add new column
    @Column(name = "storeOperatorDate")
    private Date storeOperatorDate;

    //add new column
    @Column(name = "returnRejectQuantity")
    private Integer returnRejectQuantity;

    //add new column
    @Column(name = "returnRejectDate")
    private Date returnRejectDate;

    //add new column
    @Column(name = "returnRejectTime")
    private Time returnRejectTime;

    //add new column
    @Column(name = "reissueDate")
    private Date reissueDate;

    //add new column
    @Column(name = "reissueTime")
    private Time reissueTime;

    //add new column

    @ManyToOne
    @JoinColumn(name = "returnRejectReasonId", referencedColumnName = "id")
    private Reason returnRejectReason;

    //add new column
    @Column(name = "reissue")
    private Boolean reissue;

    //add new column
    @Column(name = "reissueQuantity")
    private Integer reissueQuantity;

    //add new column
    @Column(name = "reissuedQuantity")
    private Integer reissuedQuantity;

    //add new column
    @ManyToOne
    @JoinColumn(name = "reissueItemId", referencedColumnName = "id")
    private Item reissueItem;


    @Column(name = "requiredQuantity")
    private Integer requiredQuantity;

    @Column(name = "requiredBy")
    private Date requiredBy;

    @Column(name = "shortage")
    private Integer shortage;

    @Column(name = "inPipeline")
    private Integer inPipeline;

    @Column(name = "store", length = 30)
    private String store;

    @Column(name = "eta")
    private Integer eta;


    @Column(name = "isScheduled")
    private Boolean isScheduled;

    @Column(name="allocatedQty")
    private Integer allocatedQty;

    @Column(name = "isDeleted")
    private Boolean isDeleted;

    @Column(name = "createdBy")
    private Integer createdBy;

    @Column(name = "createdOn")
    private Date createdOn;

    @Column(name = "modifiedBy")
    private Integer modifiedBy;

    @Column(name = "modifiedOn")
    private Date modifiedOn;

    @Transient
    private List<StockMovement> stockMovements;
}
