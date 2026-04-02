package com.srijan.user_service.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.srijan.user_service.Model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    Optional<Invitation> findByToken(String token);
}