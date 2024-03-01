package com.ark.security.models.recommender;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@IdClass(TastePreferencesKey.class)
public class TastePreferences {

    @Id
    private Long userId;
    @Id
    private Long itemId;

    private Double preference;

}
