package com.filespace.domain.dashboard.enumuration;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public enum ExtensionType {

    IMAGE("Image", Arrays.asList("png", "jpeg", "jpg", "svg", "bmp", "gif", "raw", "tiff")),
    ZIP("Zip", Arrays.asList("zip", "tar", "gzip", "alz", "egg", "rar")),
    DOC("Doc", Arrays.asList("pdf", "txt", "xls", "ppt", "doc", "ai", "psd", "hwp")),
    AUDIO("Audio", Arrays.asList("m4a", "wav", "wma", "mp3", "mp4", "mkv", "avi", "flv", "mov")),
    OTHER("Others", Arrays.asList());

    private String title;
    private List<String> extensionList;

    ExtensionType (String title, List<String> extensionList) {
        this.title = title;
        this.extensionList = extensionList;
    }

    public static ExtensionType findByExtension(String extension) {
        return Arrays.stream(ExtensionType.values())
                .filter(extensionType -> extensionType.hasExtension(extension))
                .findAny()
                .orElse(OTHER);
    }

    private boolean hasExtension (String extension) {
        return extensionList.stream()
                .anyMatch(regExt -> regExt.equals(extension));
    }

}
