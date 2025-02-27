package com.naga.filemanager.filesystem.compressed;

import android.os.Environment;

import com.naga.filemanager.BuildConfig;
import com.naga.filemanager.R;
import com.naga.filemanager.adapters.data.CompressedObjectParcelable;
import com.naga.filemanager.asynchronous.asynctasks.compress.ZipHelperTask;
import com.naga.filemanager.filesystem.compressed.extractcontents.Extractor;
import com.naga.filemanager.filesystem.compressed.extractcontents.helpers.ZipExtractor;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.multidex.ShadowMultiDex;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowMultiDex.class})
public class B0rkenZipTest {

    private File zipfile1 = new File(Environment.getExternalStorageDirectory(), "zip-slip.zip");
    private File zipfile2 = new File(Environment.getExternalStorageDirectory(), "zip-slip-win.zip");
    private File zipfile3 = new File(Environment.getExternalStorageDirectory(), "test-slashprefix.zip");

    private Extractor.OnUpdate emptyListener = new Extractor.OnUpdate() {

        @Override
        public void onStart(long totalBytes, String firstEntryName) {

        }

        @Override
        public void onUpdate(String entryPath) {

        }

        @Override
        public void onFinish() {

        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    };

    @Before
    public void setUp() throws Exception{
        IOUtils.copy(getClass().getClassLoader().getResourceAsStream("zip-slip.zip"), new FileOutputStream(zipfile1));
        IOUtils.copy(getClass().getClassLoader().getResourceAsStream("zip-slip-win.zip"), new FileOutputStream(zipfile2));
        IOUtils.copy(getClass().getClassLoader().getResourceAsStream("test-slashprefix.zip"), new FileOutputStream(zipfile3));
    }

    @Test
    public void testExtractZipWithWrongPathUnix() throws Exception{
        Extractor extractor = new ZipExtractor(RuntimeEnvironment.application, zipfile1.getAbsolutePath(), Environment.getExternalStorageDirectory().getAbsolutePath(), emptyListener);
        extractor.extractEverything();
        assertEquals(1, extractor.getInvalidArchiveEntries().size());
        assertTrue(new File(Environment.getExternalStorageDirectory(), "good.txt").exists());
    }

    @Test
    public void testExtractZipWithWrongPathWindows() throws Exception{
        Extractor extractor = new ZipExtractor(RuntimeEnvironment.application, zipfile2.getAbsolutePath(), Environment.getExternalStorageDirectory().getAbsolutePath(), emptyListener);
        extractor.extractEverything();
        assertEquals(1, extractor.getInvalidArchiveEntries().size());
        assertTrue(new File(Environment.getExternalStorageDirectory(), "good.txt").exists());
    }

    @Test
    public void testExtractZipWithSlashPrefixEntry() throws Exception{
        Extractor extractor = new ZipExtractor(RuntimeEnvironment.application, zipfile3.getAbsolutePath(), Environment.getExternalStorageDirectory().getAbsolutePath(), emptyListener);
        extractor.extractFiles(new String[]{"/test.txt"});
        assertEquals(0, extractor.getInvalidArchiveEntries().size());
        assertTrue(new File(Environment.getExternalStorageDirectory(), "test.txt").exists());
    }

    @Test
    public void testZipHelperTaskShouldOmitInvalidEntries() throws Exception {
        ZipHelperTask task = new ZipHelperTask(RuntimeEnvironment.application, zipfile1.getAbsolutePath(), null, false, (data) -> {});
        List<CompressedObjectParcelable> result = task.execute().get().result;
        assertEquals(1, result.size());
        assertEquals("good.txt", result.get(0).path);
        assertEquals(RuntimeEnvironment.application.getString(R.string.multiple_invalid_archive_entries), ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testZipHelperTaskShouldOmitInvalidEntriesWithBackslash() throws Exception {
        ZipHelperTask task = new ZipHelperTask(RuntimeEnvironment.application, zipfile2.getAbsolutePath(), null, false, (data) -> {});
        List<CompressedObjectParcelable> result = task.execute().get().result;
        assertEquals(1, result.size());
        assertEquals("good.txt", result.get(0).path);
        assertEquals(RuntimeEnvironment.application.getString(R.string.multiple_invalid_archive_entries), ShadowToast.getTextOfLatestToast());
    }
}
