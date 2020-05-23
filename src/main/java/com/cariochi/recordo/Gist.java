package com.cariochi.recordo;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gist {

    private String description;

    @Singular
    private Map<String, File> files;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class File {
        private String content;
    }
}
