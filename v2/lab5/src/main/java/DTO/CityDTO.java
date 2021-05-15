package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO implements Serializable {
    private Long id;
    private Long countryId;
    private String name;
    private Long population;
}
