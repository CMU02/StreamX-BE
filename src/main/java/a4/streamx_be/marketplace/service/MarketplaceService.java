package a4.streamx_be.marketplace.service;

import a4.streamx_be.marketplace.dto.request.MarketplaceCreateRequest;
import a4.streamx_be.marketplace.dto.response.MarketplaceResponse;
import a4.streamx_be.marketplace.entity.Marketplace;
import a4.streamx_be.marketplace.repository.MarketplaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketplaceService {

    private final MarketplaceRepository repository;

    public MarketplaceResponse createMarketplace(MarketplaceCreateRequest request) {
        Marketplace entity = Marketplace.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .userId(request.getUserId())
                .build();

        Marketplace saved = repository.save(entity);

        return toResponse(saved);
    }

    public MarketplaceResponse getMarketplace(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Marketplace not found"));
    }

    public List<MarketplaceResponse> getAllMarketplaces() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteMarketplace(Long id) {
        repository.deleteById(id);
    }

    private MarketplaceResponse toResponse(Marketplace entity) {
        return MarketplaceResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .userId(entity.getUserId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
