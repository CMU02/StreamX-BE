package a4.streamx_be.services;

import a4.streamx_be.domain.model.ChatType;
import reactor.core.publisher.Flux;

/**
 * 제네릭 전략 인터페이스
 * @param <REQ> Request DTO
 * @param <RES> Response DTO
 */
public interface ChatStrategy<REQ, RES> {
    Boolean supports(ChatType type);
    Flux<RES> execute(REQ dto);
}
