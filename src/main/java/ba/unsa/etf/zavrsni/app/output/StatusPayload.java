package ba.unsa.etf.zavrsni.app.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusPayload {
    private String message;
    private String action;
    private Boolean success;
}
