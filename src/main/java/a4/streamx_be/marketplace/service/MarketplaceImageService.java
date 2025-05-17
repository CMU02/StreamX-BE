package a4.streamx_be.marketplace.service;

import a4.streamx_be.marketplace.dto.response.MarketplaceImageResponse;
import a4.streamx_be.marketplace.entity.MarketplaceImage;
import a4.streamx_be.marketplace.repository.MarketplaceImageRepository;
import a4.streamx_be.marketplace.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MarketplaceImageService {

    private final MarketplaceImageRepository repository;

    public MarketplaceImageResponse uploadImage(MultipartFile file, Long marketId, Integer orderIndex) {
        String filePath = FileUtils.saveFile(file);

        MarketplaceImage image = new MarketplaceImage();
        image.setMarketplaceId(marketId);
        image.setOrderIndex(orderIndex);
        image.setImageUrl(filePath);

        MarketplaceImage saved = repository.save(image);
        return new MarketplaceImageResponse(saved.getId(), saved.getImageUrl());
    }
}