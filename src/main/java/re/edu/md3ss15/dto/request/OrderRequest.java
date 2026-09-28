package re.edu.md3ss15.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemRequest> items;
}
