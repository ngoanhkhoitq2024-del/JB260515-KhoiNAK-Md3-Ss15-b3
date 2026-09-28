package re.edu.md3ss15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.edu.md3ss15.entity.Order;
import re.edu.md3ss15.entity.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedDateDesc(User user);
}