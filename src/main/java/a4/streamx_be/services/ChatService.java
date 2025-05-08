package a4.streamx_be.services;

import reactor.core.publisher.Flux;

public interface ChatService<REQ, RES> {
    Flux<RES> chat(REQ dto);
}
