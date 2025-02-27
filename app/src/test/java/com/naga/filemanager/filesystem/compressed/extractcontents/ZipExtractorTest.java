package com.naga.filemanager.filesystem.compressed.extractcontents;

import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.ZipExtractor;

public class ZipExtractorTest extends AbstractExtractorTest {
    @Override
    protected String getArchiveType() {
        return "zip";
    }

    @Override
    protected Class<? extends Extractor> extractorClass() {
        return ZipExtractor.class;
    }
}
