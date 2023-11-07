package com.ark.security.models.product;

import com.ark.security.config.CustomResolver;
import com.ark.security.dto.SaleDto;
import com.ark.security.models.product.Product;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id", resolver = CustomResolver.class)
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date start;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date end;
    private Integer discount;
    private Boolean status;

    @OneToMany(mappedBy = "sale", fetch = FetchType.LAZY)
    //@JsonManagedReference(value = "sale-product")
    @JsonIgnore
    private List<Product> productList;

    public SaleDto convertToDto(){
        return SaleDto.builder()
                .id(this.id)
                .discount(this.discount)
                .build();
    }

}
