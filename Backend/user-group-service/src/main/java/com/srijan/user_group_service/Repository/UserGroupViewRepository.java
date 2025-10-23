package com.srijan.user_group_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srijan.user_group_service.Model.UserGroupView;

public interface UserGroupViewRepository extends JpaRepository<UserGroupView, Integer>{

    public UserGroupView findByUserIdAndGroupId(int userId, int groupId);

    public List<UserGroupView> findByGroupId(int groupId);

}
