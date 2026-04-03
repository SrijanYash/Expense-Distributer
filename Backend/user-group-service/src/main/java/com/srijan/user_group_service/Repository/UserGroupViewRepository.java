package com.srijan.user_group_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.srijan.user_group_service.Model.UserGroupView;

public interface UserGroupViewRepository extends JpaRepository<UserGroupView, Integer>{

    public UserGroupView findTopByUserIdAndGroupIdOrderByIdDesc(int userId, int groupId);

    @Query(value = "SELECT DISTINCT ON (user_id) * FROM user_group_view WHERE group_id = :groupId ORDER BY user_id, id DESC", nativeQuery = true)
    public List<UserGroupView> findLatestPerUserInGroup(@Param("groupId") int groupId);

    public List<UserGroupView> findByGroupId(int groupId);

}
