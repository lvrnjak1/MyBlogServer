package ba.unsa.etf.zavrsni.app.rest.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginPayload {
    private String token;
    private Long accountId;
}
