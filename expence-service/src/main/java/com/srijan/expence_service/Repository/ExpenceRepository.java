package com.srijan.expence_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.srijan.expence_service.Model.Expence;


@Repository
public interface ExpenceRepository extends JpaRepository<Expence, Integer> {

    public List<Expence> findByGroupId(int groupId);

    @Query(value = "DELETE FROM expence WHERE group_id = :groupId", nativeQuery = true)
    @Modifying
    @Transactional
    public void deleteAllByGroupId(int groupId);

    public List<Expence> findByDescription(String description);
}
