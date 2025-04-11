package a4.streamx_be.services;

import a4.streamx_be.domain.dto.request.AIReqDtoV1;
import a4.streamx_be.domain.dto.response.AIResDtoV1;

public interface ChatService {
    AIResDtoV1 generateChatV1(AIReqDtoV1 dto);


}
