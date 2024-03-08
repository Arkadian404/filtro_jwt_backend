package com.ark.security.repository.recommender;

import com.ark.security.models.recommender.TastePreferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastePreferencesRepository extends JpaRepository<TastePreferences, Long> {
    boolean existsByUserId(Long userId);
}
