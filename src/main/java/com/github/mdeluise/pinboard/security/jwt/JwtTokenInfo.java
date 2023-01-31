package com.github.mdeluise.pinboard.security.jwt;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record JwtTokenInfo(
        String token,
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy hh:mm:ss"
        )
        Date expiresOn
) {
}
