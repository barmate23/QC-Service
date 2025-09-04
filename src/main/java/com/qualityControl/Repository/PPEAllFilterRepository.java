package com.qualityControl.Repository;

import com.qualityControl.Model.PpeFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PPEAllFilterRepository extends JpaRepository<PpeFilter,Integer> {

    List<PpeFilter> findByIsDeletedOrderBySequenceAsc(Boolean b);
}
