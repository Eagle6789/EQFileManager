/*
 * TarDecompressor.java
 *
 * Copyright (C) 2017-2018 Emmanuel Messulam<emmanuelbendavid@gmail.com>,
 * Raymond Lai <airwave209gt@gmail.com>.
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

package com.naga.filemanager.filesystem.compressed.showcontents.helpers;

import android.content.Context;

import com.naga.filemanager.adapters.data.CompressedObjectParcelable;
import com.naga.filemanager.asynchronous.asynctasks.AsyncTaskResult;
import com.naga.filemanager.asynchronous.asynctasks.compress.TarHelperTask;
import com.naga.filemanager.filesystem.compressed.showcontents.Decompressor;
import com.naga.filemanager.utils.OnAsyncTaskFinished;

import java.util.ArrayList;

public class TarDecompressor extends Decompressor {

    public TarDecompressor(Context context) {
        super(context);
    }

    @Override
    public TarHelperTask changePath(String path, boolean addGoBackItem, OnAsyncTaskFinished<AsyncTaskResult<ArrayList<CompressedObjectParcelable>>> onFinish) {
        return new TarHelperTask(filePath, path, addGoBackItem, onFinish);
    }

}
