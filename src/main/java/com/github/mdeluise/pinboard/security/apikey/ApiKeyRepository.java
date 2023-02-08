package com.github.mdeluise.pinboard.security.apikey;

import com.github.mdeluise.pinboard.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Collection<ApiKey> findByUserUsername(String username);

    Collection<ApiKey> findByUserId(long userId);

    Collection<ApiKey> findByUser(User user);

    void deleteByUser(User user);

    void deleteByUserId(long userId);

    Optional<ApiKey> findByValue(String apiKeyValue);

    boolean existsByValue(String apiKeyValue);
}
