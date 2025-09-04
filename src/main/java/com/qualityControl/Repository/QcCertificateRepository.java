package com.qualityControl.Repository;

import com.qualityControl.Model.QCCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QcCertificateRepository extends JpaRepository<QCCertificate,Integer> {
    QCCertificate findByIsDeletedAndSubOrganizationIdAndItemId(boolean b, Integer subOrgId, Integer id);

    QCCertificate findByIsDeletedAndSubOrganizationIdAndAsnLineId(boolean b, Integer subOrgId, Integer id);

    QCCertificate findByIsDeletedAndSubOrganizationIdAndPurchaseOrderLineId(boolean b, Integer subOrgId, Integer id);
}
