/*
 * CompressedHelper.java
 *
 * Copyright (C) 2017-2019 Emmanuel Messulam<emmanuelbendavid@gmail.com>,
 * Raymond Lai <airwave209gt@gmail.com> and Contributors.
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.naga.filemanager.filesystem.compressed;

import android.content.Context;
import androidx.annotation.NonNull;

import com.naga.filemanager.filesystem.compressed.extractcontents.Extractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.Bzip2Extractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.GzipExtractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.LzmaExtractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.RarExtractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.SevenZipExtractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.TarExtractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.XzExtractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.ZipExtractor;
import com.naga.filemanager.filesystem.compressed.showcontents.Decompressor;
import com.naga.filemanager.filesystem.compressed.showcontents.helpers.Bzip2Decompressor;
import com.naga.filemanager.filesystem.compressed.showcontents.helpers.GzipDecompressor;
import com.naga.filemanager.filesystem.compressed.showcontents.helpers.LzmaDecompressor;
import com.naga.filemanager.filesystem.compressed.showcontents.helpers.RarDecompressor;
import com.naga.filemanager.filesystem.compressed.showcontents.helpers.SevenZipDecompressor;
import com.naga.filemanager.filesystem.compressed.showcontents.helpers.TarDecompressor;
import com.naga.filemanager.filesystem.compressed.showcontents.helpers.XzDecompressor;
import com.naga.filemanager.filesystem.compressed.showcontents.helpers.ZipDecompressor;
import com.naga.filemanager.utils.Utils;

import java.io.File;

/**
 * @author Emmanuel
 *         on 23/11/2017, at 17:46.
 */

public abstract class CompressedHelper {

    /**
     * Path separator used by all Decompressors and Extractors.
     * e.g. rar internally uses '\' but is converted to "/" for the app.
     */
    public static final char SEPARATOR_CHAR = '/';
    public static final String SEPARATOR = String.valueOf(SEPARATOR_CHAR).intern();

    public static final String fileExtensionZip = "zip", fileExtensionJar = "jar", fileExtensionApk = "apk";
    public static final String fileExtensionTar = "tar";
    public static final String fileExtensionGzipTarLong = "tar.gz", fileExtensionGzipTarShort = "tgz";
    public static final String fileExtensionBzip2TarLong = "tar.bz2", fileExtensionBzip2TarShort = "tbz";
    public static final String fileExtensionRar = "rar";
    public static final String fileExtension7zip = "7z";
    public static final String fileExtensionLzma = "tar.lzma";
    public static final String fileExtensionXz = "tar.xz";

    /**
     * To add compatibility with other compressed file types edit this method
     */
    public static Extractor getExtractorInstance(@NonNull Context context, @NonNull File file, @NonNull String outputPath,
                                                 @NonNull Extractor.OnUpdate listener) {
        Extractor extractor;
        String type = getExtension(file.getPath());

        if (isZip(type)) {
            extractor = new ZipExtractor(context, file.getPath(), outputPath, listener);
        } else if (isRar(type)) {
            extractor = new RarExtractor(context, file.getPath(), outputPath, listener);
        } else if(isTar(type)) {
            extractor = new TarExtractor(context, file.getPath(), outputPath, listener);
        } else if(isGzippedTar(type)) {
            extractor = new GzipExtractor(context, file.getPath(), outputPath, listener);
        } else if(isBzippedTar(type)) {
            extractor = new Bzip2Extractor(context, file.getPath(), outputPath, listener);
        } else if(isXzippedTar(type)) {
            extractor = new XzExtractor(context, file.getPath(), outputPath, listener);
        } else if(isLzippedTar(type)) {
            extractor = new LzmaExtractor(context, file.getPath(), outputPath, listener);
        } else if(is7zip(type)) {
            extractor = new SevenZipExtractor(context, file.getPath(), outputPath, listener);
        } else {
            return null;
        }

        return extractor;
    }

    /**
     * To add compatibility with other compressed file types edit this method
     */
    public static Decompressor getCompressorInstance(@NonNull Context context, @NonNull File file) {
        Decompressor decompressor;
        String type = getExtension(file.getPath());

        if (isZip(type)) {
            decompressor = new ZipDecompressor(context);
        } else if (isRar(type)) {
            decompressor = new RarDecompressor(context);
        } else if(isTar(type)) {
            decompressor = new TarDecompressor(context);
        } else if(isGzippedTar(type)) {
            decompressor = new GzipDecompressor(context);
        } else if(isBzippedTar(type)) {
            decompressor = new Bzip2Decompressor(context);
        } else if(isXzippedTar(type)) {
            decompressor = new XzDecompressor(context);
        } else if(isLzippedTar(type)) {
            decompressor = new LzmaDecompressor(context);
        } else if(is7zip(type)) {
            decompressor = new SevenZipDecompressor(context);
        } else {
            return null;
        }

        decompressor.setFilePath(file.getPath());
        return decompressor;
    }

    public static boolean isFileExtractable(String path) {
        String type = getExtension(path);

        return isZip(type) || isTar(type) || isRar(type) || isGzippedTar(type) || is7zip(type) || isBzippedTar(type) || isXzippedTar(type) || isLzippedTar(type);
    }

    /**
     * Gets the name of the file without compression extention.
     * For example:
     * "s.tar.gz" to "s"
     * "s.tar" to "s"
     */
    public static String getFileName(String compressedName) {
        compressedName = compressedName.toLowerCase();
        if(isZip(compressedName) || isTar(compressedName) || isRar(compressedName) || is7zip(compressedName)
                || compressedName.endsWith(fileExtensionGzipTarShort) || compressedName.endsWith(fileExtensionBzip2TarShort)) {
            return compressedName.substring(0, compressedName.lastIndexOf("."));
        } else if (isGzippedTar(compressedName) || isXzippedTar(compressedName) || isLzippedTar(compressedName) || isBzippedTar(compressedName)) {
            return compressedName.substring(0,
                    Utils.nthToLastCharIndex(2, compressedName, '.'));
        } else {
            return compressedName;
        }
    }

    public static final boolean isEntryPathValid(String entryPath){
        return !entryPath.startsWith("..\\") && !entryPath.startsWith("../") && !entryPath.equals("..");
    }

    private static boolean isZip(String type) {
        return type.endsWith(fileExtensionZip) || type.endsWith(fileExtensionJar)
                || type.endsWith(fileExtensionApk);
    }

    private static boolean isTar(String type) {
         return type.endsWith(fileExtensionTar);
    }

    private static boolean isGzippedTar(String type) {
         return type.endsWith(fileExtensionGzipTarLong) || type.endsWith(fileExtensionGzipTarShort);
    }

    private static boolean isBzippedTar(String type) {
        return type.endsWith(fileExtensionBzip2TarLong) || type.endsWith(fileExtensionBzip2TarShort);
    }

    private static boolean isRar(String type) {
        return type.endsWith(fileExtensionRar);
    }
    
    private static boolean is7zip(String type) {
        return type.endsWith(fileExtension7zip);
    }

    private static boolean isXzippedTar(String type) {
        return type.endsWith(fileExtensionXz);
    }

    private static boolean isLzippedTar(String type) {
        return type.endsWith(fileExtensionLzma);
    }

    private static String getExtension(String path) {
        return path.substring(path.indexOf('.')+1, path.length()).toLowerCase();
    }

}
