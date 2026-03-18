package by.beltam.practice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank()
        @Size(min = 4, max = 20)
        String username,

        @NotBlank()
        @Size(min = 8, max = 64)
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S+$",
                message = "Password must have 1 symbol in uppercase, lowercase, 1 digit adn no spaces"
        )
        String password

) {
}
