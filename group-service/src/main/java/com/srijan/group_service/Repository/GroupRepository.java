package com.srijan.group_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srijan.group_service.Model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

}
