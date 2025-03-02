package takee.dev.springboot_jms_amq.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageModel(@Schema(example = "1") String id,
        @Schema(example = "test correction id") String content) {

}
