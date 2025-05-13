package a4.streamx_be.marketplace.controller;

import a4.streamx_be.marketplace.dto.response.MarketplaceImageResponse;
import a4.streamx_be.marketplace.service.MarketplaceImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/marketplace/v1/image")
public class MarketplaceImageController {

    private final MarketplaceImageService service;

    @PostMapping("/upload")
    public ResponseEntity<MarketplaceImageResponse> uploadImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam("marketId") Long marketId,
            @RequestParam("orderIndex") Integer orderIndex
    ) {
        return ResponseEntity.ok(service.uploadImage(file, marketId, orderIndex));
    }
}