package com.example.demo.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContestDto{
    private String id;
    private String owner;
    private String name;
    private String startMessage;
    private String endMessage;
    private Boolean isStarted;
    private Boolean isStopped;
    private Long durationInSeconds;
    private String createdAt;
    private String updatedAt;

    @Override
    public String toString() {
        return "ContestDto{" +
                "id='" + id + '\'' +
                ", owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", startMessage='" + startMessage + '\'' +
                ", endMessage='" + endMessage + '\'' +
                ", isStarted=" + isStarted +
                ", isStopped=" + isStopped +
                ", durationInSeconds=" + durationInSeconds +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
