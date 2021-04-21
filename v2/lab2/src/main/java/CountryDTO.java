import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CountryDTO {
    private long id;
    private String name;
    private List<CityDTO> cities = new ArrayList<>();
}
