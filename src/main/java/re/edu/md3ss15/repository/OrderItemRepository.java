package re.edu.md3ss15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.edu.md3ss15.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}