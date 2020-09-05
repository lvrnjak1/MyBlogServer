package ba.unsa.etf.zavrsni.app.rest.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    @NotBlank
    private String dateTimePosted;
}
