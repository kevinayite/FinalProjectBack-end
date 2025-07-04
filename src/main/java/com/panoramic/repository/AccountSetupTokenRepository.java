package com.panoramic.repository;

import com.panoramic.model.AccountSetupToken;
import com.panoramic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountSetupTokenRepository extends JpaRepository<AccountSetupToken, Long> {
    Optional<AccountSetupToken> findByToken(String token);
    Optional<AccountSetupToken> findByUser(User user);
    void deleteByUser(User user);
}
