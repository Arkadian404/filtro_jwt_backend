package com.ark.security.models.recommender;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TastePreferencesKey implements Serializable {

    private Long userId;
    private Long itemId;

}
