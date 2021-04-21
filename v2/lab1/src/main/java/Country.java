import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Country {
    private String id;
    private String name;
    private List<City> cities = new ArrayList<>();
}
