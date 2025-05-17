package a4.streamx_be.marketplace.controller;

import a4.streamx_be.marketplace.dto.request.MarketplaceCreateRequest;
import a4.streamx_be.marketplace.dto.response.MarketplaceResponse;
import a4.streamx_be.marketplace.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/marketplace/v1")
public class MarketplaceController {

    private final MarketplaceService marketplaceService;

    @PostMapping("/add-product")
    public ResponseEntity<MarketplaceResponse> create(@RequestBody MarketplaceCreateRequest request) {
        return ResponseEntity.ok(marketplaceService.createMarketplace(request));
    }

    @GetMapping("/get-product/{id}")
    public ResponseEntity<MarketplaceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(marketplaceService.getMarketplace(id));
    }

    @GetMapping("/get-all-product")
    public ResponseEntity<List<MarketplaceResponse>> getAll() {
        return ResponseEntity.ok(marketplaceService.getAllMarketplaces());
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        marketplaceService.deleteMarketplace(id);
        return ResponseEntity.noContent().build();
    }
}
