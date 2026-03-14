package com.thacbao.neki.controllers.pub;

import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.DiscountResponse;
import com.thacbao.neki.enums.DiscountType;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.UserRepository;
import com.thacbao.neki.security.SecurityUtils;
import com.thacbao.neki.security.UserPrincipal;
import com.thacbao.neki.services.DiscountCalculationService;
import com.thacbao.neki.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discount")
public class DiscountController {
    private final DiscountService discountService;
    private final DiscountCalculationService discountCalculationService;
    private final UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> getAllDiscount(@RequestParam String discountType) {
        List<DiscountResponse> responses = discountService.getAllByType(discountType);
        return ResponseEntity.ok(
                ApiResponse.<List<DiscountResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(responses)
                        .build()
        );
    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> applyDiscountCode(
            @RequestBody Map<String, Object> body) {
        String discountCode = (String) body.get("discountCode");
        BigDecimal orderAmount = new BigDecimal(body.get("orderAmount").toString());
        BigDecimal shippingFee = new BigDecimal(body.get("shippingFee").toString());

        UserPrincipal userPrincipal = SecurityUtils.getCurrentUser();
        if (userPrincipal == null) {
            throw new NotFoundException("User not logged in");
        }
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Map<DiscountType, BigDecimal> result = discountCalculationService.applyDiscountCode(
                discountCode, user, orderAmount, shippingFee);

        Map<String, BigDecimal> response = new HashMap<>();
        response.put("amountDiscount", result.get(DiscountType.AMOUNT));
        response.put("shipDiscount", result.get(DiscountType.SHIP));

        return ResponseEntity.ok(
                ApiResponse.<Map<String, BigDecimal>>builder()
                        .code(200)
                        .status("success")
                        .data(response)
                        .build()
        );
    }
}
