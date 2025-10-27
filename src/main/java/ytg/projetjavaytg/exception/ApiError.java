package ytg.projetjavaytg.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiError {

    private String message;
    private int code;
    private LocalDateTime timestamp;
    private int status;
    private String error;
}
