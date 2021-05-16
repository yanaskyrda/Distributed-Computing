package data.dto;

import java.io.Serializable;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class CityDTO implements Serializable {
    private Long id;
    private Long countryId;
    private String name;
    private Long population;
}
