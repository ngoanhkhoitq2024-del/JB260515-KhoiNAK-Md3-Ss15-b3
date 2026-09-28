package re.edu.md3ss15.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import re.edu.md3ss15.dto.request.OrderRequest;
import re.edu.md3ss15.dto.request.OrderStatusRequest;
import re.edu.md3ss15.entity.Order;
import re.edu.md3ss15.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest request, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(orderService.createOrder(request, email));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(orderService.getMyOrders(email));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Long id, @RequestBody OrderStatusRequest request) {

        return ResponseEntity.ok(orderService.updateStatus(id, request.getStatus()));
    }
}