package com.example.example100.user.model;

import com.example.example100.user.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    private Long totalCount;
    private List<User> data;
}
