package a4.streamx_be.chat.services;

import a4.streamx_be.user.domain.entity.User;
import reactor.core.publisher.Flux;

public interface ChatService<REQ, RES> {
    Flux<RES> chat(REQ dto, User user);
}
