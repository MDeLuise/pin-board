package com.github.mdeluise.pinboard.authentication.payload.response;

import java.util.List;

public record UserInfoResponse(long id, String username, List<String> authorities) {
}
