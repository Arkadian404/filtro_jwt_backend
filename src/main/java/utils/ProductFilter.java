package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductFilter {
    List<String> brands;
    List<String> categories;
    List<String> flavors;
    List<String> origins;
    List<String> vendors;
}
