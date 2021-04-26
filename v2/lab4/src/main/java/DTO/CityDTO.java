package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO implements Serializable {
    private long id;
    private long countryId;
    private String name;
    private long population;
}
