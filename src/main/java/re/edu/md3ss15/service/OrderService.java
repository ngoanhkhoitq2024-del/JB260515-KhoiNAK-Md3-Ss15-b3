package re.edu.md3ss15.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.edu.md3ss15.dto.request.OrderItemRequest;
import re.edu.md3ss15.dto.request.OrderRequest;
import re.edu.md3ss15.entity.Order;
import re.edu.md3ss15.entity.OrderItem;
import re.edu.md3ss15.entity.Product;
import re.edu.md3ss15.entity.User;
import re.edu.md3ss15.repository.OrderRepository;
import re.edu.md3ss15.repository.ProductRepository;
import re.edu.md3ss15.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Order createOrder(OrderRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        Order order = Order.builder()
                .user(user)
                .createdDate(LocalDateTime.now())
                .status("PENDING")
                .totalMoney(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal totalMoney = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy sản phẩm"));

            BigDecimal priceBuy = product.getPrice();
            BigDecimal itemTotal = priceBuy.multiply(
                            BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceBuy(priceBuy)
                    .build();

            order.getItems().add(orderItem);
            totalMoney = totalMoney.add(itemTotal);
        }
        order.setTotalMoney(totalMoney);
        return orderRepository.save(order);
    }

    public List<Order> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        return orderRepository.findByUserOrderByCreatedDateDesc(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}