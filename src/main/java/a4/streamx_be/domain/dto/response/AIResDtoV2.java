package a4.streamx_be.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AIResDtoV2 {
    private String text;
    private String audioUrl;
}
